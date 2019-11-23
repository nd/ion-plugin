package ion.psi;

import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ArrayUtil;
import com.intellij.util.ObjectUtils;
import com.intellij.util.Processor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonReference extends PsiReferenceBase<IonPsiElement> {
  public IonReference(@NotNull IonPsiElement element, TextRange rangeInElement) {
    super(element, rangeInElement);
  }

  @Override
  @Nullable
  public PsiElement resolve() {
    if (myElement instanceof IonLabelName) {
      return resolveLabel((IonLabelName) myElement);
    }
    if (myElement instanceof IonExprField) {
      return resolveExprField((IonExprField) myElement);
    }
    PsiElement parent = myElement.getParent();
    IonExprField field = ObjectUtils.tryCast(parent, IonExprField.class);
    if (field != null && myElement == getFieldName(field)) {
      return resolveExprField(field);
    }

    IonCompoundFieldNamed compoundField = ObjectUtils.tryCast(parent, IonCompoundFieldNamed.class);
    if (compoundField != null && myElement == getCompoundFieldName(compoundField)) {
      return resolveCompoundField(compoundField);
    }

    Ref<PsiElement> result = Ref.create();
    CharSequence name = getRangeInElement().subSequence(myElement.getText());
    PsiElement processedChild = myElement;
    while (parent != null) {
      boolean stop = !processDeclarations(parent, processedChild, decl -> {
        PsiElement nameElement = decl.getNameIdentifier();
        if (nameElement != null && nameElement.textMatches(name)) {
          result.set(decl);
          return false;
        }
        return true;
      });
      if (stop) {
        break;
      }
      processedChild = parent;
      parent = parent.getParent();
    }
    return result.get();
  }

  @Nullable
  public static PsiElement resolveExprField(@NotNull IonExprField fieldExpr) {
    PsiElement qualifier = getQualifier(fieldExpr);
    if (qualifier == null) {
      return null;
    }
    PsiElement type = resolveType(qualifier);
    PsiElement unwrapped = unwrapParAndConstType(type);
    if (unwrapped instanceof IonTypePtr) {
      PsiElement underlying = getUnderlyingPtrType((IonTypePtr) unwrapped);
      unwrapped = unwrapParAndConstType(underlying);
    }
    if (unwrapped != type) {
      type = resolveType(unwrapped);
    }
    if (type instanceof IonDeclAggregate) {
      PsiElement nameElement = getFieldName(fieldExpr);
      if (nameElement == null) {
        return null;
      }
      String name = nameElement.getText();
      for (PsiElement child : type.getChildren()) {
        if (child instanceof IonDeclField) {
          IonDeclFieldName[] fieldNames = PsiTreeUtil.getChildrenOfType(child, IonDeclFieldName.class);
          if (fieldNames != null) {
            for (IonDeclFieldName fieldName : fieldNames) {
              PsiElement nameIdentifier = fieldName.getNameIdentifier();
              if (nameIdentifier != null && nameIdentifier.textMatches(name)) {
                return fieldName;
              }
            }
          }
        }
      }
    }
    return null;
  }

  @Nullable
  public static PsiElement resolveCompoundField(@NotNull IonCompoundFieldNamed compoundField) {
    PsiElement type = getLitCompoundType(ObjectUtils.tryCast(compoundField.getParent(), IonExprLitCompound.class));
    PsiElement baseType = getBaseType(type);
    if (baseType != type) {
      type = resolveType(baseType);
    }
    if (type instanceof IonDeclAggregate) {
      PsiElement nameElement = getCompoundFieldName(compoundField);
      if (nameElement == null) {
        return null;
      }
      String name = nameElement.getText();
      for (PsiElement child : type.getChildren()) {
        if (child instanceof IonDeclField) {
          IonDeclFieldName[] fieldNames = PsiTreeUtil.getChildrenOfType(child, IonDeclFieldName.class);
          if (fieldNames != null) {
            for (IonDeclFieldName fieldName : fieldNames) {
              PsiElement nameIdentifier = fieldName.getNameIdentifier();
              if (nameIdentifier != null && nameIdentifier.textMatches(name)) {
                return fieldName;
              }
            }
          }
        }
      }
    }
    return null;
  }

  @Nullable
  private static PsiElement getLitCompoundType(@Nullable IonExprLitCompound element) {
    if (element == null) {
      return null;
    }
    PsiElement parent = element.getParent();
    if (parent instanceof IonExprLitCompoundTyped) {
      return resolveType(parent);
    }
    if (parent instanceof IonExprUnary) {
      PsiElement operator = getOperator((IonExprUnary) parent);
      if (operator != null && operator.getNode().getElementType() == IonToken.AND) {
        parent = parent.getParent();
      }
    }
    if (parent instanceof IonExprCallArg) {
      return resolveType(parent);
    }
    if (parent instanceof IonStmtInit) {
      return resolveType(parent);
    }
    if (parent instanceof IonStmtAssign) {
      return resolveType(parent);
    }
    if (parent instanceof IonCompoundFieldNamed) {
      PsiElement resolvedField = resolveCompoundField((IonCompoundFieldNamed) parent);
      return resolveType(resolvedField);
    }
    if (parent instanceof IonCompoundField) {
      IonExprLitCompound parentLiteral = ObjectUtils.tryCast(parent.getParent(), IonExprLitCompound.class);
      if (parentLiteral != null) {
        PsiElement parentType = getLitCompoundType(parentLiteral);
        IonCompoundField[] fields = PsiTreeUtil.getChildrenOfType(parentLiteral, IonCompoundField.class);
        int n = ArrayUtil.indexOf(fields, parent);
        if (n >= 0) {
          if (parentType instanceof IonTypeArray) {
            PsiElement baseType = getUnderlyingType(parentType);
            PsiReference reference = baseType != null ? baseType.getReference() : null;
            return reference != null ? reference.resolve() : baseType;
          }
          if (parentType instanceof IonDeclAggregate) {
            IonDeclField[] fieldDecls = PsiTreeUtil.getChildrenOfType(parentType, IonDeclField.class);
            IonDeclField fieldDecl = n < fieldDecls.length ? fieldDecls[n] : null;
            return resolveType(fieldDecl);
          }
        }
      }
    }
    if (parent instanceof IonStmtReturn) {
      IonDeclFunc func = PsiTreeUtil.getParentOfType(parent, IonDeclFunc.class);
      return resolveType(func);
    }
    return null;
  }

  @Nullable
  private static PsiElement resolveType(@Nullable PsiElement element) {
    if (element == null) {
      return null;
    }
    if (element instanceof IonTypeName) {
      PsiReference reference = element != null ? element.getReference() : null;
      return reference != null ? reference.resolve() : null;
    }
    if (element instanceof IonStmtInit) {
      PsiElement type = getStmtInitType((IonStmtInit) element);
      if (type != null) {
        PsiElement resolved = resolveType(type);
        return resolved != null ? resolved : type;
      } else {
        PsiElement expr = getStmtInitExpr((IonStmtInit) element);
        return resolveType(expr);
      }
    }
    if (element instanceof IonStmtAssign) {
      PsiElement lhs = getStmtAssignLhs((IonStmtAssign) element);
      PsiReference reference = lhs != null ? lhs.getReference() : null;
      PsiElement resolvedLhs = reference != null ? reference.resolve() : null;
      return resolveType(resolvedLhs);
    }
    if (element instanceof IonDeclVar) {
      PsiElement type = getDeclVarType((IonDeclVar) element);
      if (type != null) {
        return resolveType(type);
      } else {
        PsiElement expr = getDeclVarExpr((IonDeclVar) element);
        return resolveType(expr);
      }
    }
    // todo IonDeclConst
    if (element instanceof IonDeclField) {
      PsiElement type = getDeclFieldType((IonDeclField) element);
      PsiReference reference = type != null ? type.getReference() : null;
      return reference != null ? reference.resolve() : type;
    }
    if (element instanceof IonDeclFieldName) {
      IonDeclField field = ObjectUtils.tryCast(element.getParent(), IonDeclField.class);
      PsiElement type = field != null ? getDeclFieldType(field) : null;
      PsiReference reference = type != null ? type.getReference() : null;
      return reference != null ? reference.resolve() : type;
    }
    if (element instanceof IonDeclFuncParam) {
      PsiElement type = getDeclFuncParamType((IonDeclFuncParam) element);
      PsiReference reference = type != null ? type.getReference() : null;
      return reference != null ? reference.resolve() : type;
    }
    if (element instanceof IonDeclFunc) {
      PsiElement type = getDeclFuncType((IonDeclFunc) element);
      PsiReference reference = type != null ? type.getReference() : null;
      return reference != null ? reference.resolve() : type;
    }
    if (element instanceof IonExprLitCompoundTyped) {
      PsiElement type = getExprCompoundTypedType((IonExprLitCompoundTyped) element);
      PsiReference reference = type != null ? type.getReference() : null;
      return reference != null ? reference.resolve() : type;
    }
    if (element instanceof IonExprCallArg) {
      IonExprCall call = ObjectUtils.tryCast(element.getParent(), IonExprCall.class);
      PsiElement[] children = call.getChildren();
      PsiElement callElement = children.length > 0 ? children[0] : null;
      PsiReference ref = callElement != null ? callElement.getReference() : null;
      PsiElement resolved = ref != null ? ref.resolve() : null;
      if (resolved instanceof IonDeclFunc) {
        IonExprCallArg[] args = PsiTreeUtil.getChildrenOfType(call, IonExprCallArg.class);
        int n = ArrayUtil.indexOf(args, element);
        IonDeclFuncParam[] params = PsiTreeUtil.getChildrenOfType(resolved, IonDeclFuncParam.class);
        if (n >= 0 && n < params.length) {
          return resolveType(params[n]);
        }
      }
    }
    if (element instanceof IonExprName) {
      PsiReference reference = element.getReference();
      PsiElement resolved = reference != null ? reference.resolve() : null;
      return resolveType(resolved);
    }
    if (element instanceof IonExprField) {
      PsiElement resolved = resolveExprField((IonExprField) element);
      return resolveType(resolved);
    }
    if (element instanceof IonExprCall) {
      PsiElement name = ArrayUtil.getFirstElement(element.getChildren());
      PsiReference reference = name != null ? name.getReference() : null;
      return resolveType(reference.resolve());
    }
    if (element instanceof IonExprIndex) {
      PsiElement indexedExpr = getExprIndexExpr((IonExprIndex) element);
      PsiElement type = resolveType(indexedExpr);
      if (type instanceof IonTypeArray || type instanceof IonTypePtr) {
        PsiElement underlyingType = getUnderlyingType(type);
        PsiElement underlyingResolved = resolveType(underlyingType);
        return underlyingResolved != null ? underlyingResolved : underlyingType;
      }
    }
    PsiReference reference = element.getReference();
    return resolveType(reference != null ? reference.resolve() : null);
  }

  @Nullable
  public static PsiElement resolveLabel(@NotNull IonLabelName labelName) {
    IonDeclFunc func = PsiTreeUtil.getParentOfType(labelName, IonDeclFunc.class);
    if (func == null) {
      return null;
    }
    Ref<PsiElement> result = Ref.create();
    CharSequence name = labelName.getText();
    PsiTreeUtil.processElements(func, it -> {
      IonStmtLabel label = ObjectUtils.tryCast(it, IonStmtLabel.class);
      if (label != null) {
        PsiElement nameIdentifier = label.getNameIdentifier();
        if (nameIdentifier != null && nameIdentifier.textMatches(name)) {
          result.set(label);
          return false;
        }
      }
      return true;
    });
    return result.get();
  }

  public static boolean processDeclarations(@NotNull PsiElement element,
                                            @Nullable PsiElement processedChild,
                                            @NotNull Processor<IonDecl> processor) {
    for (PsiElement child : element.getChildren()) {
      if (!(element instanceof IonPsiFile) && child.equals(processedChild)) {
        break;
      }
      if (child instanceof IonDecl && !(child instanceof IonStmtLabel)) {
        if (!processor.process((IonDecl) child)) {
          return false;
        }
      }
    }
    return true;
  }

  @Nullable
  private static PsiElement getQualifier(@NotNull IonExprField field) {
    return field.getFirstChild();
  }

  @Nullable
  static PsiElement getFieldName(@NotNull IonExprField field) {
    PsiElement[] children = field.getChildren();
    return children.length == 2 ? children[1] : null;
  }

  @Nullable
  private static PsiElement getCompoundFieldName(@NotNull IonCompoundFieldNamed compoundField) {
    return PsiTreeUtil.findChildOfType(compoundField, IonExprName.class);
  }

  @Nullable
  private static PsiElement getStmtAssignLhs(@NotNull IonStmtAssign eleemnt) {
    return eleemnt.getFirstChild();
  }

  @Nullable
  private static PsiElement getStmtInitType(@NotNull IonStmtInit stmt) {
    PsiElement name = stmt.getFirstChild();
    PsiElement element = PsiTreeUtil.skipWhitespacesAndCommentsForward(name);
    if (element != null && element.getNode().getElementType() == IonToken.COLON) {
      return PsiTreeUtil.skipWhitespacesAndCommentsForward(element);
    }
    return null;
  }

  @Nullable
  private static PsiElement getStmtInitExpr(@NotNull IonStmtInit stmt) {
    IonExpr[] exprs = PsiTreeUtil.getChildrenOfType(stmt, IonExpr.class);
    return exprs != null && exprs.length > 0 ? exprs[0] : null;
  }

  @Nullable
  private static PsiElement getDeclVarType(@NotNull IonDeclVar decl) {
    PsiElement var = decl.getFirstChild();
    PsiElement name = PsiTreeUtil.skipWhitespacesAndCommentsForward(var);
    PsiElement element = PsiTreeUtil.skipWhitespacesAndCommentsForward(name);
    if (element != null && element.getNode().getElementType() == IonToken.COLON) {
      return PsiTreeUtil.skipWhitespacesAndCommentsForward(element);
    }
    return null;
  }

  @Nullable
  private static PsiElement getDeclVarExpr(@NotNull IonDeclVar decl) {
    return PsiTreeUtil.getChildOfType(decl, IonExpr.class);
  }

  @Nullable
  private static PsiElement getExprIndexExpr(@NotNull IonExprIndex element) {
    IonExpr[] children = PsiTreeUtil.getChildrenOfType(element, IonExpr.class);
    return children != null && children.length > 0 ? children[0] : null;
  }

  @Nullable
  private static PsiElement getDeclFieldType(@NotNull IonDeclField field) {
    PsiElement name = field.getFirstChild();
    PsiElement element = PsiTreeUtil.skipWhitespacesAndCommentsForward(name);
    if (element != null && element.getNode().getElementType() == IonToken.COLON) {
      return PsiTreeUtil.skipWhitespacesAndCommentsForward(element);
    }
    return null;
  }

  @Nullable
  private static PsiElement getDeclFuncParamType(@NotNull IonDeclFuncParam param) {
    PsiElement name = param.getFirstChild();
    PsiElement element = PsiTreeUtil.skipWhitespacesAndCommentsForward(name);
    if (element != null && element.getNode().getElementType() == IonToken.COLON) {
      return PsiTreeUtil.skipWhitespacesAndCommentsForward(element);
    }
    return null;
  }

  @Nullable
  private static PsiElement getDeclFuncType(@NotNull IonDeclFunc func) {
    PsiElement child = func.getFirstChild();
    while (child != null) {
      if (child != null && child.getNode().getElementType() == IonToken.COLON) {
        return PsiTreeUtil.skipWhitespacesAndCommentsForward(child);
      }
      child = PsiTreeUtil.skipWhitespacesAndCommentsForward(child);
    }
    return null;
  }

  @Nullable
  private static PsiElement getExprCompoundTypedType(@NotNull IonExprLitCompoundTyped lit) {
    return PsiTreeUtil.findChildOfType(lit, IonTypeName.class);
  }

  @Nullable
  private static PsiElement unwrapParAndConstType(@Nullable PsiElement type) {
    while (true) {
      PsiElement base = null;
      if (type instanceof IonTypeConst) {
        base = getUnderlyingConstType((IonTypeConst) type);
      }
      if (type instanceof IonTypePar) {
        base = getUnderlyingParType((IonTypePar) type);
      }
      if (base == null) {
        return type;
      } else {
        type = base;
      }
    }
  }

  @Nullable
  private static PsiElement getBaseType(@Nullable PsiElement type) {
    while (true) {
      PsiElement base = getUnderlyingType(type);
      if (base == null) {
        return type;
      } else {
        type = base;
      }
    }
  }

  @Nullable
  private static PsiElement getUnderlyingType(@Nullable PsiElement type) {
    if (type instanceof IonTypePtr) {
      return getUnderlyingPtrType((IonTypePtr) type);
    } else if (type instanceof IonTypeArray) {
      return getUnderlyingArrayType((IonTypeArray) type);
    } else if (type instanceof IonTypeConst) {
      return getUnderlyingConstType((IonTypeConst) type);
    } else if (type instanceof IonTypePar) {
      return getUnderlyingParType((IonTypePar) type);
    }
    return null;
  }

  @Nullable
  private static PsiElement getUnderlyingPtrType(@NotNull IonTypePtr type) {
    return type.getChildren()[0];
  }

  @Nullable
  private static PsiElement getUnderlyingArrayType(@NotNull IonTypeArray type) {
    return type.getChildren()[0];
  }

  @Nullable
  private static PsiElement getUnderlyingConstType(@NotNull IonTypeConst type) {
    return type.getChildren()[0];
  }

  @Nullable
  private static PsiElement getUnderlyingParType(@NotNull IonTypePar type) {
    return type.getChildren()[0];
  }

  @NotNull
  private static PsiElement getOperator(@NotNull IonExprUnary expr) {
    return expr.getFirstChild();
  }
}

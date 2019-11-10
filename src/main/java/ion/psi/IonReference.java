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
import com.intellij.util.containers.ContainerUtil;
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
    IonExprField field = ObjectUtils.tryCast(myElement.getParent(), IonExprField.class);
    if (field != null && myElement == getFieldName(field)) {
      return resolveField((IonExprField) myElement.getParent());
    }
    Ref<PsiElement> result = Ref.create();
    CharSequence name = getRangeInElement().subSequence(myElement.getText());
    PsiElement parent = myElement.getParent();
    PsiElement processedChild = myElement;
    while (parent != null) {
      boolean stop = !processDeclarations(parent, processedChild, decl -> {
        if (decl.getNameIdentifier().textMatches(name)) {
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
  public static PsiElement resolveField(@NotNull IonExprField fieldExpr) {
    PsiElement qualifier = getQualifier(fieldExpr);
    if (qualifier == null) {
      return null;
    }
    PsiElement resolvedQualifer = null;
    if (qualifier instanceof IonExprField) {
      resolvedQualifer = resolveField((IonExprField) qualifier);
    } else if (qualifier instanceof IonExprCall) {
      PsiElement name = ArrayUtil.getFirstElement(qualifier.getChildren());
      PsiReference reference = name != null ? name.getReference() : null;
      resolvedQualifer = reference.resolve();
    } else {
      PsiReference reference = qualifier.getReference();
      resolvedQualifer = reference != null ? reference.resolve() : null;
    }
    if (resolvedQualifer == null) {
      return null;
    }
    PsiElement type = resolveType(resolvedQualifer);
    if (type instanceof IonDeclAggregate) {
      PsiElement nameElement = getFieldName(fieldExpr);
      if (nameElement == null) {
        return null;
      }
      String name = nameElement.getText();
      for (PsiElement child : type.getChildren()) {
        if (child instanceof IonDeclField) {
          if (((IonDeclField) child).getNameIdentifier().textMatches(name)) {
            return child;
          }
        }
      }
    }
    return null;
  }

  @Nullable
  private static PsiElement resolveType(@Nullable PsiElement decl) {
    if (decl == null) {
      return null;
    }
    if (decl instanceof IonStmtInit) {
      PsiElement type = getStmtInitType((IonStmtInit) decl);
      PsiReference reference = type != null ? type.getReference() : null;
      return reference != null ? reference.resolve() : null;
    }
    if (decl instanceof IonDeclField) {
      PsiElement type = getDeclFieldType((IonDeclField) decl);
      PsiReference reference = type != null ? type.getReference() : null;
      return reference != null ? reference.resolve() : null;
    }
    if (decl instanceof IonDeclFuncParam) {
      PsiElement type = getDeclFuncParamType((IonDeclFuncParam) decl);
      PsiReference reference = type != null ? type.getReference() : null;
      return reference != null ? reference.resolve() : null;
    }
    if (decl instanceof IonDeclFunc) {
      PsiElement type = getDeclFuncType((IonDeclFunc) decl);
      PsiReference reference = type != null ? type.getReference() : null;
      return reference != null ? reference.resolve() : null;
    }
    return null;
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
        if (label.getNameIdentifier().textMatches(name)) {
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
  private static PsiElement getFieldName(@NotNull IonExprField field) {
    PsiElement[] children = field.getChildren();
    return children.length == 2 ? children[1] : null;
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
}

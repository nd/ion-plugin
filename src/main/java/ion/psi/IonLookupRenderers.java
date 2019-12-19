package ion.psi;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

class IonLookupRenderers {
  private static LookupElementRenderer<LookupElement> FUNC = new LookupElementRenderer<>() {
    @Override
    public void renderElement(LookupElement element, LookupElementPresentation p) {
      IonDeclFunc decl = ObjectUtils.tryCast(element.getPsiElement(), IonDeclFunc.class);
      if (decl == null) {
        return;
      }
      String name = decl.getName();
      p.setItemText(name + "()");
      PsiElement type = decl.getType();
      if (type != null) {
        String typePresentation = getTypePresentation(type);
        if (typePresentation != null) {
          p.setTypeText(typePresentation);
        }
      } else {
        p.setTypeText("void");
      }
    }
  };

  private static LookupElementRenderer<LookupElement> VAR = new LookupElementRenderer<>() {
    @Override
    public void renderElement(LookupElement element, LookupElementPresentation p) {
      IonDecl decl = ObjectUtils.tryCast(element.getPsiElement(), IonDecl.class);
      if (decl == null) {
        return;
      }
      String name = decl.getName();
      PsiElement type = null;
      if (decl instanceof IonDeclVar) {
        p.setItemText(name);
        p.setTailText(" var", true);
        type = ((IonDeclVar) decl).getType();
      } else if (decl instanceof IonDeclConst) {
        p.setItemText(name);
        p.setTailText(" const", true);
        type = ((IonDeclConst) decl).getType();
      }
      String typePresentation = getTypePresentation(type);
      if (typePresentation != null) {
        p.setTypeText(typePresentation);
      }
    }
  };

  private static LookupElementRenderer<LookupElement> TYPEDEF = new LookupElementRenderer<>() {
    @Override
    public void renderElement(LookupElement element, LookupElementPresentation p) {
      IonDeclTypedef decl = ObjectUtils.tryCast(element.getPsiElement(), IonDeclTypedef.class);
      if (decl == null) {
        return;
      }
      String name = decl.getName();
      p.setItemText(name);
      p.setTailText(" type", true);
      PsiElement type = decl.getType();
      String typePresentation = getTypePresentation(type);
      if (typePresentation != null) {
        p.setTypeText(typePresentation);
      }
    }
  };

  private static LookupElementRenderer<LookupElement> FIELD = new LookupElementRenderer<>() {
    @Override
    public void renderElement(LookupElement element, LookupElementPresentation p) {
      IonDeclFieldName decl = ObjectUtils.tryCast(element.getPsiElement(), IonDeclFieldName.class);
      if (decl == null) {
        return;
      }
      String name = decl.getName();
      p.setItemText(name);
      p.setTailText(" field", true);
      IonDeclField fieldDecl = PsiTreeUtil.getStubOrPsiParentOfType(decl, IonDeclField.class);
      PsiElement type = fieldDecl != null ? fieldDecl.getType() : null;
      String typePresentation = getTypePresentation(type);
      if (typePresentation != null) {
        p.setTypeText(typePresentation);
      }
    }
  };

  private static LookupElementRenderer<LookupElement> AGGREGATE = new LookupElementRenderer<>() {
    @Override
    public void renderElement(LookupElement element, LookupElementPresentation p) {
      IonDeclAggregate decl = ObjectUtils.tryCast(element.getPsiElement(), IonDeclAggregate.class);
      if (decl == null) {
        return;
      }
      String name = decl.getName();
      PsiElement firstChild = decl.getFirstChild();
      IElementType elementType = firstChild != null ? firstChild.getNode().getElementType() : null;
      String type = null;
      if (elementType == IonToken.STRUCT) {
        p.setItemText(name);
        p.setTailText(" type", true);
        type = "struct{}";
      } else if (elementType == IonToken.UNION) {
        p.setItemText(name);
        p.setTailText(" type", true);
        type = "union{}";
      }
      if (type != null) {
        p.setTypeText(type);
      }
    }
  };

  private static LookupElementRenderer<LookupElement> ENUM = new LookupElementRenderer<>() {
    @Override
    public void renderElement(LookupElement element, LookupElementPresentation p) {
      IonDeclEnum decl = ObjectUtils.tryCast(element.getPsiElement(), IonDeclEnum.class);
      if (decl == null) {
        return;
      }
      p.setItemText(decl.getName());
      p.setTailText(" type", true);
      p.setTypeText("enum");
    }
  };

  private static LookupElementRenderer<LookupElement> ENUM_ITEM = new LookupElementRenderer<>() {
    @Override
    public void renderElement(LookupElement element, LookupElementPresentation p) {
      IonDeclEnumItem decl = ObjectUtils.tryCast(element.getPsiElement(), IonDeclEnumItem.class);
      if (decl == null) {
        return;
      }
      p.setItemText(decl.getName());
      p.setTailText(" enum const", true);
      IonDeclEnum enumDecl = ObjectUtils.tryCast(decl.getParent(), IonDeclEnum.class);
      if (enumDecl != null) {
        String name = enumDecl.getName();
        if (StringUtil.isNotEmpty(name)) {
          p.setTypeText(name);
        }
      }
    }
  };

  @Nullable
  private static String getTypePresentation(@Nullable PsiElement type) {
    if (type == null) {
      return null;
    }
    if (type instanceof IonTypeName) {
      return ((IonTypeName) type).getName();
    }
    if (type instanceof IonTypePtr) {
      IonType underlying = PsiTreeUtil.getStubChildOfType(type, IonType.class);
      return getTypePresentation(underlying) + "*";
    }
    if (type instanceof IonTypeArray) {
      IonType underlying = PsiTreeUtil.getStubChildOfType(type, IonType.class);
      return getTypePresentation(underlying) + "[]";
    }
    if (type instanceof IonTypeConst) {
      IonType underlying = PsiTreeUtil.getStubChildOfType(type, IonType.class);
      return getTypePresentation(underlying) + " const";
    }
    if (type instanceof IonTypePar) {
      IonType underlying = PsiTreeUtil.getStubChildOfType(type, IonType.class);
      return "(" + getTypePresentation(underlying) + ")";
    }
    if (type instanceof IonTypeTuple) {
      List<IonType> items = PsiTreeUtil.getStubChildrenOfTypeAsList(type, IonType.class);
      return "{" + StringUtil.join(ContainerUtil.map(items, IonLookupRenderers::getTypePresentation), ", ") + "}";
    }
    if (type instanceof IonTypeFunc) {
      List<IonDeclFuncParam> params = PsiTreeUtil.getStubChildrenOfTypeAsList(type, IonDeclFuncParam.class);
      String paramsStr = StringUtil.join(ContainerUtil.map(params, param -> {
        String paramName = param.getName();
        String paramType = getTypePresentation(param.getType());
        if (paramName == null && paramType == null) {
          return "...";
        }
        if (paramType == null) {
          return null;
        }
        return paramName != null ? paramName + ": " + paramType : paramType;
      }), ", ");
      IonType returnType = PsiTreeUtil.getStubChildOfType(type, IonType.class);
      return "func(" + paramsStr + ")" + (returnType != null ? ": " + getTypePresentation(returnType) : "");
    }
    return type.getText();
  }

  @Nullable
  static LookupElementRenderer<LookupElement> getRenderer(@NotNull PsiElement element) {
    if (element instanceof IonDeclVar || element instanceof IonDeclConst) {
      return IonLookupRenderers.VAR;
    } else if (element instanceof IonDeclFunc) {
      return IonLookupRenderers.FUNC;
    } else if (element instanceof IonDeclTypedef) {
      return IonLookupRenderers.TYPEDEF;
    } else if (element instanceof IonDeclFieldName) {
      return IonLookupRenderers.FIELD;
    } else if (element instanceof IonDeclAggregate) {
      return IonLookupRenderers.AGGREGATE;
    } else if (element instanceof IonDeclEnum) {
      return IonLookupRenderers.ENUM;
    } else if (element instanceof IonDeclEnumItem) {
      return IonLookupRenderers.ENUM_ITEM;
    }
    return null;
  }
}

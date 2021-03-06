package ion.psi;

import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import ion.IonIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IonCompletion {
  private static LookupElementRenderer<LookupElement> FUNC = new LookupElementRenderer<LookupElement>() {
    @Override
    public void renderElement(LookupElement element, LookupElementPresentation p) {
      IonDeclFunc decl = ObjectUtils.tryCast(element.getPsiElement(), IonDeclFunc.class);
      if (decl == null) {
        return;
      }
      String name = decl.getName();
      String paramsStr = getParamsString(PsiTreeUtil.getStubChildrenOfTypeAsList(decl, IonDeclFuncParam.class));
      p.setItemText(name);
      p.setTailText("(" + paramsStr + ")", true);
      PsiElement type = decl.getType();
      if (type != null) {
        String typePresentation = getTypePresentation(type);
        if (typePresentation != null) {
          p.setTypeText(typePresentation);
        }
      } else {
        p.setTypeText("void");
      }
      p.setIcon(IonIcons.FUNC);
    }
  };

  private static LookupElementRenderer<LookupElement> VAR = new LookupElementRenderer<LookupElement>() {
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
      p.setIcon(IonIcons.VAR);
    }
  };

  private static LookupElementRenderer<LookupElement> TYPEDEF = new LookupElementRenderer<LookupElement>() {
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
      p.setIcon(IonIcons.TYPE);
    }
  };

  private static LookupElementRenderer<LookupElement> FIELD = new LookupElementRenderer<LookupElement>() {
    @Override
    public void renderElement(LookupElement element, LookupElementPresentation p) {
      IonDeclFieldName decl = ObjectUtils.tryCast(element.getPsiElement(), IonDeclFieldName.class);
      if (decl == null) {
        return;
      }
      String name = decl.getName();
      p.setItemText(name);
      p.setTailText(" field", true);
      PsiElement type = decl.getType();
      String typePresentation = getTypePresentation(type);
      if (typePresentation != null) {
        p.setTypeText(typePresentation);
      }
      p.setIcon(IonIcons.FIELD);
    }
  };

  private static LookupElementRenderer<LookupElement> AGGREGATE = new LookupElementRenderer<LookupElement>() {
    @Override
    public void renderElement(LookupElement element, LookupElementPresentation p) {
      IonDeclAggregate decl = ObjectUtils.tryCast(element.getPsiElement(), IonDeclAggregate.class);
      if (decl == null) {
        return;
      }
      String name = decl.getName();
      String type = null;
      switch (decl.getKind()) {
        case STRUCT:
          p.setItemText(name);
          p.setTailText(" type", true);
          type = "struct{}";
          p.setIcon(IonIcons.STRUCT);
          break;
        case UNION:
          p.setItemText(name);
          p.setTailText(" type", true);
          type = "union{}";
          p.setIcon(IonIcons.UNION);
          break;
      }
      p.setTypeText(type);
    }
  };

  private static LookupElementRenderer<LookupElement> ENUM = new LookupElementRenderer<LookupElement>() {
    @Override
    public void renderElement(LookupElement element, LookupElementPresentation p) {
      IonDeclEnum decl = ObjectUtils.tryCast(element.getPsiElement(), IonDeclEnum.class);
      if (decl == null) {
        return;
      }
      p.setItemText(decl.getName());
      p.setTailText(" type", true);
      p.setTypeText("enum");
      p.setIcon(IonIcons.ENUM);
    }
  };


  private static LookupElementRenderer<LookupElement> ENUM_ITEM = new LookupElementRenderer<LookupElement>() {
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
      p.setIcon(IonIcons.ENUM_ITEM);
    }
  };

  @Nullable
  public static String getTypePresentation(@Nullable PsiElement type) {
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
      return "{" + StringUtil.join(ContainerUtil.map(items, IonCompletion::getTypePresentation), ", ") + "}";
    }
    if (type instanceof IonTypeFunc) {
      String paramsStr = getParamsString(PsiTreeUtil.getStubChildrenOfTypeAsList(type, IonDeclFuncParam.class));
      IonType returnType = PsiTreeUtil.getStubChildOfType(type, IonType.class);
      return "func(" + paramsStr + ")" + (returnType != null ? ": " + getTypePresentation(returnType) : "");
    }
    if (type instanceof IonTypeQName) {
      List<IonTypeName> names = PsiTreeUtil.getStubChildrenOfTypeAsList(type, IonTypeName.class);
      return names.isEmpty() ? null : getTypePresentation(ContainerUtil.getLastItem(names));
    }
    return type.getText();
  }

  @Nullable
  static LookupElementRenderer<LookupElement> getRenderer(@NotNull PsiElement element) {
    if (element instanceof IonDeclVar || element instanceof IonDeclConst) {
      return VAR;
    } else if (element instanceof IonDeclFunc) {
      return FUNC;
    } else if (element instanceof IonDeclTypedef) {
      return TYPEDEF;
    } else if (element instanceof IonDeclFieldName) {
      return FIELD;
    } else if (element instanceof IonDeclAggregate) {
      return AGGREGATE;
    } else if (element instanceof IonDeclEnum) {
      return ENUM;
    } else if (element instanceof IonDeclEnumItem) {
      return ENUM_ITEM;
    }
    return null;
  }

  @Nullable
  static InsertHandler<LookupElement> getInsertHandler(@NotNull PsiElement element) {
    if (element instanceof IonDeclFunc) {
      int paramsCount = PsiTreeUtil.getStubChildrenOfTypeAsList(element, IonDeclFuncParam.class).size();
      return paramsCount == 0 ? ParenthesesInsertHandler.NO_PARAMETERS : ParenthesesInsertHandler.WITH_PARAMETERS;
    }
    return null;
  }

  @NotNull
  public static String getParamsString(@NotNull List<IonDeclFuncParam> params) {
    return StringUtil.join(ContainerUtil.map(params, param -> {
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
  }
}

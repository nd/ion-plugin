package ion.psi;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class IonLookupRenderers {
  private static LookupElementRenderer<LookupElement> FUNC = new LookupElementRenderer<LookupElement>() {
    @Override
    public void renderElement(LookupElement element, LookupElementPresentation p) {
      IonDeclFunc decl = ObjectUtils.tryCast(element.getPsiElement(), IonDeclFunc.class);
      if (decl == null) {
        return;
      }
      String name = decl.getName();
      p.setItemText(name + "()");
      PsiElement type = IonReference.getDeclFuncType(decl);
      if (type != null) {
        p.setTypeText(type.getText());
      }
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
        type = IonReference.getDeclVarType((IonDeclVar) decl);
      } else if (decl instanceof IonDeclConst) {
        p.setItemText(name);
        p.setTailText(" const", true);
        type = IonReference.getDeclConstType((IonDeclConst) decl);
      }
      if (type != null) {
        p.setTypeText(type.getText());
      }
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
      PsiElement type = IonReference.getDeclTypedefType(decl);
      if (type != null) {
        p.setTypeText(type.getText());
      }
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
      IonDeclField fieldDecl = ObjectUtils.tryCast(decl.getParent(), IonDeclField.class);
      PsiElement type = fieldDecl != null ? IonReference.getDeclFieldType(fieldDecl) : null;
      if (type != null) {
        p.setTypeText(type.getText());
      }
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
    }
  };

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

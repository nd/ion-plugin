package ion.psi;

import com.intellij.model.SymbolResolveResult;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ObjectUtils;
import com.intellij.util.Processor;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

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
}

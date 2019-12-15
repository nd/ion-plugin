package ion.psi;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonNameManipulator extends AbstractElementManipulator<PsiElement> {
  @Nullable
  @Override
  public PsiElement handleContentChange(@NotNull PsiElement element,
                                        @NotNull TextRange range,
                                        String newContent) throws IncorrectOperationException {
    PsiElement currentName = element.getFirstChild();
    PsiElement newName = IonElementFactory.createName(element.getProject(), newContent);
    currentName.replace(newName);
    return element;
  }
}

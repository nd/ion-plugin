package ion.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface IonTypeOwner {
  @Nullable
  PsiElement getType();
}

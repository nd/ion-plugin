package ion.psi;

import com.intellij.psi.PsiNameIdentifierOwner;

import javax.annotation.Nullable;

public interface IonDecl extends IonPsiElement, PsiNameIdentifierOwner {

  @Nullable
  String getName();

}

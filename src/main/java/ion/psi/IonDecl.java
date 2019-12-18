package ion.psi;

import com.intellij.psi.PsiNameIdentifierOwner;

import javax.annotation.Nullable;

public interface IonDecl extends PsiNameIdentifierOwner {

  @Nullable
  String getName();

}

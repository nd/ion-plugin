package ion.psi.stub;

import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubElement;

import javax.annotation.Nullable;

public interface IonDeclStub<T extends PsiElement> extends StubElement<T> {

  @Nullable
  String getName();

}

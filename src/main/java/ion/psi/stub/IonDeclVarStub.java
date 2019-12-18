package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclVar;
import org.jetbrains.annotations.Nullable;

public interface IonDeclVarStub extends StubElement<IonDeclVar> {

  @Nullable
  String getName();

}

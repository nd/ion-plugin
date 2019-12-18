package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclVar;
import ion.psi.IonElementType;
import org.jetbrains.annotations.Nullable;

public interface IonDeclStubVar extends IonDeclStub<IonDeclVar> {

  class Impl extends IonDeclStubBase<IonDeclVar> implements IonDeclStubVar {
    public Impl(StubElement parent, @Nullable String name) {
      super(parent, IonElementType.DECL_VAR, name);
    }
  }

}

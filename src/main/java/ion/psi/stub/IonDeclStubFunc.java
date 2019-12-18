package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclFunc;
import ion.psi.IonElementType;
import org.jetbrains.annotations.Nullable;

public interface IonDeclStubFunc extends IonDeclStub<IonDeclFunc> {

  class Impl extends IonDeclStubBase<IonDeclFunc> implements IonDeclStubFunc {
    public Impl(StubElement parent, @Nullable String name) {
      super(parent, IonElementType.DECL_FUNC, name);
    }
  }

}

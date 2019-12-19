package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclTypedef;
import ion.psi.IonElementType;
import org.jetbrains.annotations.Nullable;

public interface IonDeclStubTypedef extends IonDeclStub<IonDeclTypedef> {

  class Impl extends IonDeclStubBase<IonDeclTypedef> implements IonDeclStubTypedef {
    public Impl(StubElement parent, @Nullable String name) {
      super(parent, IonElementType.DECL_TYPEDEF, name);
    }
  }

}

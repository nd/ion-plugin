package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclConst;
import ion.psi.IonElementType;
import org.jetbrains.annotations.Nullable;

public interface IonDeclStubConst extends IonDeclStub<IonDeclConst> {

  class Impl extends IonDeclStubBase<IonDeclConst> implements IonDeclStubConst {
    public Impl(StubElement parent, @Nullable String name) {
      super(parent, IonElementType.DECL_CONST, name);
    }
  }

}

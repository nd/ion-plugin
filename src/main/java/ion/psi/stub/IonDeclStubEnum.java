package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclEnum;
import ion.psi.IonElementType;
import org.jetbrains.annotations.Nullable;

public interface IonDeclStubEnum extends IonDeclStub<IonDeclEnum> {

  class Impl extends IonDeclStubBase<IonDeclEnum> implements IonDeclStubEnum {
    public Impl(StubElement parent, @Nullable String name) {
      super(parent, IonElementType.DECL_ENUM, name);
    }
  }

}


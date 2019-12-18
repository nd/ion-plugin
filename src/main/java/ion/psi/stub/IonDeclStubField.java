package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclField;
import ion.psi.IonElementType;

public interface IonDeclStubField extends IonDeclStub<IonDeclField> {

  class Impl extends IonDeclStubBase<IonDeclField> implements IonDeclStubField {
    public Impl(StubElement parent) {
      super(parent, IonElementType.DECL_FIELD, null);
    }
  }

}

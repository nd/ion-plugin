package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclField;
import ion.psi.IonElementType;

public class IonDeclStubField extends IonDeclStubBase<IonDeclField> implements IonDeclStub<IonDeclField> {
  public IonDeclStubField(StubElement parent) {
    super(parent, IonElementType.DECL_FIELD, null);
  }
}

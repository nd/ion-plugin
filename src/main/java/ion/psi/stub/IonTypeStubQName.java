package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonElementType;
import ion.psi.IonTypeQName;

public class IonTypeStubQName extends IonTypeStubBase<IonTypeQName> implements IonTypeStub<IonTypeQName> {
  public IonTypeStubQName(StubElement parent) {
    super(parent, IonElementType.TYPE_QNAME);
  }
}

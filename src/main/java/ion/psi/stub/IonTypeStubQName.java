package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonElementType;
import ion.psi.IonTypeQName;

public interface IonTypeStubQName extends IonTypeStub<IonTypeQName> {

  class Impl extends IonTypeStubBase<IonTypeQName> implements IonTypeStubQName {
    public Impl(StubElement parent) {
      super(parent, IonElementType.TYPE_QNAME);
    }
  }

}

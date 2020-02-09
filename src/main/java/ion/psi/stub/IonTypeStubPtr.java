package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonElementType;
import ion.psi.IonTypePtr;

public class IonTypeStubPtr extends IonTypeStubBase<IonTypePtr> implements IonTypeStub<IonTypePtr> {
  public IonTypeStubPtr(StubElement parent) {
    super(parent, IonElementType.TYPE_PTR);
  }
}

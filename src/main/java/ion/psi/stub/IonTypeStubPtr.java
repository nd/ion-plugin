package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonElementType;
import ion.psi.IonTypePtr;

public interface IonTypeStubPtr extends IonTypeStub<IonTypePtr> {

  class Impl extends IonTypeStubBase<IonTypePtr> implements IonTypeStubPtr {
    public Impl(StubElement parent) {
      super(parent, IonElementType.TYPE_PTR);
    }
  }

}

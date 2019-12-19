package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonElementType;
import ion.psi.IonTypeTuple;

public interface IonTypeStubTuple extends IonTypeStub<IonTypeTuple> {

  class Impl extends IonTypeStubBase<IonTypeTuple> implements IonTypeStubTuple {
    public Impl(StubElement parent) {
      super(parent, IonElementType.TYPE_TUPLE);
    }
  }

}

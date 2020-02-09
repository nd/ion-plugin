package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonElementType;
import ion.psi.IonTypeTuple;

public class IonTypeStubTuple extends IonTypeStubBase<IonTypeTuple> implements IonTypeStub<IonTypeTuple> {
  public IonTypeStubTuple(StubElement parent) {
    super(parent, IonElementType.TYPE_TUPLE);
  }
}

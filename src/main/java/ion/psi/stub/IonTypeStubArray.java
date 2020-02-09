package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonElementType;
import ion.psi.IonTypeArray;

public class IonTypeStubArray extends IonTypeStubBase<IonTypeArray> implements IonTypeStub<IonTypeArray> {
  public IonTypeStubArray(StubElement parent) {
    super(parent, IonElementType.TYPE_ARRAY);
  }
}

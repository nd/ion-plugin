package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonElementType;
import ion.psi.IonTypeFunc;

public class IonTypeStubFunc extends IonTypeStubBase<IonTypeFunc> implements IonTypeStub<IonTypeFunc> {
  public IonTypeStubFunc(StubElement parent) {
    super(parent, IonElementType.TYPE_FUNC);
  }
}

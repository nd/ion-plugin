package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonElementType;
import ion.psi.IonTypeFunc;

public interface IonTypeStubFunc extends IonTypeStub<IonTypeFunc> {

  class Impl extends IonTypeStubBase<IonTypeFunc> implements IonTypeStubFunc {
    public Impl(StubElement parent) {
      super(parent, IonElementType.TYPE_FUNC);
    }
  }

}

package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonElementType;
import ion.psi.IonTypeArray;

public interface IonTypeStubArray extends IonTypeStub<IonTypeArray> {

  class Impl extends IonTypeStubBase<IonTypeArray> implements IonTypeStubArray {
    public Impl(StubElement parent) {
      super(parent, IonElementType.TYPE_ARRAY);
    }
  }

}

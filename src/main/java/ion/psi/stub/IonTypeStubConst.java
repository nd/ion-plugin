package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonElementType;
import ion.psi.IonTypeConst;

public interface IonTypeStubConst extends IonTypeStub<IonTypeConst> {

  class Impl extends IonTypeStubBase<IonTypeConst> implements IonTypeStubConst {
    public Impl(StubElement parent) {
      super(parent, IonElementType.TYPE_CONST);
    }
  }

}

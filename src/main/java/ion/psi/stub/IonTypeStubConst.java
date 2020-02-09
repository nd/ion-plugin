package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonElementType;
import ion.psi.IonTypeConst;

public class IonTypeStubConst extends IonTypeStubBase<IonTypeConst> implements IonTypeStub<IonTypeConst> {
  public IonTypeStubConst(StubElement parent) {
    super(parent, IonElementType.TYPE_CONST);
  }
}

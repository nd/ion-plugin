package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonElementType;
import ion.psi.IonTypePar;

public class IonTypeStubPar extends IonTypeStubBase<IonTypePar> implements IonTypeStub<IonTypePar> {
  public IonTypeStubPar(StubElement parent) {
    super(parent, IonElementType.TYPE_PAR);
  }
}


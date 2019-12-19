package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonElementType;
import ion.psi.IonTypePar;

public interface IonTypeStubPar extends IonTypeStub<IonTypePar> {

  class Impl extends IonTypeStubBase<IonTypePar> implements IonTypeStubPar {
    public Impl(StubElement parent) {
      super(parent, IonElementType.TYPE_PAR);
    }
  }

}


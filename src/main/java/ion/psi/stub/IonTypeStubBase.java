package ion.psi.stub;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import ion.psi.IonType;

public class IonTypeStubBase<T extends IonType> extends StubBase<T> implements IonTypeStub<T> {
  public IonTypeStubBase(StubElement parent, IStubElementType elementType) {
    super(parent, elementType);
  }
}

package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonElementType;
import ion.psi.IonTypeName;
import org.jetbrains.annotations.Nullable;

public class IonTypeStubName extends IonTypeStubBase<IonTypeName> implements IonTypeStub<IonTypeName> {
  private final String myName;

  public IonTypeStubName(StubElement parent, @Nullable String name) {
    super(parent, IonElementType.TYPE_NAME);
    myName = name;
  }

  public @Nullable String getName() {
    return myName;
  }
}

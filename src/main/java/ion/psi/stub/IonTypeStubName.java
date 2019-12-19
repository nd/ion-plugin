package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonElementType;
import ion.psi.IonTypeName;
import org.jetbrains.annotations.Nullable;

public interface IonTypeStubName extends IonTypeStub<IonTypeName> {

  @Nullable
  String getName();

  class Impl extends IonTypeStubBase<IonTypeName> implements IonTypeStubName {
    private final String myName;
    public Impl(StubElement parent, @Nullable String name) {
      super(parent, IonElementType.TYPE_NAME);
      myName = name;
    }

    @Override
    public @Nullable String getName() {
      return myName;
    }
  }

}

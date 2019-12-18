package ion.psi.stub;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclFunc;
import ion.psi.IonElementType;
import org.jetbrains.annotations.Nullable;

public interface IonDeclStubFunc extends IonDeclStub<IonDeclFunc> {

  class Impl extends StubBase<IonDeclFunc> implements IonDeclStubFunc {
    private final String myName;

    public Impl(StubElement parent, @Nullable String name) {
      super(parent, IonElementType.DECL_FUNC);
      myName = name;
    }

    @Override
    @Nullable
    public String getName() {
      return myName;
    }
  }
}

package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclNote;
import ion.psi.IonElementType;
import org.jetbrains.annotations.Nullable;

public interface IonDeclStubNote extends IonDeclStub<IonDeclNote> {

  class Impl extends IonDeclStubBase<IonDeclNote> implements IonDeclStubNote {
    public Impl(StubElement parent, @Nullable String name) {
      super(parent, IonElementType.DECL_NOTE, name);
    }
  }

}

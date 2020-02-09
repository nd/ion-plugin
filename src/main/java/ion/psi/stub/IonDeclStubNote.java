package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclNote;
import ion.psi.IonElementType;
import org.jetbrains.annotations.Nullable;

public class IonDeclStubNote extends IonDeclStubBase<IonDeclNote> implements IonDeclStub<IonDeclNote> {
  public IonDeclStubNote(StubElement parent, @Nullable String name) {
    super(parent, IonElementType.DECL_NOTE, name);
  }
}

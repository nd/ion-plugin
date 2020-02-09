package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclTypedef;
import ion.psi.IonElementType;
import org.jetbrains.annotations.Nullable;

public class IonDeclStubTypedef extends IonDeclStubBase<IonDeclTypedef> implements IonDeclStub<IonDeclTypedef> {
  public IonDeclStubTypedef(StubElement parent, @Nullable String name) {
    super(parent, IonElementType.DECL_TYPEDEF, name);
  }
}

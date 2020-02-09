package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclFunc;
import ion.psi.IonElementType;
import org.jetbrains.annotations.Nullable;

public class IonDeclStubFunc extends IonDeclStubBase<IonDeclFunc> implements IonDeclStub<IonDeclFunc> {
  public IonDeclStubFunc(StubElement parent, @Nullable String name) {
    super(parent, IonElementType.DECL_FUNC, name);
  }
}

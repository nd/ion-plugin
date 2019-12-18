package ion.psi.stub;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclVar;
import ion.psi.IonElementType;
import org.jetbrains.annotations.Nullable;

public class IonDeclVarStubImpl extends StubBase<IonDeclVar> implements IonDeclVarStub {
  private final String myName;

  public IonDeclVarStubImpl(StubElement parent, @Nullable String name) {
    super(parent, IonElementType.DECL_VAR);
    myName = name;
  }

  @Override
  @Nullable
  public String getName() {
    return myName;
  }
}

package ion.psi.stub;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclVar;
import ion.psi.IonElementType;
import org.jetbrains.annotations.Nullable;

public class IonDeclStubVarImpl extends StubBase<IonDeclVar> implements IonDeclStubVar {
  private final String myName;

  public IonDeclStubVarImpl(StubElement parent, @Nullable String name) {
    super(parent, IonElementType.DECL_VAR);
    myName = name;
  }

  @Override
  @Nullable
  public String getName() {
    return myName;
  }
}

package ion.psi.stub;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclConst;
import ion.psi.IonElementType;
import org.jetbrains.annotations.Nullable;

public class IonDeclStubConstImpl extends StubBase<IonDeclConst> implements IonDeclStubConst {
  private final String myName;

  public IonDeclStubConstImpl(StubElement parent, @Nullable String name) {
    super(parent, IonElementType.DECL_CONST);
    myName = name;
  }

  @Override
  @Nullable
  public String getName() {
    return myName;
  }
}

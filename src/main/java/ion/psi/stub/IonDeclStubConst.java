package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclConst;
import ion.psi.IonElementType;
import org.jetbrains.annotations.Nullable;

public class IonDeclStubConst extends IonDeclStubBase<IonDeclConst> implements IonDeclStub<IonDeclConst> {
  public IonDeclStubConst(StubElement parent, @Nullable String name) {
    super(parent, IonElementType.DECL_CONST, name);
  }
}

package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclEnum;
import ion.psi.IonElementType;
import org.jetbrains.annotations.Nullable;

public class IonDeclStubEnum extends IonDeclStubBase<IonDeclEnum> implements IonDeclStub<IonDeclEnum> {
  public IonDeclStubEnum(StubElement parent, @Nullable String name) {
    super(parent, IonElementType.DECL_ENUM, name);
  }
}


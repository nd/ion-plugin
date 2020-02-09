package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclFieldName;
import ion.psi.IonElementType;
import org.jetbrains.annotations.Nullable;

public class IonDeclStubFieldName extends IonDeclStubBase<IonDeclFieldName> implements IonDeclStub<IonDeclFieldName> {
  public IonDeclStubFieldName(StubElement parent, @Nullable String name) {
    super(parent, IonElementType.DECL_FIELD_NAME, name);
  }
}

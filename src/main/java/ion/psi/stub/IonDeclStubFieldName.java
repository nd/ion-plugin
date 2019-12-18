package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclFieldName;
import ion.psi.IonElementType;
import org.jetbrains.annotations.Nullable;

public interface IonDeclStubFieldName extends IonDeclStub<IonDeclFieldName> {

  class Impl extends IonDeclStubBase<IonDeclFieldName> implements IonDeclStubFieldName {
    public Impl(StubElement parent, @Nullable String name) {
      super(parent, IonElementType.DECL_FIELD_NAME, name);
    }
  }

}

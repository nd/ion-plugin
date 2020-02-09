package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclVar;
import ion.psi.IonElementType;
import org.jetbrains.annotations.Nullable;

public class IonDeclStubVar extends IonDeclStubBase<IonDeclVar> {
  public IonDeclStubVar(StubElement parent, @Nullable String name) {
    super(parent, IonElementType.DECL_VAR, name);
  }
}

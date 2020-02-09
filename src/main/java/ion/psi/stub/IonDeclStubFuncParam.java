package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclFuncParam;
import ion.psi.IonElementType;
import org.jetbrains.annotations.Nullable;

public class IonDeclStubFuncParam extends IonDeclStubBase<IonDeclFuncParam> implements IonDeclStub<IonDeclFuncParam> {
  public IonDeclStubFuncParam(StubElement parent, @Nullable String name) {
    super(parent, IonElementType.DECL_FUNC_PARAM, name);
  }
}


package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclFuncParam;
import ion.psi.IonElementType;
import org.jetbrains.annotations.Nullable;

public interface IonDeclStubFuncParam extends IonDeclStub<IonDeclFuncParam> {

  class Impl extends IonDeclStubBase<IonDeclFuncParam> implements IonDeclStubFuncParam {
    public Impl(StubElement parent, @Nullable String name) {
      super(parent, IonElementType.DECL_FUNC_PARAM, name);
    }
  }

}


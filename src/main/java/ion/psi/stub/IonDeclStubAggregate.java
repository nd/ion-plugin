package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclAggregate;
import ion.psi.IonElementType;
import org.jetbrains.annotations.Nullable;

public interface IonDeclStubAggregate extends IonDeclStub<IonDeclAggregate> {

  class Impl extends IonDeclStubBase<IonDeclAggregate> implements IonDeclStubAggregate {
    public Impl(StubElement parent, @Nullable String name) {
      super(parent, IonElementType.DECL_AGGREGATE, name);
    }
  }

}

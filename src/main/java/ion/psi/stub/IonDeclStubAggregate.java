package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclAggregate;
import ion.psi.IonElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IonDeclStubAggregate extends IonDeclStub<IonDeclAggregate> {

  @NotNull
  IonDeclAggregate.Kind getKind();

  class Impl extends IonDeclStubBase<IonDeclAggregate> implements IonDeclStubAggregate {
    private final IonDeclAggregate.Kind myKind;

    public Impl(StubElement parent, @Nullable String name, @NotNull IonDeclAggregate.Kind kind) {
      super(parent, IonElementType.DECL_AGGREGATE, name);
      myKind = kind;
    }

    @Override
    public @NotNull IonDeclAggregate.Kind getKind() {
      return myKind;
    }
  }

}

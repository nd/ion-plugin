package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclAggregate;
import ion.psi.IonElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonDeclStubAggregate extends IonDeclStubBase<IonDeclAggregate> implements IonDeclStub<IonDeclAggregate> {
  private final IonDeclAggregate.Kind myKind;

  public IonDeclStubAggregate(StubElement parent, @Nullable String name, @NotNull IonDeclAggregate.Kind kind) {
    super(parent, IonElementType.DECL_AGGREGATE, name);
    myKind = kind;
  }

  public @NotNull IonDeclAggregate.Kind getKind() {
    return myKind;
  }
}

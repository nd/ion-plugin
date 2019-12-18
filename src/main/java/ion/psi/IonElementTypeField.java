package ion.psi;

import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import ion.psi.stub.IonDeclStubField;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class IonElementTypeField extends IonStubElementType<IonDeclStubField, IonDeclField> {
  public IonElementTypeField(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
    super(debugName, typeId);
  }

  @NotNull
  @Override
  public IonDeclStubField createStub(@NotNull IonDeclField psi, StubElement parentStub) {
    return new IonDeclStubField.Impl(parentStub);
  }

  @Override
  public IonDeclField createPsi(@NotNull IonDeclStubField stub) {
    return new IonDeclFieldPsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonDeclStubField stub, @NotNull StubOutputStream dataStream) throws IOException {
  }

  @NotNull
  @Override
  public IonDeclStubField deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new IonDeclStubField.Impl(parentStub);
  }
}


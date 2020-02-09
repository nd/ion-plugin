package ion.psi;

import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import ion.psi.stub.IonDeclStubTypedef;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class IonElementTypeTypedef extends IonStubElementType<IonDeclStubTypedef, IonDeclTypedef> {
  public IonElementTypeTypedef(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
    super(debugName, typeId);
  }

  @NotNull
  @Override
  public IonDeclStubTypedef createStub(@NotNull IonDeclTypedef psi, StubElement parentStub) {
    return new IonDeclStubTypedef(parentStub, psi.getName());
  }

  @Override
  public IonDeclTypedef createPsi(@NotNull IonDeclStubTypedef stub) {
    return new IonDeclTypedefPsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonDeclStubTypedef stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getName());
  }

  @NotNull
  @Override
  public IonDeclStubTypedef deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new IonDeclStubTypedef(parentStub, dataStream.readNameString());
  }
}

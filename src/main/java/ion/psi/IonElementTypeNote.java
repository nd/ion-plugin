package ion.psi;

import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import ion.psi.stub.IonDeclStubNote;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class IonElementTypeNote extends IonStubElementType<IonDeclStubNote, IonDeclNote> {
  public IonElementTypeNote(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
    super(debugName, typeId);
  }

  @NotNull
  @Override
  public IonDeclStubNote createStub(@NotNull IonDeclNote psi, StubElement parentStub) {
    return new IonDeclStubNote(parentStub, psi.getName());
  }

  @Override
  public IonDeclNote createPsi(@NotNull IonDeclStubNote stub) {
    return new IonDeclNotePsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonDeclStubNote stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getName());
  }

  @NotNull
  @Override
  public IonDeclStubNote deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new IonDeclStubNote(parentStub, dataStream.readNameString());
  }
}


package ion.psi;

import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import ion.psi.stub.IonDeclStubEnum;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class IonElementTypeEnum extends IonStubElementType<IonDeclStubEnum, IonDeclEnum> {
  public IonElementTypeEnum(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
    super(debugName, typeId);
  }

  @NotNull
  @Override
  public IonDeclStubEnum createStub(@NotNull IonDeclEnum psi, StubElement parentStub) {
    return new IonDeclStubEnum.Impl(parentStub, psi.getName());
  }

  @Override
  public IonDeclEnum createPsi(@NotNull IonDeclStubEnum stub) {
    return new IonDeclEnumPsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonDeclStubEnum stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getName());
  }

  @NotNull
  @Override
  public IonDeclStubEnum deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new IonDeclStubEnum.Impl(parentStub, dataStream.readNameString());
  }
}

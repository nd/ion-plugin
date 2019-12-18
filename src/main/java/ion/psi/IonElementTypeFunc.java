package ion.psi;

import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import ion.psi.stub.IonDeclStubFunc;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class IonElementTypeFunc extends IonStubElementType<IonDeclStubFunc, IonDeclFunc> {
  public IonElementTypeFunc(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
    super(debugName, typeId);
  }

  @NotNull
  @Override
  public IonDeclStubFunc createStub(@NotNull IonDeclFunc psi, StubElement parentStub) {
    return new IonDeclStubFunc.Impl(parentStub, psi.getName());
  }

  @Override
  public IonDeclFuncPsi createPsi(@NotNull IonDeclStubFunc stub) {
    return new IonDeclFuncPsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonDeclStubFunc stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getName());
  }

  @NotNull
  @Override
  public IonDeclStubFunc deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new IonDeclStubFunc.Impl(parentStub, dataStream.readNameString());
  }
}

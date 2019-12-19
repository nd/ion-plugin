package ion.psi;

import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import ion.psi.stub.IonDeclStubFuncParam;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class IonElementTypeFuncParam extends IonStubElementType<IonDeclStubFuncParam, IonDeclFuncParam> {
  public IonElementTypeFuncParam(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
    super(debugName, typeId);
  }

  @NotNull
  @Override
  public IonDeclStubFuncParam createStub(@NotNull IonDeclFuncParam psi, StubElement parentStub) {
    return new IonDeclStubFuncParam.Impl(parentStub, psi.getName());
  }

  @Override
  public IonDeclFuncParam createPsi(@NotNull IonDeclStubFuncParam stub) {
    return new IonDeclFuncParamPsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonDeclStubFuncParam stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getName());
  }

  @NotNull
  @Override
  public IonDeclStubFuncParam deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new IonDeclStubFuncParam.Impl(parentStub, dataStream.readNameString());
  }
}


package ion.psi;

import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import ion.psi.stub.IonTypeStubName;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class IonElementTypeTypeName extends IonStubElementType<IonTypeStubName, IonTypeName> {
  public IonElementTypeTypeName(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
    super(debugName, typeId);
  }

  @NotNull
  @Override
  public IonTypeStubName createStub(@NotNull IonTypeName psi, StubElement parentStub) {
    return new IonTypeStubName.Impl(parentStub, psi.getName());
  }

  @Override
  public IonTypeName createPsi(@NotNull IonTypeStubName stub) {
    return new IonTypeNamePsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonTypeStubName stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getName());
  }

  @NotNull
  @Override
  public IonTypeStubName deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new IonTypeStubName.Impl(parentStub, dataStream.readNameString());
  }

  @Override
  public void indexStub(@NotNull IonTypeStubName stub, @NotNull IndexSink sink) {
    // don't index
  }
}
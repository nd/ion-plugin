package ion.psi;

import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import ion.psi.stub.IonTypeStubArray;
import org.jetbrains.annotations.NotNull;

public class IonElementTypeTypeArray extends IonStubElementType<IonTypeStubArray, IonTypeArray> {
  public IonElementTypeTypeArray(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
    super(debugName, typeId);
  }

  @NotNull
  @Override
  public IonTypeStubArray createStub(@NotNull IonTypeArray psi, StubElement parentStub) {
    return new IonTypeStubArray(parentStub);
  }

  @Override
  public IonTypeArray createPsi(@NotNull IonTypeStubArray stub) {
    return new IonTypeArrayPsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonTypeStubArray stub, @NotNull StubOutputStream dataStream) {
  }

  @NotNull
  @Override
  public IonTypeStubArray deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) {
    return new IonTypeStubArray(parentStub);
  }

  @Override
  public void indexStub(@NotNull IonTypeStubArray stub, @NotNull IndexSink sink) {
    // don't index
  }
}

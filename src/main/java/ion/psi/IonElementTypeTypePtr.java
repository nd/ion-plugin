package ion.psi;

import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import ion.psi.stub.IonTypeStubPtr;
import org.jetbrains.annotations.NotNull;

public class IonElementTypeTypePtr extends IonStubElementType<IonTypeStubPtr, IonTypePtr> {
  public IonElementTypeTypePtr(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
    super(debugName, typeId);
  }

  @NotNull
  @Override
  public IonTypeStubPtr createStub(@NotNull IonTypePtr psi, StubElement parentStub) {
    return new IonTypeStubPtr.Impl(parentStub);
  }

  @Override
  public IonTypePtr createPsi(@NotNull IonTypeStubPtr stub) {
    return new IonTypePtrPsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonTypeStubPtr stub, @NotNull StubOutputStream dataStream) {
  }

  @NotNull
  @Override
  public IonTypeStubPtr deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) {
    return new IonTypeStubPtr.Impl(parentStub);
  }

  @Override
  public void indexStub(@NotNull IonTypeStubPtr stub, @NotNull IndexSink sink) {
    // don't index
  }
}

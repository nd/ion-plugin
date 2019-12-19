package ion.psi;

import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import ion.psi.stub.IonTypeStubFunc;
import org.jetbrains.annotations.NotNull;

public class IonElementTypeTypeFunc extends IonStubElementType<IonTypeStubFunc, IonTypeFunc> {
  public IonElementTypeTypeFunc(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
    super(debugName, typeId);
  }

  @NotNull
  @Override
  public IonTypeStubFunc createStub(@NotNull IonTypeFunc psi, StubElement parentStub) {
    return new IonTypeStubFunc.Impl(parentStub);
  }

  @Override
  public IonTypeFunc createPsi(@NotNull IonTypeStubFunc stub) {
    return new IonTypeFuncPsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonTypeStubFunc stub, @NotNull StubOutputStream dataStream) {
  }

  @NotNull
  @Override
  public IonTypeStubFunc deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) {
    return new IonTypeStubFunc.Impl(parentStub);
  }

  @Override
  public void indexStub(@NotNull IonTypeStubFunc stub, @NotNull IndexSink sink) {
    // don't index
  }
}

package ion.psi;

import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import ion.psi.stub.IonTypeStubPar;
import org.jetbrains.annotations.NotNull;

public class IonElementTypeTypePar extends IonStubElementType<IonTypeStubPar, IonTypePar> {
  public IonElementTypeTypePar(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
    super(debugName, typeId);
  }

  @NotNull
  @Override
  public IonTypeStubPar createStub(@NotNull IonTypePar psi, StubElement parentStub) {
    return new IonTypeStubPar.Impl(parentStub);
  }

  @Override
  public IonTypePar createPsi(@NotNull IonTypeStubPar stub) {
    return new IonTypeParPsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonTypeStubPar stub, @NotNull StubOutputStream dataStream) {
  }

  @NotNull
  @Override
  public IonTypeStubPar deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) {
    return new IonTypeStubPar.Impl(parentStub);
  }

  @Override
  public void indexStub(@NotNull IonTypeStubPar stub, @NotNull IndexSink sink) {
    // don't index
  }
}

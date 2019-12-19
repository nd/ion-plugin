package ion.psi;

import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import ion.psi.stub.IonTypeStubConst;
import org.jetbrains.annotations.NotNull;

public class IonElementTypeTypeConst extends IonStubElementType<IonTypeStubConst, IonTypeConst> {
  public IonElementTypeTypeConst(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
    super(debugName, typeId);
  }

  @NotNull
  @Override
  public IonTypeStubConst createStub(@NotNull IonTypeConst psi, StubElement parentStub) {
    return new IonTypeStubConst.Impl(parentStub);
  }

  @Override
  public IonTypeConst createPsi(@NotNull IonTypeStubConst stub) {
    return new IonTypeConstPsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonTypeStubConst stub, @NotNull StubOutputStream dataStream) {
  }

  @NotNull
  @Override
  public IonTypeStubConst deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) {
    return new IonTypeStubConst.Impl(parentStub);
  }

  @Override
  public void indexStub(@NotNull IonTypeStubConst stub, @NotNull IndexSink sink) {
    // don't index
  }
}


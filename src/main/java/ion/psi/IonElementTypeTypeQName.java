package ion.psi;

import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import ion.psi.stub.IonTypeStubQName;
import org.jetbrains.annotations.NotNull;

public class IonElementTypeTypeQName extends IonStubElementType<IonTypeStubQName, IonTypeQName> {
  public IonElementTypeTypeQName(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
    super(debugName, typeId);
  }

  @NotNull
  @Override
  public IonTypeStubQName createStub(@NotNull IonTypeQName psi, StubElement parentStub) {
    return new IonTypeStubQName(parentStub);
  }

  @Override
  public IonTypeQName createPsi(@NotNull IonTypeStubQName stub) {
    return new IonTypeQNamePsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonTypeStubQName stub, @NotNull StubOutputStream dataStream) {
  }

  @NotNull
  @Override
  public IonTypeStubQName deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) {
    return new IonTypeStubQName(parentStub);
  }

  @Override
  public void indexStub(@NotNull IonTypeStubQName stub, @NotNull IndexSink sink) {
    // don't index
  }
}

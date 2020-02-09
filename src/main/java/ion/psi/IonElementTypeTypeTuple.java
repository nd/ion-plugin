package ion.psi;

import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import ion.psi.stub.IonTypeStubTuple;
import org.jetbrains.annotations.NotNull;

public class IonElementTypeTypeTuple extends IonStubElementType<IonTypeStubTuple, IonTypeTuple> {
  public IonElementTypeTypeTuple(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
    super(debugName, typeId);
  }

  @NotNull
  @Override
  public IonTypeStubTuple createStub(@NotNull IonTypeTuple psi, StubElement parentStub) {
    return new IonTypeStubTuple(parentStub);
  }

  @Override
  public IonTypeTuple createPsi(@NotNull IonTypeStubTuple stub) {
    return new IonTypeTuplePsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonTypeStubTuple stub, @NotNull StubOutputStream dataStream) {
  }

  @NotNull
  @Override
  public IonTypeStubTuple deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) {
    return new IonTypeStubTuple(parentStub);
  }

  @Override
  public void indexStub(@NotNull IonTypeStubTuple stub, @NotNull IndexSink sink) {
    // don't index
  }
}


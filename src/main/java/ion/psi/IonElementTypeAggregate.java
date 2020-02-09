package ion.psi;

import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import ion.psi.stub.IonDeclStubAggregate;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class IonElementTypeAggregate extends IonStubElementType<IonDeclStubAggregate, IonDeclAggregate> {
  public IonElementTypeAggregate(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
    super(debugName, typeId);
  }

  @NotNull
  @Override
  public IonDeclStubAggregate createStub(@NotNull IonDeclAggregate psi, StubElement parentStub) {
    return new IonDeclStubAggregate(parentStub, psi.getName(), psi.getKind());
  }

  @Override
  public IonDeclAggregate createPsi(@NotNull IonDeclStubAggregate stub) {
    return new IonDeclAggregatePsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonDeclStubAggregate stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getName());
    dataStream.writeInt(stub.getKind().ordinal());
  }

  @NotNull
  @Override
  public IonDeclStubAggregate deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    String name = dataStream.readNameString();
    int kindOrdinal = dataStream.readInt();
    IonDeclAggregate.Kind kind = IonDeclAggregate.Kind.values()[kindOrdinal];
    return new IonDeclStubAggregate(parentStub, name, kind);
  }
}


package ion.psi;

import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import ion.psi.stub.IonDeclStubFieldName;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class IonElementTypeFieldName extends IonStubElementType<IonDeclStubFieldName, IonDeclFieldName> {
  public IonElementTypeFieldName(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
    super(debugName, typeId);
  }

  @NotNull
  @Override
  public IonDeclStubFieldName createStub(@NotNull IonDeclFieldName psi, StubElement parentStub) {
    return new IonDeclStubFieldName(parentStub, psi.getName());
  }

  @Override
  public IonDeclFieldName createPsi(@NotNull IonDeclStubFieldName stub) {
    return new IonDeclFieldNamePsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonDeclStubFieldName stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getName());
  }

  @NotNull
  @Override
  public IonDeclStubFieldName deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new IonDeclStubFieldName(parentStub, dataStream.readNameString());
  }

  @Override
  public void indexStub(@NotNull IonDeclStubFieldName stub, @NotNull IndexSink sink) {
    // do not include field names into index
  }
}

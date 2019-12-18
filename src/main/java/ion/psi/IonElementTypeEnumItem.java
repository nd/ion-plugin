package ion.psi;

import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import ion.psi.stub.IonDeclStubEnumItem;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class IonElementTypeEnumItem extends IonStubElementType<IonDeclStubEnumItem, IonDeclEnumItem> {
  public IonElementTypeEnumItem(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
    super(debugName, typeId);
  }

  @NotNull
  @Override
  public IonDeclStubEnumItem createStub(@NotNull IonDeclEnumItem psi, StubElement parentStub) {
    return new IonDeclStubEnumItem.Impl(parentStub, psi.getName());
  }

  @Override
  public IonDeclEnumItem createPsi(@NotNull IonDeclStubEnumItem stub) {
    return new IonDeclEnumItemPsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonDeclStubEnumItem stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getName());
  }

  @NotNull
  @Override
  public IonDeclStubEnumItem deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new IonDeclStubEnumItem.Impl(parentStub, dataStream.readNameString());
  }
}

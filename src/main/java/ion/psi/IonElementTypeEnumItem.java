package ion.psi;

import com.intellij.lang.LighterAST;
import com.intellij.lang.LighterASTNode;
import com.intellij.lang.LighterASTTokenNode;
import com.intellij.psi.impl.source.tree.LightTreeUtil;
import com.intellij.psi.stubs.*;
import ion.IonLanguage;
import ion.psi.stub.IonDeclStubEnumItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class IonElementTypeEnumItem extends ILightStubElementType<IonDeclStubEnumItem, IonDeclEnumItem> implements IonElementTypeIdOwner {
  private final IonElementType.TypeId myTypeId;

  public IonElementTypeEnumItem(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
    super(debugName, IonLanguage.INSTANCE);
    myTypeId = typeId;
  }

  @Override
  @Nullable
  public IonElementType.TypeId getTypeId() {
    return myTypeId;
  }

  @Override
  public @NotNull String getExternalId() {
    return IonLanguage.INSTANCE.getID() + "." + toString();
  }

  @NotNull
  @Override
  public IonDeclStubEnumItem createStub(@NotNull LighterAST tree, @NotNull LighterASTNode node, @NotNull StubElement parentStub) {
    LighterASTNode nameNode = LightTreeUtil.firstChildOfType(tree, node, IonToken.NAME);
    String name = nameNode != null ? ((LighterASTTokenNode)nameNode).getText().toString() : null;
    return new IonDeclStubEnumItem.Impl(parentStub, name);
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

  @Override
  public void indexStub(@NotNull IonDeclStubEnumItem stub, @NotNull IndexSink sink) {
    String name = stub.getName();
    if (name != null) {
      sink.occurrence(IonNameIndex.KEY, name);
    }
  }
}

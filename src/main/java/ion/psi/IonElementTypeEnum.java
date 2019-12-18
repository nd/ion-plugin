package ion.psi;

import com.intellij.lang.LighterAST;
import com.intellij.lang.LighterASTNode;
import com.intellij.lang.LighterASTTokenNode;
import com.intellij.psi.impl.source.tree.LightTreeUtil;
import com.intellij.psi.stubs.*;
import ion.IonLanguage;
import ion.psi.stub.IonDeclStubEnum;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class IonElementTypeEnum extends ILightStubElementType<IonDeclStubEnum, IonDeclEnum> implements IonElementTypeIdOwner {
  private final IonElementType.TypeId myTypeId;

  public IonElementTypeEnum(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
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
  public IonDeclStubEnum createStub(@NotNull LighterAST tree, @NotNull LighterASTNode node, @NotNull StubElement parentStub) {
    LighterASTNode nameNode = LightTreeUtil.firstChildOfType(tree, node, IonToken.NAME);
    String name = nameNode != null ? ((LighterASTTokenNode)nameNode).getText().toString() : null;
    return new IonDeclStubEnum.Impl(parentStub, name);
  }

  @NotNull
  @Override
  public IonDeclStubEnum createStub(@NotNull IonDeclEnum psi, StubElement parentStub) {
    return new IonDeclStubEnum.Impl(parentStub, psi.getName());
  }

  @Override
  public IonDeclEnum createPsi(@NotNull IonDeclStubEnum stub) {
    return new IonDeclEnumPsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonDeclStubEnum stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getName());
  }

  @NotNull
  @Override
  public IonDeclStubEnum deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new IonDeclStubEnum.Impl(parentStub, dataStream.readNameString());
  }

  @Override
  public void indexStub(@NotNull IonDeclStubEnum stub, @NotNull IndexSink sink) {
    String name = stub.getName();
    if (name != null) {
      sink.occurrence(IonNameIndex.KEY, name);
    }
  }
}



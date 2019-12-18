package ion.psi;

import com.intellij.lang.LighterAST;
import com.intellij.lang.LighterASTNode;
import com.intellij.lang.LighterASTTokenNode;
import com.intellij.psi.impl.source.tree.LightTreeUtil;
import com.intellij.psi.stubs.*;
import ion.IonLanguage;
import ion.psi.stub.IonDeclStubConst;
import ion.psi.stub.IonDeclStubConstImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class IonElementTypeConst extends ILightStubElementType<IonDeclStubConst, IonDeclConst> implements IonElementTypeIdOwner {
  private final IonElementType.TypeId myTypeId;

  public IonElementTypeConst(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
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
  public IonDeclStubConst createStub(@NotNull LighterAST tree, @NotNull LighterASTNode node, @NotNull StubElement parentStub) {
    LighterASTNode nameNode = LightTreeUtil.firstChildOfType(tree, node, IonToken.NAME);
    String name = nameNode != null ? ((LighterASTTokenNode)nameNode).getText().toString() : null;
    return new IonDeclStubConstImpl(parentStub, name);
  }

  @NotNull
  @Override
  public IonDeclStubConst createStub(@NotNull IonDeclConst psi, StubElement parentStub) {
    return new IonDeclStubConstImpl(parentStub, psi.getName());
  }

  @Override
  public IonDeclConst createPsi(@NotNull IonDeclStubConst stub) {
    return new IonDeclConstPsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonDeclStubConst stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getName());
  }

  @NotNull
  @Override
  public IonDeclStubConst deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new IonDeclStubConstImpl(parentStub, dataStream.readNameString());
  }

  @Override
  public void indexStub(@NotNull IonDeclStubConst stub, @NotNull IndexSink sink) {
    String name = stub.getName();
    if (name != null) {
      sink.occurrence(IonNameIndex.KEY, name);
    }
  }
}

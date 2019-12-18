package ion.psi;

import com.intellij.lang.LighterAST;
import com.intellij.lang.LighterASTNode;
import com.intellij.lang.LighterASTTokenNode;
import com.intellij.psi.impl.source.tree.LightTreeUtil;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import ion.psi.stub.IonDeclStubConst;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class IonElementTypeConst extends IonStubElementType<IonDeclStubConst, IonDeclConst> {
  public IonElementTypeConst(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
    super(debugName, typeId);
  }

  @NotNull
  @Override
  public IonDeclStubConst createStub(@NotNull LighterAST tree, @NotNull LighterASTNode node, @NotNull StubElement parentStub) {
    LighterASTNode nameNode = LightTreeUtil.firstChildOfType(tree, node, IonToken.NAME);
    String name = nameNode != null ? ((LighterASTTokenNode)nameNode).getText().toString() : null;
    return new IonDeclStubConst.Impl(parentStub, name);
  }

  @NotNull
  @Override
  public IonDeclStubConst createStub(@NotNull IonDeclConst psi, StubElement parentStub) {
    return new IonDeclStubConst.Impl(parentStub, psi.getName());
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
    return new IonDeclStubConst.Impl(parentStub, dataStream.readNameString());
  }
}

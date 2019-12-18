package ion.psi;

import com.intellij.lang.LighterAST;
import com.intellij.lang.LighterASTNode;
import com.intellij.lang.LighterASTTokenNode;
import com.intellij.psi.impl.source.tree.LightTreeUtil;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import ion.psi.stub.IonDeclStubVar;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class IonElementTypeVar extends IonStubElementType<IonDeclStubVar, IonDeclVar> {
  public IonElementTypeVar(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
    super(debugName, typeId);
  }

  @NotNull
  @Override
  public IonDeclStubVar createStub(@NotNull LighterAST tree, @NotNull LighterASTNode node, @NotNull StubElement parentStub) {
    LighterASTNode nameNode = LightTreeUtil.firstChildOfType(tree, node, IonToken.NAME);
    String name = nameNode != null ? ((LighterASTTokenNode)nameNode).getText().toString() : null;
    return new IonDeclStubVar.Impl(parentStub, name);
  }

  @NotNull
  @Override
  public IonDeclStubVar createStub(@NotNull IonDeclVar psi, StubElement parentStub) {
    return new IonDeclStubVar.Impl(parentStub, psi.getName());
  }

  @Override
  public IonDeclVar createPsi(@NotNull IonDeclStubVar stub) {
    return new IonDeclVarPsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonDeclStubVar stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getName());
  }

  @NotNull
  @Override
  public IonDeclStubVar deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new IonDeclStubVar.Impl(parentStub, dataStream.readNameString());
  }
}

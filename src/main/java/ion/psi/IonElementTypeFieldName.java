package ion.psi;

import com.intellij.lang.LighterAST;
import com.intellij.lang.LighterASTNode;
import com.intellij.lang.LighterASTTokenNode;
import com.intellij.psi.impl.source.tree.LightTreeUtil;
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
  public IonDeclStubFieldName createStub(@NotNull LighterAST tree, @NotNull LighterASTNode node, @NotNull StubElement parentStub) {
    LighterASTNode nameNode = LightTreeUtil.firstChildOfType(tree, node, IonToken.NAME);
    String name = nameNode != null ? ((LighterASTTokenNode) nameNode).getText().toString() : null;
    return new IonDeclStubFieldName.Impl(parentStub, name);
  }

  @NotNull
  @Override
  public IonDeclStubFieldName createStub(@NotNull IonDeclFieldName psi, StubElement parentStub) {
    return new IonDeclStubFieldName.Impl(parentStub, psi.getName());
  }

  @Override
  public IonDeclFieldNamePsi createPsi(@NotNull IonDeclStubFieldName stub) {
    return new IonDeclFieldNamePsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonDeclStubFieldName stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getName());
  }

  @NotNull
  @Override
  public IonDeclStubFieldName deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new IonDeclStubFieldName.Impl(parentStub, dataStream.readNameString());
  }
}

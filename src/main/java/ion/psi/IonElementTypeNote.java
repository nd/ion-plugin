package ion.psi;

import com.intellij.lang.LighterAST;
import com.intellij.lang.LighterASTNode;
import com.intellij.lang.LighterASTTokenNode;
import com.intellij.psi.impl.source.tree.LightTreeUtil;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import ion.psi.stub.IonDeclStubNote;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class IonElementTypeNote extends IonStubElementType<IonDeclStubNote, IonDeclNote> {
  public IonElementTypeNote(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
    super(debugName, typeId);
  }

  @NotNull
  @Override
  public IonDeclStubNote createStub(@NotNull LighterAST tree, @NotNull LighterASTNode node, @NotNull StubElement parentStub) {
    LighterASTNode nameNode = LightTreeUtil.firstChildOfType(tree, node, IonToken.NAME);
    String name = nameNode != null ? ((LighterASTTokenNode) nameNode).getText().toString() : null;
    return new IonDeclStubNote.Impl(parentStub, name);
  }

  @NotNull
  @Override
  public IonDeclStubNote createStub(@NotNull IonDeclNote psi, StubElement parentStub) {
    return new IonDeclStubNote.Impl(parentStub, psi.getName());
  }

  @Override
  public IonDeclNotePsi createPsi(@NotNull IonDeclStubNote stub) {
    return new IonDeclNotePsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonDeclStubNote stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getName());
  }

  @NotNull
  @Override
  public IonDeclStubNote deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new IonDeclStubNote.Impl(parentStub, dataStream.readNameString());
  }
}


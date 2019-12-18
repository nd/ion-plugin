package ion.psi;

import com.intellij.lang.LighterAST;
import com.intellij.lang.LighterASTNode;
import com.intellij.lang.LighterASTTokenNode;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.LightTreeUtil;
import com.intellij.psi.stubs.*;
import ion.IonLanguage;
import ion.psi.stub.IonDeclVarStub;
import ion.psi.stub.IonDeclVarStubImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class IonElementTypeVar extends ILightStubElementType<IonDeclVarStub, IonDeclVar> implements IonElementTypeIdOwner {
  private final IonElementType.TypeId myTypeId;

  public IonElementTypeVar(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
    super(debugName, IonLanguage.INSTANCE);
    myTypeId = typeId;
  }

  @Override
  public @Nullable IonElementType.TypeId getTypeId() {
    return myTypeId;
  }

  @Override
  public @NotNull String getExternalId() {
    return "ion.var";
  }

  @NotNull
  @Override
  public IonDeclVarStub createStub(@NotNull LighterAST tree, @NotNull LighterASTNode node, @NotNull StubElement parentStub) {
    LighterASTNode nameNode = LightTreeUtil.firstChildOfType(tree, node, IonToken.NAME);
    String name = nameNode != null ? ((LighterASTTokenNode)nameNode).getText().toString() : null;
    return new IonDeclVarStubImpl(parentStub, name);
  }

  @NotNull
  @Override
  public IonDeclVarStub createStub(@NotNull IonDeclVar psi, StubElement parentStub) {
    return new IonDeclVarStubImpl(parentStub, psi.getName());
  }

  @Override
  public IonDeclVar createPsi(@NotNull IonDeclVarStub stub) {
    return new IonDeclVarPsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonDeclVarStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getName());
  }

  @NotNull
  @Override
  public IonDeclVarStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new IonDeclVarStubImpl(parentStub, dataStream.readNameString());
  }

  @Override
  public void indexStub(@NotNull IonDeclVarStub stub, @NotNull IndexSink sink) {
    String name = stub.getName();
    if (name != null) {
      sink.occurrence(IonNameIndex.KEY, name);
    }
  }
}

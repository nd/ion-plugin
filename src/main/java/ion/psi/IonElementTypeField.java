package ion.psi;

import com.intellij.lang.LighterAST;
import com.intellij.lang.LighterASTNode;
import com.intellij.psi.stubs.*;
import ion.IonLanguage;
import ion.psi.stub.IonDeclStubField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class IonElementTypeField extends ILightStubElementType<IonDeclStubField, IonDeclField> implements IonElementTypeIdOwner {
  private final IonElementType.TypeId myTypeId;

  public IonElementTypeField(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
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
  public IonDeclStubField createStub(@NotNull LighterAST tree, @NotNull LighterASTNode node, @NotNull StubElement parentStub) {
    return new IonDeclStubField.Impl(parentStub);
  }

  @NotNull
  @Override
  public IonDeclStubField createStub(@NotNull IonDeclField psi, StubElement parentStub) {
    return new IonDeclStubField.Impl(parentStub);
  }

  @Override
  public IonDeclField createPsi(@NotNull IonDeclStubField stub) {
    return new IonDeclFieldPsi(stub, this);
  }

  @Override
  public void serialize(@NotNull IonDeclStubField stub, @NotNull StubOutputStream dataStream) throws IOException {
  }

  @NotNull
  @Override
  public IonDeclStubField deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new IonDeclStubField.Impl(parentStub);
  }

  @Override
  public void indexStub(@NotNull IonDeclStubField stub, @NotNull IndexSink sink) {
  }
}


package ion.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import ion.IonLanguage;
import ion.psi.stub.IonDeclStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class IonStubElementType<StubType extends StubElement<?>, PsiType extends PsiElement> extends IStubElementType<StubType, PsiType> implements IonElementTypeIdOwner {

  protected final IonElementType.TypeId myTypeId;

  public IonStubElementType(@NotNull String debugName, @NotNull IonElementType.TypeId typeId) {
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

  @Override
  public void indexStub(@NotNull StubType stub, @NotNull IndexSink sink) {
    if (stub instanceof IonDeclStub<?>) {
      IonDeclStub<?> declStub = (IonDeclStub<?>) stub;
      String name = declStub.getName();
      if (name != null) {
        sink.occurrence(IonNameIndex.KEY, name);
      }
    }
  }
}

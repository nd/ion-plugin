package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.tree.IElementType;
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

  @Override
  public boolean shouldCreateStub(ASTNode node) {
    IElementType type = node.getElementType();
    IonElementType.TypeId typeId = type instanceof IonElementTypeIdOwner ? ((IonElementTypeIdOwner) type).getTypeId() : null;
    if (typeId != null) {
      switch (typeId) {
        case TYPE_NAME:
        case TYPE_QNAME:
        case TYPE_PAR:
        case TYPE_PTR:
        case TYPE_CONST:
        case TYPE_ARRAY:
        case TYPE_TUPLE:
        case TYPE_FUNC:
          ASTNode parent = IonPsiUtil.getNonTypeParentNode(node);
          if (parent != null) {
            IElementType elementType = parent.getElementType();
            if (elementType == IonElementType.STMT_INIT || elementType == IonElementType.EXPR_CAST) {
              return false;
            }
          }
      }
    }
    return super.shouldCreateStub(node);
  }
}

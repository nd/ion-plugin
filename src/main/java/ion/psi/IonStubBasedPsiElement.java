package ion.psi;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.IncorrectOperationException;
import ion.IonLanguage;
import ion.psi.stub.IonDeclStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonStubBasedPsiElement<T extends StubElement<?>> extends StubBasedPsiElementBase<T> implements StubBasedPsiElement<T>, PsiNameIdentifierOwner {
  public IonStubBasedPsiElement(@NotNull ASTNode node) {
    super(node);
  }

  public IonStubBasedPsiElement(@NotNull T stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  @NotNull
  public Language getLanguage() {
    return IonLanguage.INSTANCE;
  }

  @Override
  public int getTextOffset() {
    PsiElement name = getNameIdentifier();
    return name != null ? name.getTextOffset() : super.getTextOffset();
  }

  @Override
  @Nullable
  public PsiElement getNameIdentifier() {
    ASTNode nameNode = getNode().findChildByType(IonToken.NAME);
    return nameNode != null ? nameNode.getPsi() : null;
  }

  @Override
  public String getName() {
    T greenStub = getGreenStub();
    if (greenStub instanceof IonDeclStub) {
      return ((IonDeclStub<?>) greenStub).getName();
    }
    PsiElement name = getNameIdentifier();
    return name != null ? name.getText() : super.getName();
  }

  @Override
  public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
    PsiElement nameElement = getNameIdentifier();
    if (nameElement == null) {
      throw new IncorrectOperationException("Name element is not found");
    }
    if (nameElement instanceof LeafElement) {
      ((LeafElement) nameElement).replaceWithText(name);
      return this;
    } else {
      throw new IncorrectOperationException("Name element is not a leaf element");
    }
  }
}

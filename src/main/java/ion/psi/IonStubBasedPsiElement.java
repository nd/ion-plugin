package ion.psi;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import ion.IonLanguage;
import org.jetbrains.annotations.NotNull;

public class IonStubBasedPsiElement<T extends StubElement<?>> extends StubBasedPsiElementBase<T> implements StubBasedPsiElement<T> {
  public IonStubBasedPsiElement(@NotNull T stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public IonStubBasedPsiElement(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  @NotNull
  public Language getLanguage() {
    return IonLanguage.INSTANCE;
  }
}

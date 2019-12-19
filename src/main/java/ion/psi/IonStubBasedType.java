package ion.psi;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import ion.IonLanguage;
import org.jetbrains.annotations.NotNull;

public class IonStubBasedType<T extends StubElement<?>> extends StubBasedPsiElementBase<T> implements StubBasedPsiElement<T> {
  public IonStubBasedType(@NotNull ASTNode node) {
    super(node);
  }

  public IonStubBasedType(@NotNull T stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  @NotNull
  public Language getLanguage() {
    return IonLanguage.INSTANCE;
  }
}


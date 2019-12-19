package ion.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public class IonTypeBase extends IonPsiElementBase implements IonType {
  public IonTypeBase(@NotNull ASTNode node) {
    super(node);
  }
}

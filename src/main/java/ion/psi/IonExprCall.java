package ion.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public class IonExprCall extends IonExpr {
  public IonExprCall(@NotNull ASTNode node) {
    super(node);
  }
}

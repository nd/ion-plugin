package ion.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public class IonExprUnary extends IonExpr {
  public IonExprUnary(@NotNull ASTNode node) {
    super(node);
  }
}

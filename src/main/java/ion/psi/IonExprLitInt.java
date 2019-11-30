package ion.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public class IonExprLitInt extends IonExpr {
  public IonExprLitInt(@NotNull ASTNode node) {
    super(node);
  }
}

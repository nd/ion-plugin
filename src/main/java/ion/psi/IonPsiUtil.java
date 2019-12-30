
package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.Nullable;

public class IonPsiUtil {
  @Nullable
  static ASTNode getNonTypeParentNode(@Nullable ASTNode node) {
    ASTNode parent = node != null ? node.getTreeParent() : null;
    while (parent != null) {
      IonElementTypeIdOwner type = ObjectUtils.tryCast(parent.getElementType(), IonElementTypeIdOwner.class);
      IonElementType.TypeId typeId = type != null ? type.getTypeId() : null;
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
            // continue walk up tree
            parent = parent.getTreeParent();
            break;
          default:
            return parent;
        }
      } else {
        return parent;
      }
    }
    return parent;
  }
}

package ion.psi;

import com.intellij.psi.tree.IFileElementType;
import ion.IonLanguage;

public class IonFileElementType extends IFileElementType {
  public static final IonFileElementType INSTANCE = new IonFileElementType();

  public IonFileElementType() {
    super("IonFile", IonLanguage.INSTANCE);
  }
}

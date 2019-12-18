package ion.psi;

import com.intellij.psi.stubs.PsiFileStub;
import com.intellij.psi.tree.IStubFileElementType;
import ion.IonLanguage;

public class IonFileElementType extends IStubFileElementType<PsiFileStub<?>> {
  public static final IonFileElementType INSTANCE = new IonFileElementType();

  public IonFileElementType() {
    super("IonFile", IonLanguage.INSTANCE);
  }
}

package ion.psi;

import com.intellij.psi.stubs.PsiFileStub;
import com.intellij.psi.tree.IStubFileElementType;
import ion.IonLanguage;

public class IonFileElementType extends IStubFileElementType<PsiFileStub<?>> {
  private static final int VERSION = 2;

  public static final IonFileElementType INSTANCE = new IonFileElementType();

  public IonFileElementType() {
    super("IonFile", IonLanguage.INSTANCE);
  }

  @Override
  public int getStubVersion() {
    return super.getStubVersion() + VERSION;
  }
}

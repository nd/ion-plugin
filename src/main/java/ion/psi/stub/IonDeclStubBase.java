package ion.psi.stub;

import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import org.jetbrains.annotations.Nullable;

public class IonDeclStubBase<T extends PsiElement> extends StubBase<T> implements IonDeclStub<T> {
  protected final String myName;

  public IonDeclStubBase(StubElement parent, IStubElementType elementType, @Nullable String name) {
    super(parent, elementType);
    myName = name;
  }

  @Override
  @Nullable
  public String getName() {
    return myName;
  }
}

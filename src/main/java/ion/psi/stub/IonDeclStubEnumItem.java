package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclEnumItem;
import ion.psi.IonElementType;
import org.jetbrains.annotations.Nullable;

public class IonDeclStubEnumItem extends IonDeclStubBase<IonDeclEnumItem> implements IonDeclStub<IonDeclEnumItem> {
  public IonDeclStubEnumItem(StubElement parent, @Nullable String name) {
    super(parent, IonElementType.DECL_ENUM_ITEM, name);
  }
}

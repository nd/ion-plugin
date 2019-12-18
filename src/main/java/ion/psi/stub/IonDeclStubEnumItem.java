package ion.psi.stub;

import com.intellij.psi.stubs.StubElement;
import ion.psi.IonDeclEnumItem;
import ion.psi.IonElementType;
import org.jetbrains.annotations.Nullable;

public interface IonDeclStubEnumItem extends IonDeclStub<IonDeclEnumItem> {

  class Impl extends IonDeclStubBase<IonDeclEnumItem> implements IonDeclStubEnumItem {
    public Impl(StubElement parent, @Nullable String name) {
      super(parent, IonElementType.DECL_ENUM_ITEM, name);
    }
  }

}

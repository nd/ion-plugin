package ion.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import ion.psi.stub.IonDeclStubFieldName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonDeclFieldNamePsi extends IonStubBasedPsiElement<IonDeclStubFieldName> implements IonDeclFieldName {
  public IonDeclFieldNamePsi(@NotNull ASTNode node) {
    super(node);
  }

  public IonDeclFieldNamePsi(@NotNull IonDeclStubFieldName stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "IonDeclFieldName(field_name)";
  }

  @Override
  @Nullable
  public PsiElement getType() {
    IonDeclField fieldDecl = PsiTreeUtil.getStubOrPsiParentOfType(this, IonDeclField.class);
    return fieldDecl != null ? fieldDecl.getType() : null;
  }
}

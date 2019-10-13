package ion.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import ion.IonFileType;
import ion.IonLanguage;
import org.jetbrains.annotations.NotNull;

public class IonPsiFile extends PsiFileBase {
  public IonPsiFile(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, IonLanguage.INSTANCE);
  }

  @Override
  public @NotNull FileType getFileType() {
    return IonFileType.INSTANCE;
  }
}

package ion.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ObjectUtils;
import ion.IonLanguage;
import org.jetbrains.annotations.NotNull;

public class IonElementFactory {

  public static IonPsiFile createFile(@NotNull Project project, @NotNull String text) {
    return (IonPsiFile) PsiFileFactory.getInstance(project).createFileFromText  ("elementFactory.go", IonLanguage.INSTANCE, text);
  }

  @NotNull
  public static PsiElement createName(@NotNull Project project, @NotNull String name) {
    IonPsiFile file = createFile(project, "var " + name + ";");
    IonDeclVar var = ObjectUtils.notNull(PsiTreeUtil.findChildOfType(file, IonDeclVar.class));
    return ObjectUtils.notNull(PsiTreeUtil.skipWhitespacesAndCommentsForward(var.getFirstChild()));
  }

}

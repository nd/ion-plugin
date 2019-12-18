package ion.psi;

import a.h.K;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class IonNameIndex extends StringStubIndexExtension<IonDecl> {
  public static final StubIndexKey<String, IonDecl> KEY = StubIndexKey.createIndexKey("ion.name.index");

  @Override
  public @NotNull StubIndexKey<String, IonDecl> getKey() {
    return KEY;
  }

  @Override
  public Collection<IonDecl> get(@NotNull String key, @NotNull Project project, @NotNull GlobalSearchScope scope) {
    return StubIndex.getElements(getKey(), key, project, scope, IonDecl.class);
  }
}

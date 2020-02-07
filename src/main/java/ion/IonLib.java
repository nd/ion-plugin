package ion;

import com.intellij.icons.AllIcons;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.roots.SyntheticLibrary;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IonLib extends SyntheticLibrary implements ItemPresentation {
  private final String myName;
  private final List<VirtualFile> myDirs;

  public IonLib(@NotNull String name, @NotNull List<VirtualFile> dirs) {
    myName = name;
    myDirs = dirs;
  }

  @Nullable
  @Override
  public String getPresentableText() {
    return myName;
  }

  @Nullable
  @Override
  public String getLocationString() {
    return null;
  }

  @Nullable
  @Override
  public Icon getIcon(boolean unused) {
    return AllIcons.Nodes.PpLib;
  }

  @NotNull
  @Override
  public Collection<VirtualFile> getSourceRoots() {
    List<VirtualFile> result = new ArrayList<>();
    for (VirtualFile dir : myDirs) {
      for (VirtualFile child : dir.getChildren()) {
        if (child.isDirectory()) {
          result.add(child);
        }
      }
    }
    return result;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof IonLib && ((IonLib) o).myName.equals(myName) && ((IonLib) o).myDirs.equals(myDirs);
  }

  @Override
  public int hashCode() {
    return myName.hashCode() + 31 * myDirs.hashCode();
  }

  @NotNull
  public List<VirtualFile> getDirs() {
    return myDirs;
  }
}

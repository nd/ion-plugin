package ion;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.AdditionalLibraryRootsProvider;
import com.intellij.openapi.roots.SyntheticLibrary;
import com.intellij.openapi.roots.ex.ProjectRootManagerEx;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.EnvironmentUtil;
import ion.cfg.IonConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class IonLibProvider extends AdditionalLibraryRootsProvider {
  private final static AtomicReference<List<SyntheticLibrary>> ourLibs = new AtomicReference<>();

  @Override
  @NotNull
  public Collection<SyntheticLibrary> getAdditionalProjectLibraries(@NotNull Project project) {
    computeIfNeeded();
    Collection<SyntheticLibrary> result = ourLibs.get();
    return result != null ? result : Collections.emptyList();
  }

  public static void reset() {
    ourLibs.set(null);
    Project[] projects = ProjectManager.getInstance().getOpenProjects();
    ApplicationManager.getApplication().invokeLater(() -> {
      WriteAction.run(() -> {
        for (Project project : projects) {
          ProjectRootManagerEx.getInstanceEx(project).clearScopesCachesForModules();
          ProjectRootManagerEx.getInstanceEx(project).makeRootsChange(() -> {}, true, true);
        }
      });
    });
  }

  private static void computeIfNeeded() {
    if (ourLibs.get() == null) {
      List<SyntheticLibrary> libs = new ArrayList<>();

      IonConfig config = IonConfig.getConfig();
      String ionHome = config.IONHOME;
      if (StringUtil.isEmpty(ionHome)) {
        ionHome = EnvironmentUtil.getValue("IONHOME");
      }
      if (StringUtil.isNotEmpty(ionHome)) {
        VirtualFile ionHomeDir = LocalFileSystem.getInstance().findFileByPath(ionHome);
        if (ionHomeDir != null && ionHomeDir.isDirectory()) {
          VirtualFile systemPackages = ionHomeDir.findChild("system_packages");
          if (systemPackages != null && systemPackages.isDirectory()) {
            libs.add(new IonLib("IONHOME", Arrays.asList(systemPackages)));
          }
        }
      }

      String ionPath = config.IONPATH;
      if (StringUtil.isEmpty(ionPath)) {
        ionPath = EnvironmentUtil.getValue("IONPATH");
      }
      if (StringUtil.isNotEmpty(ionPath)) {
        List<String> pathEntries = StringUtil.split(ionPath, ";");
        List<VirtualFile> dirs = new ArrayList<>();
        for (String pathEntry : pathEntries) {
          VirtualFile virtualDir = LocalFileSystem.getInstance().findFileByPath(pathEntry);
          if (virtualDir != null && virtualDir.isDirectory()) {
            dirs.add(virtualDir);
          }
        }
        if (!dirs.isEmpty()) {
          libs.add(new IonLib("IONPATH", dirs));
        }
      }

      ourLibs.compareAndSet(null, libs);
    }
  }

  @NotNull
  @Override
  public Collection<VirtualFile> getRootsToWatch(@NotNull Project project) {
    computeIfNeeded();
    List<VirtualFile> result = new ArrayList<>();
    List<SyntheticLibrary> libs = ourLibs.get();
    for (SyntheticLibrary lib : libs) {
      if (lib instanceof IonLib) {
        result.addAll(((IonLib) lib).getDirs());
      }
    }
    return result;
  }

  @NotNull
  public static List<SyntheticLibrary> getLibs() {
    computeIfNeeded();
    return ourLibs.get();
  }
}

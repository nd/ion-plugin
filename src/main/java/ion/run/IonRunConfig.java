package ion.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.filters.PatternBasedFileHyperlinkFilter;
import com.intellij.execution.filters.PatternBasedFileHyperlinkRawDataFinder;
import com.intellij.execution.filters.PatternHyperlinkFormat;
import com.intellij.execution.filters.PatternHyperlinkPart;
import com.intellij.execution.process.KillableProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizerUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ui.FormBuilder;
import ion.cfg.IonConfig;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class IonRunConfig extends RunConfigurationBase<Element> {
  private final static PatternBasedFileHyperlinkRawDataFinder LINK_FINDER = new PatternBasedFileHyperlinkRawDataFinder(
          new PatternHyperlinkFormat[]{
                  // /path/to/file.ion(line): error:
                  new PatternHyperlinkFormat(Pattern.compile("^((.+)\\((\\d+)\\)): error:"), false, false,
                          PatternHyperlinkPart.HYPERLINK, PatternHyperlinkPart.PATH, PatternHyperlinkPart.LINE),
          });

  private String myPackage;
  private String myArgs;

  public IonRunConfig(@NotNull Project project, @Nullable ConfigurationFactory factory, @Nullable String name) {
    super(project, factory, name);
  }

  @Override
  public @NotNull SettingsEditor<IonRunConfig> getConfigurationEditor() {
    return new Editor();
  }

  @Override
  public @Nullable RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) {
    IonConfig ionConfig = IonConfig.getConfig();
    String ionHome = ionConfig.IONHOME;
    String ionExec = StringUtil.isEmpty(ionHome) ? "ion" : StringUtil.trimEnd(ionHome, File.separator) + "/ion";
    List<String> args = StringUtil.isEmpty(myArgs.trim()) ? Collections.emptyList() : StringUtil.split(myArgs, " ");
    CommandLineState ionCmdState = new CommandLineState(environment) {
      @Override
      protected @NotNull ProcessHandler startProcess() throws ExecutionException {
        GeneralCommandLine cmd = new GeneralCommandLine(ionExec);
        cmd.setWorkDirectory(environment.getProject().getBasePath());
        if (!ionConfig.IONHOME.isEmpty()) {
          cmd.withEnvironment("IONHOME", ionConfig.IONHOME);
        }
        if (!ionConfig.IONPATH.isEmpty()) {
          cmd.withEnvironment("IONPATH", ionConfig.IONPATH);
        }
        if (!args.isEmpty()) {
          cmd.withParameters(args);
        }
        if (!myPackage.isEmpty()) {
          cmd.addParameter(myPackage);
        }
        return new KillableProcessHandler(cmd);
      }
    };
    ionCmdState.addConsoleFilters(new PatternBasedFileHyperlinkFilter(environment.getProject(), environment.getProject().getBasePath(), LINK_FINDER));
    return ionCmdState;
  }

  @Override
  public void readExternal(@NotNull Element element) throws InvalidDataException {
    super.readExternal(element);
    myPackage = JDOMExternalizerUtil.readField(element, "package", "");
    myArgs = JDOMExternalizerUtil.readField(element, "args", "");
  }

  @Override
  public void writeExternal(@NotNull Element element) {
    super.writeExternal(element);
    JDOMExternalizerUtil.writeField(element, "package", myPackage);
    JDOMExternalizerUtil.writeField(element, "args", myArgs);
  }

  private static class Editor extends SettingsEditor<IonRunConfig> {
    private final JTextField myPackage;
    private final JTextField myArgs;

    public Editor() {
      myPackage = new JTextField();
      myArgs = new JTextField();
    }

    @Override
    protected @NotNull JComponent createEditor() {
      return FormBuilder.createFormBuilder()
              .addLabeledComponent("Package:", myPackage)
              .addLabeledComponent("Ion arguments:", myArgs)
              .getPanel();
    }

    @Override
    protected void resetEditorFrom(@NotNull IonRunConfig config) {
      myPackage.setText(config.myPackage);
      myArgs.setText(config.myArgs);
    }

    @Override
    protected void applyEditorTo(@NotNull IonRunConfig config) throws ConfigurationException {
      config.myPackage = myPackage.getText();
      config.myArgs = myArgs.getText();
    }
  }
}

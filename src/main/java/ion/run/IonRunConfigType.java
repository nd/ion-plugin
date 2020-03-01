package ion.run;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class IonRunConfigType implements ConfigurationType {
  @Override
  public @NotNull String getDisplayName() {
    return "Ion";
  }

  @Nls
  @Override
  public String getConfigurationTypeDescription() {
    return "Ion";
  }

  @Override
  public Icon getIcon() {
    return AllIcons.RunConfigurations.Application;
  }

  @Override
  public @NotNull String getId() {
    return "ION_CONFIGURATION_TYPE";
  }

  @Override
  public ConfigurationFactory[] getConfigurationFactories() {
    return new ConfigurationFactory[]{new Factory(this)};
  }

  public static class Factory extends ConfigurationFactory {
    public Factory(@NotNull IonRunConfigType type) {
      super(type);
    }

    @Override
    public @NotNull RunConfiguration createTemplateConfiguration(@NotNull Project project) {
      return new IonRunConfig(project, this, "Ion");
    }
  }
}

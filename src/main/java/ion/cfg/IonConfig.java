package ion.cfg;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.xmlb.XmlSerializerUtil;
import ion.IonLibProvider;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

@State(name = "BitwiseIon", storages = {@Storage(StoragePathMacros.NON_ROAMABLE_FILE)})
public class IonConfig implements PersistentStateComponent<IonConfig> {
  public String IONHOME = "";
  public String IONPATH = "";

  @Nullable
  @Override
  public IonConfig getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull IonConfig state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  @NotNull
  public static IonConfig getConfig() {
    return ApplicationManager.getApplication().getComponent(IonConfig.class);
  }

  public static class UI implements SearchableConfigurable {
    private final IonConfig myConfig;
    private final JTextField myIonHome;
    private final JTextField myIonPath;
    private final JPanel myPanel;

    public UI() {
      myConfig = IonConfig.getConfig();
      myIonHome = new JTextField();
      myIonPath = new JTextField();
      myPanel = new JPanel(new BorderLayout());
      myPanel.add(FormBuilder.createFormBuilder()
              .addLabeledComponent("IONHOME", myIonHome)
              .addLabeledComponent("IONPATH", myIonPath)
              .getPanel(), BorderLayout.NORTH);
    }

    @Override
    @NotNull
    public String getId() {
      return "BitwiseIon";
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
      return "Ion";
    }

    @Override
    public @Nullable JComponent createComponent() {
      return myPanel;
    }

    @Override
    public boolean isModified() {
      return !Objects.equals(myIonHome.getText(), myConfig.IONHOME) ||
             !Objects.equals(myIonPath.getText(), myConfig.IONPATH);
    }

    @Override
    public void apply() throws ConfigurationException {
      myConfig.IONHOME = myIonHome.getText();
      myConfig.IONPATH = myIonPath.getText();
      IonLibProvider.reset();
    }

    @Override
    public void reset() {
      myIonHome.setText(myConfig.IONHOME);
      myIonPath.setText(myConfig.IONPATH);
    }
  }
}

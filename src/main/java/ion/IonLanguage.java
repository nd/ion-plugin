package ion;

import com.intellij.lang.Language;

public class IonLanguage extends Language {
  public static final IonLanguage INSTANCE = new IonLanguage();
  private IonLanguage() {
    super("ion");
  }
}

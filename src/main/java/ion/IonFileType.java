package ion;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class IonFileType extends LanguageFileType {
  public static IonFileType INSTANCE = new IonFileType();

  public IonFileType() {
    super(IonLanguage.INSTANCE);
  }

  @Override
  public @NotNull String getName() {
    return "ion";
  }

  @Override
  public @NotNull String getDescription() {
    return "ion";
  }

  @Override
  public @NotNull String getDefaultExtension() {
    return "ion";
  }

  @Override
  public @Nullable Icon getIcon() {
    return null;
  }

  public static class Factory extends FileTypeFactory {
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
      fileTypeConsumer.consume(IonFileType.INSTANCE);
    }
  }
}

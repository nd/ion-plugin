package ion;

import com.intellij.icons.AllIcons;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.lang.reflect.Field;

public class IonIcons {
  public static final Icon CONST;
  public static final Icon ENUM;
  public static final Icon ENUM_ITEM;
  public static final Icon FIELD;
  public static final Icon FUNC;
  public static final Icon PARAM;
  public static final Icon STRUCT;
  public static final Icon TYPE;
  public static final Icon UNION;
  public static final Icon VAR;

  static {
    Class<?> clionIcons = null;
    try {
      clionIcons = IonIcons.class.getClassLoader().loadClass("icons.CidrLangIcons");
    } catch (Exception ignore) {
    }
    CONST = AllIcons.Nodes.Constant;
    ENUM = getIcon(clionIcons, "CodeAssistantEnum");
    ENUM_ITEM = getIcon(clionIcons, "CodeAssistantEnumConst");
    FIELD = getIcon(clionIcons, "CodeAssistantField");
    FUNC = getIcon(clionIcons, "CodeAssistantFunction");
    PARAM = getIcon(clionIcons, "CodeAssistantParameter");
    STRUCT = getIcon(clionIcons, "CodeAssistantStruct");
    TYPE = getIcon(clionIcons, "CodeAssistantType");
    UNION = getIcon(clionIcons, "CodeAssistantUnion");
    VAR = AllIcons.Nodes.Variable;
  }

  @Nullable
  private static Icon getIcon(@Nullable Class<?> klass, @NotNull String fieldName) {
    if (klass == null) {
      return null;
    }
    try {
      Field field = klass.getField(fieldName);
      return ObjectUtils.tryCast(field.get(null), Icon.class);
    } catch (Exception e) {
      return null;
    }
  }
}

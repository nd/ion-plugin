package ion;

import com.intellij.lang.Language;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.util.ObjectUtils;
import ion.psi.IonExprLitString;
import ion.psi.IonNote;
import ion.psi.IonNoteParam;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class IonForeignInjector implements MultiHostInjector {
  private final static List<Class<? extends PsiElement>> CLASSES = Collections.singletonList(IonExprLitString.class);

  @Override
  public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar, @NotNull PsiElement context) {
    IonExprLitString string = ObjectUtils.tryCast(context, IonExprLitString.class);
    if (string == null || !string.isMultilineString()) {
      return;
    }
    IonNoteParam noteParam = ObjectUtils.tryCast(context.getParent(), IonNoteParam.class);
    if (noteParam == null) {
      return;
    }
    PsiElement noteParamName = noteParam.getNoteParamName();
    if (noteParamName == null || !(noteParamName.textMatches("preamble") || noteParamName.textMatches("postamble"))) {
      return;
    }
    IonNote note = ObjectUtils.tryCast(noteParam.getParent(), IonNote.class);
    if (note == null) {
      return;
    }
    PsiElement noteName = note.getNoteName();
    if (noteName == null || !noteName.textMatches("foreign")) {
      return;
    }
    Language c = Language.findLanguageByID("ObjectiveC");
    if (c != null) {
      registrar.startInjecting(c);
      int quoteLen = string.getQuoteLen();
      TextRange range = TextRange.create(quoteLen, string.getTextLength() - quoteLen);
      registrar.addPlace(null, null, string, range);
      registrar.doneInjecting();
    }
  }

  @Override
  @NotNull
  public List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
    return CLASSES;
  }
}

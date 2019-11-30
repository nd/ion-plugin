package ion.psi;

import com.intellij.lang.*;
import com.intellij.lang.impl.PsiBuilderImpl;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IReparseableElementType;
import com.intellij.util.ObjectUtils;
import ion.IonLanguage;
import ion.IonParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IonBlockElementType extends IReparseableElementType {
  public IonBlockElementType() {
    super("block", IonLanguage.INSTANCE);
  }

  @Override
  public boolean isParsable(@NotNull CharSequence buffer, @NotNull Language fileLanguage, @NotNull Project project) {
    if (fileLanguage != IonLanguage.INSTANCE) {
      return false;
    }
    Lexer lexer = LanguageParserDefinitions.INSTANCE.forLanguage(fileLanguage).createLexer(project);
    return PsiBuilderUtil.hasProperBraceBalance(buffer, lexer, IonToken.LBRACE, IonToken.RBRACE);
  }

  @Override
  public @Nullable ASTNode createNode(CharSequence text) {
    return new IonLazyParseablePsiElement(this, text);
  }

  @Override
  protected ASTNode doParseContents(@NotNull ASTNode chameleon, @NotNull PsiElement psi) {
    Project project = psi.getProject();
    Language lang = IonLanguage.INSTANCE;
    PsiBuilder builder = PsiBuilderFactory.getInstance().createBuilder(project, chameleon, null, lang, chameleon.getChars());
    ParserDefinition parserDefinition = LanguageParserDefinitions.INSTANCE.forLanguage(lang);
    IonParser parser = ObjectUtils.tryCast(parserDefinition.createParser(project), IonParser.class);
    parser.expectStmtBlock(builder);
    ASTNode tree = builder.getTreeBuilt(); // throws ReparsedSuccessfullyException exception to signal success
    return tree.getFirstChildNode();
  }

  @NotNull
  public PsiElement createPsiElement(ASTNode node) {
    return new IonBlock(node);
  }
}

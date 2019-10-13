package ion;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ObjectUtils;
import ion.psi.IonElementType;
import ion.psi.IonFileElementType;
import ion.psi.IonPsiFile;
import ion.psi.IonToken;
import org.jetbrains.annotations.NotNull;

public class IonParserDefinition implements ParserDefinition {
  @NotNull
  @Override
  public Lexer createLexer(Project project) {
    return new IonLexer();
  }

  @Override
  public PsiParser createParser(Project project) {
    return new IonParser();
  }

  @Override
  public IFileElementType getFileNodeType() {
    return IonFileElementType.INSTANCE;
  }

  @NotNull
  @Override
  public TokenSet getCommentTokens() {
    return IonToken.COMMENTS;
  }

  @NotNull
  @Override
  public TokenSet getStringLiteralElements() {
    return TokenSet.create();
  }

  @NotNull
  @Override
  public PsiElement createElement(ASTNode node) {
    IonElementType type = ObjectUtils.tryCast(node.getElementType(), IonElementType.class);
    return type != null ? type.createPsiElement(node) : PsiUtilCore.NULL_PSI_ELEMENT;
  }

  @Override
  public PsiFile createFile(FileViewProvider viewProvider) {
    return new IonPsiFile(viewProvider);
  }
}

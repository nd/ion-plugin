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
import ion.psi.*;
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
    if (type != null) {
      switch (type.getTypeId()) {
        case EXPR_NAME:
          return new IonExprName(node);
        case DECL_VAR:
          return new IonDeclVar(node);
        case DECL_FUNC:
          return new IonDeclFunc(node);
        case STMT_INIT:
          return new IonStmtInit(node);
      }
      return type.createPsiElement(node);
    }
    IonBlockElementType blockType = ObjectUtils.tryCast(node.getElementType(), IonBlockElementType.class);
    if (blockType != null) {
      return blockType.createPsiElement(node);
    }
    return PsiUtilCore.NULL_PSI_ELEMENT;
  }

  @Override
  public PsiFile createFile(FileViewProvider viewProvider) {
    return new IonPsiFile(viewProvider);
  }
}

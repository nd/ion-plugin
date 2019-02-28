package ion;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import ion.psi.IonToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.*;
import static com.intellij.openapi.editor.HighlighterColors.BAD_CHARACTER;

public class IonSyntaxHighlighter extends SyntaxHighlighterBase {
  private static final Map<IElementType, TextAttributesKey> ATTRIBUTES = new HashMap<>();

  static {
    fillMap(ATTRIBUTES, PARENTHESES, IonToken.LPAREN, IonToken.RPAREN);
    fillMap(ATTRIBUTES, BRACES, IonToken.LBRACE, IonToken.RBRACE);
    fillMap(ATTRIBUTES, BRACKETS, IonToken.LBRACKET, IonToken.RBRACKET);
    fillMap(ATTRIBUTES, COMMA, IonToken.COMMA);
    fillMap(ATTRIBUTES, SEMICOLON, IonToken.SEMICOLON);
    fillMap(ATTRIBUTES, BAD_CHARACTER, TokenType.BAD_CHARACTER);
    fillMap(ATTRIBUTES, DOT, IonToken.DOT);
    fillMap(ATTRIBUTES, IonToken.KEYWORDS, KEYWORD);
    fillMap(ATTRIBUTES, IonToken.OPERATORS, OPERATION_SIGN);
    fillMap(ATTRIBUTES, STRING, IonToken.STRING, IonToken.MULTILINE_STRING, IonToken.CHAR);
    fillMap(ATTRIBUTES, LINE_COMMENT, IonToken.LINE_COMMENT);
    fillMap(ATTRIBUTES, BLOCK_COMMENT, IonToken.BLOCK_COMMENT);
    fillMap(ATTRIBUTES, IDENTIFIER, IonToken.NAME);
    fillMap(ATTRIBUTES, NUMBER, IonToken.INT, IonToken.FLOAT);
  }

  @NotNull
  @Override
  public Lexer getHighlightingLexer() {
    return new IonLexer();
  }

  @NotNull
  @Override
  public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
    return pack(ATTRIBUTES.get(tokenType));
  }

  public static class Factory extends SyntaxHighlighterFactory {
    @NotNull
    @Override
    public SyntaxHighlighter getSyntaxHighlighter(@Nullable Project project, @Nullable VirtualFile virtualFile) {
      return new IonSyntaxHighlighter();
    }
  }
}

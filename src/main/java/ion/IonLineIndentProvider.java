package ion;

import com.intellij.lang.Language;
import com.intellij.psi.TokenType;
import com.intellij.psi.impl.source.codeStyle.SemanticEditorPosition;
import com.intellij.psi.impl.source.codeStyle.lineIndent.JavaLikeLangLineIndentProvider;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

import static com.intellij.psi.impl.source.codeStyle.lineIndent.JavaLikeLangLineIndentProvider.JavaLikeElement.*;
import static ion.psi.IonToken.*;

public class IonLineIndentProvider extends JavaLikeLangLineIndentProvider {
  private final static HashMap<IElementType, SemanticEditorPosition.SyntaxElement> SYNTAX_MAP =
          new HashMap<IElementType, SemanticEditorPosition.SyntaxElement>() {
            {
              put(TokenType.WHITE_SPACE, Whitespace);
              put(SEMICOLON, Semicolon);
              put(LBRACE, BlockOpeningBrace);
              put(RBRACE, BlockClosingBrace);
              put(LBRACKET, ArrayOpeningBracket);
              put(RBRACKET, ArrayClosingBracket);
              put(RPAREN, RightParenthesis);
              put(LPAREN, LeftParenthesis);
              put(COLON, Colon);
              put(CASE, SwitchCase);
              put(DEFAULT, SwitchDefault);
              put(IF, IfKeyword);
              put(ELSE, ElseKeyword);
              put(FOR, ForKeyword);
              put(COMMA, Comma);
              put(BLOCK_COMMENT, BlockComment);
              put(LINE_COMMENT, LineComment);
            }
          };

  @Nullable
  @Override
  protected SemanticEditorPosition.SyntaxElement mapType(@NotNull IElementType tokenType) {
    return SYNTAX_MAP.get(tokenType);
  }

  @Override
  public boolean isSuitableForLanguage(@NotNull Language language) {
    return language == IonLanguage.INSTANCE;
  }
}

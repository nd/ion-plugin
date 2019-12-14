package ion.fmt;

import com.intellij.formatting.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import ion.IonLanguage;
import ion.psi.IonElementType;
import ion.psi.IonToken;
import org.jetbrains.annotations.NotNull;

public class IonFmtModelBuilder implements FormattingModelBuilder {
  @NotNull
  @Override
  public FormattingModel createModel(PsiElement element, CodeStyleSettings settings) {
    Block block = IonFmtBlock.createBlock(element, createSpacingBuilder(settings));
    return FormattingModelProvider.createFormattingModelForPsiFile(element.getContainingFile(), block, settings);
  }

  @NotNull
  private static SpacingBuilder createSpacingBuilder(@NotNull CodeStyleSettings settings) {
    return new SpacingBuilder(settings, IonLanguage.INSTANCE)
            .after(IonToken.COMMA).spaceIf(true)
            .between(IonToken.RETURN, IonToken.SEMICOLON).none()
            .after(IonToken.RETURN).spaceIf(true)
            .between(IonToken.RPAREN, IonElementType.STMT_BLOCK).spacing(1, 1, 0, true, 0)
            .between(IonToken.RPAREN, IonToken.LBRACE).spacing(1, 1, 0, true, 0)
            .between(IonToken.SWITCH, IonToken.LPAREN).spacing(1, 1, 0, true, 0)
            .between(IonToken.FOR, IonToken.LPAREN).spacing(1, 1, 0, true, 0)
            .between(IonToken.WHILE, IonToken.LPAREN).spacing(1, 1, 0, true, 0)
            .between(IonToken.IF, IonToken.LPAREN).spacing(1, 1, 0, true, 0)
            .between(IonElementType.STMT_BLOCK, IonElementType.STMT_ELSE).spacing(1, 1, 0, true, 0)
            .between(IonElementType.STMT_IF, IonElementType.STMT_ELSE).spacing(1, 1, 0, true, 0)
            .between(IonToken.ELSE, IonElementType.STMT_BLOCK).spacing(1, 1, 0, true, 0)
            .between(IonElementType.STMT_IF, IonElementType.STMT_BLOCK).spacing(1, 1, 0, true, 0)
            .between(IonToken.ELSE, IonToken.IF).spacing(1, 1, 0, true, 0)

            .afterInside(IonToken.COLON, IonElementType.DECL_FUNC_PARAM).spacing(1, 1, 0, true, 0)
            .afterInside(IonToken.COLON, IonElementType.COMPOUND_FIELD_NAMED).spacing(1, 1, 0, true, 0)
            .afterInside(IonToken.COLON, IonElementType.DECL_VAR).spacing(1, 1, 0, true, 0)
            .beforeInside(IonToken.COLON, IonElementType.DECL_VAR).spacing(0, 0, 0, false, 0)
            .afterInside(IonToken.COLON, IonElementType.DECL_CONST).spacing(1, 1, 0, true, 0)
            .beforeInside(IonToken.COLON, IonElementType.DECL_CONST).spacing(0, 0, 0, false, 0)
            .afterInside(IonToken.COLON, IonElementType.EXPR_CAST).spacing(0, 0, 0, false, 0)

            .aroundInside(IonToken.NAME, IonElementType.DECL_AGGREGATE).spacing(1, 1, 0, true, 0)
            .aroundInside(IonToken.NAME, IonElementType.DECL_ENUM).spacing(1, 1, 0, true, 0)
            .beforeInside(IonToken.NAME, IonElementType.DECL_VAR).spacing(1, 1, 0, true, 0)
            .beforeInside(IonToken.NAME, IonElementType.DECL_CONST).spacing(1, 1, 0, true, 0)

            .afterInside(IonToken.SEMICOLON, IonElementType.STMT_FOR).spacing(0, 1, 0, true, 0)

            .around(IonToken.COLON_ASSIGN).spacing(1, 1, 0, true, 0)
            .around(IonToken.LT).spacing(1, 1, 0, true, 0)
            .around(IonToken.LTEQ).spacing(1, 1, 0, true, 0)
            .around(IonToken.GT).spacing(1, 1, 0, true, 0)
            .around(IonToken.GTEQ).spacing(1, 1, 0, true, 0)
            .around(IonToken.NOTEQ).spacing(1, 1, 0, true, 0)
            .around(IonToken.EQ).spacing(1, 1, 0, true, 0)
            .around(IonToken.NEG).spacing(0, 0, 0, false, 0)
            .around(IonToken.NOT).spacing(0, 0, 0, false, 0)
            .afterInside(IonToken.AND, IonElementType.EXPR_UNARY).spacing(0, 0, 0, false, 0)
            .around(IonToken.AND).spacing(1, 1, 0, true, 0)
            .around(IonToken.AND_ASSIGN).spacing(1, 1, 0, true, 0)
            .around(IonToken.AND_AND).spacing(1, 1, 0, true, 0)
            .around(IonToken.OR).spacing(1, 1, 0, true, 0)
            .around(IonToken.OR_ASSIGN).spacing(1, 1, 0, true, 0)
            .around(IonToken.OR_OR).spacing(1, 1, 0, true, 0)
            .around(IonToken.XOR).spacing(1, 1, 0, true, 0)
            .around(IonToken.XOR_ASSIGN).spacing(1, 1, 0, true, 0)
            .around(IonToken.ADD).spacing(1, 1, 0, true, 0)
            .around(IonToken.ADD_ASSIGN).spacing(1, 1, 0, true, 0)
            .afterInside(IonToken.SUB, IonElementType.EXPR_UNARY).spacing(0, 0, 0, false, 0)
            .around(IonToken.SUB).spacing(1, 1, 0, true, 0)
            .around(IonToken.SUB_ASSIGN).spacing(1, 1, 0, true, 0)
            .beforeInside(IonToken.MUL, IonElementType.TYPE_PTR).spacing(0, 0, 0, false, 0)
            .afterInside(IonToken.MUL, IonElementType.EXPR_UNARY).spacing(0, 0, 0, false, 0)
            .around(IonToken.MUL).spacing(1, 1, 0, true, 0)
            .around(IonToken.MUL_ASSIGN).spacing(1, 1, 0, true, 0)
            .around(IonToken.DIV).spacing(1, 1, 0, true, 0)
            .around(IonToken.DIV_ASSIGN).spacing(1, 1, 0, true, 0)
            .around(IonToken.MOD).spacing(1, 1, 0, true, 0)
            .around(IonToken.MOD_ASSIGN).spacing(1, 1, 0, true, 0)
            .around(IonToken.LSHIFT).spacing(1, 1, 0, true, 0)
            .around(IonToken.LSHIFT_ASSIGN).spacing(1, 1, 0, true, 0)
            .around(IonToken.RSHIFT).spacing(1, 1, 0, true, 0)
            .around(IonToken.RSHIFT_ASSIGN).spacing(1, 1, 0, true, 0)

            .around(IonToken.QUESTION).spacing(1, 1, 0, true, 0)
            .aroundInside(IonToken.COLON, IonElementType.EXPR_TERNARY).spacing(1, 1, 0, true, 0)
            ;
  }
}

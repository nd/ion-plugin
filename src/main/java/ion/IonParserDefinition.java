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
        case EXPR_FIELD:
          return new IonExprField(node);
        case EXPR_CALL:
          return new IonExprCall(node);
        case EXPR_CALL_ARG:
          return new IonExprCallArg(node);
        case EXPR_UNARY:
          return new IonExprUnary(node);
        case EXPR_INDEX:
          return new IonExprIndex(node);
        case TYPE_NAME:
          return new IonTypeName(node);
        case TYPE_PAR:
          return new IonTypePar(node);
        case TYPE_PTR:
          return new IonTypePtr(node);
        case TYPE_CONST:
          return new IonTypeConst(node);
        case TYPE_ARRAY:
          return new IonTypeArray(node);
        case TYPE_TUPLE:
          return new IonTypeTuple(node);
        case LABEL_NAME:
          return new IonLabelName(node);
        case DECL_NOTE:
          return new IonDeclNote(node);
        case DECL_VAR:
          return new IonDeclVar(node);
        case DECL_CONST:
          return new IonDeclConst(node);
        case DECL_FUNC:
          return new IonDeclFunc(node);
        case DECL_FUNC_PARAM:
          return new IonDeclFuncParam(node);
        case DECL_AGGREGATE:
          return new IonDeclAggregate(node);
        case DECL_FIELD:
          return new IonDeclField(node);
        case DECL_FIELD_NAME:
          return new IonDeclFieldName(node);
        case DECL_TYPEDEF:
          return new IonDeclTypedef(node);
        case DECL_ENUM:
          return new IonDeclEnum(node);
        case DECL_ENUM_ITEM:
          return new IonDeclEnumItem(node);
        case STMT_INIT:
          return new IonStmtInit(node);
        case STMT_ASSIGN:
          return new IonStmtAssign(node);
        case STMT_RETURN:
          return new IonStmtReturn(node);
        case STMT_LABEL:
          return new IonStmtLabel(node);
        case COMPOUND_FIELD:
          return new IonCompoundField(node);
        case COMPOUND_FIELD_NAMED:
          return new IonCompoundFieldNamed(node);
        case EXPR_LITERAL_COMPOUND:
          return new IonExprLitCompound(node);
        case EXPR_LITERAL_INT:
          return new IonExprLitInt(node);
        case EXPR_LITERAL_COMPOUND_TYPED:
          return new IonExprLitCompoundTyped(node);
        case DECL_IMPORT:
          return new IonDeclImport(node);
        case IMPORT_PATH:
          return new IonImportPath(node);
        case DECL_IMPORT_ITEM:
          return new IonDeclImportItem(node);
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

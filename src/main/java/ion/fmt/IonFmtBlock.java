package ion.fmt;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import ion.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class IonFmtBlock implements ASTBlock {
  private final PsiElement myElement;
  private final SpacingBuilder mySpacingBuilder;
  private final Indent myIndent;

  private IonFmtBlock(@NotNull PsiElement element,
                      @NotNull SpacingBuilder spacingBuilder,
                      @Nullable Indent indent) {
    myElement = element;
    mySpacingBuilder = spacingBuilder;
    myIndent = indent;
  }

  @NotNull
  static IonFmtBlock createBlock(@NotNull PsiElement element, @NotNull SpacingBuilder spacingBuilder) {
    return new IonFmtBlock(element, spacingBuilder, getIndent(element));
  }

  @Nullable
  @Override
  public ASTNode getNode() {
    return myElement.getNode();
  }

  @NotNull
  private static Indent getIndent(@NotNull PsiElement element) {
    IElementType elementType = element.getNode().getElementType();
    if (elementType == IonElementType.STMT_LIST) {
      return element.getChildren().length == 0 ? Indent.getNoneIndent() : Indent.getNormalIndent();
    }

    if (element instanceof IonDeclField) {
      return Indent.getNormalIndent();
    }

    if (element instanceof IonDeclEnumItem) {
      return Indent.getNormalIndent();
    }

    PsiElement parent = element.getParent();
    if (parent instanceof IonExprLitCompound || parent instanceof IonExprLitCompoundTyped) {
      if (elementType == IonToken.LBRACE || elementType == IonToken.RBRACE) {
        return Indent.getNoneIndent();
      }
      return Indent.getNormalIndent();
    }

    if (parent instanceof IonSwitchCaseBlock) {
      if (elementType == IonToken.CASE || elementType == IonToken.DEFAULT) {
        return Indent.getNoneIndent();
      }
      return Indent.getNormalIndent();
    }

    return Indent.getNoneIndent();
  }

  @NotNull
  @Override
  public ChildAttributes getChildAttributes(int newChildIndex) {
    Indent childIndent = Indent.getNoneIndent();
    if (myElement instanceof IonBlock ||
            myElement instanceof IonExprLitCompound ||
            myElement instanceof IonExprLitCompoundTyped ||
            myElement instanceof IonSwitchCaseBlock ||
            myElement instanceof IonDeclAggregate ||
            myElement instanceof IonDeclEnum ||
            myElement.getNode().getElementType() == IonElementType.STMT_SWITCH) {
      childIndent = Indent.getNormalIndent();
    }
    return new ChildAttributes(childIndent, null);
  }

  @NotNull
  @Override
  public TextRange getTextRange() {
    return myElement.getTextRange();
  }

  @NotNull
  @Override
  public List<Block> getSubBlocks() {
    ASTNode node = myElement.getNode();
    ASTNode child = node.getFirstChildNode();
    List<Block> result = new ArrayList<>();
    while (child != null) {
      if (child.getElementType() != TokenType.WHITE_SPACE) {
        result.add(createBlock(child.getPsi(), mySpacingBuilder));
      }
      child = child.getTreeNext();
    }
    return result;
  }

  @Nullable
  @Override
  public Wrap getWrap() {
    return null;
  }

  @Nullable
  @Override
  public Indent getIndent() {
    return myIndent;
  }

  @Nullable
  @Override
  public Alignment getAlignment() {
    return null;
  }

  @Nullable
  @Override
  public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
    return mySpacingBuilder.getSpacing(this, child1, child2);
  }

  @Override
  public boolean isIncomplete() {
    return false;
  }

  @Override
  public boolean isLeaf() {
    return false;
  }
}

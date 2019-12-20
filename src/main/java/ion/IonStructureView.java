package ion;

import com.intellij.ide.structureView.*;
import com.intellij.ide.util.treeView.smartTree.Filter;
import com.intellij.ide.util.treeView.smartTree.Grouper;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.lang.PsiStructureViewFactory;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import ion.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class IonStructureView implements PsiStructureViewFactory {

  @Override
  @Nullable
  public StructureViewBuilder getStructureViewBuilder(@NotNull PsiFile file) {
    return new TreeBasedStructureViewBuilder() {
      @Override
      @NotNull
      public StructureViewModel createStructureViewModel(@Nullable Editor editor) {
        return new IonStructureViewModel(file, editor);
      }
    };
  }

  private static class IonStructureViewModel implements StructureViewModel {
    private final PsiFile myFile;
    private final Editor myEditor;
    private final IonFileTreeElement myRoot;
    public IonStructureViewModel(@NotNull PsiFile file, @Nullable Editor editor) {
      myFile = file;
      myEditor = editor;
      myRoot = new IonFileTreeElement(myFile);
    }

    @Override
    @Nullable
    public Object getCurrentEditorElement() {
      if (myEditor == null) {
        return null;
      }
      int offset = myEditor.getCaretModel().getOffset();
      PsiElement element = myFile.findElementAt(offset);
      return PsiTreeUtil.getStubOrPsiParentOfType(element, IonDecl.class);
    }

    @Override
    public void addEditorPositionListener(@NotNull FileEditorPositionListener listener) {

    }

    @Override
    public void removeEditorPositionListener(@NotNull FileEditorPositionListener listener) {

    }

    @Override
    public void addModelListener(@NotNull ModelListener modelListener) {

    }

    @Override
    public void removeModelListener(@NotNull ModelListener modelListener) {

    }

    @Override
    @NotNull
    public StructureViewTreeElement getRoot() {
      return myRoot;
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean shouldEnterElement(Object element) {
      return false;
    }

    @NotNull
    @Override
    public Grouper[] getGroupers() {
      return new Grouper[0];
    }

    @NotNull
    @Override
    public Sorter[] getSorters() {
      return new Sorter[0];
    }

    @NotNull
    @Override
    public Filter[] getFilters() {
      return new Filter[0];
    }
  }

  private static class IonFileTreeElement implements StructureViewTreeElement {
    private final PsiFile myFile;

    public IonFileTreeElement(@NotNull PsiFile file) {
      myFile = file;
    }

    @Override
    public Object getValue() {
      return null;
    }

    @Override
    @NotNull
    public ItemPresentation getPresentation() {
      return new IonFilePresentation(myFile);
    }

    @NotNull
    @Override
    public TreeElement[] getChildren() {
      List<IonDecl> decls = PsiTreeUtil.getStubChildrenOfTypeAsList(myFile, IonDecl.class);
      List<StructureViewTreeElement> result = new ArrayList<>(decls.size());
      for (IonDecl decl : decls) {
        if (decl instanceof IonDeclImport || decl instanceof IonDeclImportItem) {
          continue;
        }
        result.add(new IonDeclTreeElement(decl));
      }
      return result.toArray(StructureViewTreeElement.EMPTY_ARRAY);
    }

    @Override
    public void navigate(boolean requestFocus) {
      myFile.navigate(requestFocus);
    }

    @Override
    public boolean canNavigate() {
      return true;
    }

    @Override
    public boolean canNavigateToSource() {
      return true;
    }
  }

  private static class IonDeclTreeElement implements StructureViewTreeElement {
    private final IonDecl myElement;
    public IonDeclTreeElement(@NotNull IonDecl element) {
      myElement = element;
    }

    @Override
    @NotNull
    public ItemPresentation getPresentation() {
      return new IonDeclPresentation(myElement);
    }

    @NotNull
    @Override
    public TreeElement[] getChildren() {
      if (myElement instanceof IonDeclAggregate || myElement instanceof IonDeclField) {
        List<IonDeclField> fields = PsiTreeUtil.getStubChildrenOfTypeAsList(myElement, IonDeclField.class);
        List<StructureViewTreeElement> result = new ArrayList<>(fields.size());
        for (IonDeclField field : fields) {
          List<IonDeclFieldName> fieldNames = PsiTreeUtil.getStubChildrenOfTypeAsList(field, IonDeclFieldName.class);
          for (IonDeclFieldName fieldName : fieldNames) {
            result.add(new IonDeclTreeElement(fieldName));
          }
        }
        return result.toArray(StructureViewTreeElement.EMPTY_ARRAY);
      }
      if (myElement instanceof IonDeclEnum) {
        List<IonDeclEnumItem> items = PsiTreeUtil.getStubChildrenOfTypeAsList(myElement, IonDeclEnumItem.class);
        List<StructureViewTreeElement> result = new ArrayList<>(items.size());
        for (IonDeclEnumItem item : items) {
          result.add(new IonDeclTreeElement(item));
        }
        return result.toArray(StructureViewTreeElement.EMPTY_ARRAY);
      }
      return new TreeElement[0];
    }

    @Override
    public Object getValue() {
      return myElement;
    }

    @Override
    public void navigate(boolean requestFocus) {
      ((NavigatablePsiElement) myElement).navigate(requestFocus);
    }

    @Override
    public boolean canNavigate() {
      return myElement instanceof NavigatablePsiElement;
    }

    @Override
    public boolean canNavigateToSource() {
      return myElement instanceof NavigatablePsiElement;
    }
  }

  private static class IonFilePresentation implements ItemPresentation {
    private final PsiFile myFile;
    public IonFilePresentation(@NotNull PsiFile file) {
      myFile = file;
    }

    @Override
    public @Nullable String getPresentableText() {
      return myFile.getName();
    }

    @Override
    public @Nullable String getLocationString() {
      return null;
    }

    @Override
    public @Nullable Icon getIcon(boolean unused) {
      return null;
    }
  }

  private static class IonDeclPresentation implements ItemPresentation {
    private final IonDecl myElement;
    public IonDeclPresentation(@NotNull IonDecl element) {
      myElement = element;
    }

    @Override
    @Nullable
    public String getPresentableText() {
      String type = null;
      if (myElement instanceof IonTypeOwner) {
        type = IonCompletion.getTypePresentation(((IonTypeOwner) myElement).getType());
      }
      String name = myElement.getName();
      if (myElement instanceof IonDeclFunc && name != null) {
        String paramsStr = IonCompletion.getParamsString(PsiTreeUtil.getStubChildrenOfTypeAsList(myElement, IonDeclFuncParam.class));
        name = name + "(" + paramsStr + ")";
      }
      if (name == null) {
        return "anonymous";
      }
      return name + (type != null ? ": " + type : "");
    }

    @Override
    @Nullable
    public String getLocationString() {
      if (myElement instanceof IonDeclVar) {
        return "var";
      } else if (myElement instanceof IonDeclConst) {
        return "const";
      } else if (myElement instanceof IonDeclFunc) {
        return null; // it is clear from ()
      } else if (myElement instanceof IonDeclTypedef) {
        return "typedef";
      } else if (myElement instanceof IonDeclFieldName) {
        return null; // clear from indent
      } else if (myElement instanceof IonDeclAggregate) {
        return ((IonDeclAggregate) myElement).getKind().toString().toLowerCase();
      } else if (myElement instanceof IonDeclEnum) {
        return "enum";
      } else if (myElement instanceof IonDeclEnumItem) {
        return null; // clear from indent
      }
      return null;
    }

    @Override
    @Nullable
    public Icon getIcon(boolean unused) {
      return null;
    }
  }
}

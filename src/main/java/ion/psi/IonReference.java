package ion.psi;

import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.SyntheticLibrary;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ArrayUtil;
import com.intellij.util.ObjectUtils;
import com.intellij.util.Processor;
import com.intellij.util.containers.ContainerUtil;
import ion.IonLib;
import ion.IonLibProvider;
import kotlin.reflect.jvm.internal.impl.utils.SmartList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;

public class IonReference extends PsiReferenceBase<IonPsiElement> {
  private final static Set<String> BUILTIN_TYPES = ContainerUtil.set(
          "void", "bool", "char", "schar", "uchar",
          "short", "ushort", "int", "uint", "long",
          "ulong", "llong", "ullong", "float", "double"
  );

  public IonReference(@NotNull IonPsiElement element, TextRange rangeInElement) {
    super(element, rangeInElement);
  }

  @NotNull
  @Override
  public Object[] getVariants() {
    List<LookupElement> items = new SmartList<>();
    processResolveVariants(it -> {
      IonDecl decl = ObjectUtils.tryCast(it, IonDecl.class);
      if (decl != null) {
        String name = decl.getName();
        if (!StringUtil.isEmpty(name)) {
          LookupElementBuilder builder = LookupElementBuilder.create(decl).withLookupString(name);
          LookupElementRenderer<LookupElement> renderer = IonCompletion.getRenderer(decl);
          if (renderer != null) {
            builder = builder.withRenderer(renderer);
          }
          InsertHandler<LookupElement> insertHandler = IonCompletion.getInsertHandler(decl);
          if (insertHandler != null) {
            builder = builder.withInsertHandler(insertHandler);
          }
          items.add(builder);
        }
      }
      return true;
    });
    return items.toArray(LookupElement.EMPTY_ARRAY);
  }

  public boolean processResolveVariants(@NotNull Processor<PsiElement> processor) {
    if (myElement instanceof IonLabelName) {
      return processLabels(myElement, processor);
    }

    if (myElement instanceof IonExprField) {
      return processExprFieldVariants((IonExprField) myElement, processor);
    }

    PsiElement parent = myElement.getParent();
    IonExprField field = ObjectUtils.tryCast(parent, IonExprField.class);
    if (field != null && myElement == getFieldName(field)) {
      return processExprFieldVariants(field, processor);
    }

    IonCompoundFieldNamed compoundField = ObjectUtils.tryCast(parent, IonCompoundFieldNamed.class);
    if (compoundField != null && myElement == getCompoundFieldName(compoundField)) {
      return processCompoundFieldVariants(compoundField, processor);
    }

    IonTypeQName typeQName = ObjectUtils.tryCast(parent, IonTypeQName.class);
    if (typeQName != null && myElement == getTypeName(typeQName)) {
      return processTypeQNameVariants(typeQName, processor);
    }

    IonDeclImportItem importedItem = ObjectUtils.tryCast(parent, IonDeclImportItem.class);
    if (importedItem != null) {
      IonDeclImport importDecl = PsiTreeUtil.getParentOfType(importedItem, IonDeclImport.class);
      IonImportPath importPath = PsiTreeUtil.getChildOfType(importDecl, IonImportPath.class);
      PsiDirectory packageDir = resolveImport(myElement.getContainingFile(), importPath != null ? importPath.getText() : null);
      return processPackageDeclarations(packageDir, processor);
    }

    return processDeclarationsUp(parent, myElement, processor);
  }

  @Override
  @Nullable
  public PsiElement resolve() {
    PsiFile file = myElement.getOriginalElement().getContainingFile();
    ConcurrentMap<PsiElement, PsiElement> resolveCache = null;
    if (true) {// Registry.is("ion.resolve.cache.enabled") doesn't work
      resolveCache = getResolveCache(file);
      PsiElement result = resolveCache != null ? resolveCache.get(myElement) : null;
      if (result != null && result.isValid()) {
        return result;
      }
    }

    if (myElement instanceof IonLabelName) {
      ExactMatchProcessor processor = new ExactMatchProcessor(myElement.getText());
      processLabels(myElement, processor);
      return processor.getElement();
    }

    if (myElement instanceof IonExprField) {
      PsiElement nameElement = getFieldName((IonExprField) myElement);
      if (nameElement == null) {
        return null;
      }
      ExactMatchProcessor processor = new ExactMatchProcessor(nameElement.getText());
      processExprFieldVariants((IonExprField) myElement, processor);
      return processor.getElement();
    }

    PsiElement parent = myElement.getParent();
    IonExprField field = ObjectUtils.tryCast(parent, IonExprField.class);
    if (field != null && myElement == getFieldName(field)) {
      ExactMatchProcessor processor = new ExactMatchProcessor(myElement.getText());
      processExprFieldVariants(field, processor);
      return processor.getElement();
    }

    IonCompoundFieldNamed compoundField = ObjectUtils.tryCast(parent, IonCompoundFieldNamed.class);
    if (compoundField != null && myElement == getCompoundFieldName(compoundField)) {
      ExactMatchProcessor processor = new ExactMatchProcessor(myElement.getText());
      processCompoundFieldVariants(compoundField, processor);
      return processor.getElement();
    }

    IonTypeQName typeQName = ObjectUtils.tryCast(parent, IonTypeQName.class);
    if (typeQName != null && myElement == getTypeName(typeQName)) {
      ExactMatchProcessor processor = new ExactMatchProcessor(myElement.getText());
      processTypeQNameVariants(typeQName, processor);
      return processor.getElement();
    }

    CharSequence name = getRangeInElement().subSequence(myElement.getText());
    if (BUILTIN_TYPES.contains(name.toString())) {
      return null;
    }
    IonDeclImportItem importedItem = ObjectUtils.tryCast(parent, IonDeclImportItem.class);
    if (importedItem != null) {
      IonDeclImport importDecl = PsiTreeUtil.getParentOfType(importedItem, IonDeclImport.class);
      IonImportPath importPath = PsiTreeUtil.getChildOfType(importDecl, IonImportPath.class);
      PsiDirectory packageDir = resolveImport(myElement.getContainingFile(), importPath != null ? importPath.getText() : null);
      if (packageDir != null) {
        Project project = myElement.getProject();
        ExactMatchProcessor processor = new ExactMatchProcessor(name);
        if (!DumbService.isDumb(project)) {
          GlobalSearchScope scope = GlobalSearchScope.filesScope(project, Arrays.asList(packageDir.getVirtualFile().getChildren()));
          for (IonDecl element : StubIndex.getElements(IonNameIndex.KEY, name.toString(), project, scope, IonDecl.class)) {
            if (!processor.process(element)) {
              break;
            }
          }
        } else {
          processPackageDeclarations(packageDir, processor);
        }
        return processor.getElement();
      }
      return null;
    }

    Ref<Ref<Pair<CharSequence, PsiElement>>> blockCacheRef = Ref.create();
    ExactMatchProcessor processor = new ExactMatchProcessor(name) {
      private boolean myProcessedBlock;

      @Override
      public boolean process(PsiElement element) {
        IonDecl decl = ObjectUtils.tryCast(element, IonDecl.class);
        if (decl == null) {
          return true;
        }
        if (decl instanceof IonStmtLabel) {
          return true;
        }
        if (decl instanceof IonDeclImport && !file.equals(decl.getContainingFile())) {
          // ignore imports used in other files in the same package
          return true;
        }
        return super.process(element);
      }

      @Override
      public boolean childrenProcessed(@NotNull PsiElement parent) {
        if (!myProcessedBlock && parent instanceof IonBlock) {
          myProcessedBlock = true;
          Ref<Pair<CharSequence, PsiElement>> blockCache = getBlockResolveCache(file, (IonBlock) parent);
          blockCacheRef.set(blockCache);
          Pair<CharSequence, PsiElement> cached = blockCache != null ? blockCache.get() : null;
          PsiElement lastResolveResult = cached != null ? cached.second : null;
          if (lastResolveResult != null && !super.process(lastResolveResult)) {
            return false;
          }
        }
        return true;
      }
    };
    processDeclarationsUp(parent, myElement, processor);

    PsiElement result = processor.getElement();
    if (result != null) {
      if (resolveCache != null) {
        resolveCache.put(myElement, result);
      }
      Ref<Pair<CharSequence, PsiElement>> blockCache = blockCacheRef.get();
      if (blockCache != null) {
        blockCache.set(Pair.create(name, result));
      }
    }
    return result;
  }

  private static boolean processExprFieldVariants(@NotNull IonExprField fieldExpr,
                                                  @NotNull Processor<PsiElement> processor) {
    PsiElement qualifier = getQualifier(fieldExpr);
    if (qualifier == null) {
      return true;
    }
    PsiElement type = resolveType(qualifier);
    PsiElement unwrapped = unwrapParAndConstType(type);
    if (unwrapped instanceof IonTypePtr) {
      PsiElement underlying = getUnderlyingPtrType((IonTypePtr) unwrapped);
      unwrapped = unwrapParAndConstType(underlying);
    }
    if (unwrapped != type) {
      type = resolveType(unwrapped);
    }
    if (type instanceof IonDeclAggregate && !processDeclAggregate((IonDeclAggregate) type, processor)) {
      return false;
    }
    if (type == null) {
      PsiReference reference = qualifier.getReference();
      PsiElement resolvedQualifier = reference != null ? reference.resolve() : null;
      if (resolvedQualifier != null) {
        if (resolvedQualifier instanceof IonDeclImport || resolvedQualifier.getParent() instanceof IonImportPath) {
          return processImport(PsiTreeUtil.getParentOfType(resolvedQualifier, IonDeclImport.class, false), processor);
        }
      }
    }
    return true;
  }

  private static boolean processTypeQNameVariants(@NotNull IonTypeQName typeQName,
                                                  @NotNull Processor<PsiElement> processor) {

    PsiElement qualifier = getQualifier(typeQName);
    PsiReference ref = qualifier != null ? qualifier.getReference() : null;
    PsiElement resolvedQualifier = ref != null ? ref.resolve() : null;
    if (resolvedQualifier != null && (resolvedQualifier instanceof IonDeclImport || resolvedQualifier.getParent() instanceof IonImportPath)) {
      return processImport(PsiTreeUtil.getParentOfType(resolvedQualifier, IonDeclImport.class, false), processor);
    }
    return true;
  }

  private static boolean processDeclAggregate(@NotNull IonDeclAggregate aggregate, @NotNull Processor<PsiElement> processor) {
    for (IonDeclField field : PsiTreeUtil.getStubChildrenOfTypeAsList(aggregate, IonDeclField.class)) {
      if (!processDeclField(field, processor)) {
        return false;
      }
    }
    return true;
  }

  private static boolean processDeclField(@NotNull IonDeclField field, @NotNull Processor<PsiElement> processor) {
    List<IonDeclFieldName> fieldNames = PsiTreeUtil.getStubChildrenOfTypeAsList(field, IonDeclFieldName.class);
    if (!fieldNames.isEmpty()) {
      for (IonDeclFieldName fieldName : fieldNames) {
        if (!processor.process(fieldName)) {
          return false;
        }
      }
    } else {
      // anonymous field
      for (IonDeclField f : PsiTreeUtil.getStubChildrenOfTypeAsList(field, IonDeclField.class)) {
        if (!processDeclField(f, processor)) {
          return false;
        }
      }
    }
    return true;
  }

  private static boolean processCompoundFieldVariants(@NotNull IonCompoundFieldNamed compoundField,
                                                      @NotNull Processor<PsiElement> processor) {
    PsiElement type = getLitCompoundType(ObjectUtils.tryCast(compoundField.getParent(), IonExprLitCompound.class));
    PsiElement baseType = getBaseType(type);
    if (baseType != type) {
      type = resolveType(baseType);
    }
    if (type instanceof IonDeclAggregate) {
      return processDeclAggregate(((IonDeclAggregate) type), processor);
    }
    return true;
  }

  @Nullable
  private static PsiElement getLitCompoundType(@Nullable IonExprLitCompound element) {
    if (element == null) {
      return null;
    }
    PsiElement parent = element.getParent();
    if (parent instanceof IonExprLitCompoundTyped) {
      return resolveType(parent);
    }
    if (parent instanceof IonExprUnary) {
      PsiElement operator = getOperator((IonExprUnary) parent);
      if (operator.getNode().getElementType() == IonToken.AND) {
        parent = parent.getParent();
      }
    }
    if (parent instanceof IonExprCallArg) {
      return resolveType(parent);
    }
    if (parent instanceof IonStmtInit) {
      return resolveType(parent);
    }
    if (parent instanceof IonStmtAssign) {
      return resolveType(parent);
    }
    if (parent instanceof IonDeclVar) {
      return resolveType(parent);
    }
    if (parent instanceof IonCompoundFieldNamed) {
      PsiElement nameElement = getCompoundFieldName((IonCompoundFieldNamed) parent);
      if (nameElement == null) {
        return null;
      }
      ExactMatchProcessor processor = new ExactMatchProcessor(nameElement.getText());
      processCompoundFieldVariants((IonCompoundFieldNamed) parent, processor);
      return resolveType(processor.getElement());
    }
    if (parent instanceof IonCompoundField) {
      IonExprLitCompound parentLiteral = ObjectUtils.tryCast(parent.getParent(), IonExprLitCompound.class);
      if (parentLiteral != null) {
        PsiElement parentType = getLitCompoundType(parentLiteral);
        IonCompoundField[] fields = PsiTreeUtil.getChildrenOfType(parentLiteral, IonCompoundField.class);
        int n = fields != null ? ArrayUtil.indexOf(fields, parent) : -1;
        if (n >= 0) {
          if (parentType instanceof IonTypeArray) {
            PsiElement baseType = getUnderlyingType(parentType);
            PsiReference reference = baseType != null ? baseType.getReference() : null;
            return reference != null ? reference.resolve() : baseType;
          }
          if (parentType instanceof IonDeclAggregate) {
            List<IonDeclField> fieldDecls = PsiTreeUtil.getStubChildrenOfTypeAsList(parentType, IonDeclField.class);
            IonDeclField fieldDecl = n < fieldDecls.size() ? fieldDecls.get(n) : null;
            return resolveType(fieldDecl);
          }
        }
      }
    }
    if (parent instanceof IonStmtReturn) {
      IonDeclFunc func = PsiTreeUtil.getParentOfType(parent, IonDeclFunc.class);
      return resolveType(func);
    }
    return null;
  }

  @Nullable
  private static PsiElement resolveType(@Nullable PsiElement element) {
    if (element == null) {
      return null;
    }
    if (element instanceof IonDeclAggregate) {
      return element;
    }
    if (element instanceof IonTypeName) {
      PsiReference reference = element.getReference();
      return reference != null ? reference.resolve() : null;
    }
    if (element instanceof IonStmtInit) {
      PsiElement type = getStmtInitType((IonStmtInit) element);
      if (type != null) {
        PsiElement resolved = resolveType(type);
        return resolved != null ? resolved : type;
      } else {
        PsiElement expr = getStmtInitExpr((IonStmtInit) element);
        return resolveType(expr);
      }
    }
    if (element instanceof IonStmtAssign) {
      PsiElement lhs = getStmtAssignLhs((IonStmtAssign) element);
      PsiReference reference = lhs != null ? lhs.getReference() : null;
      PsiElement resolvedLhs = reference != null ? reference.resolve() : null;
      return resolveType(resolvedLhs);
    }
    if (element instanceof IonDeclVar) {
      PsiElement type = ((IonDeclVar) element).getType();
      if (type != null) {
        return resolveType(type);
      } else {
        PsiElement expr = getDeclVarExpr((IonDeclVar) element);
        return resolveType(expr);
      }
    }
    // todo IonDeclConst
    if (element instanceof IonDeclField) {
      PsiElement type = ((IonDeclField) element).getType();
      PsiReference reference = type != null ? type.getReference() : null;
      return reference != null ? reference.resolve() : type;
    }
    if (element instanceof IonDeclFieldName) {
      IonDeclField field = PsiTreeUtil.getStubOrPsiParentOfType(element, IonDeclField.class);
      PsiElement type = field != null ? field.getType() : null;
      PsiReference reference = type != null ? type.getReference() : null;
      return reference != null ? reference.resolve() : type;
    }
    if (element instanceof IonDeclFuncParam) {
      PsiElement type = ((IonDeclFuncParam) element).getType();
      PsiReference reference = type != null ? type.getReference() : null;
      return reference != null ? reference.resolve() : type;
    }
    if (element instanceof IonDeclFunc) {
      PsiElement type = ((IonDeclFunc) element).getType();
      PsiReference reference = type != null ? type.getReference() : null;
      return reference != null ? reference.resolve() : type;
    }
    if (element instanceof IonExprLitCompoundTyped) {
      PsiElement type = getExprCompoundTypedType((IonExprLitCompoundTyped) element);
      PsiReference reference = type != null ? type.getReference() : null;
      return reference != null ? reference.resolve() : type;
    }
    if (element instanceof IonExprCallArg) {
      IonExprCall call = ObjectUtils.tryCast(element.getParent(), IonExprCall.class);
      PsiElement[] children = call.getChildren();
      PsiElement callElement = children.length > 0 ? children[0] : null;
      PsiReference ref = callElement != null ? callElement.getReference() : null;
      PsiElement resolved = ref != null ? ref.resolve() : null;
      if (resolved instanceof IonDeclFunc) {
        IonExprCallArg[] args = PsiTreeUtil.getChildrenOfType(call, IonExprCallArg.class);
        int n = args != null ? ArrayUtil.indexOf(args, element) : -1;
        List<IonDeclFuncParam> params = PsiTreeUtil.getStubChildrenOfTypeAsList(resolved, IonDeclFuncParam.class);
        if (n >= 0 && n < params.size()) {
          return resolveType(params.get(n));
        }
      }
      if (resolved instanceof IonTypeOwner) {
        PsiElement type = ((IonTypeOwner) resolved).getType();
        if (type instanceof IonTypeFunc) {
          IonExprCallArg[] args = PsiTreeUtil.getChildrenOfType(call, IonExprCallArg.class);
          int n = args != null ? ArrayUtil.indexOf(args, element) : -1;
          List<IonDeclFuncParam> params = PsiTreeUtil.getStubChildrenOfTypeAsList(type, IonDeclFuncParam.class);
          if (n >= 0 && n < params.size()) {
            return resolveType(params.get(n));
          }
        }
      }
      return null;
    }
    if (element instanceof IonExprName) {
      PsiReference reference = element.getReference();
      PsiElement resolved = reference != null ? reference.resolve() : null;
      return resolveType(resolved);
    }
    if (element instanceof IonExprField) {
      PsiElement nameElement = getFieldName((IonExprField) element);
      if (nameElement == null) {
        return null;
      }
      ExactMatchProcessor processor = new ExactMatchProcessor(nameElement.getText());
      processExprFieldVariants((IonExprField) element, processor);
      return resolveType(processor.getElement());
    }
    if (element instanceof IonExprCall) {
      PsiElement name = ArrayUtil.getFirstElement(element.getChildren());
      PsiReference reference = name != null ? name.getReference() : null;
      PsiElement resolved = reference != null ? reference.resolve() : null;
      PsiElement type = resolveType(resolved);
      if (!(resolved instanceof IonDeclFunc) && type instanceof IonTypeFunc) {
        IonType returnType = PsiTreeUtil.getChildOfType(type, IonType.class);
        return resolveType(returnType);
      }
      return type;
    }
    if (element instanceof IonExprIndex) {
      PsiElement indexedExpr = getExprIndexExpr((IonExprIndex) element);
      PsiElement type = resolveType(indexedExpr);
      if (type instanceof IonTypeArray || type instanceof IonTypePtr) {
        PsiElement underlyingType = getUnderlyingType(type);
        PsiElement underlyingResolved = resolveType(underlyingType);
        return underlyingResolved != null ? underlyingResolved : underlyingType;
      }
      if (type instanceof IonTypeTuple) {
        IonExprLitInt index = PsiTreeUtil.getNextSiblingOfType(indexedExpr, IonExprLitInt.class);
        if (index != null) {
          String text = index.getText();
          try {
            int idx = Integer.parseInt(text);
            List<IonType> tupleItems = PsiTreeUtil.getStubChildrenOfTypeAsList(type, IonType.class);
            if (idx < tupleItems.size()) {
              IonType itemType = tupleItems.get(idx);
              PsiElement resolved = resolveType(itemType);
              return resolved != null ? resolved : itemType;
            }
          } catch (NumberFormatException e) {
            // ignore
          }
        }
      }
    }
    if (element instanceof IonExprUnary) {
      IonExpr expr = PsiTreeUtil.getChildOfType(element, IonExpr.class);
      PsiElement type = resolveType(expr);
      return type != null ? new IonTypePtrLight(type) : null;
    }
    if (element instanceof IonDeclImportItem) {
      PsiElement name = getImportedName((IonDeclImportItem) element);
      PsiReference reference = name != null ? name.getReference() : null;
      return resolveType(reference != null ? reference.resolve() : null);
    }
    PsiReference reference = element.getReference();
    return resolveType(reference != null ? reference.resolve() : null);
  }

  private static boolean processLabels(@Nullable PsiElement element, @NotNull Processor<PsiElement> processor) {
    IonDeclFunc func = PsiTreeUtil.getParentOfType(element, IonDeclFunc.class, false);
    return func == null || PsiTreeUtil.processElements(func, it -> {
      IonStmtLabel label = ObjectUtils.tryCast(it, IonStmtLabel.class);
      if (label != null) {
        return processor.process(label);
      }
      return true;
    });
  }

  private static boolean processPackageDeclarations(@Nullable PsiDirectory packageDir,
                                                    @NotNull Processor<PsiElement> processor) {
    if (packageDir != null) {
      for (PsiFile pkgFile : packageDir.getFiles()) {
        if (pkgFile instanceof IonPsiFile) {
          StubElement<?> stub = ((IonPsiFile) pkgFile).getGreenStub();
          if (stub != null) {
            for (StubElement<?> child : stub.getChildrenStubs()) {
              PsiElement psi = child.getPsi();
              if (psi instanceof IonDecl && !(psi instanceof IonDeclImport)) {
                if (!processDecl(psi, processor)) {
                  return false;
                }
              }
            }
          } else {
            boolean shouldContinue = processDeclarations(pkgFile, null, false, processor);
            if (!shouldContinue) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }

  interface UpProcessor<T> extends Processor<T> {
    default boolean childrenProcessed(@NotNull PsiElement parent) {
      return true;
    }
  }

  private static boolean processDeclarationsUp(@Nullable PsiElement parent,
                                               @Nullable PsiElement processedChild,
                                               @NotNull Processor<PsiElement> processor) {
    while (parent != null) {
      if (!processDeclarations(parent, processedChild, true, processor)) {
        return false;
      }

      if (processor instanceof UpProcessor) {
        if (!((UpProcessor) processor).childrenProcessed(parent)) {
          return false;
        }
      }
      // children processed

      if (parent instanceof PsiFile) {
        PsiDirectory builtinPkg = resolveImport((PsiFile) parent, "builtin");
        if (!processPackageDeclarations(builtinPkg, processor)) {
          return false;
        }
      }

      if (parent instanceof PsiDirectory) {
        // we processed package files and didn't find a definition
        break;
      }

      processedChild = parent;
      parent = parent.getParent();
    }
    return true;
  }

  private static boolean processDeclarations(@NotNull PsiElement parent,
                                             @Nullable PsiElement processedChild,
                                             boolean processImports,
                                             @NotNull Processor<PsiElement> processor) {
    boolean isFile = parent instanceof IonPsiFile;
    boolean isDir = parent instanceof PsiDirectory;
    for (PsiElement child : parent.getChildren()) {
      if (!isFile && !isDir && child.equals(processedChild)) {
        break;
      }
      if (!processDecl(child, processor)) {
        return false;
      }
      if (processImports && child instanceof IonDeclImport && ((IonDeclImport) child).isDotImport()) {
        if (!processImport((IonDeclImport) child, processor)) {
          return false;
        }
      }
      if (child instanceof IonPsiFile && child != processedChild) {
        for (PsiElement topLevelChild : PsiTreeUtil.getStubChildrenOfTypeAsList(child, PsiElement.class)) {
          if (!processDecl(topLevelChild, processor)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  private static boolean processImport(@Nullable IonDeclImport importDecl, @NotNull Processor<PsiElement> processor) {
    if (importDecl == null) {
      return true;
    }
    boolean hasSpec = importDecl.hasImportSpec();
    IonDeclImportItem[] importItems = PsiTreeUtil.getChildrenOfType(importDecl, IonDeclImportItem.class);
    IonImportPath importPath = PsiTreeUtil.getChildOfType(importDecl, IonImportPath.class);
    if (importPath == null) {
      return true;
    }
    Set<String> importedNames = new HashSet<>();
    boolean hasEllipsis = !hasSpec; // treat `import x` as `import x {...}`, see std/std.ion
    if (importItems != null) {
      for (IonDeclImportItem item : importItems) {
        if (!processor.process(item)) {
          return false;
        }
        PsiElement importedName = getImportedName(item);
        String alias = item.getName();
        if (alias == null && importedName != null) {
          importedNames.add(importedName.getText());
        } else if (item.getNode().findChildByType(IonToken.ELLIPSIS) != null) {
          hasEllipsis = true;
        }
      }
    }
    if (hasEllipsis || !importedNames.isEmpty()) {
      PsiDirectory packageDir = resolveImport(importDecl.getContainingFile(), importPath.getText());
      if (packageDir == null) {
        return true;
      }
      Project project = importDecl.getProject();
      Predicate<String> importedNamePredicate = hasEllipsis ? it -> true : importedNames::contains;
      if (!DumbService.isDumb(project) && processor instanceof ExactMatchProcessor) {
        String name = ((ExactMatchProcessor) processor).myName.toString();
        if (!importedNamePredicate.test(name)) {
          return true;
        }
        GlobalSearchScope scope = GlobalSearchScope.filesScope(project, Arrays.asList(packageDir.getVirtualFile().getChildren()));
        for (IonDecl element : StubIndex.getElements(IonNameIndex.KEY, name, project, scope, IonDecl.class)) {
          if (!processor.process(element)) {
            return false;
          }
        }
      } else {
        Processor<PsiElement> proc = it -> {
          IonDecl decl = ObjectUtils.tryCast(it, IonDecl.class);
          return decl == null || !importedNamePredicate.test(decl.getName()) || processor.process(decl);
        };
        return processPackageDeclarations(packageDir, proc);
      }
    }
    return true;
  }

  private static boolean processDecl(@Nullable PsiElement child, @NotNull Processor<PsiElement> processor) {
    if (child instanceof IonDecl) {
      if (!processor.process(child)) {
        return false;
      }
      if (child instanceof IonDeclEnum) {
        for (IonDeclEnumItem enumItem : PsiTreeUtil.getStubChildrenOfTypeAsList(child, IonDeclEnumItem.class)) {
          if (!processor.process(enumItem)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  @Nullable
  private static PsiElement getQualifier(@NotNull IonExprField field) {
    return field.getFirstChild();
  }

  @Nullable
  private static PsiElement getQualifier(@NotNull IonTypeQName typeQName) {
    return typeQName.getFirstChild();
  }

  @Nullable
  static PsiElement getFieldName(@NotNull IonExprField field) {
    PsiElement[] children = field.getChildren();
    return children.length == 2 ? children[1] : null;
  }

  @Nullable
  static PsiElement getTypeName(@NotNull IonTypeQName typeQName) {
    PsiElement[] children = typeQName.getChildren();
    return children.length == 2 ? children[1] : null;
  }

  @Nullable
  private static PsiElement getCompoundFieldName(@NotNull IonCompoundFieldNamed compoundField) {
    return PsiTreeUtil.findChildOfType(compoundField, IonExprName.class);
  }

  @Nullable
  private static PsiElement getStmtAssignLhs(@NotNull IonStmtAssign eleemnt) {
    return eleemnt.getFirstChild();
  }

  @Nullable
  private static PsiElement getStmtInitType(@NotNull IonStmtInit stmt) {
    return PsiTreeUtil.getChildOfType(stmt, IonType.class);
  }

  @Nullable
  private static PsiElement getStmtInitExpr(@NotNull IonStmtInit stmt) {
    IonExpr[] exprs = PsiTreeUtil.getChildrenOfType(stmt, IonExpr.class);
    return exprs != null && exprs.length > 0 ? exprs[0] : null;
  }

  @Nullable
  private static PsiElement getDeclVarExpr(@NotNull IonDeclVar decl) {
    return PsiTreeUtil.getChildOfType(decl, IonExpr.class);
  }

  @Nullable
  private static PsiElement getExprIndexExpr(@NotNull IonExprIndex element) {
    IonExpr[] children = PsiTreeUtil.getChildrenOfType(element, IonExpr.class);
    return children != null && children.length > 0 ? children[0] : null;
  }

  @Nullable
  private static PsiElement getExprCompoundTypedType(@NotNull IonExprLitCompoundTyped lit) {
    PsiElement expr = PsiTreeUtil.findChildOfType(lit, IonExprField.class);
    if (expr == null) {
      expr = PsiTreeUtil.findChildOfType(lit, IonExprName.class);
    }
    return expr;
  }

  @Nullable
  private static PsiElement unwrapParAndConstType(@Nullable PsiElement type) {
    while (true) {
      PsiElement base = null;
      if (type instanceof IonTypeConst) {
        base = getUnderlyingConstType((IonTypeConst) type);
      }
      if (type instanceof IonTypePar) {
        base = getUnderlyingParType((IonTypePar) type);
      }
      if (base == null) {
        return type;
      } else {
        type = base;
      }
    }
  }

  @Nullable
  private static PsiElement getBaseType(@Nullable PsiElement type) {
    while (true) {
      PsiElement base = getUnderlyingType(type);
      if (base == null) {
        return type;
      } else {
        type = base;
      }
    }
  }

  @Nullable
  private static PsiElement getUnderlyingType(@Nullable PsiElement type) {
    if (type instanceof IonTypePtr) {
      return getUnderlyingPtrType((IonTypePtr) type);
    } else if (type instanceof IonTypeArray) {
      return getUnderlyingArrayType((IonTypeArray) type);
    } else if (type instanceof IonTypeConst) {
      return getUnderlyingConstType((IonTypeConst) type);
    } else if (type instanceof IonTypePar) {
      return getUnderlyingParType((IonTypePar) type);
    }
    return null;
  }

  @Nullable
  private static PsiElement getUnderlyingPtrType(@NotNull IonTypePtr type) {
    if (type instanceof IonTypePtrLight) {
      return ((IonTypePtrLight) type).myType;
    }
    return PsiTreeUtil.getStubChildOfType(type, IonType.class);
  }

  @Nullable
  private static PsiElement getUnderlyingArrayType(@NotNull IonTypeArray type) {
    return PsiTreeUtil.getStubChildOfType(type, IonType.class);
  }

  @Nullable
  private static PsiElement getUnderlyingConstType(@NotNull IonTypeConst type) {
    return PsiTreeUtil.getStubChildOfType(type, IonType.class);
  }

  @Nullable
  private static PsiElement getUnderlyingParType(@NotNull IonTypePar type) {
    return PsiTreeUtil.getStubChildOfType(type, IonType.class);
  }

  @NotNull
  private static PsiElement getOperator(@NotNull IonExprUnary expr) {
    return expr.getFirstChild();
  }

  @Nullable
  private static ConcurrentMap<PsiElement, PsiElement> getResolveCache(@Nullable PsiFile file) {
    if (file == null) {
      return null;
    }
    return CachedValuesManager.getCachedValue(file, () -> {
      ConcurrentHashMap<PsiElement, PsiElement> cache = new ConcurrentHashMap<>();
      return CachedValueProvider.Result.create(cache, file, PsiModificationTracker.MODIFICATION_COUNT);
    });
  }

  @Nullable
  private static Ref<Pair<CharSequence, PsiElement>> getBlockResolveCache(@Nullable PsiFile file, @Nullable IonBlock block) {
    if (file == null || block == null) {
      return null;
    }
    return CachedValuesManager.getCachedValue(block, () -> {
      return CachedValueProvider.Result.create(Ref.create(), file, PsiModificationTracker.MODIFICATION_COUNT);
    });
  }

  @Nullable
  private static ConcurrentMap<String, PsiDirectory> getPackageResolveCache(@Nullable PsiDirectory dir) {
    return dir == null ? null : CachedValuesManager.getCachedValue(dir, () -> {
      ConcurrentHashMap<String, PsiDirectory> cache = new ConcurrentHashMap<>();
      return CachedValueProvider.Result.create(cache, dir, PsiModificationTracker.MODIFICATION_COUNT);
    });
  }

  private static boolean isRelativeImportPath(@NotNull String importPath) {
    return importPath.startsWith(".");
  }

  @Nullable
  private static PsiElement getImportedName(@NotNull IonDeclImportItem importItem) {
    return PsiTreeUtil.getChildOfType(importItem, IonExprName.class);
  }

  @Nullable
  private static PsiDirectory resolveImport(@NotNull PsiFile file, @Nullable String importPath) {
    if (importPath == null) {
      return null;
    }
    PsiDirectory result = null;
    PsiDirectory originDir = file.getOriginalFile().getContainingDirectory();
    if (originDir == null) {
      return null;
    }
    ConcurrentMap<String, PsiDirectory> cache = getPackageResolveCache(originDir);
    if (cache != null) {
      result = cache.get(importPath);
      if (result != null) {
        return result;
      }
    }
    if (isRelativeImportPath(importPath)) {
      result = resolveImport(originDir, importPath);
    } else {
      for (SyntheticLibrary lib : IonLibProvider.getLibs(file.getProject())) {
        if (lib instanceof IonLib) {
          IonLib ionLib = (IonLib) lib;
          List<VirtualFile> dirs = ionLib.getDirs();
          for (VirtualFile dir : dirs) {
            VirtualFile pkgDir = resolveImport(dir, importPath);
            if (pkgDir != null) {
              result = PsiManager.getInstance(file.getProject()).findDirectory(pkgDir);
            }
          }
        }
      }
    }
    if (cache != null && result != null) {
      cache.putIfAbsent(importPath, result);
    }
    return result;
  }

  @Nullable
  private static PsiDirectory resolveImport(@NotNull PsiDirectory dir, @NotNull String importPath) {
    List<String> pathParts = StringUtil.split(importPath, ".");
    for (String pathPart : pathParts) {
      PsiDirectory subDir = dir.findSubdirectory(pathPart);
      if (subDir != null) {
        dir = subDir;
      } else {
        return null;
      }
    }
    return dir;
  }

  @Nullable
  private static VirtualFile resolveImport(@NotNull VirtualFile dir, @NotNull String importPath) {
    if (!dir.isDirectory()) {
      dir = dir.getParent();
    }
    List<String> pathParts = StringUtil.split(importPath, ".");
    for (String pathPart : pathParts) {
      VirtualFile child = dir.findChild(pathPart);
      if (child != null && child.isDirectory()) {
        dir = child;
      } else {
        return null;
      }
    }
    return dir;
  }

  private static class ExactMatchProcessor implements UpProcessor<PsiElement> {
    private final CharSequence myName;
    private PsiElement myElement;

    public ExactMatchProcessor(@NotNull CharSequence name) {
      myName = name;
    }

    @Override
    public boolean process(PsiElement psiElement) {
      if (myElement != null) {
        return false;
      }
      IonDecl decl = ObjectUtils.tryCast(psiElement, IonDecl.class);
      if (decl != null) {
        if (StringUtil.equals(myName, decl.getName())) {
          myElement = psiElement;
          return false;
        }
      }
      return true;
    }

    @Nullable
    public PsiElement getElement() {
      return myElement;
    }
  }

  private static class IonTypePtrLight extends IonTypePtrPsi {
    private final PsiElement myType;

    public IonTypePtrLight(@NotNull PsiElement type) {
      super(type.getNode());
      myType = type;
    }

    @NotNull
    @Override
    public PsiElement[] getChildren() {
      return new PsiElement[]{myType};
    }
  }
}

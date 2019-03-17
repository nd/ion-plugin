package ion.dbg;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.xdebugger.XSourcePosition;
import com.jetbrains.cidr.execution.debugger.CidrDebugProcess;
import com.jetbrains.cidr.execution.debugger.CidrDebuggerLanguageSupport;
import com.jetbrains.cidr.execution.debugger.CidrEvaluator;
import com.jetbrains.cidr.execution.debugger.CidrStackFrame;
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver;
import com.jetbrains.cidr.execution.debugger.backend.LLValue;
import com.jetbrains.cidr.execution.debugger.evaluation.CidrDebuggerTypesHelper;
import com.jetbrains.cidr.execution.debugger.evaluation.CidrMemberValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

public class IonDebuggerLanguageSupport extends CidrDebuggerLanguageSupport {
  @Override
  protected @NotNull CidrDebuggerTypesHelper createTypesHelper(@NotNull CidrDebugProcess process) {
    return new CidrDebuggerTypesHelper(process) {
      @Override
      public @Nullable Boolean isImplicitContextVariable(@NotNull XSourcePosition xSourcePosition, @NotNull LLValue llValue) {
        return true; // show all c variables in a frame
      }

      @Override
      public @Nullable XSourcePosition computeSourcePosition(@NotNull CidrMemberValue cidrMemberValue) {
        return null;
      }

      @Override
      public @Nullable XSourcePosition resolveProperty(@NotNull CidrMemberValue cidrMemberValue, @Nullable String s) {
        return null;
      }

      @Override
      protected @Nullable PsiReference createReferenceFromText(@NotNull LLValue llValue, @NotNull PsiElement psiElement) {
        return null;
      }
    };
  }

  @Override
  protected @Nullable CidrEvaluator createEvaluator(@NotNull CidrStackFrame frame) {
    return null;
  }

  @Override
  public Set<DebuggerDriver.DebuggerLanguage> getSupportedDebuggerLanguages() {
    return Collections.emptySet();
  }
}

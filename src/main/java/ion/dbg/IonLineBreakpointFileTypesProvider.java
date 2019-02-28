package ion.dbg;

import com.intellij.openapi.fileTypes.FileType;
import com.jetbrains.cidr.execution.debugger.breakpoints.CidrLineBreakpointFileTypesProvider;
import ion.IonFileType;

import java.util.Collections;
import java.util.Set;

public class IonLineBreakpointFileTypesProvider implements CidrLineBreakpointFileTypesProvider {
  @Override
  public Set<FileType> getFileTypes() {
    return Collections.singleton(IonFileType.INSTANCE);
  }
}

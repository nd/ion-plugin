package ion.psi;

import org.jetbrains.annotations.Nullable;

public interface IonElementTypeIdOwner {
  @Nullable
  IonElementType.TypeId getTypeId();
}

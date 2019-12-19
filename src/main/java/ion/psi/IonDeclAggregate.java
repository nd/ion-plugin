package ion.psi;

import org.jetbrains.annotations.NotNull;

public interface IonDeclAggregate extends IonDecl {

  @NotNull
  Kind getKind();

  enum Kind {
    STRUCT,
    UNION
  }
}

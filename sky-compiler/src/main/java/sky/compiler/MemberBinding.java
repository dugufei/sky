package sky.compiler;

/** A field or method view binding. */
interface MemberBinding {
  /** A description of the binding in human readable form (e.g., "field 'foo'"). */
  String getDescription();
}

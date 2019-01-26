package sk.di;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.SimpleTypeVisitor6;

/**
 * @author sky
 * @version 1.0 on 2018-07-04 下午5:34
 * @see SKUtils
 */
class SKUtils {

	static TypeName bestGuess(TypeMirror type) {
		switch (type.getKind()) {
			case VOID:
				return TypeName.VOID;
			case BOOLEAN:
				return TypeName.BOOLEAN;
			case BYTE:
				return TypeName.BYTE;
			case CHAR:
				return TypeName.CHAR;
			case DOUBLE:
				return TypeName.DOUBLE;
			case FLOAT:
				return TypeName.FLOAT;
			case INT:
				return TypeName.INT;
			case LONG:
				return TypeName.LONG;
			case SHORT:
				return TypeName.SHORT;
			default:
				return ClassName.get(type);
		}
	}

	static String lowerCase(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	static AnnotationMirror getAnnotationMirror(Element typeElement, String className) {
		for (AnnotationMirror m : typeElement.getAnnotationMirrors()) {
			if (m.getAnnotationType().toString().equals(className)) {
				return m;
			}
		}
		return null;
	}

	static AnnotationMirror getAnnotationMirror(TypeElement typeElement, String className) {
		for (AnnotationMirror m : typeElement.getAnnotationMirrors()) {
			if (m.getAnnotationType().toString().equals(className)) {
				return m;
			}
		}
		return null;
	}

	static AnnotationValue getAnnotationValue(AnnotationMirror annotationMirror, String key) {
		for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet()) {
			if (entry.getKey().getSimpleName().toString().equals(key)) {
				return entry.getValue();
			}
		}
		return null;
	}

	static List<Class> getAnnotationValueAsType(AnnotationMirror annotationMirror, String key) {
		AnnotationValue annotationValue = getAnnotationValue(annotationMirror, key);
		if (annotationValue == null) {
			return null;
		}

		return (List<Class>) annotationValue.getValue();
	}

	/**
	 * Returns a string for {@code type}. Primitive types are always boxed.
	 */
	public static String typeToString(TypeMirror type) {
		StringBuilder result = new StringBuilder();
		typeToString(type, result, '.');
		return result.toString();
	}

	public static void typeToString(final TypeMirror type, final StringBuilder result, final char innerClassSeparator) {
		type.accept(new SimpleTypeVisitor6<Void, Void>() {

			@Override public Void visitDeclared(DeclaredType declaredType, Void v) {
				TypeElement typeElement = (TypeElement) declaredType.asElement();
				rawTypeToString(result, typeElement, innerClassSeparator);
				List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
				if (!typeArguments.isEmpty()) {
					result.append("<");
					for (int i = 0; i < typeArguments.size(); i++) {
						if (i != 0) {
							result.append(", ");
						}
						typeToString(typeArguments.get(i), result, innerClassSeparator);
					}
					result.append(">");
				}
				return null;
			}

			@Override public Void visitPrimitive(PrimitiveType primitiveType, Void v) {
				result.append(box((PrimitiveType) type));
				return null;
			}

			@Override public Void visitArray(ArrayType arrayType, Void v) {
				TypeMirror type = arrayType.getComponentType();
				if (type instanceof PrimitiveType) {
					result.append(type.toString()); // Don't box, since this is an array.
				} else {
					typeToString(arrayType.getComponentType(), result, innerClassSeparator);
				}
				result.append("[]");
				return null;
			}

			@Override public Void visitTypeVariable(TypeVariable typeVariable, Void v) {
				result.append(typeVariable.asElement().getSimpleName());
				return null;
			}

			@Override public Void visitError(ErrorType errorType, Void v) {
				// Error type found, a type may not yet have been generated, but we need the
				// type
				// so we can generate the correct code in anticipation of the type being
				// available
				// to the compiler.

				// TODO(cgruber): Figure out a strategy for non-FQCN cases.
				result.append(errorType.toString());
				return null;
			}

			@Override protected Void defaultAction(TypeMirror typeMirror, Void v) {
				throw new UnsupportedOperationException("Unexpected TypeKind " + typeMirror.getKind() + " for " + typeMirror);
			}
		}, null);
	}

	static void rawTypeToString(StringBuilder result, TypeElement type, char innerClassSeparator) {
		String packageName = getPackage(type).getQualifiedName().toString();
		String qualifiedName = type.getQualifiedName().toString();
		if (packageName.isEmpty()) {
			result.append(qualifiedName.replace('.', innerClassSeparator));
		} else {
			result.append(packageName);
			result.append('.');
			result.append(qualifiedName.substring(packageName.length() + 1).replace('.', innerClassSeparator));
		}
	}

	public static PackageElement getPackage(Element type) {
		while (type.getKind() != ElementKind.PACKAGE) {
			type = type.getEnclosingElement();
		}
		return (PackageElement) type;
	}

	static TypeName box(PrimitiveType primitiveType) {
		switch (primitiveType.getKind()) {
			case BYTE:
				return ClassName.get(Byte.class);
			case SHORT:
				return ClassName.get(Short.class);
			case INT:
				return ClassName.get(Integer.class);
			case LONG:
				return ClassName.get(Long.class);
			case FLOAT:
				return ClassName.get(Float.class);
			case DOUBLE:
				return ClassName.get(Double.class);
			case BOOLEAN:
				return ClassName.get(Boolean.class);
			case CHAR:
				return ClassName.get(Character.class);
			case VOID:
				return ClassName.get(Void.class);
			default:
				throw new AssertionError();
		}

	}
}

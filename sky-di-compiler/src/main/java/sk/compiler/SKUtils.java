package sk.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;

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
}

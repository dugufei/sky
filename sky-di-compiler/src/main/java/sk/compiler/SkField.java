package sk.compiler;

import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

final class SkField {

	private final String		name;

	private final TypeName		type;

	private final TypeMirror	elementType;

	private boolean				isBind;

	SkField(String name, TypeName type, TypeMirror elementType) {
		this.name = name;
		this.type = type;
		this.elementType = elementType;
	}

	public String getName() {
		return name;
	}

	public TypeName getType() {
		return type;
	}

	public TypeMirror getElementType() {
		return elementType;
	}

	public boolean isBind() {
		return isBind;
	}

	public void setBind(boolean bind) {
		isBind = bind;
	}
}

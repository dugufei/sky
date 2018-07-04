package sk.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

final class SkMethod {

	private final String							name;

	private final List<? extends VariableElement>	parameters;

	private final TypeMirror						returnType;

	private final boolean							isSingle;

	SkMethod(String name, List<? extends VariableElement> parameters, TypeMirror returnType,boolean isSingle) {
		this.name = name;
		this.parameters = Collections.unmodifiableList(new ArrayList<>(parameters));
		this.returnType = returnType;
		this.isSingle = isSingle;
	}

	public String getName() {
		return name;
	}

	public TypeMirror getReturnType() {
		return returnType;
	}

	public List<? extends VariableElement> getParameters() {
		return parameters;
	}

	public boolean isSingle() {
		return isSingle;
	}
}

package sky.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

final class SkyMethod {

	private final String							name;

	private final List<? extends VariableElement>	parameters;

	private final TypeMirror						returnType;

	SkyMethod(String name, List<? extends VariableElement> parameters, TypeMirror returnType) {
		this.name = name;
		this.parameters = Collections.unmodifiableList(new ArrayList<>(parameters));
		this.returnType = returnType;
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
}

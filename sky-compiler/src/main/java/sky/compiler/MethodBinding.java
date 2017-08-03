package sky.compiler;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

final class MethodBinding implements MemberBinding {

	private final String							name;

	private final Annotation						annotation;

	private final boolean							repeat;

	private final List<? extends VariableElement>	parameters;

	private final TypeMirror						returnType;

	MethodBinding(String name, Annotation annotation, boolean repeat, List<? extends VariableElement> parameters, TypeMirror returnType) {
		this.name = name;
		this.annotation = annotation;
		this.repeat = repeat;
		this.parameters = Collections.unmodifiableList(new ArrayList<>(parameters));
		this.returnType = returnType;
	}

	public String getName() {
		return name;
	}

	public Annotation getAnnotation() {
		return annotation;
	}

	public boolean getRepeat() {
		return repeat;
	}

	public TypeMirror getReturnType() {
		return returnType;
	}

	public List<? extends VariableElement> getParameters() {
		return parameters;
	}

	@Override public String getDescription() {
		return "method '" + name + "'";
	}
}

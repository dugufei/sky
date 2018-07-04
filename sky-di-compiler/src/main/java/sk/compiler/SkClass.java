package sk.compiler;

import java.util.List;

import javax.lang.model.type.TypeMirror;

final class SkClass {

	private final String			name;

	private final List<TypeMirror>	parameters;

	private final TypeMirror		type;

	public final List<SkField>		fields;

	SkClass(String name, List<TypeMirror> parameters, TypeMirror type, List<SkField> fields) {
		this.name = name;
		this.parameters = parameters;
		this.type = type;
		this.fields = fields;
	}

	public String getName() {
		return name;
	}

	public List<TypeMirror> getParameters() {
		return parameters;
	}

	public TypeMirror getType() {
		return type;
	}

	public List<SkField> getFields() {
		return fields;
	}
}

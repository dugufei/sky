package sky.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

/**
 * @author sky
 * @version 1.0 on 2017-07-27 下午5:25
 * @see FieldBinding
 */
public class FieldBinding {

	private final String	name;

	private final TypeName	type;

    FieldBinding(String name, TypeName type) {
        this.name = name;
        this.type = type;
    }

	public String getName() {
		return name;
	}

	public TypeName getType() {
		return type;
	}

	public ClassName getRawType() {
		if (type instanceof ParameterizedTypeName) {
			return ((ParameterizedTypeName) type).rawType;
		}
		return (ClassName) type;
	}
}

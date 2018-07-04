package sk.compiler;

import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor7;

/**
 * @author sky
 * @version 1.0 on 2018-06-28 下午9:50
 * @see SKValueVisitor
 */
public class SKValueVisitor extends SimpleAnnotationValueVisitor7<Void, Void> {

	@Override public Void visitInt(int i, Void p) {
		System.out.printf(">> intValue: %d\n", i);
		return p;
	}

	@Override public Void visitString(String s, Void p) {
		System.out.printf(">> stringValue: %s\n", s);
		return p;
	}

	@Override public Void visitEnumConstant(VariableElement c, Void p) {
		System.out.printf(">> enumValue: %s\n", c.getSimpleName());
		return p;
	}

	@Override public Void visitAnnotation(AnnotationMirror a, Void p) {
		System.out.printf(">> annotationTypeValue: %s\n", a.toString());
		return p;
	}

	@Override public Void visitType(TypeMirror t, Void p) {
		System.out.printf(">> classValue: %s\n", t.toString());
		return p;
	}

	@Override public Void visitArray(List<? extends AnnotationValue> vals, Void p) {
		for (AnnotationValue val : vals) {
			val.accept(this, p);
		}
		return p;
	}
}
package sk.modules;

import com.squareup.javawriter.JavaWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.EnumSet;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

import sk.modules.model.SkClassModel;
import sk.modules.model.SkFieldModel;

/**
 * @author sky
 * @version 1.0 on 2018-12-26 3:18 PM
 * @see SkAutoIDCreate
 */
class SkAutoIDCreate {

	void createAutoID(String moduleName, ArrayList<SkClassModel> skClassModels) throws IOException {

		for (SkClassModel classModel : skClassModels) {
			// 类文件
			StringBuilder filePath = new StringBuilder();
			filePath.append(moduleName);
			filePath.append("/src/main/java/");
			filePath.append(classModel.packageName.replaceAll("\\.", "/"));
			filePath.append("/");
			filePath.append(classModel.className.simpleName());
			filePath.append(".java");

			File outFile = new File(filePath.toString());
			if (!outFile.getParentFile().exists()) {
				outFile.getParentFile().mkdirs();
			}
			if (!outFile.exists()) {
				outFile.createNewFile();
			}
			// 写入内容
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outFile));
			JavaWriter jw = new JavaWriter(writer);

			jw.emitPackage(classModel.packageName);
			jw.emitImports("sky.AutoID");
			jw.emitImports("sky.AutoExplain");

			for (SkFieldModel fieldModel : classModel.skFieldModels) {
				int count = fieldModel.params.size();
				if (count > 0) {
					StringBuilder params = new StringBuilder();
					params.append("{");
					for (int i = 0; i < count; i++) {
						TypeMirror typeMirror = fieldModel.params.get(i);
						jw.emitImports(SkUtils.bestGuess(typeMirror).box().toString());
					}
				}
			}

			jw.emitJavadoc("1.@AutoID注释类,会自动生成属性ID\n2.ID由架构统一管理,你可以定义任何属性不需要添加(public,static,final)..\n3.@AutoExplain 描述和参数");
			jw.emitAnnotation("AutoID",String.valueOf(classModel.className.reflectionName().hashCode()));
			jw.beginType(classModel.packageName + "." + classModel.className.simpleName(), "class", EnumSet.of(Modifier.PUBLIC, Modifier.FINAL));

			for (SkFieldModel fieldModel : classModel.skFieldModels) {
				int count = fieldModel.params.size();

				if (count > 0) {
					StringBuilder params = new StringBuilder();
					params.append("{");
					for (int i = 0; i < count; i++) {
						TypeMirror typeMirror = fieldModel.params.get(i);
						if (i != 0) {
							params.append(",");
						}
						String[] param = SkUtils.bestGuess(typeMirror).box().toString().split("\\.");
						params.append(param[param.length - 1]);
						params.append(".class");
					}
					params.append("}");

					jw.emitAnnotation("AutoExplain", "describe = \"" + fieldModel.describe + "\",params = " + params.toString());
				} else {
					jw.emitAnnotation("AutoExplain", "describe = \"" + fieldModel.describe + "\"");
				}

				String id = String.valueOf((classModel.className.reflectionName() + fieldModel.name).hashCode());

				jw.emitField(fieldModel.type.toString(), fieldModel.name, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL), id);
			}
			jw.endType();
			jw.close();
		}
	}

}

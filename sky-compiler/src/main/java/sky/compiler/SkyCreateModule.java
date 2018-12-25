package sky.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Collection;

import sky.compiler.model.SkyModuleModel;

import static javax.lang.model.element.Modifier.PUBLIC;
import static sky.compiler.SkyConsts.METHOD_LOAD_INTO;
import static sky.compiler.SkyConsts.NAME_METHOD;
import static sky.compiler.SkyConsts.SKY_I_MODULE;
import static sky.compiler.SkyConsts.SKY_I_MODULE_PARAM;
import static sky.compiler.SkyConsts.SKY_I_MODULE_PARAM_MODEL;
import static sky.compiler.SkyConsts.WARNING_TIPS;

/**
 * @author sky
 * @version 1.0 on 2018-12-25 11:23 AM
 * @see SkyCreateModule
 */
public class SkyCreateModule {

    private final Collection<SkyModuleModel> methodViewBindings;

    public SkyCreateModule(Collection<SkyModuleModel> methodViewBindings) {
        this.methodViewBindings = methodViewBindings;
    }
    public JavaFile brewModuleBiz() {

        return JavaFile.builder("sky.module", createModule()).addFileComment("Generated code from Sky. Do not modify!").build();
    }
    private TypeSpec createModule() {
        /*
         * ```SparseArray<SKYIMethodRun>```
         */
        ParameterizedTypeName inputMapTypeOfGroup = ParameterizedTypeName.get(SKY_I_MODULE_PARAM,SKY_I_MODULE_PARAM_MODEL);

        /*
         * Build input param name.
         */
        ParameterSpec groupParamSpec = ParameterSpec.builder(inputMapTypeOfGroup, "sparseArray").build();

        MethodSpec.Builder loadIntoMethodOfGroupBuilder = MethodSpec.methodBuilder(METHOD_LOAD_INTO).addAnnotation(Override.class).addModifiers(PUBLIC).addParameter(groupParamSpec);

        for(SkyModuleModel item : methodViewBindings){
            String providerName = item.getClassName(NAME_METHOD);
            ClassName providerClassName = ClassName.get(item.packageName, providerName);
            String lowerCase = SkyUtils.lowerCase(providerClassName.simpleName());
            loadIntoMethodOfGroupBuilder.addStatement("$T $N = new $T()",providerClassName, lowerCase,providerClassName);

            loadIntoMethodOfGroupBuilder.addStatement("sparseArray.put($N.METHOD_NUMBER, $N)",lowerCase,lowerCase);
        }
        String packageName = "sk";
        ClassName skyClassName = ClassName.get(packageName, "Modules");

        TypeSpec.Builder result = TypeSpec.classBuilder(skyClassName);

        result.addJavadoc(WARNING_TIPS);
        result.addSuperinterface(SKY_I_MODULE);
        result.addModifiers(PUBLIC);
        result.addMethod(loadIntoMethodOfGroupBuilder.build());
        return result.build();
    }
}

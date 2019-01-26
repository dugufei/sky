package sk.modules;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Collection;

import sk.modules.model.SkModuleModel;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static sk.modules.SkConsts.METHOD_LOAD_INTO;
import static sk.modules.SkConsts.NAME_METHOD;
import static sk.modules.SkConsts.SK_I_MODULE;
import static sk.modules.SkConsts.SK_I_MODULE_PARAM;
import static sk.modules.SkConsts.SK_I_MODULE_RUN;
import static sk.modules.SkConsts.WARNING_TIPS;

/**
 * @author sky
 * @version 1.0 on 2018-12-25 11:23 AM
 * @see SkCreateModule
 */
public class SkCreateModule {

    private final Collection<SkModuleModel> methodViewBindings;
    private final String moduleName;

    public SkCreateModule(Collection<SkModuleModel> methodViewBindings, String moduleName) {
        this.methodViewBindings = methodViewBindings;
        this.moduleName = moduleName;
    }
    public JavaFile brewModuleBiz() {

        return JavaFile.builder("sk.module", createModule()).addFileComment("Generated code from Sky. Do not modify!").build();
    }
    private TypeSpec createModule() {
        /*
         * ```SparseArray<SKYIMethodRun>```
         */
        ParameterizedTypeName inputMapTypeOfGroup = ParameterizedTypeName.get(SK_I_MODULE_PARAM,SK_I_MODULE_RUN);

        /*
         * Build input param name.
         */
        ParameterSpec groupParamSpec = ParameterSpec.builder(inputMapTypeOfGroup, "sparseArray").build();

        MethodSpec.Builder loadIntoMethodOfGroupBuilder = MethodSpec.methodBuilder(METHOD_LOAD_INTO).addAnnotation(Override.class).addModifiers(PUBLIC).addParameter(groupParamSpec);

        for(SkModuleModel item : methodViewBindings){
            String providerName = item.getClassName(NAME_METHOD);
            ClassName providerClassName = ClassName.get(item.packageName, providerName);
            String lowerCase = SkUtils.lowerCase(providerClassName.simpleName());
            loadIntoMethodOfGroupBuilder.addStatement("$T $N = new $T()",providerClassName, lowerCase,providerClassName);

            loadIntoMethodOfGroupBuilder.addStatement("sparseArray.put($N.METHOD_NUMBER, $N)",lowerCase,lowerCase);
        }
        ClassName skyClassName = ClassName.get("sk.module", SkUtils.upperCase(moduleName)+"_Module");

        TypeSpec.Builder result = TypeSpec.classBuilder(skyClassName);

        result.addJavadoc(WARNING_TIPS);
        result.addSuperinterface(SK_I_MODULE);
        result.addModifiers(PUBLIC,FINAL);
        result.addMethod(loadIntoMethodOfGroupBuilder.build());
        return result.build();
    }
}

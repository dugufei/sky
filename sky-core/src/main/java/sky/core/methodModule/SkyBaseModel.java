package sky.core.methodModule;

/**
 * @author sky
 * @version 1.0 on 2017-10-30 下午9:03
 * @see SkyBaseModel
 */
abstract class SkyBaseModel implements SKYIMethodRun {

	Class	clazz;

	String	methodName;

	Class[]	paramTypes;

	public SkyBaseModel(Class clazz, String methodName, Class[] paramTypes) {
		this.methodName = methodName;
		this.paramTypes = paramTypes;
		this.clazz = clazz;
	}
}

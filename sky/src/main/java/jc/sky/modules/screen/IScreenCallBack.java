package jc.sky.modules.screen;

/**
 * @author sky
 * @version 1.0 on 2016-11-17 下午10:37
 * @see IScreenCallBack 回调
 */
public interface IScreenCallBack {

	/**
	 * 回调
	 * 
	 * @param nextStep
	 *            内容
	 * @return 参数
	 */
	boolean CallBack(SKYActivityTransporter nextStep);
}

package sk.livedata;

/**
 * @author sky
 * @version 1.0 on 2018-08-02 下午9:09
 * @see SKActionModel
 */
public class SKActionModel {

	public SKActionModel(int state, Object[] extra) {
		this.state = state;
		this.extra = extra;
	}

	public int		state;

	public Object[]	extra;
}

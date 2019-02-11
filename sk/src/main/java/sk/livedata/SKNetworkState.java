package sk.livedata;

/**
 * @author sky
 * @version 1.0 on 2018-08-04 下午10:00
 * @see SKNetworkState
 */
public enum SKNetworkState {
	RUNNING, SUCCESS, FAILED;

	public String Message;

	public void setMessage(String message) {
		this.Message = message;
	}
}

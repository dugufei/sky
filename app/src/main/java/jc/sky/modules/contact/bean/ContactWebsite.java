package jc.sky.modules.contact.bean;

/**
 * @author sky
 * @version 版本
 */
public class ContactWebsite implements Cloneable {

	public String	websit;

	public int		type;

	/**
	 * 克隆
	 *
	 * @return 返回值
	 */
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
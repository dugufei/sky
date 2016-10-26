package jc.sky.modules.contact.bean;

import android.graphics.Bitmap;

import java.util.List;

/**
 * @author sky
 * @version 版本
 */
public class ContactModel implements Cloneable {

	/**
	 * 数据库-联系人ID
	 */
	public String				contactId;

	/**
	 * 联系人头像
	 */
	public Bitmap				photo;

	/**
	 * 用户名称
	 */
	public String				displayName;

	/**
	 * 最后更新时间
	 */
	public long					lastUpdate;

	/**
	 * 电子邮件, Key = type of email, Value = Email address
	 */
	public List<ContactEmail>	emailAddresses;

	/**
	 * 所有电话号码, Key = type of phone number, Value = phone numbers
	 */
	public List<ContactPhone>	phoneNumbers;

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

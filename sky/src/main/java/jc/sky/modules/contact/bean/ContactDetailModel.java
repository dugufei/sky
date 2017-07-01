package jc.sky.modules.contact.bean;

import java.util.List;

import android.graphics.Bitmap;

/**
 * @author sky
 * @version 版本
 */
public class ContactDetailModel implements Cloneable {

	/**
	 * 用户名称
	 */
	public String				name;

	/**
	 * 昵称
	 */
	public String				nickname;

	/**
	 * 公司
	 */
	public String				organization;

	/**
	 * 联网电话
	 */
	public String				networkPhone;

	/**
	 * 生日
	 */
	public String				birthday;

	/**
	 * 阴历生日
	 */
	public String				lunarBirthday;

	/**
	 * 备注
	 */
	public String				note;

	/**
	 * 数据库-联系人ID
	 */
	public String				contactId;

	/**
	 * 联系人头像
	 */
	public Bitmap				photo;

	/**
	 * 电子邮件, Key = type of email, Value = Email address
	 */
	public List<ContactEmail>	emailAddresses;

	/**
	 * 所有电话号码, Key = type of phone number, Value = phone numbers
	 */
	public List<ContactPhone>	phoneNumbers;

	/**
	 * IM
	 */
	public List<ContactIM>		contactIMs;

	/**
	 * 地址
	 */
	public List<ContactAddress>	contactAddresses;

	/**
	 * 网址
	 */
	public List<ContactWebsite>	contactWebsites;

	/**
	 * 最后更新时间
	 */
	public long					lastUpdate;

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

	/**
	 * @return 返回值
	 */
	@Override public String toString() {
		return "ContactDetailModel{" + "name='" + name + '\'' + ", nickname='" + nickname + '\'' + ", organization='" + organization + '\'' + ", networkPhone='" + networkPhone + '\'' + ", birthday='"
				+ birthday + '\'' + ", lunarBirthday='" + lunarBirthday + '\'' + ", note='" + note + '\'' + ", contactId='" + contactId + '\'' + ", photo=" + photo + ", emailAddresses="
				+ emailAddresses + ", phoneNumbers=" + phoneNumbers + ", contactIMs=" + contactIMs + ",  contactAddresses=" + contactAddresses + ", contactWebsites=" + contactWebsites + '}';
	}
}

package jc.sky.modules.contact.bean;

import android.content.Context;
import android.provider.ContactsContract;

/**
 * @author sky
 * @version 版本
 */
public class ContactEmail implements Cloneable {

	public String	emailAddress;

	public int		emailType;

	public String getEmailTypeValue(Context context) {
		return context.getString(ContactsContract.CommonDataKinds.Email.getTypeLabelResource(emailType));
	}

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
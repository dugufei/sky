package jc.sky.modules.contact.bean;

import android.content.Context;
import android.provider.ContactsContract;

/**
 * @author sky
 * @version 版本
 */
public class ContactPhone implements Cloneable {

	public String	phone;

	public int		phoneType;

	public String getPhoneTypeValue(Context context) {

		return context.getString(ContactsContract.CommonDataKinds.Phone.getTypeLabelResource(phoneType));
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
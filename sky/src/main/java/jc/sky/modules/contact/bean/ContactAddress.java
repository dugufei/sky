package jc.sky.modules.contact.bean;

import android.content.Context;
import android.provider.ContactsContract;

/**
 * @author sky
 * @version 版本
 */
public class ContactAddress implements Cloneable {

	public String	address;

	public int		type;

	/**
	 * @param context
	 *            参数
	 * @return 返回值
	 */
	public String getPhoneTypeValue(Context context) {

		return context.getString(ContactsContract.CommonDataKinds.SipAddress.getTypeLabelResource(type));
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
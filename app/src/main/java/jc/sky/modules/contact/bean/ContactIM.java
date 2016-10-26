package jc.sky.modules.contact.bean;

import android.content.Context;
import android.provider.ContactsContract;

/**
 * @author sky
 * @version 版本
 */
public class ContactIM implements Cloneable {

    public String	im;

    public int		type;

    public String getImTypeValue(Context context) {
        return context.getString(ContactsContract.CommonDataKinds.Im.getTypeLabelResource(type));
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
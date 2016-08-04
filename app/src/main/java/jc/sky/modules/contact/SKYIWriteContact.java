package jc.sky.modules.contact;

import android.content.OperationApplicationException;
import android.os.RemoteException;

import java.util.List;

import jc.sky.core.Impl;
import jc.sky.modules.contact.bean.ContactAddress;
import jc.sky.modules.contact.bean.ContactDetailModel;
import jc.sky.modules.contact.bean.ContactEmail;
import jc.sky.modules.contact.bean.ContactPhone;

/**
 * @创建人 sky
 * @创建时间 15/11/18
 * @类描述 通讯录写入
 */
@Impl(ContactManage.class)
public interface SKYIWriteContact {

	/**
	 * 写入系统通讯录
	 * 
	 * @param name
	 * @param organization
	 * @param note
	 * @param phone
	 * @param address
	 * @param emails
	 * @throws RemoteException
	 * @throws OperationApplicationException
	 */
	void writeSystemContact(String name, String organization, String note, List<ContactPhone> phone, List<ContactAddress> address, List<ContactEmail> emails) throws RemoteException,
			OperationApplicationException;

	/**
	 * 更新系统通讯录 - 根据ID
	 * 
	 * @param id
	 * @param name
	 * @param organization
	 * @param note
	 * @param phone
	 * @param address
	 * @param emails
	 * @throws RemoteException
	 * @throws OperationApplicationException
	 */
	void updateSystemContact(String id, String name, String organization, String note, List<ContactPhone> phone, List<ContactAddress> address, List<ContactEmail> emails) throws RemoteException,
			OperationApplicationException;

	/**
	 * 写入 或者更新
	 * 
	 * @param contactDetailModel
	 */
	void wirteAndUpdateSystemContact(ContactDetailModel contactDetailModel) throws RemoteException, OperationApplicationException;
}

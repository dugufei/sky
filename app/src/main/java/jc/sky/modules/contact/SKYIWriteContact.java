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
 * @author sky
 * @version 版本 版本
 */
@Impl(ContactManage.class)
public interface SKYIWriteContact {

	/**
	 * 写入系统通讯录
	 *
	 * @param name
	 *            参数
	 * @param organization
	 *            参数
	 * @param note
	 *            参数
	 * @param phone
	 *            参数
	 * @param address
	 *            参数
	 * @param emails
	 *            参数
	 * @throws RemoteException
	 *             参数
	 * @throws OperationApplicationException
	 *             参数
	 */
	void writeSystemContact(String name, String organization, String note, List<ContactPhone> phone, List<ContactAddress> address, List<ContactEmail> emails)
			throws RemoteException, OperationApplicationException;

	/**
	 * 更新系统通讯录 - 根据ID
	 *
	 * @param id
	 *            参数
	 * @param name
	 *            参数
	 * @param organization
	 *            参数
	 * @param note
	 *            参数
	 * @param phone
	 *            参数
	 * @param address
	 *            参数
	 * @param emails
	 *            参数
	 * @throws RemoteException
	 *             参数
	 * @throws OperationApplicationException
	 *             参数
	 */
	void updateSystemContact(String id, String name, String organization, String note, List<ContactPhone> phone, List<ContactAddress> address, List<ContactEmail> emails)
			throws RemoteException, OperationApplicationException;

	/**
	 * 写入 或者更新
	 *
	 * @param contactDetailModel
	 *            参数
	 * @throws RemoteException
	 *             参数
	 * @throws OperationApplicationException
	 *             参数
	 */
	void wirteAndUpdateSystemContact(ContactDetailModel contactDetailModel) throws RemoteException, OperationApplicationException;
}

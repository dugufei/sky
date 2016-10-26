package jc.sky.modules.contact;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.util.List;

import jc.sky.core.Impl;
import jc.sky.modules.contact.bean.ContactDetailModel;
import jc.sky.modules.contact.bean.ContactModel;
import jc.sky.modules.contact.bean.ContactUser;

/**
 * @author sky
 * @version 版本
 */
@Impl(ContactManage.class)
public interface SKYIContact extends SKYIWriteContact {

	/**
	 * 获取联系人头像
	 *
	 * @param id
	 *            参数
	 * @return 返回值
	 */
	Bitmap getContactPhotoByContactId(@NonNull String id);

	/**
	 * 根据名称搜索
	 *
	 * @param partialName
	 *            参数
	 * @param isPhone
	 *            开关
	 * @param isEmail
	 *            开关
	 * @return 返回值
	 */
	ContactModel getPhoneContactByName(String partialName, boolean isPhone, boolean isEmail);

	/**
	 * 获取联系人 - 根据姓名
	 *
	 * @param userName
	 *            用户名
	 * @param isPhone
	 *            开关
	 * @param isEmail
	 *            开关
	 * @return 返回值
	 */
	List<ContactModel> getAllPhoneContacts(String userName, boolean isPhone, boolean isEmail);

	/**
	 * 获取联系人 - 根据ID
	 * 
	 * @param id
	 *            参数
	 * @param isPhone
	 *            参数
	 * @param isEmail
	 *            参数
	 * @return 返回值
	 */
	List<ContactModel> getAllPhoneContactsByContactId(int id, boolean isPhone, boolean isEmail);

	/**
	 * 获取联系人 - 所有人
	 * 
	 * @param isPhone
	 *            参数
	 * @param isEmail
	 *            参数
	 * @return 返回值
	 */
	List<ContactModel> getAllPhoneContacts(boolean isPhone, boolean isEmail);

	/**
	 * 获取联系人 - 取单个手机号
	 * 
	 * @return 返回值 返回值
	 */
	List<ContactModel> getAllPhoneContacts();

	/**
	 * 获取联系人-详情 - 根据名称
	 * 
	 * @param userName
	 *            名称
	 * @return 返回值
	 */
	List<ContactDetailModel> getAllPhoneDetailContacts(String userName);

	/**
	 * 获取联系人-详情
	 * 
	 * @return 返回值
	 */
	List<ContactDetailModel> getAllPhoneDetailContacts();

	/**
	 * 获取联系人
	 * 
	 * @return 返回值
	 */
	List<String> getAllPhoneDetailIDs();

	/**
	 * 获取所有用户
	 * 
	 * @return 返回值
	 */
	List<ContactUser> getAllUser();

	/**
	 * 搜索用户
	 * 
	 * @param name
	 *            参数
	 * @return 返回值
	 */
	List<ContactUser> getAllUser(String name);

	/**
	 * @param version 参数
	 * @return 返回值
	 */
	List<ContactUser> getAllUser(int version);

	/**
	 * @param name 参数
	 * @param contactIds 参数
	 * @return 返回值
	 */
	List<ContactUser> getAllUser(String name, List<String> contactIds);

	/**
	 * 获取联系人ID - 根据版本
	 * 
	 * @param version
	 *            参数
	 * @return 返回值
	 */
	List<String> getAllPhoneDetailIDs(int version);

	/**
	 * 根据手机号 过滤获取用户ID
	 *
	 * @param number
	 *            参数
	 * @return 返回值
	 */
	List<String> getFilterPhoneNumber(String number);

	/**
	 * 获取联系人 - 找出大于 ID 的数据
	 * 
	 * @param version
	 *            版本
	 * @return 返回值
	 */
	List<ContactDetailModel> getAllPhoneDetailContacts(int version);

	/**
	 * 获取版本
	 *
	 * @return 返回值
	 */
	int getVersion();

	/**
	 * 根据ID 获取详细数据
	 * 
	 * @param id
	 *            参数
	 * @return 返回值
	 */
	ContactDetailModel getContactDataByContactId(String id);
}
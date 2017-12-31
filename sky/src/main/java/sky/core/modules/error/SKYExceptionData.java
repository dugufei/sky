package sky.core.modules.error;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author sky
 * @version 1.0 on 2016-12-18 下午8:37
 * @see SKYExceptionData 异常数据
 */
public class SKYExceptionData implements Parcelable {

	public static final String	RECOVERY_INTENTS	= "recovery_intents";

	public static final String	RECOVERY_INTENT		= "recovery_intent";

	public static final String	EXCEPTION_DATA		= "recovery_exception_data";

	String						type;

	String						msg;

	String						className;

	String						methodName;

	int							lineNumber;

	public static SKYExceptionData newInstance() {
		return new SKYExceptionData();
	}

	public SKYExceptionData type(String type) {
		this.type = type;
		return this;
	}

	public SKYExceptionData className(String className) {
		this.className = className;
		return this;
	}

	public SKYExceptionData methodName(String methodName) {
		this.methodName = methodName;
		return this;
	}

	public SKYExceptionData lineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
		return this;
	}

	public SKYExceptionData msg(String msg) {
		this.msg = msg;
		return this;
	}

	public String getType() {
		return type;
	}

	public String getMsg() {
		return msg;
	}

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	@Override public String toString() {
		return "SKYExceptionData{" + "className='" + className + '\'' + ", type='" + type + '\'' + ", msg='" + msg + '\'' + ", methodName='" + methodName + '\'' + ", lineNumber=" + lineNumber + '}';
	}

	@Override public int describeContents() {
		return 0;
	}

	@Override public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.type);
		dest.writeString(this.msg);
		dest.writeString(this.className);
		dest.writeString(this.methodName);
		dest.writeInt(this.lineNumber);
	}

	public SKYExceptionData() {}

	protected SKYExceptionData(Parcel in) {
		this.type = in.readString();
		this.msg = in.readString();
		this.className = in.readString();
		this.methodName = in.readString();
		this.lineNumber = in.readInt();
	}

	public static final Creator<SKYExceptionData> CREATOR = new Creator<SKYExceptionData>() {

		@Override public SKYExceptionData createFromParcel(Parcel source) {
			return new SKYExceptionData(source);
		}

		@Override public SKYExceptionData[] newArray(int size) {
			return new SKYExceptionData[size];
		}
	};
}
package android.hmm.lib.model;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class AppInfo implements Parcelable{
	
	protected static final long serialVersionUID = 6314789141611213738L;
	private int pid;
	private int uid;
	private int isService = 0;
	
	private String appName;
	private String packageName;
	private String className;
	private String processName;
	private Drawable icon;
	
	private int memorysize;
	private long mCodeSize;
	private long mCachesize ; //缓存大小
	private long mDatasize  ;  //数据大小 
	
	public static final int type_group_system = 0;
	public static final int type_group_user = type_group_system+1;
	
	private int type_group = type_group_system;
	private String permisson;
	private String position;//安装位置
	private String signatures;//签名
	private int startCount = 1;
	private String runTime = "";
	private String fistInstallTime ;
	private String lastUpdateTime ;
	
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public void setCodeSize(long size){
		mCodeSize = size;
	}
	public long getCodeSize(){
		return mCodeSize;
	}
	
	public void setCacheSize(long size){
		mCachesize = size;
	}
	public long getCacheSize(){
		return mCachesize;
	}
	public void setDatasize(long size){
		mDatasize = size;
	}
	public long getDatasize(){
		return mDatasize;
	}
	
	
	public String getFirstInstallTime() {
		return fistInstallTime;
	}
	public void setFirstInstallTime(String fistInstallTime) {
		this.fistInstallTime = fistInstallTime;
	}
	
	public String getPermisson() {
		return permisson;
	}
	public void setPermisson(String permisson) {
		this.permisson = permisson;
	}
	
	public String getPosition() {
		return position;
	}
	public void sePosition(String position) {
		this.position = position;
	}
	
	public void setServiceFlag(int flag){
		isService = flag;
	}
	public int isSeviceFlag(){
		return isService;
	}
	
	@Override
	public String toString() {
		return "AppInfo [appName=" + appName + ", packageName=" + packageName
				+ ", icon=" + icon + ", isSystemApp="  + "]";
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(appName);
		dest.writeString(packageName);
		dest.writeString(className);
		dest.writeString(processName);
		
		dest.writeString(fistInstallTime);
		dest.writeString(lastUpdateTime);
		dest.writeString(position);
		
		dest.writeString(permisson);
		dest.writeString(signatures);
		
		dest.writeInt(pid);
		dest.writeInt(uid);
		dest.writeInt(isService);
		dest.writeInt(type_group);
		
		dest.writeInt(memorysize);
		dest.writeLong(mCodeSize);
		dest.writeLong(mCachesize);
		dest.writeLong(mDatasize);
		
		dest.writeString(runTime);
		dest.writeInt(startCount);
//		Bitmap bmp = DrawUtils.drawableToBitmap(icon);
//		byte[] bts = DrawUtils.BitmapToBytes(bmp);
//		dest.writeByteArray(bts);
	}
	
	public void setSignatures(String signatures) {
		this.signatures = signatures;
	}
	public String getSignatures() {
		return signatures;
	}

	public void setMemorysize(int mMemorysize) {
		this.memorysize = mMemorysize;
	}
	public int getMemorysize() {
		return memorysize;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getUid() {
		return uid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getPid() {
		return pid;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getProcessName() {
		return processName;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setStartCount(int startCount) {
		this.startCount = startCount;
	}
	public int getStartCount() {
		return startCount;
	}

	/**
	 * @param runTime the runTimr to set
	 */
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	/**
	 * @return the runTimr
	 */
	public String getRunTime() {
		return runTime;
	}

	public int getTypeGroup() {
		return type_group;
	}
	public void setTypeGroup(int type_group) {
		this.type_group = type_group;
	}

	public static final Parcelable.Creator<AppInfo> CREATOR = new Parcelable.Creator<AppInfo>() {
		public AppInfo createFromParcel(Parcel in) {
			AppInfo mBook = new AppInfo();
			mBook.appName = in.readString();
			mBook.packageName = in.readString();
			mBook.className = in.readString();
			mBook.processName = in.readString();
	
			mBook.fistInstallTime = in.readString();
			mBook.lastUpdateTime = in.readString();
			mBook.position = in.readString();
			mBook.permisson = in.readString();
			mBook.signatures = in.readString();
			
			mBook.pid = in.readInt();
			mBook.uid = in.readInt();
			mBook.isService = in.readInt();
			mBook.type_group = in.readInt();
			
			mBook.memorysize = in.readInt();
			mBook.mCodeSize = in.readLong();
			mBook.mCachesize = in.readLong();
			mBook.mDatasize = in.readLong();
			
			mBook.runTime = in.readString();
			mBook.startCount = in.readInt();
			
//			mBook.icon = (Drawable) in.readValue(AppInfo.class.getClassLoader());
//			mBook.mIntent = (Intent) in.readValue(AppInfo.class.getClassLoader());
			return mBook;
		}

		public AppInfo[] newArray(int size) {
			return new AppInfo[size];
		}
	};

}

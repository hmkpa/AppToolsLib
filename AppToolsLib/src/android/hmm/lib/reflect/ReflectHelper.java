package android.hmm.lib.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import android.content.Context;

/*********************************************************
 * @Title: ReflectHelper.java
 * @Package android.hmm.lib.reflect
 * @Description: TODO(用一句话描述该文件做什么)
 * @author  heming
 * @date May 5, 2014 -- 4:04:05 PM
 * @version V1.0
 *********************************************************/
public class ReflectHelper {

	/**
	 ****************************************************
	 * 构造函数
	 * @param context
	 * @param className
	 * @param args
	 * @return
	 ****************************************************
	 */
	public static Object getConstructor(Context context, String className, Object... args){
		Object object = null;
		try {
			Class<?> clazz = context.getClassLoader().loadClass(className);
			Constructor<?> constructor = clazz.getConstructor();
			object = constructor.newInstance(args);
			
//			Object[] args = { mContext };
//			Class<?> clazz = mContext.getClassLoader().loadClass(viewName);
//			Constructor<?> constructor = clazz.getConstructor(new Class[] { Context.class });
//			parentViewTemp = (ITableControl) constructor.newInstance(args);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return object;
	}
	
	/**
	 ****************************************************
	 * 获取某个类的成员变量值
	 * @param cls
	 * @param memberVariable
	 * @return
	 ****************************************************
	 */
	public static Object getDeclaredField(Class<?> cls, String memberVariable){
		Object object = null;
		try {
			Field field = cls.getDeclaredField(memberVariable);
			field.setAccessible(true);
			return field.get(object);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return object;
	}
	
	/**
	 * ***************************************************
	 * 设置某个类的成员变量值
	 * setEnumField(wifiConf, "ipAssignment", "STATIC");
	 * @param obj
	 * @param value
	 * @param memberVariable
	 ****************************************************
	 */
	public static void setEnumField(Object obj, String memberVariable, String value) {
		try {
			Field f = obj.getClass().getField(memberVariable);
			f.set(obj, Enum.valueOf((Class<Enum>) f.getType(), value));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
}

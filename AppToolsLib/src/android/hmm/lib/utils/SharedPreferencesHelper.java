package android.hmm.lib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author HEMING
 * Time:2013-9-5
 *
 */
public class SharedPreferencesHelper {

	public static void save(Context context, String file, String Key, String value) {
		SharedPreferences preferences = context.getSharedPreferences(file, 0);
		Editor editor = preferences.edit();
		editor.remove(Key);
		editor.putString(Key, value);
		editor.commit();
	}
	
	public static void save(Context context, String file, String key, boolean value) {
		SharedPreferences preferences = context.getSharedPreferences(file, 0);
		Editor editor = preferences.edit();
		editor.remove(key);
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void delete(Context context, String file, String key) {
		SharedPreferences preferences = context.getSharedPreferences(file, 0);
		Editor editor = preferences.edit();
		editor.remove(key);
		editor.commit();
	}

	public static String getValueString(Context context, String file, String key) {
		SharedPreferences preferences = context.getSharedPreferences(file, 0);
		return preferences.getString(key, "");
	}

	public static boolean getValueBoolean(Context context, String file, String key) {
		SharedPreferences preferences = context.getSharedPreferences(file, 0);
		return preferences.getBoolean(key, false);
	}

}

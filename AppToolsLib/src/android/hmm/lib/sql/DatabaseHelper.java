package android.hmm.lib.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseHelper {
	private final String dbName = "androidapp.db";
	private final String tableName = "applist";

	private static DatabaseHelper dbHelper;
	private static Context mContext;

	private SqliteHelper sqliteHelper;

	private DatabaseHelper() {
		sqliteHelper = new SqliteHelper(mContext, dbName, null, 4);
	}

	public static DatabaseHelper instance(Context context) {
		mContext = context;

		if (dbHelper == null) {
			dbHelper = new DatabaseHelper();
		}

		return dbHelper;
	}

	public boolean isExistPkgName(String pkgName) {
		String sql = "select * from " + tableName + " where pkgName=?";
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		Cursor c = null;
		try {
			c = db.rawQuery(sql, new String[] { String.valueOf(pkgName) });

			if (c.getCount() > 0) {
				return true;
			}
		} finally {
			if (c != null)
				c.close();
		}
		db.close();

		return false;
	}

	public long addPkgName(String pkgName, ContentValues values) {
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();

		long rowId = 0;
		try {
			if (!SqliteHelper.isExistColumn(tableName, db, "devicetype")) {
				String sql = "alter table " + tableName + " add deviceType varchar";
				db.execSQL(sql);
			}

			if (!SqliteHelper.isExistColumn(tableName, db, "shortcat")) {
				String sql = "alter table " + tableName + " add shortcat int";
				db.execSQL(sql);
			}
			rowId = db.insert(tableName, "pkgName", values);
		} catch (Exception e) {
			e.printStackTrace();
		}
		db.close();

		return rowId;
	}

	public int delete(String selection, String[] selectionArgs) {
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		int rowCount = db.delete(tableName, selection, selectionArgs);
		db.close();
		return rowCount;
	}

	public Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		return db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
	}

	public HashMap<String, String> getSupportDevice() {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		HashMap<String, String> list = new HashMap<String, String>();

		Cursor c = null;

		try {
			c = db.rawQuery("select * from appList", null);
			if (c.getCount() > 0) {
				while (c.moveToNext()) {
					String pkgName = c.getString(c.getColumnIndex("pkgName"));
					String deviceType = c.getString(c.getColumnIndex("deviceType"));

					if (!list.containsKey(pkgName)) {
						list.put(pkgName, deviceType);
					}
				}
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		db.close();

		return list;
	}

	public int update(ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();

		int rowCount = db.update(tableName, values, selection, selectionArgs);

		db.close();
		return rowCount;
	}

	/**
	 * 
	 */
	public String isExistPkgInAppCategoryDB(SQLiteDatabase db, String pkg) {
		String category = null;
		String sql = "select * from " + SqliteHelper.table_appCategory + " where pkgName=?";
		printLog(sql + pkg);
		if (pkg != null) {
			try {
				Cursor c = db.rawQuery(sql, new String[] { String.valueOf(pkg) });
				if (null != c) {
					if (c.getCount() > 0) {
						if (c.moveToNext()) {
							category = c.getString(c.getColumnIndex("category"));
						}
					}
					c.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return category;
	}

	public String isExistPkgInAppCategoryDB(String pkg) {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		String category = isExistPkgInAppCategoryDB(db, pkg);
		db.close();
		return category;
	}

	public List<String> getAppCategotyByLable(String category) {
		List<String> list = new ArrayList<String>();
		String sql = "select * from " + SqliteHelper.table_appCategory + " where category=?";
		printLog(sql + category);
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		Cursor c = null;
		try {
			c = db.rawQuery(sql, new String[] { String.valueOf(category) });
			if (c.getCount() > 0) {
				while (c.moveToNext()) {
					String pkgName = c.getString(c.getColumnIndex("pkgName"));
					if (!list.contains(pkgName)) {
						list.add(pkgName);
					}
				}
			}
		} finally {
			if (c != null)
				c.close();
			sqliteHelper.close();
		}
		return list;
	}

	public boolean addAppCategoty(String pkg, String category) {
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		boolean success = false;
		try {
			if (null == pkg || null == category || null != isExistPkgInAppCategoryDB(db, pkg)) {
				return false;
			}
			String sql = "insert into " + SqliteHelper.table_appCategory + " values(null,?,?)";
			printLog(sql + ",pkg:" + pkg + ",category:" + category);
			db.execSQL(sql, new String[] { pkg, category });
			success = true;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		sqliteHelper.close();
		return success;
	}

	public boolean deleteAPpCategory(String pkg) {
		boolean success = false;
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		try {
			String sql = "delete FROM " + SqliteHelper.table_appCategory + " where pkgName=?";
			printLog(sql + pkg);
			db.execSQL(sql, new String[] { String.valueOf(pkg) });
			success = true;
		} finally {
			if (db != null)
				sqliteHelper.close();
		}
		return success;
	}

	public boolean updataAppCategory(String pkg, String category) {
		boolean success = false;
		String sql = "update " + SqliteHelper.table_appCategory + " set ";
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		try {
			String oldcategory = isExistPkgInAppCategoryDB(db, pkg);
			if (null != oldcategory && oldcategory.length() > 0) {
				if (pkg != null && null != category) {
					sql += " pkgName='" + pkg + "',";
					sql += " category='" + category + "'";
				}
				sql += " where pkgName='" + pkg + "' and category='" + oldcategory + "'";
				printLog(sql);
				db.execSQL(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		sqliteHelper.close();
		return success;
	}

	private void printLog(String message) {
		if (null != message) {
			Log.i(getClass().getName(), message);
		}
	}

}

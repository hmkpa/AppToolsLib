package android.hmm.lib.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {
	private final String tableName = "applist";
	public static String table_appCategory = "appCategory";
	
	public SqliteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table IF NOT EXISTS "+ tableName
				+ "(id INTEGER PRIMARY KEY AUTOINCREMENT,pkgName varchar," 
				+ "clsName varchar,deviceType varchar,shortcat int)";
		db.execSQL(sql);
		
		createCategoryTable(db);
	}
	
	private void createCategoryTable(SQLiteDatabase db) {
		String sql = "create table IF NOT EXISTS " + table_appCategory + "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"pkgName varchar,category varchar)";
		db.execSQL(sql);
	}

	/*
	 * sqlite 版本升级 version 2 ：增加一个支持设备的标记
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (!isExistColumn(tableName,db, "deviceType")) {
			String sql = "alter table " + tableName+ " add deviceType varchar";
			db.execSQL(sql);
			sql = "alter table " + tableName+ " add shortcat int";
			db.execSQL(sql);
		}
		
		onCreate(db);
	}

	public static boolean isExistColumn(String tableName,SQLiteDatabase db, String column) {
		try {
			String sql = "select * from sqlite_master where tbl_name=?";

			Cursor c = db.rawQuery(sql, new String[] { tableName });

			if (c.getCount() > 0) {
				while (c.moveToNext()) {
					String s = c.getString(c.getColumnIndex("sql"))
							.toLowerCase();

					if (s.indexOf(column.toLowerCase() + " ") >= 0) {
						return true;
					}
				}
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}

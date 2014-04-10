package com.example.sqlitespeedtest;

import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DbHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "sqliteSpeedTest";

	// Contacts table name
	private static final String TABLE_NOT_INDEXED = "not_indexed";
	private static final String TABLE_INDEXED = "indexed";

	// Contacts Table Columns names
	// private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_TEL = "tel";

	private static final String CREATE_TABLE_INDEXED = "CREATE TABLE "
			+ TABLE_INDEXED + " ( pid integer, " + KEY_NAME + " text,"
			+ KEY_TEL + " text);";

	private static final String CREATE_INDEX = "CREATE INDEX indName on "
			+ TABLE_INDEXED + " ( " + KEY_NAME + " );";

	private static final String CREATE_TABLE_NOT_INDEXED = "CREATE TABLE "
			+ TABLE_NOT_INDEXED + " ( pid integer, " + KEY_NAME + " text,"
			+ KEY_TEL + " text);";

	public DbHandler(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public DbHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_INDEXED);
		db.execSQL(CREATE_INDEX);
		db.execSQL("create index pId on indexed(pid)");
		db.execSQL(CREATE_TABLE_NOT_INDEXED);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INDEXED);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOT_INDEXED);
		// create new tables
		onCreate(db);
	}

	long insert_indexed(Context context, List<String[]> content) {
		SQLiteDatabase db = this.getWritableDatabase();
		long start_test = System.currentTimeMillis();
		db.beginTransaction();
		for (Iterator<String[]> iterator = content.iterator(); iterator
				.hasNext();) {
			String[] contact = (String[]) iterator.next();
			ContentValues values = new ContentValues();
			values.put("pid", contact[0]);
			values.put(KEY_TEL, contact[1]);
			values.put(KEY_NAME, contact[2]);
			db.insert(TABLE_INDEXED, null, values);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
		long end_test = System.currentTimeMillis();
		Toast.makeText(context, content.size() + " item inserted",
				Toast.LENGTH_LONG).show();
		return (end_test - start_test);
	}

	long insert_not_indexed(Context context, List<String[]> content) {
		SQLiteDatabase db = this.getWritableDatabase();
		long start_test = System.currentTimeMillis();
		db.beginTransaction();
		for (Iterator<String[]> iterator = content.iterator(); iterator
				.hasNext();) {
			String[] contact = (String[]) iterator.next();
			ContentValues values = new ContentValues();
			values.put("pid", contact[0]);
			values.put(KEY_TEL, contact[1]);
			values.put(KEY_NAME, contact[2]);
			db.insert(TABLE_NOT_INDEXED, null, values);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
		long end_test = System.currentTimeMillis();
		Toast.makeText(context, content.size() + " item inserted",
				Toast.LENGTH_LONG).show();
		return (end_test - start_test);
	}

	long select_indexed(int limit) {
		SQLiteDatabase db = this.getReadableDatabase();
		long start_test = System.currentTimeMillis();
		for (int i = 0; i < limit; i = i + 100) {
			String selectQuery = "SELECT  * FROM " + TABLE_INDEXED
					+ " WHERE pid >= " + i + " and pid <" + (i + 100) + " AND "
					+ KEY_NAME + " is not '' ;";
			Cursor c = db.rawQuery(selectQuery, null);
//			Log.i("UPDATE TEST", "COUNT COLUMN --> " + c.getCount());
			if (c.moveToFirst()) {
				do {
					// Log.i("UPDATE TEST","Select Name --> "+c.getString(1));
				} while (c.moveToNext());
			}
		}
		db.close();
		long end_test = System.currentTimeMillis();
		return (end_test - start_test);
	}

	long select_not_indexed(int limit) {
		SQLiteDatabase db = this.getReadableDatabase();
		long start_test = System.currentTimeMillis();
		for (int i = 0; i < limit; i = i + 100) {
			String selectQuery = "SELECT  * FROM " + TABLE_NOT_INDEXED
					+ " WHERE pid >= " + i + " and pid <" + (i + 100) + " AND "
					+ KEY_NAME + " is not '' ;";
			Cursor c = db.rawQuery(selectQuery, null);
//			Log.i("UPDATE TEST", "COUNT COLUMN --> " + c.getCount());
			if (c.moveToFirst()) {
				do {
					// Log.i("UPDATE TEST","Select Name --> "+c.getString(1));
				} while (c.moveToNext());
			}
		}
		db.close();
		long end_test = System.currentTimeMillis();
		return (end_test - start_test);
	}

	long update_indexed(int limit) {
		SQLiteDatabase db = this.getWritableDatabase();
		long start_test = System.currentTimeMillis();
		for (int i = 0; i < limit; i = i + 100) {
			String updateQuery = "UPDATE " + TABLE_INDEXED + " SET " + KEY_TEL
					+ " = '-updated' WHERE pid >= " + i + " and pid <"
					+ (i + 100) + " AND " + KEY_NAME + " is not '' ;";
			db.execSQL(updateQuery);
		}
		db.close();
		long end_test = System.currentTimeMillis();
		return (end_test - start_test);
	}

	long update_not_indexed(int limit) {
		SQLiteDatabase db = this.getWritableDatabase();
		long start_test = System.currentTimeMillis();
		for (int i = 0; i < limit; i = i + 100) {
			String updateQuery = "UPDATE " + TABLE_NOT_INDEXED + " SET " + KEY_TEL
					+ " = '-updated' WHERE pid >= " + i + " and pid <"
					+ (i + 100) + " AND " + KEY_NAME + " is not '' ;";
			db.execSQL(updateQuery);
		}
		db.close();
		long end_test = System.currentTimeMillis();
		return (end_test - start_test);
	}

	long delete_indexed(int limit) {
		SQLiteDatabase db = this.getWritableDatabase();
		long start_test = System.currentTimeMillis();
		for (int i = 0; i < limit; i = i + 100) {
			String deleteQuery = "DELETE FROM " + TABLE_INDEXED
					+ " WHERE pid >= " + i + " and pid < " + (i + 100) + " AND "
					+ KEY_NAME + " is not '' ;";
			db.execSQL(deleteQuery);
		}
		db.close();
		long end_test = System.currentTimeMillis();
		return (end_test - start_test);
	}

	long delete_not_indexed(int limit) {
		SQLiteDatabase db = this.getWritableDatabase();
		long start_test = System.currentTimeMillis();
		for (int i = 0; i < limit; i = i + 100) {
			String deleteQuery = "DELETE FROM " + TABLE_NOT_INDEXED
					+ " WHERE pid >= " + i + " and pid < " + (i + 100) + " AND "
					+ KEY_NAME + " is not '' ;";
			db.execSQL(deleteQuery);
		}
		db.close();
		long end_test = System.currentTimeMillis();
		return (end_test - start_test);
	}

	void delete_all(String identifier){
		SQLiteDatabase db = this.getWritableDatabase();
		String deleteQuery = "DELETE FROM " + identifier +" ;";
		db.execSQL(deleteQuery);
		db.close();
	}
}

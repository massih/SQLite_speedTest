package com.example.sqlitespeedtest;

import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
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
			+ TABLE_INDEXED + " ( " + KEY_NAME + " text," + KEY_TEL + " text);";

	private static final String CREATE_INDEX = "CREATE INDEX indName on "
			+ TABLE_INDEXED + " ( " + KEY_NAME + " );";

	private static final String CREATE_TABLE_NOT_INDEXED = "CREATE TABLE "
			+ TABLE_NOT_INDEXED + " ( " + KEY_NAME + " text," + KEY_TEL
			+ " text);";

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
		for (Iterator<String[]> iterator = content.iterator(); iterator.hasNext();) {
			String[] contact = (String[]) iterator.next();
			ContentValues values = new ContentValues();
			values.put(KEY_TEL, contact[0]);
			values.put(KEY_NAME, contact[1]);
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
		for (Iterator<String[]> iterator = content.iterator(); iterator.hasNext();) {
			String[] contact = (String[]) iterator.next();
			ContentValues values = new ContentValues();
			values.put(KEY_TEL, contact[0]);
			values.put(KEY_NAME, contact[1]);
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

	long select_indexed() {
		String selectQuery = "SELECT  * FROM " + TABLE_INDEXED + " WHERE "
				+ KEY_NAME + " LIKE '%';";
		SQLiteDatabase db = this.getReadableDatabase();
		long start_test = System.currentTimeMillis();
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				// adding to todo list
			} while (c.moveToNext());
		}
		db.close();
		long end_test = System.currentTimeMillis();
		return (end_test - start_test);
	}

	long select_not_indexed() {
		String selectQuery = "SELECT  * FROM " + TABLE_NOT_INDEXED + " ;";
		SQLiteDatabase db = this.getReadableDatabase();
		long start_test = System.currentTimeMillis();
		Cursor c = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
//				Log.i("UPDATE TEST","Select Name --> "+c.getString(1));
			} while (c.moveToNext());
		}
		db.close();
		long end_test = System.currentTimeMillis();
		return (end_test - start_test);
	}

	long update_indexed() {
		SQLiteDatabase db = this.getWritableDatabase();
		String updateQuery = "UPDATE " + TABLE_INDEXED + " SET " + KEY_TEL
				+ " = '-updated' WHERE " + KEY_NAME + " LIKE '%' ;";

		long start_test = System.currentTimeMillis();
		db.execSQL(updateQuery);
		// Log.i("UPDATE TEST","Update indexed done in: "+(end_test -
		// start_test));
		db.close();
		long end_test = System.currentTimeMillis();
		return (end_test - start_test);
	}

	long update_not_indexed() {
		SQLiteDatabase db = this.getWritableDatabase();
		String updateQuery = "UPDATE " + TABLE_NOT_INDEXED + " SET " + KEY_TEL
				+ " = 'ni-updated' ;";

		long start_test = System.currentTimeMillis();
		db.execSQL(updateQuery);
		// Log.i("UPDATE TEST","Update done in: "+(end_test - start_test));
		db.close();
		long end_test = System.currentTimeMillis();
		return (end_test - start_test);
	}

	long delete_indexed() {
		SQLiteDatabase db = this.getWritableDatabase();
		String deleteQuery = "DELETE FROM " + TABLE_INDEXED + " WHERE "
				+ KEY_TEL + " Like '%' ;";
		long start_test = System.currentTimeMillis();
		db.execSQL(deleteQuery);
		db.close();
		long end_test = System.currentTimeMillis();
		return (end_test - start_test);
	}

	long delete_not_indexed() {
		SQLiteDatabase db = this.getWritableDatabase();
		String deleteQuery = "DELETE FROM " + TABLE_NOT_INDEXED + " ;";
		long start_test = System.currentTimeMillis();
		db.execSQL(deleteQuery);
		db.close();
		long end_test = System.currentTimeMillis();
		return (end_test - start_test);
	}

}

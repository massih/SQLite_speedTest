package com.example.sqlitespeedtest;

import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.webkit.WebView.FindListener;
import android.widget.TextView;
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

    void insert_indexed(Context context,List<String[]> content) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (Iterator iterator = content.iterator(); iterator.hasNext();) {
			String[] contact = (String[]) iterator.next();
            ContentValues values = new ContentValues();
            values.put(KEY_TEL, contact[0]);
            values.put(KEY_NAME, contact[1]); 
            db.insert(TABLE_INDEXED, null, values);
		}
        db.close();
        Toast.makeText(context, content.size()+" item inserted", Toast.LENGTH_LONG).show();
    }
    
    void insert_not_indexed(Context context, List<String[]> content) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (Iterator iterator = content.iterator(); iterator.hasNext();) {
			String[] contact = (String[]) iterator.next();
            ContentValues values = new ContentValues();
            values.put(KEY_TEL, contact[0]);
            values.put(KEY_NAME, contact[1]); 
            db.insert(TABLE_NOT_INDEXED, null, values);
		}
        db.close();
        Toast.makeText(context, content.size()+" item inserted", Toast.LENGTH_LONG).show();
    }

    public void select_indexed() {
        String selectQuery = "SELECT  * FROM " + TABLE_INDEXED+
        		" WHERE "+KEY_NAME+" LIKE '%';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                // adding to todo list
            } while (c.moveToNext());
        }
        db.close();
    }
    
    public void select_not_indexed() {
        String selectQuery = "SELECT  * FROM " + TABLE_NOT_INDEXED+" ;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                // adding to todo list
            } while (c.moveToNext());
        }
        db.close();
    }
    
    public void update_indexed() {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateQuery = "UPDATE " + TABLE_INDEXED+
        		" SET "+ KEY_TEL+" = '-updated' WHERE "+KEY_NAME+" LIKE '%' ;" ;

        Long start_test = System.nanoTime();
        db.execSQL(updateQuery);
        Long end_test = System.nanoTime();
        Log.i("UPDATE TEST","Update indexed done in: "+(end_test - start_test));
        db.close();
    }
    
    public void update_not_indexed() {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateQuery = "UPDATE " + TABLE_NOT_INDEXED+
        		" SET "+ KEY_TEL+" = 'updated' ;";
        
        Long start_test = System.currentTimeMillis();
        db.execSQL(updateQuery);
        Long end_test = System.currentTimeMillis();
        Log.i("UPDATE TEST","Update done in: "+(end_test - start_test));
        db.close();
    }

    public void delete_indexed() {
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_INDEXED+
        		" WHERE "+ KEY_TEL+" Like '%' ;";
        // updating row
        db.execSQL(deleteQuery);
        db.close();
    }
    
    public void delete_not_indexed() {
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_NOT_INDEXED+" ;";
        // updating row
        db.execSQL(deleteQuery);
        db.close();
    }

}

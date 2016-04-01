package Code.DAO;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

	public SQLiteDatabase db;
	private DBOpenHelper helper;
	private Context context;
	
	private static String DB_NAME = "myDB.db";
	private static int DB_VERSION = 1;
	
	public static String DB_TABLE_NAME = "myTable";
	public static String KEY_ID = "id";
	public static String KEY_NAME = "name";
	public static String KEY_AGE = "";
	public static String KEY_SEX = "";
	public static String KEY_CITY = "";
	public DBAdapter(Context context) {
		this.context = context;
	}
	
	public void close() {
		if (db!=null) {
			db.close();
			db = null;
		}
	}
	public void open() {
		helper = new DBOpenHelper(context, DB_NAME, null, DB_VERSION);
		try {
			db = helper.getWritableDatabase();
		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			db = helper.getReadableDatabase();
		}
	}
	
	public void insert(String name) {
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name);
		db.insert(DB_TABLE_NAME, null, values);
	}
	public void inserts(List<String> names) {
		ContentValues values = new ContentValues();
		for (int i = 0; i < names.size(); i++) {
			values.put(KEY_NAME, names.get(i).toString());
			db.insert(DB_TABLE_NAME, null, values);
			values.clear();
		}
	}
	
	public void inserts_best(List<String> names) {
		db.beginTransaction();
		try {
			for (String string : names) {
				ContentValues values = new ContentValues();
				values.put(KEY_NAME, string);
				db.insert(DB_TABLE_NAME, null, values);
				values.clear();
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("error", e.toString());
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
		
		
	}
	public void query(String name) {
		Cursor results = db.query(DB_TABLE_NAME, new String[]{KEY_ID,KEY_NAME,KEY_SEX,KEY_AGE,KEY_CITY}, KEY_NAME+"="+name, null, null, null, null);
		ConvertToPeople(results);
	}
	private void ConvertToPeople(Cursor results) {
		// TODO Auto-generated method stub
		int resultCounts = results.getCount();
		if (resultCounts==0&&!results.moveToFirst()) {
			return;
		}
		String[] str = new String[resultCounts];
		for (int i = 0; i < resultCounts; i++) {
			str[i] = results.getString(i);
			results.moveToNext();
		}
	}

	public void querys() {
		Cursor results = db.query(DB_TABLE_NAME, null, null, null, null, null, null);
		
	}
	public void delete(String name) {
		db.delete(DB_TABLE_NAME, KEY_NAME+"="+name, null);
	}
	public void deletes() {
		db.delete(DB_TABLE_NAME, null, null);
	}
	public void update(String name) {
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name);
		db.update(DB_TABLE_NAME, values, KEY_NAME+"="+name, null);
	}
	public void updates(List<String> names) {
		ContentValues values = new ContentValues();
		for (int i = 0; i < names.size(); i++) {
			values.put(KEY_NAME, names.get(i).toString());
			db.update(DB_TABLE_NAME, values, KEY_NAME+"="+names.get(i).toString(), null);
			values.clear();
		}
	}
	private static class DBOpenHelper extends SQLiteOpenHelper {

		public DBOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		private static final String CREATE_TABLE = "create table "+ DB_TABLE_NAME +" ("+ KEY_ID +" integer primary key autoincrement, "+
		KEY_NAME +" text not null, "+ KEY_AGE +" integer, "+ KEY_SEX +" text not null, "+ KEY_CITY +" text not null);";
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(CREATE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}

	
		
	}
}

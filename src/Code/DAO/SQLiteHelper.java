package Code.DAO;

import java.util.Date;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ��SQLite���ݿ⸨��
 * 
 * @author Administrator
 * 
 */
public class SQLiteHelper extends SQLiteOpenHelper {

	/**
	 * ���ݿ�����
	 */
	private static final String dbName = "CodepadDb.db";

	/**
	 * �汾��
	 */
	private static final int version = 4;

	public SQLiteHelper(Context context) {
		super(context, dbName, null, version);
	}

	/**
	 * ����ʱ���ã�ֻ����һ��
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// ����
		// ��1 ���±�
		db.execSQL("create table articles("
				+ " id integer primary key autoincrement,"
				+ " name nvarchar(40) not null,"
				+ " Arcode nvarchar(40) ,"
				+ " user nvarchar(40) ,"
				+ " content text default '',"
				+ " date varchar(40) not null)");

		db.execSQL("insert into articles(name,content,date) values(?,?,?)",
				new Object[] { "test title", "test content", new Date() });
		db.execSQL("create table articles_out("
				+ " id integer primary key autoincrement,"
				+ " name nvarchar(40) not null," 
				+ " content text default '',"
				+ " date varchar(40) not null," 
				+ " w_number nvarchar(40) not null)"
				);

		db.execSQL("insert into articles_out(name,content,date,w_number) values(?,?,?,?)",
				new Object[] { "test title", "test content", new Date(), "test w_number" });
		db.execSQL("create table work_number("
				+ " id integer primary key autoincrement,"
				+ " name nvarchar(40) not null)" );
	}

	/**
	 * �汾�Ÿ���ʱ����
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (oldVersion) {
		default:				
		}
	}

}

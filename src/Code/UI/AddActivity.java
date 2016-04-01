package Code.UI;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Code.DAO.ArticleDAO;
import Code.DAO.SQLiteHelper;
import Code.Entites.Article;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddActivity extends BaseActivity {
	private static final int AUTO_ADD_RESULT_OK = 0;
	private static final int AUTO_ADD_RESULT_REPEAT  = 1;
	private static final int ADD_RESULT_OK = 2;
	private static final int ADD_RESULT_REPEAT = 3;
	private static final int ADD_RESULT_NULL = 4;
	private static final int CLEAR_ALL = 5;
	private static final int PREPARE_IN_REPEAT = 6;
	private static final int IN_REPEAT = 7;
	private static final int UPLOAD_IN_REPEAT = 8; 
	String user;
	String area;
	String Arcode;
	EditText txtTitle;
	ListView listView;
	String result;
	String str;
	boolean flag = false;
	List<Article> articles;
	AddListViewAdapter adapter;
	final Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case AUTO_ADD_RESULT_OK:
				String input = (String) msg.obj;
				btnSave_click_auto(input);
				showList2();
				break;
case AUTO_ADD_RESULT_REPEAT:
   txtTitle.setText("");
Toast.makeText(AddActivity.this, "条码已存在!", 1000).show();
break;

case ADD_RESULT_OK:
String input2 = (String) msg.obj;
btnSave_click(input2);
showList2();

break;
case ADD_RESULT_REPEAT:
txtTitle.setText("");
Toast.makeText(AddActivity.this, "此条码已存在", 1000).show();
				break;
case ADD_RESULT_NULL:
Toast.makeText(AddActivity.this, "请输入条码", 1000).show();
break;
case CLEAR_ALL:
showList2();
break;
case PREPARE_IN_REPEAT:
	txtTitle.setText("");
	Toast.makeText(AddActivity.this, "此条码已添加", 1000).show();
break;
case IN_REPEAT:
	txtTitle.setText("");
	Toast.makeText(AddActivity.this, "此条码已入库", 1000).show();
break;
case UPLOAD_IN_REPEAT:
	txtTitle.setText("");
	Toast.makeText(AddActivity.this, "此条码已上传", 1000).show();
break;
			}
		}
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);  
		area = getIntent().getStringExtra("area");
		Arcode = getIntent().getStringExtra("Arcode");
		this.setTitle(area);
		Log.i("Arcode", Arcode);
		txtTitle = (EditText) findViewById(R.id.txtTitle);
		listView = (ListView) findViewById(R.id.add_lv);
		Button btnSave = (Button) findViewById(R.id.btnSave);
		Button bthClear = (Button) findViewById(R.id.btnClear);
		Button bthAdd= (Button) findViewById(R.id.btnAdd);
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("URLS", MODE_PRIVATE);
		user = sharedPreferences.getString("USER", "");
		showList();
	
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String input = txtTitle.getText()+"";
				if (input!=null&&input.length()>0&&isNull(input)) {
//					input = removeChar(input, removeText);
					if (checkRepeat(input)) {
						Message message = handler.obtainMessage();
						message.what = AUTO_ADD_RESULT_OK;
						message.obj = input;
						handler.sendMessage(message);
					} 
				}
			}
		};
		timer.schedule(task, 0, 500);
		
		if (result == null) {
			result = "";
		}
		bthAdd.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String input = txtTitle.getText()+"";
				if (input!=null&&input.length()>0&&checkRepeat(input)) {
					Message msg = handler.obtainMessage();
					msg.what = ADD_RESULT_OK;
					msg.obj = input;
					handler.sendMessage(msg);
				} else if (input.length()==0) {
					Message message = handler.obtainMessage();
					message.what = ADD_RESULT_NULL;
					handler.sendMessage(message);
				}
				// TODO Auto-generated method stub
			
			}
		});
		bthClear.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!isHave()) {
					return;
				}
				SQLiteHelper sqLiteHelper = new SQLiteHelper(AddActivity.this);
				SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
				ArticleDAO articleDao = new ArticleDAO();
				articleDao.DeleteAllPerpare(db);
				db.close();
				Message msg = handler.obtainMessage();
				msg.what = CLEAR_ALL;
				handler.sendMessage(msg);
			}
		});

		btnSave.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isHave()) {
					Toast.makeText(AddActivity.this, "内容不正确,请重新扫码！", 1000).show();
					return;
				}
					saveToAdd();

				
				Intent intent = new Intent(AddActivity.this, menuActivity.class);
				startActivity(intent);
				finish();
			}
		});
	
	}
	public boolean isHave() {
		SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
		ArticleDAO articleDao = new ArticleDAO();
		articles = articleDao.GetAllPerpareArticles(db);
		int i = articles.size();
		db.close();
		if (i>0) {
			return true;
		}
		return false;
	}
	
	public void saveToAdd() {
		SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
		ArticleDAO articleDao = new ArticleDAO();
		articles = articleDao.GetAllPerpareArticles(db);
		for (int i = 0; i < articles.size(); i++) {
			Article article = articles.get(i);
			article.setContent("1");
			//article.setArea(area);
			article.setArcode(Arcode);
			articleDao.UpdateArticle(db, article,user);
		}
		db.close();
	}
	
	public void btnSave_click(String input) {
		    txtTitle.setText("");
			Article article = new Article();
			article.setName(input);
			article.setContent("0");
			article.setDate(new Date());
			SQLiteHelper sqLiteHelper = new SQLiteHelper(
					getApplicationContext());
			SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
			ArticleDAO articleDao = new ArticleDAO();
			articleDao.AddPerpareArticle(db, article);
			db.close();
		
	}
		public void btnSave_click_auto(String input) {
			    txtTitle.setText("");
				Article article = new Article();
				article.setName(input);
				article.setContent("0");
				article.setDate(new Date());
				SQLiteHelper sqLiteHelper = new SQLiteHelper(
						getApplicationContext());
				SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
				ArticleDAO articleDao = new ArticleDAO();
				articleDao.AddPerpareArticle(db, article);
				db.close();
		
	}
	public void showList() {
		SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();

		ArticleDAO articleDao = new ArticleDAO();
		articles = articleDao.GetAllPerpareArticles(db);
		adapter = new AddListViewAdapter(articles, getApplicationContext());
		listView.setAdapter(adapter);
		db.close();
	}
	public void showList2() {
		SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();

		ArticleDAO articleDao = new ArticleDAO();
		articles = articleDao.GetAllPerpareArticles(db);
		adapter.onDataChange(articles);
		//listView.setSelection(listView.getCount());
		db.close();
	}
	public void add_out_mode() {
		str = txtTitle.getText()+"";
		txtTitle.setText("");
		Article article = new Article();
		article.setName(str);
		article.setContent("0");
		article.setDate(new Date());
		SQLiteHelper sqLiteHelper = new SQLiteHelper(
				getApplicationContext());
		SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
		ArticleDAO articleDao = new ArticleDAO();
		articleDao.AddPerpareArticle_out(db, article);
		db.close();	
	}
	

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backToIndex();
			return false;
		} 
		
		return super.onKeyDown(keyCode, event);
	}

	private void backToIndex() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, menuActivity.class);
		startActivity(intent);
		finish();
	}
	
	public boolean checkRepeatPrepare(String input) {
		SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
		ArticleDAO articleDao = new ArticleDAO();
		articles = articleDao.GetAllPerpareArticles(db);
		db.close();
		for (int i = 0; i < articles.size(); i++) {
			if (input.equals(articles.get(i).getName())) {
				Message message = handler.obtainMessage();
				message.what = PREPARE_IN_REPEAT;
				handler.sendMessage(message);
				return false;
			}
		}
		return true;
	}
	public boolean checkRepeatIn(String input) {
		SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
		ArticleDAO articleDao = new ArticleDAO();
		articles = articleDao.GetAllArticles(db);
		db.close();
		for (int i = 0; i < articles.size(); i++) {
			if (input.equals(articles.get(i).getName())) {
				Message message = handler.obtainMessage();
				message.what = IN_REPEAT;
				handler.sendMessage(message);
				return false;
			}
		}
		return true;
	}
	public boolean checkRepeatUpload(String input) {
		SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
		ArticleDAO articleDao = new ArticleDAO();
		articles = articleDao.GetAllUploadArticles(db);
		db.close();
		for (int i = 0; i < articles.size(); i++) {
			if (input.equals(articles.get(i).getName())) {
				Message message = handler.obtainMessage();
				message.what = UPLOAD_IN_REPEAT;
				handler.sendMessage(message);
				return false;
			}
		}
		return true;
	}
	public boolean checkRepeat(String input) {
		if (checkRepeatIn(input)&&checkRepeatPrepare(input)&&checkRepeatUpload(input)) {
			return true;
		}
		return false;
	}
	
	public String removeChar(String input,String removeString) {
		return  input.replace(input.substring((input.indexOf(removeString)), input.length()), "");
	}
	public boolean isNull(String input) {
		if (input.contains(" ")) {
			return true;
		}
		return false;
	}
}

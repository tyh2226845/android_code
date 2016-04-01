package Code.UI;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Code.DAO.ArticleDAO;
import Code.DAO.SQLiteHelper;
import Code.Entites.Article;
import Code.Util.T;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class AddOutActivity extends BaseActivity{

	private static final int AUTO_ADD_RESULT_OK = 0;
	private static final int AUTO_ADD_RESULT_REPEAT  = 1;
	private static final int ADD_RESULT_OK = 2;
	private static final int ADD_RESULT_REPEAT = 3;
	private static final int ADD_RESULT_NULL = 4;
	private static final int CLEAR_ALL = 5;
	private static final int PREPARE_OUT_REPEAT = 6;
	private static final int  OUT_REPEAT = 7;
	private static final int UPLOAD_OUT_REPEAT = 8;
	String user;
	String work_number;
	EditText txtTitle;
	ListView listView;
	String str;
	List<Article> articles;
	AddOutListViewAdapter out_Adapter;
	final Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case AUTO_ADD_RESULT_OK:
				String input = (String) msg.obj;
				add_out_mode_auto(input);
				show_out_List2();
			//	listView.setSelection(out_Adapter.getCount());
				break;
	       case AUTO_ADD_RESULT_REPEAT:
	    	   txtTitle.setText("");
				Toast.makeText(AddOutActivity.this, "条码已存在！", 1000).show();
				break;
	
	case ADD_RESULT_OK:
		String input2 = (String) msg.obj;
		add_out_mode(input2);
		show_out_List2();
		break;
	case ADD_RESULT_REPEAT:
		   txtTitle.setText("");
			Toast.makeText(AddOutActivity.this, "条码已存在！", 1000).show();
			break;
	case ADD_RESULT_NULL:
		Toast.makeText(AddOutActivity.this, "请输入条码！", 1000).show();
		break;
	case CLEAR_ALL:
		show_out_List2();
		break;
	case PREPARE_OUT_REPEAT:
		txtTitle.setText("");
		Toast.makeText(AddOutActivity.this, "此条码已添加", 1000).show();
		break;
	case OUT_REPEAT:
		txtTitle.setText("");
		Toast.makeText(AddOutActivity.this, "此条码已出库", 1000).show();
		break;
	case UPLOAD_OUT_REPEAT:
		txtTitle.setText("");
		Toast.makeText(AddOutActivity.this, "此条码已上传", 1000).show();
		break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_out); 
		work_number = getIntent().getStringExtra("work_number");
		this.setTitle(work_number+"作业单");
		txtTitle = (EditText) findViewById(R.id.out_txtTitle);
		listView = (ListView) findViewById(R.id.out_add_lv);
		Button btnSave = (Button) findViewById(R.id.out_btnSave);
		Button bthClear = (Button) findViewById(R.id.out_btnClear);
	 	Button bthAdd = (Button) findViewById(R.id.out_btnAdd);
	 	SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("URLS", MODE_PRIVATE);
		user = sharedPreferences.getString("USER", "");
	    show_out_List();
		
	Timer timer = new Timer();
	TimerTask task = new TimerTask() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			String input = txtTitle.getText()+"";
			if (input!=null&&input.length()>0&&isNull(input)) {
//				input = removeChar(input, removeText);
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
		

		bthAdd.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String input = txtTitle.getText()+"";
				if (input!=null&&input.length()>0&&checkRepeat(input)) {
					Message msg = handler.obtainMessage();
					msg.what =ADD_RESULT_OK;
					msg.obj = input;
					handler.sendMessage(msg);
				}else if (input.length()==0) {
					Message msg = handler.obtainMessage();
					msg.what =ADD_RESULT_NULL;
					handler.sendMessage(msg);
				}
			}
		});

		bthClear.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!isHave()) {
					return;
				}
				SQLiteHelper sqLiteHelper = new SQLiteHelper(AddOutActivity.this);
				SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
				ArticleDAO articleDao = new ArticleDAO();
				articleDao.DeleteAllPerpare_out_n(db,work_number);
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
					Toast.makeText(AddOutActivity.this, "内容不正确,请重新扫码！", 1000).show();
					return;
				}
				saveToAdd_Out();
				finish();
			}
		});
	
	}
	public boolean isHave() {
		SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
		ArticleDAO articleDao = new ArticleDAO();
		articles = articleDao.GetAllPerpareArticles_out_n(db,work_number);
		int j = articles.size();
		db.close();
		if (j>0) {
			return true;
		}
		return false;
	}
	
	public void saveToAdd_Out() {
		SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
		ArticleDAO articleDao = new ArticleDAO();
		articles = articleDao.GetAllPerpareArticles_out_n(db,work_number);
		for (int i = 0; i < articles.size(); i++) {
			Article article = articles.get(i);
			article.setContent("1");
			articleDao.UpdateArticle_out_n(db, article,work_number);
		}
		db.close();
	}
	
	public void add_out_mode(String input) {
		txtTitle.setText("");
		Article article = new Article();
		article.setName(input);
		article.setContent("0");
		article.setDate(new Date());
		SQLiteHelper sqLiteHelper = new SQLiteHelper(
				getApplicationContext());
		SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
		ArticleDAO articleDao = new ArticleDAO();
		articleDao.AddPerpareArticle_out_n(db, article,work_number);
		db.close();	
	}
	public void add_out_mode_auto(String input) {
		txtTitle.setText("");
		Article article = new Article();
		article.setName(input);
		article.setContent("0");
		article.setDate(new Date());
		SQLiteHelper sqLiteHelper = new SQLiteHelper(
				getApplicationContext());
		SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
		ArticleDAO articleDao = new ArticleDAO();
		articleDao.AddPerpareArticle_out_n(db, article,work_number);
		db.close();	
	}
	
	public void show_out_List() {
		SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();

		ArticleDAO articleDao = new ArticleDAO();
		articles = articleDao.GetAllPerpareArticles_out_n(db,work_number);
		out_Adapter = new AddOutListViewAdapter(articles, getApplicationContext(),work_number);
		listView.setAdapter(out_Adapter);
		db.close();
	}
	
	public void show_out_List2() {
		SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();

		ArticleDAO articleDao = new ArticleDAO();
		articles = articleDao.GetAllPerpareArticles_out_n(db,work_number);
		out_Adapter.onDataChange(articles);
		//T.showShort(getApplicationContext(), listView.getCount()+"");
		
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
//		Intent intent = new Intent(this, menuActivity.class);
//		startActivity(intent);
		finish();
	}
	public boolean checkRepeatPrepare(String input) {
		SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
		ArticleDAO articleDao = new ArticleDAO();
		articles = articleDao.GetAllPerpareArticles_out(db);
		db.close();
		for (int i = 0; i < articles.size(); i++) {
			if (input.equals(articles.get(i).getName())) {
				Message message = handler.obtainMessage();
				message.what = PREPARE_OUT_REPEAT;
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
		articles = articleDao.GetAllArticles_out(db);
		db.close();
		for (int i = 0; i < articles.size(); i++) {
			if (input.equals(articles.get(i).getName())) {
				Message message = handler.obtainMessage();
				message.what = OUT_REPEAT;
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
		articles = articleDao.GetAllUploadArticles_out(db);
		db.close();
		for (int i = 0; i < articles.size(); i++) {
			if (input.equals(articles.get(i).getName())) {
				Message message = handler.obtainMessage();
				message.what = UPLOAD_OUT_REPEAT;
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
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}

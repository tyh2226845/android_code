package Code.UI;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Code.DAO.ArticleDAO;
import Code.DAO.SQLiteHelper;
import Code.Entites.Article;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ListView;

public class InCheckActivity extends BaseActivity{
 
	private InCheckListViewAdapter adapter;
	private List<Article> articles;
	private ListView listView;
	private EditText editText;
	private Timer timer;
	private String str;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what==1) {
				check_bar();
				SQLiteHelper sqLiteHelper = new SQLiteHelper(InCheckActivity.this);
				SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
				ArticleDAO articleDao = new ArticleDAO();
				articles = articleDao.GetAllArticles(db);
				adapter.onDataChange(articles);
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_incheck);
	      powerManager = (PowerManager)this.getSystemService(this.POWER_SERVICE);  
          wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");  
		initView();
		initEvent();
	}
	private void initEvent() {
		// TODO Auto-generated method stub
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		listView = (ListView) findViewById(R.id.incheck_lv);
		editText = (EditText) findViewById(R.id.incheck_et);
		SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
		ArticleDAO articleDao = new ArticleDAO();
		articles = articleDao.GetAllArticles(db);
		db.close();
		Log.i("articles", articles.size()+"");
		adapter = new InCheckListViewAdapter(articles, InCheckActivity.this);
		listView.setAdapter(adapter);
		timer = new Timer();
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String input = editText.getText()+"";
				if (input!=null&&input.length()>0) {
					Message message = handler.obtainMessage();
					message.what = 1;
					handler.sendMessage(message);
				}
			}
		};
		timer.schedule(task,0,500);
	}
	
	public void check_bar() {
		str = editText.getText()+"";
		editText.setText("");
		Article article = new Article();
		article.setName(str);
		article.setContent("2");
		article.setDate(new Date());
		SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
		ArticleDAO articleDao = new ArticleDAO();
		articleDao.UpdateArticle(db, article);
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
		Intent intent = new Intent(this, CheckMenuActivity.class);
		startActivity(intent);
		finish();
	} 
}

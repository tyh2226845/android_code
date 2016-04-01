package Code.UI;

import Code.DAO.ArticleDAO;
import Code.DAO.SQLiteHelper;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class menuActivity extends BaseActivity implements OnClickListener{

	private Button inButton,outButton,uploadButton,exitButton,checkButton;
	private TextView inTextView,outTextView,userTextView;
	private long firstTime = 0;
	private String user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.for_select);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
		initView();
		initEvent();
	}
	private void initEvent() {
		// TODO Auto-generated method stub
		inButton.setOnClickListener(this);
		outButton.setOnClickListener(this);
		uploadButton.setOnClickListener(this);
		checkButton.setOnClickListener(this);
		exitButton.setOnClickListener(this);
	}
	private void initView() {
		// TODO Auto-generated method stub
		inButton = (Button) findViewById(R.id.btn_index1);
		outButton = (Button) findViewById(R.id.btn_index2);
		uploadButton = (Button) findViewById(R.id.btn_index3);
		checkButton = (Button) findViewById(R.id.btn_index4);
		exitButton = (Button) findViewById(R.id.bth_index5);
		inTextView = (TextView) findViewById(R.id.in_menu);
		outTextView = (TextView) findViewById(R.id.out_menu);
		userTextView = (TextView) findViewById(R.id.user_txt);
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("URLS", MODE_PRIVATE);
		user = sharedPreferences.getString("USER", "");
		SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
		ArticleDAO articleDao = new ArticleDAO();
		int inSize = articleDao.GetAllArticles(db).size();
		int outSize = articleDao.GetAllArticles_out(db).size();
		db.close();
		inTextView.setText("已入库："+inSize+"");
		outTextView.setText("已出库："+outSize+"");
		userTextView.setText("操作员："+user);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (arg0.getId()) {
		case R.id.btn_index1:
			intent = new Intent(menuActivity.this, AreaAddActivity.class);
			startActivity(intent);
			finish();
			break;

	case R.id.btn_index2:
		intent = new Intent(menuActivity.this, AddOutWorkNumberActivity.class);
		startActivity(intent);
		finish();
			break;
	case R.id.btn_index3:
		intent = new Intent(menuActivity.this, UploadMenuActivity.class);
		startActivity(intent);
		finish();
		break;
	case R.id.btn_index4:
		intent = new Intent(menuActivity.this, loginActivity.class);
		startActivity(intent);
		finish();
		break;
	case R.id.bth_index5:
		System.exit(0);
		break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}  
	
	private void exit() {
		long secondTime = System.currentTimeMillis();
		if (secondTime-firstTime>2000) {
			Toast.makeText(menuActivity.this, "再按一次退出程序", 1000).show();
			firstTime = secondTime;
		} else {
			finish();
			System.exit(0);
		}
		
	}
}

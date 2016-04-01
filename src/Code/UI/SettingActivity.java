package Code.UI;

import java.util.Date;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import Code.DAO.ArticleDAO;
import Code.DAO.SQLiteHelper;
import Code.Entites.Article;
import Code.Entites.Data;
import Code.Util.RegexUtils;
import Code.Util.SharedPrefsUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends BaseActivity implements OnClickListener{

	//&&regexUtils.checkIpAddress(dataUrl) 
	private ProgressDialog dialog;
	private EditText editText;
	private Button enterButton,resetButton,testButton,backButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
	       powerManager = (PowerManager)this.getSystemService(this.POWER_SERVICE);  
           wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock"); 
		this.setTitle("设置");
		initView();
		initEvent();
	}
	private void initEvent() {
		// TODO Auto-generated method stub
		enterButton.setOnClickListener(this);
		resetButton.setOnClickListener(this);
		testButton.setOnClickListener(this);
		backButton.setOnClickListener(this);
	}
	private void initView() {
		// TODO Auto-generated method stub
		dialog = new ProgressDialog(this);
		dialog.setCancelable(false);
		dialog.setMessage("正在测试...请稍后");
		editText = (EditText) findViewById(R.id.setting_et);
		enterButton = (Button) findViewById(R.id.setting_enter_bth);
		resetButton = (Button) findViewById(R.id.setting_reset_bth);
		testButton = (Button) findViewById(R.id.setting_test_bth);
		backButton = (Button) findViewById(R.id.setting_back_menu_bth);
		getDefaultString();
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.setting_enter_bth:
			String dataUrl = editText.getText()+"";
			RegexUtils regexUtils = new RegexUtils();
			if (dataUrl!=null&&dataUrl.length()>0) {
				SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("URLS", MODE_PRIVATE);
				Editor editor = sharedPreferences.edit();
				editor.clear();
				editor.commit();
				editor.putString("URL", dataUrl);
				editor.commit();
				Toast.makeText(SettingActivity.this, "保存成功！", 1000).show();
				editText.setText(sharedPreferences.getString("URL", "http://192.168.15.61/Service1.asmx"));
				break;
			}
		Toast.makeText(SettingActivity.this, "请输入有效的地址！", 1000).show();
			break;
case R.id.setting_reset_bth:
	SharedPreferences sharedPreferences2 = getApplicationContext().getSharedPreferences("URLS", MODE_PRIVATE);
	Editor editor2 = sharedPreferences2.edit();
	editor2.clear();
	editor2.commit();
	Toast.makeText(SettingActivity.this, "恢复默认成功", 1000).show();
	editText.setText(sharedPreferences2.getString("URL", "http://192.168.15.61/Service1.asmx"));
			break;
case R.id.setting_test_bth:
	TestAsyncTask asyncTask = new TestAsyncTask();
	asyncTask.execute();
	break;
case R.id.setting_back_menu_bth:
	Intent intent = new Intent(SettingActivity.this, menuActivity.class);
	startActivity(intent);
	finish();
	break;
		}
	}

	public void getDefaultString() {
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("URLS", MODE_PRIVATE);
		String defaultString = sharedPreferences.getString("URL", "http://192.168.15.61/Service1.asmx");
		editText.setText(defaultString);
	}
	class TestAsyncTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialog.show();
		}
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			switch (result) {
			case 0:
				Toast.makeText(SettingActivity.this, "服务器连接成功",
						Toast.LENGTH_SHORT).show();
				break;

			case 1:
				Toast.makeText(SettingActivity.this, "服务器连接失败！",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
		@Override
		protected Integer doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("URLS", MODE_PRIVATE);
			String urlData = sharedPreferences.getString("URL", new Data().getUrlData());
				String code = "1";
				String state = "1";
					// TODO Auto-generated method stub
					SoapObject rpc = new SoapObject("http://tempuri.org/",
							"SumString");
					rpc.addProperty("num1", code);
					rpc.addProperty("num2", state);
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);
					envelope.dotNet = true;
					envelope.setOutputSoapObject(rpc);
					HttpTransportSE transport = new HttpTransportSE(
							urlData); 
//					SumString
//					insertCode
					try {
						// 调用WebService
						transport.call("http://tempuri.org/SumString",
								envelope);
					} catch (Exception e) {
//						e.printStackTrace();
						return 1;	
					}
			return 0;
		}
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
}

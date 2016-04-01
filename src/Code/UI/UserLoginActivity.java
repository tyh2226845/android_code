package Code.UI;

import java.util.Date;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import Code.DAO.ArticleDAO;
import Code.DAO.SQLiteHelper;
import Code.Entites.Article;
import Code.Entites.Data;
import Code.Util.T;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserLoginActivity extends BaseActivity{

	private static int SUCCESS_LOGIN = 0;
	private static int FAIL_LOGIN = 1;
	private TextView version_tv;
	private EditText user_et;
	private EditText pwd_et;
	private Button login_bt;
	String urlData;
	ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_user_login2); 
		user_et = (EditText) findViewById(R.id.edit_user);
		pwd_et = (EditText) findViewById(R.id.edit_pwd);
		login_bt = (Button) findViewById(R.id.btn_Logoin1);
		version_tv = (TextView) findViewById(R.id.version_tv);
		version_tv.setText(new Data().getVersion());
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("URLS", MODE_PRIVATE);
		urlData = sharedPreferences.getString("URL", super.urlData);
		dialog = new ProgressDialog(UserLoginActivity.this);
		dialog.setCancelable(false);
		dialog.setMessage("登录中...");
		login_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String user = user_et.getText()+"";
				String password = pwd_et.getText()+"";
				if (TextUtils.isEmpty(user)||TextUtils.isEmpty(password)) {
					T.showShort(getApplicationContext(), "输入有误，请重新输入！");
				} else{
					CheckAsyncTask asyncTask = new CheckAsyncTask();
					asyncTask.execute();
				}
				
			}
		});
	}
	 	
	class CheckAsyncTask extends AsyncTask<Void, Void, String>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//Log.i("URL", urlData);
			dialog.show();
		}
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String user = user_et.getText()+"";
			String password = pwd_et.getText()+"";
				// TODO Auto-generated method stub
				SoapObject rpc = new SoapObject("http://tempuri.org/",
						"CheckUser");
				rpc.addProperty("user", user);
				rpc.addProperty("password", password);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(rpc);
				HttpTransportSE transport = new HttpTransportSE(
						urlData); 
				try {
					// 调用WebService
					transport.call("http://tempuri.org/CheckUser",
							envelope);
				} catch (Exception e) {
					//Log.i("ERROR", e.toString());
//					e.printStackTrace();
					return "false";	
				}
				SoapObject result = (SoapObject) envelope.bodyIn;  
				String flag = result.getProperty(0).toString();
		return flag;
	}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.i("result:", result);
			dialog.dismiss();
			if (!result.equals("false")) {
				SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("URLS", MODE_PRIVATE);
				Editor editor = sharedPreferences.edit();
				editor.clear();
				editor.commit();
				editor.putString("USER", result);
				editor.commit();
				Intent intent = new Intent(UserLoginActivity.this,menuActivity.class);
				startActivity(intent);
				finish();
			} else {
				T.showShort(getApplicationContext(), "登录失败，请重新登录！");
			}
		}
	}
}

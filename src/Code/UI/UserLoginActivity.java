package Code.UI;

import java.util.ArrayList;
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
import Code.Entites.UserInfo;
import Code.Util.HttpConnSoap2;
import Code.Util.T;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
		if (isConnect(UserLoginActivity.this)) {
			try {
				GetUserInfoAsyncTask guia_task = new GetUserInfoAsyncTask();
				guia_task.execute();
			} catch (Exception e) {
				// TODO: handle exception
				T.showShort(getApplicationContext(), "未知错误1");
			}	
		}	
		login_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String user = user_et.getText()+"";
				String password = pwd_et.getText()+"";
				if (TextUtils.isEmpty(user)||TextUtils.isEmpty(password)) {
					T.showShort(getApplicationContext(), "输入有误，请重新输入！");
				} else{
					if (isConnect(UserLoginActivity.this)) {
						try {
							CheckAsyncTask_net net_asyncTask = new CheckAsyncTask_net();
							net_asyncTask.execute();
						} catch (Exception e) {
							// TODO: handle exception
							T.showShort(getApplicationContext(), "未知错误2");
						}
					
					} else {
						try {
							CheckAsyncTask_notnet notnet_task = new CheckAsyncTask_notnet();
							notnet_task.execute();
						} catch (Exception e) {
							// TODO: handle exception
							T.showShort(getApplicationContext(), "未知错误3");
						}
						
					}				
				}				
			}
		});
	}
	
	class CheckAsyncTask_notnet extends AsyncTask<Void, Void, Boolean> {

	String user;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialog.show();
			
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String username = user_et.getText()+"";
			String password = pwd_et.getText()+"";
			SQLiteHelper sqLiteHelper = new SQLiteHelper(UserLoginActivity.this);
			SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
			ArticleDAO articleDao = new ArticleDAO();
			String[] userinfo = articleDao.GetUserPassword(db, username);
			db.close();
			if (userinfo[0].equals(password)) {
				user = userinfo[1];
				return true;
			} else {
				return false;
			}
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if (result) {
				SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("URLS", MODE_PRIVATE);
				Editor editor = sharedPreferences.edit();
				editor.clear();
				editor.commit();
				editor.putString("USER", user);
				editor.commit();
				Intent intent = new Intent(UserLoginActivity.this,menuActivity.class);
				startActivity(intent);
				finish();
			} else {
				T.showShort(getApplicationContext(), "登录失败，请重新登录！");
			}
		}
		
		
	}
	public static boolean isConnect(Context context) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.v("error", e.toString());
		}
		return false;
	}
	
	class GetUserInfoAsyncTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("URLS", MODE_PRIVATE);
			int userInfoCount = sharedPreferences.getInt("userInfoCount", 0);
			List<UserInfo> list = new ArrayList<UserInfo>();
			HttpConnSoap2 hcs = new HttpConnSoap2();
			list = hcs.parase_userInfo(hcs.GetWebServer("GetUserList", urlData));
			SQLiteHelper sqLiteHelper = new SQLiteHelper(UserLoginActivity.this);
			SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
			ArticleDAO articleDao = new ArticleDAO();
			if (articleDao.GetUserCount(db)==0||list.size()!=userInfoCount) {
				articleDao.DeleteAllUserInfo(db);
				articleDao.InsertUserInfo(db, list);
			}
			Editor editor = sharedPreferences.edit();
			editor.clear();
			editor.commit();
			editor.putInt("userInfoCount", list.size());
			editor.commit();
			db.close();
			return null;
		}	
	}
	
	 
	class CheckAsyncTask_net extends AsyncTask<Void, Void, String>{

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
			super.onPreExecute();
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

package Code.UI;

import Code.Entites.*;
import java.util.Timer;
import java.util.TimerTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import Code.Util.T;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.text.style.SuperscriptSpan;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.EditText;

public class AreaAddActivity extends BaseActivity{

	private final static int SUCCESS = 0;
	private final static int TRAY = 2;
	private final static int TWO = 1;
	private EditText area_et;
	String urlData = "";
	String Arcode;
	String area;
	String Arcode_area;
	String Arcode_tray;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				Arcode_area = Arcode;
				area_et.setText("");
				AreaAsyncTask asyncTask = new AreaAsyncTask();
				asyncTask.execute();
			case TWO:
				break;
			case TRAY:
				TrayAsyncTask tat = new TrayAsyncTask();
				tat.execute();
			default:
				break;
			}
		};
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_area); 
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		area_et = (EditText) findViewById(R.id.area_et);
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("URLS", MODE_PRIVATE);
		urlData = sharedPreferences.getString("URL", super.urlData);
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Arcode = area_et.getText()+"";
				if (!TextUtils.isEmpty(Arcode)&&isNull(Arcode)) {
				Message message = handler.obtainMessage();
				message.what = SUCCESS;
				handler.sendMessage(message);
				}
			}
		};
		timer.schedule(task, 0, 500);
	}
	public boolean isNull(String input) {
		if (input.contains(" ")) {
			return true;
		}
		return false;
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
	
	class AreaAsyncTask extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject rpc = new SoapObject("http://tempuri.org/",
					"GetAreaName");
			rpc.addProperty("Arcode", Arcode_area);
			//rpc.addProperty("password", password);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(
					urlData); 
//			SumString
//			insertCode
			try {
				// 调用WebService
				transport.call("http://tempuri.org/GetAreaName",
						envelope);
			} catch (Exception e) {
//				e.printStackTrace();
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
			//T.showShort(getApplicationContext(), result);
			Arcode_tray = Arcode_area;
			boolean flag_area = true;
			if (result.contains("anyType")) {
			//	T.showShort(getApplicationContext(), "无此区域，请扫描正确的条码！");
				flag_area = false;
			} else if (result.equals("false")) {
				T.showShort(getApplicationContext(), "网络连接失败！");
				flag_area = false;		
    		}
			if (!flag_area) {
				Message message = handler.obtainMessage();
				message.what = TRAY;
				handler.sendMessage(message);
			} else {		
				Intent intent = new Intent(AreaAddActivity.this,AddActivity.class);
				intent.putExtra("area", result);
				intent.putExtra("Arcode", Arcode);
				startActivity(intent);
				finish();
			}	
			//break;
		}
		
	}
	
	class TrayAsyncTask extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject rpc = new SoapObject("http://tempuri.org/",
					"GetTrayName");
			rpc.addProperty("Arcode", Arcode_tray);
			//rpc.addProperty("password", password);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(
					urlData); 
//			SumString
//			insertCode
			try {
				// 调用WebService
				transport.call("http://tempuri.org/GetTrayName",
						envelope);
			} catch (Exception e) {
//				e.printStackTrace();
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
			//T.showShort(getApplicationContext(), result);
			area_et.setText("");
			if (result.contains("anyType")) {
				T.showShort(getApplicationContext(), "无此区域，请扫描正确的条码！");
				return;
			} else if (result.equals("false")) {
				T.showShort(getApplicationContext(), "网络连接失败！");
				return;
			}
			Intent intent = new Intent(AreaAddActivity.this,AddActivity.class);
			intent.putExtra("area", result);
			intent.putExtra("Arcode", Arcode_tray);
			startActivity(intent);
			finish();
			//break;
		}
		
	}
}

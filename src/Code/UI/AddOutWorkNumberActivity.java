package Code.UI;

import Code.Entites.*;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import Code.DAO.ArticleDAO;
import Code.DAO.SQLiteHelper;
import Code.Entites.Article;
import Code.Entites.PlanarWorkOut;
import Code.Util.HttpConnSoap2;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class AddOutWorkNumberActivity extends BaseActivity{

	private static final int AUTO_ADD_RESULT_OK = 10000;
	private static final int AUTO_ADD_RESULT_REPEAT  = 1;
	private static final int ADD_RESULT_OK = 2;
	private static final int ADD_RESULT_REPEAT = 3;
	private static final int ADD_RESULT_NULL = 10001;
	private static final int CLEAR_ALL = 5;
	private static final int PREPARE_OUT_REPEAT = 6;
	private static final int  OUT_REPEAT = 7;
	private static final int UPLOAD_OUT_REPEAT = 8;
	HttpConnSoap2 soap2 = new HttpConnSoap2();  
	ProgressDialog dialog;
	String work_number_result;
	String urlData;
	EditText txtTitle;
	ListView listView;
	String str;
	List<Article> articles;
	List<PlanarWorkOut> pwos;
	AddOutWorkNumberAdapter adapter;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AUTO_ADD_RESULT_OK:
				txtTitle.setText("");
				String input = (String) msg.obj;
				Intent intent = new Intent(AddOutWorkNumberActivity.this, AddOutActivity.class);
				intent.putExtra("work_number", input);
				startActivity(intent);
//				AddOutWorkNumberAsyncTask asyncTask = new AddOutWorkNumberAsyncTask();
//				asyncTask.execute(input);
				break;

			case ADD_RESULT_NULL:
				Toast.makeText(AddOutWorkNumberActivity.this, "你的输入为空", 1000).show();
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_outworknumber);
	      powerManager = (PowerManager)this.getSystemService(this.POWER_SERVICE);  
          wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");  
		dialog = new ProgressDialog(AddOutWorkNumberActivity.this);
		dialog.setCancelable(false);
		this.setTitle("单号添加");
		txtTitle = (EditText) findViewById(R.id.outWorkNumber_et);
		listView = (ListView) findViewById(R.id.outWorkNumber_add_lv);
		Button bthAdd = (Button) findViewById(R.id.outWorkNumber_btnAdd);
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("URLS", MODE_PRIVATE);
		urlData = sharedPreferences.getString("URL", super.urlData);
	//	urlData = "http://192.168.8.64/WuHan/";
		listAsyncTask lAsyncTask = new listAsyncTask();
		lAsyncTask.execute();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AddOutWorkNumberActivity.this, AddOutActivity.class);
				intent.putExtra("work_number", adapter.getWorkId(position));
				startActivity(intent);
			}
		});
//	    show_out_List();
		
	Timer timer = new Timer();
	TimerTask task = new TimerTask() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			String input = txtTitle.getText()+"";
			if (input!=null&&input.length()>0&&isNull(input)) {
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
					txtTitle.setText("");
					Intent intent = new Intent(AddOutWorkNumberActivity.this, AddOutActivity.class);
					intent.putExtra("work_number", input);
					startActivity(intent);			
				}else if (input.length()==0) {
					Message msg = handler.obtainMessage();
					msg.what =ADD_RESULT_NULL;
					handler.sendMessage(msg);
				}
			}
		});
	
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
	
	
	public boolean isNull(String input) {
		if (input.contains(" ")) {
			return true;
		}
		return false;
	}
	

	class AddOutWorkNumberAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialog.show();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			String[] checks = getWorkIdList();
			Log.i("result", result);
			boolean flag = false;
			if (!result.equals("")&&result!=null) {	
				for (int i = 0; i < pwos.size(); i++) {
					if (result.equals(checks[i])) {
						flag = true;
						Intent intent = new Intent(AddOutWorkNumberActivity.this, AddOutActivity.class);
						intent.putExtra("work_number", result);
						startActivity(intent);
						break;
					}
				}
				if (!flag) {
					Toast.makeText(AddOutWorkNumberActivity.this, "无此作业单！", 1000).show();
				}
			} else {
				Toast.makeText(AddOutWorkNumberActivity.this, "未知错误", 1000).show();
			}
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String work_number = arg0[0];
			
//				String code = article.getName();
					// TODO Auto-generated method stub
					SoapObject rpc = new SoapObject("http://tempuri.org/",
							"GetWorkformid");
					rpc.addProperty("work_number", work_number);
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
						transport.call("http://tempuri.org/GetWorkformid",
								envelope);
					} catch (Exception e) {
//						e.printStackTrace();
						return "fail";	
					}
					SoapObject result = (SoapObject) envelope.bodyIn;  
					work_number_result = result.getProperty(0).toString();		
			return work_number_result;
		}
	}
	class listAsyncTask extends AsyncTask<String, Void, List<PlanarWorkOut>> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialog.show();
		}
		@Override
		protected void onPostExecute(List<PlanarWorkOut> result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if (result != null) {
				Log.i("result", result.get(0).getWorkId()+"");
				adapter = new AddOutWorkNumberAdapter(result, AddOutWorkNumberActivity.this);
				listView.setAdapter(adapter);
			} else {
				Toast.makeText(AddOutWorkNumberActivity.this, "数据读取失败", 1000).show();
			}
		
		}
		@Override
		protected List<PlanarWorkOut> doInBackground(String... arg0) {
			try {
				pwos = soap2.paraseC(soap2.GetWebServer("GetPlanarWorkOut",urlData));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return pwos;
		}
	}
	public String[] getWorkIdList() {
		String[] workIds = new String[pwos.size()];
		for (int i=0;i<workIds.length;i++) {
			workIds[i] = pwos.get(i).getWorkId();
		}
		return workIds;
	}
	public boolean isCheckHaveWorkId(String result,String checks[]) {
		for (int i = 0; i < pwos.size(); i++) {
			if (result.equals(checks[i])) {
				listView.setSelection(i);
				return true;
			}
		}
		return false;
	}
}

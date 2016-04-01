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
import Code.UI.UploadListViewAdapter.ChangeTextListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UploadActivity extends BaseActivity  {

	String urlData;
	ProgressDialog dialog ;
	private Button bthforclear;
	UploadListViewAdapter adapter;
	TextView uploadTextView;
	TextView pUploadTextView;
	ListView listArticles;
	List<Article> articles;
	Article updateart;
	Button btnforupload; 
	String user;
	int maxProgressLenght = 0;

	@SuppressWarnings("null")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);
		this.setTitle("条码上传");
		dialog = new ProgressDialog(UploadActivity.this);
		dialog.setCancelable(false);
		dialog.setMessage("正在上传...请稍后");
		dialog.setIndeterminate(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		bthforclear = (Button) findViewById(R.id.btnforclear);
		listArticles = (ListView) findViewById(R.id.listforupload);
		btnforupload = (Button) findViewById(R.id.btnforupload);
		uploadTextView = (TextView) findViewById(R.id.in_upload_tv);
		pUploadTextView = (TextView) findViewById(R.id.in_pupload_tv);
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("URLS", MODE_PRIVATE);
		urlData = sharedPreferences.getString("URL", super.urlData);
	    user = sharedPreferences.getString("USER", "");
		Log.i("urlData", urlData+"");
		if (isConnect(this) == false) {
			new AlertDialog.Builder(this).setTitle("网络错误")
					.setMessage("网络连接失败，请确认网络连接").show();
		}
		setNum();
		Log.i("Msg", articles.size() + "");
		adapter = new UploadListViewAdapter(articles,
				this);
		adapter.setOnChangeTextListener(new ChangeTextListener() {
			
			@Override
			public void changeText() {
				// TODO Auto-generated method stub
				setNum();
			}
		});
		try {
			listArticles.setAdapter(adapter);
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(UploadActivity.this, "2、" + e, Toast.LENGTH_SHORT)
					.show();
		}
		bthforclear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SQLiteHelper sqLiteHelper = new SQLiteHelper(UploadActivity.this);
				SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
				ArticleDAO articleDao = new ArticleDAO();
				articleDao.DeleteAllUpload(db);
				int i = articleDao.GetAllUploadArticles(db).size();
				UploadActivity.this.uploadTextView.setText("已上传："+i);
				db.close();
			}
		});

		btnforupload.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isHave()) {
					return;
				}
				UploadAsyncTask asyncTask = new UploadAsyncTask();
				asyncTask.execute(articles);
			}
		});
	}

	public boolean isHave() {
		SQLiteHelper sqLiteHelper = new SQLiteHelper(UploadActivity.this);
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
		ArticleDAO articleDao = new ArticleDAO();
		int i = articleDao.GetAllArticles(db).size();
		db.close();
		if (i>0) {
			return true;
		}
		return false;
	
	}
 
	public void setNum() {
		SQLiteHelper sqLiteHelper = new SQLiteHelper(getApplicationContext());
		SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
		ArticleDAO articleDao = new ArticleDAO();
		int i = articleDao.GetAllUploadArticles(db).size();
		UploadActivity.this.uploadTextView.setText("已上传："+i);
		articles = articleDao.GetAllArticles(db);
		int j = articles.size();
		dialog.setMax(j);
		maxProgressLenght = j;
		UploadActivity.this.pUploadTextView.setText("待上传："+j);
		db.close();
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
		Intent intent = new Intent(this, UploadMenuActivity.class);
		startActivity(intent);
		finish();
	}

	class UploadAsyncTask extends AsyncTask<List<Article>, Integer, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialog.show();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			SQLiteHelper sqLiteHelper = new SQLiteHelper(getApplicationContext());
			SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
			ArticleDAO articleDao = new ArticleDAO();
			articles = articleDao.GetAllArticles(db);
			UploadActivity.this.adapter.onDataChange(articles);
			UploadActivity.this.setNum();
			if (result.equals("true")) {
				
				Toast.makeText(UploadActivity.this, "上传成功！",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(UploadActivity.this, "上传失败！请重新上传",
						Toast.LENGTH_SHORT).show();
			}
//			
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			dialog.setProgress(values[0]);
		}
		@Override
		protected String doInBackground(List<Article>... arg0) {
			// TODO Auto-generated method stub
			String result ="";
			int progress_state = 0;
			for (Article article : articles) {
				publishProgress((progress_state*100)/maxProgressLenght);
				progress_state++;
				String code = article.getName();
				String state = article.getContent();
				String arcode = article.getArcode();
					// TODO Auto-generated method stub
					SoapObject rpc = new SoapObject("http://tempuri.org/",
							"insertCode");
					rpc.addProperty("code", code);
					rpc.addProperty("state", state);
					rpc.addProperty("user", user);
					rpc.addProperty("arcode", arcode);
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
						transport.call("http://tempuri.org/insertCode",
								envelope);
					} catch (Exception e) {
//						e.printStackTrace();
						return "false";	
					}
					SoapObject object = (SoapObject) envelope.bodyIn;
					result = object.getProperty(0).toString();
					SQLiteHelper sqLiteHelper = new SQLiteHelper(
							getApplicationContext());
					ArticleDAO articleDao = new ArticleDAO();
					article.setName(code);
					article.setContent("3");
					article.setDate(new Date());
					SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
					articleDao.UpdateArticle(db, article);
					db.close();					
			}
			return result;
		}
		
	}
}

package Code.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class UploadMenuActivity extends BaseActivity implements OnClickListener{
	
	private Button in_uploadButton,out_uploadButton,back_Button; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_menu);
		this.setTitle("ÌõÂëÉÏ´«");
		initView();
		initEvent();
	}
	private void initEvent() {
		// TODO Auto-generated method stub
		in_uploadButton.setOnClickListener(this);
		out_uploadButton.setOnClickListener(this);
		back_Button.setOnClickListener(this);
	}
	private void initView() {
		// TODO Auto-generated method stub
		in_uploadButton = (Button) findViewById(R.id.in_upload);
		out_uploadButton = (Button) findViewById(R.id.out_upload);
		back_Button = (Button) findViewById(R.id.out_back);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (arg0.getId()) {
		case R.id.in_upload:
			intent = new Intent(UploadMenuActivity.this, UploadActivity.class);
			startActivity(intent);
			finish();
			break;

		case R.id.out_upload:
			intent = new Intent(UploadMenuActivity.this, OutUploadActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.out_back:
			intent = new Intent(UploadMenuActivity.this, menuActivity.class);
			startActivity(intent);
			finish();
			break;
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

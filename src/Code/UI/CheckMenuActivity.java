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

public class CheckMenuActivity extends BaseActivity implements OnClickListener{

	private Button in_checkButton,out_checkButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_menu);
		initView();
		initEvent();
	}
	private void initEvent() {
		// TODO Auto-generated method stub
		in_checkButton.setOnClickListener(this);
		out_checkButton.setOnClickListener(this);
	}
	private void initView() {
		// TODO Auto-generated method stub
		in_checkButton = (Button) findViewById(R.id.in_check);
		out_checkButton = (Button) findViewById(R.id.out_check);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (arg0.getId()) {
		case R.id.in_check:
			intent = new Intent(CheckMenuActivity.this, InCheckActivity.class);
			startActivity(intent);
			finish();
			break;

		case R.id.out_check:
			intent = new Intent(CheckMenuActivity.this, OutCheckActivity.class);
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

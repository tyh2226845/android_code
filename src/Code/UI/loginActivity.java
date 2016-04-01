package Code.UI;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class loginActivity extends BaseActivity implements OnClickListener{

	private EditText loginEditText,passwordEditText;
	private Button loginButton,resetButton; 
	private String username = "54321";
	private String password = "54321";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);  
		this.setTitle("登录");
		initView();
		initEvent();
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		loginButton.setOnClickListener(this);
		resetButton.setOnClickListener(this);
	}

	private void initView() {
		// TODO Auto-generated method stub
		loginButton = (Button) findViewById(R.id.login_bth);
		resetButton = (Button) findViewById(R.id.reset_bth);
		loginEditText = (EditText) findViewById(R.id.login_et);
		passwordEditText = (EditText) findViewById(R.id.password_et);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.login_bth:
			if (!isCheck()) {
				Toast.makeText(loginActivity.this, "用户名或密码错误，请重新输入", 1000).show();
				resetEdit();
				break;
			}
			Toast.makeText(loginActivity.this, "登录成功！", 1000).show();
			Intent intent = new Intent(loginActivity.this, SettingActivity.class);
			startActivity(intent);
			finish();
			break;

    case R.id.reset_bth:
    	resetEdit();
			break;
		}
	}
	public boolean isCheck() {
		if ((loginEditText.getText()+"").equals(username)&&(passwordEditText.getText()+"").equals(password)) {
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
	
	public void resetEdit() {
		loginEditText.setText("");
    	passwordEditText.setText("");
	}
	

}

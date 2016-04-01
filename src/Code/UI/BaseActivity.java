package Code.UI;

import java.util.Date;

import Code.Entites.*;
import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import Code.Util.*;
public class BaseActivity extends Activity{

	 public String urlData= new Data().getUrlData();
	 PowerManager powerManager = null;  
     WakeLock wakeLock = null;  

     @Override  
     protected void onCreate(Bundle savedInstanceState) {  
         super.onCreate(savedInstanceState);  
         powerManager = (PowerManager)this.getSystemService(this.POWER_SERVICE);  
         wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");  
         ActivityCollector.addActivity(this);
    }  

    @Override  
    protected void onResume() {  
        super.onResume();  
        wakeLock.acquire();  
    }  

    @Override  
    protected void onPause() {  
        super.onPause();  
        wakeLock.release();  
    }  
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	ActivityCollector.removeActivity(this);
    }
}

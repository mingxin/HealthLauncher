package com.winon.healthex.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.lurencun.android.sdk.res.AssetsReader;
import com.lurencun.android.sdk.res.ResJSONHandler;
import com.winon.healthex.AppConfig;
import com.winon.healthex.R;
import com.winon.healthex.entity.TabEntity;
import com.winon.healthex.service.MessageService;

public class HealthLauncherActivity extends ActivityGroup {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);    
        setContentView(R.layout.launcher);
        TabHost tabHost = (TabHost)findViewById(R.id.taghost);
        tabHost.setup(getLocalActivityManager());
        ConfigReader configReader = new ConfigReader(this);
        
        tabHost.getTabWidget().setBackgroundResource(R.drawable.tab_background);
        //从配置文件中添加Tab到TabHost中
        for(TabEntity te : configReader.load(R.array.tabitems)){
        	ImageView tabTitle = new ImageView(this);
        	tabTitle.setBackgroundResource(R.drawable.tab_item_background);
        	tabTitle.setImageBitmap(AssetsReader.readBitmap(this, te.titleImage));
        	TabSpec tab = tabHost.newTabSpec("TAG("+te.index+")");
        	tab.setIndicator(tabTitle);
        	
        	//让TabPageActivity根据Value的值加载不同的页面
        	
        	Intent tabIntent = new Intent(this,TabPageActivity.class);
        	tabIntent.putExtra(AppConfig.TABHOST_KEY, te.index);
        	tab.setContent(tabIntent);
        	tabHost.addTab(tab);
        }
        Intent service = new Intent(this,MessageService.class);
        startService(service);
    }
    
    private class ConfigReader extends ResJSONHandler<TabEntity>{

		public ConfigReader(Context context) {
			super(context);
		}

		@Override
		protected TabEntity convert(JSONObject json) throws JSONException {
			TabEntity te = new TabEntity();
			te.titleImage = json.getString("title");
			te.index = json.getInt("index");
			return te;
		}
    	
    }
    
  //屏蔽返回键
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
//    public boolean onKeyDown(int keyCode,KeyEvent event){
//    switch(keyCode){
//    	case KeyEvent.KEYCODE_HOME:return true;
//    	case KeyEvent.KEYCODE_BACK:return true;
//    	case KeyEvent.KEYCODE_CALL:return true;
//    	case KeyEvent.KEYCODE_SYM: return true;
//    	case KeyEvent.KEYCODE_VOLUME_DOWN: return true;
//    	case KeyEvent.KEYCODE_VOLUME_UP: return true;
//    	case KeyEvent.KEYCODE_STAR: return true;
//    	}
//    	return super.onKeyDown(keyCode, event);
//    }
}
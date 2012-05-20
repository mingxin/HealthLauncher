package com.winon.healthex.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lurencun.android.sdk.res.AssetsReader;
import com.lurencun.android.sdk.res.ResJSONHandler;
import com.lurencun.android.sdk.util.ActivitySwitcher;
import com.lurencun.android.support.v2.widget.ViewPager;
import com.lurencun.android.support.v2.widget.ViewPagerAdapter;
import com.lurencun.android.support.widget.CommonAdapter;
import com.winon.healthex.AppConfig;
import com.winon.healthex.R;
import com.winon.healthex.entity.AppEntity;
import com.winon.healthex.entity.GridEntity;

public class TabPageActivity extends Activity {

	private static final int COUNT_PRE_PAGE = 8;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabpage);
		int resId = getIntent().getIntExtra(AppConfig.TABHOST_KEY, 0);
		ViewPager pager = (ViewPager)findViewById(R.id.pager);
		PageAdapter adapter = new PageAdapter(this);
		List<AppEntity> dataCache = new PageConfigReader(this).load(AppConfig.PAGE_RES[resId]);
		List<GridEntity> data = new ArrayList<GridEntity>();
		int pageCount = dataCache.size() / COUNT_PRE_PAGE + (dataCache.size()%COUNT_PRE_PAGE > 0 ? 1 : 0);
		for(int i=0;i<pageCount;i++){
			GridEntity itemData = new GridEntity();
			int startIndex = i * COUNT_PRE_PAGE;
			int endIndex = (i+1) * COUNT_PRE_PAGE;
			if(endIndex > dataCache.size()) endIndex = dataCache.size();
			itemData.gridData = dataCache.subList(startIndex, endIndex);
			data.add(itemData);
		}
		adapter.setData(data);
		pager.setAdapter(adapter);
		
	}
	
	private class PageConfigReader extends ResJSONHandler<AppEntity>{

		public PageConfigReader(Context context) {
			super(context);
		}

		@Override
		protected AppEntity convert(JSONObject json) throws JSONException {
			AppEntity appEntity = new AppEntity();
			appEntity.classpath = json.getString("classpath");
			appEntity.ackage = json.getString("packagepath");
			appEntity.actionType = json.getInt("actionType");
			appEntity.image = AssetsReader.readBitmap(mContext, json.getString("image"));
			return appEntity;
		}
		
	}
	
	private class PageAdapter extends ViewPagerAdapter<GridEntity>{

		public PageAdapter(Context context) {
			super(context);
		}

		@Override
		public View createView(GridEntity data, int position) {
			RelativeLayout view = (RelativeLayout) mInflater.inflate(R.layout.pagegrid, null);
			GridView gird = (GridView)view.findViewById(R.id.app_grid);
			GridAdapter adapter = new GridAdapter(mContext);
			adapter.updateDataCache(data.gridData);
			gird.setAdapter(adapter);
			return view;
		}
		
		private class GridAdapter extends CommonAdapter<AppEntity>{

			public GridAdapter(Context context) {
				super(context);
			}

			@Override
			protected View createView(LayoutInflater inflater, AppEntity data,
					int position, View convertView, ViewGroup parent) {
				ImageView item = new ImageView(mContext);
				item.setImageBitmap(data.image);
				item.setBackgroundResource(R.drawable.button_item_background);
				
				final AppEntity dataex = data;
				item.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						switch(dataex.actionType){
						case 0:
							//打开 应用
							ComponentName comp = new ComponentName(dataex.ackage, dataex.classpath);
							Intent intent = new Intent();
							intent.setComponent(comp);
							TabPageActivity.this.startActivity(intent);
							Log.d("D", "打开 应用");
							break;
						case 1:
							//打开 Activity
							try {
								ActivitySwitcher.switchTo(TabPageActivity.this, Class.forName(dataex.classpath));
							} catch (ClassNotFoundException e) {
								Log.e("E","配置文件中的class所指向的类不存在！class = "+dataex.classpath);
								e.printStackTrace();
							}
							break;
						case 2:
							//打开广告
							break;
						}
					}
				});
				return item;
			}
			
		}

		@Override
		protected void finishedUpdate(ViewPager container) { }
		@Override
		protected void startingUpdate(ViewPager container) { }
	}
	
	
}

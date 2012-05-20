package com.winon.healthex.service;

import java.util.ArrayList;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.winon.healthex.R;
import com.winon.healthex.StompClient;
import com.winon.healthex.ui.MessageActivity;

public class MessageService extends Service{

	//
	private List<String> mMessageCache = new ArrayList<String>();
	
	private StompClient mStompClient;
	
	private NotificationManager mNotificationManager;
	
	private boolean hasShowed = false;

	@Override
	public void onCreate() {
		super.onCreate();
		mStompClient=new StompClient(new MsgReceHandler());
		mStompClient.initConnection();
		mNotificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	
	private class MsgReceHandler extends Handler
	{
		public void handleMessage(Message msg){
	           String message=(String)msg.obj;
	           mMessageCache.add(message);
	           if(!mMessageCache.isEmpty() && !hasShowed)
	           {
	        	   String tip = "您有"+mMessageCache.size()+"条新消息，请查收！";
	        	   //int id = mMessageCache.size();
	        	   Notification note = new Notification(R.drawable.message_icon,tip,System.currentTimeMillis());
	        	   Intent mIntent = new Intent();
	        	   String title = "消息（"+mMessageCache.size()+"）";
	        	   String content = message;
	        	   mIntent.putExtra("title", title);
	        	   mIntent.putExtra("content", content);
	        	   
	        	   mIntent.setClass(MessageService.this,MessageActivity.class);
	        	   mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	
	        	   PendingIntent mContentIntent =PendingIntent.getActivity(MessageService.this,0, mIntent, 0);
//						
	        	   note.setLatestEventInfo(MessageService.this, title,content , mContentIntent);
	        	   note.defaults = Notification.DEFAULT_SOUND;
	        	   note.defaults = Notification.DEFAULT_VIBRATE;
	        	   //note.flags = Notification.FLAG_ONGOING_EVENT;
	        	   mNotificationManager.notify(1, note);
	        	   hasShowed = true;
	           }
	        }
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

}

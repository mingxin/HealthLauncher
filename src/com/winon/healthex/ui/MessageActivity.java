package com.winon.healthex.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.winon.healthex.R;



public class MessageActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messagedisplay);
		
		TextView titleView = (TextView)findViewById(R.id.title_b);
		TextView contentView = (TextView)findViewById(R.id.content_b);
		
		Intent valIntent = getIntent();
        String title = valIntent.getStringExtra("title");
        String val = valIntent.getStringExtra("content");
        Log.e("E",title==null?"NULL":title);
        titleView.setText(title);
        contentView.setText(val);
	}


	
}

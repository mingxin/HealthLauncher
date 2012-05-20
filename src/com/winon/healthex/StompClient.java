package com.winon.healthex;

import pk.aamir.stompj.*;
import android.os.Handler;
import android.os.Message;

public class StompClient {
	private Handler handler;
	
	public StompClient(Handler handler)
	{
		this.handler=handler;
	}
	
	public void initConnection(){
		Connection con = new Connection("192.168.1.110", 61613, "system", "manager");
		try {
		    con.connect();
		    con.subscribe("/topic/CHAT.DEMO",false);
		    con.setErrorHandler(new ErrorHandler() {
		        public void onError(ErrorMessage errorMsg) {
		            System.out.println(errorMsg.getMessage());
		            System.out.println(errorMsg.getContentAsString());
		        }
		    });
			con.addMessageHandler("/topic/CHAT.DEMO", new MessageHandler() {
			    public void onMessage(pk.aamir.stompj.Message msg) {
			        System.out.println(msg.getContentAsString());
			        String str_temp=msg.getContentAsString();
			        Message message = Message.obtain();
			        message.obj=str_temp;
			        //通过Handler发布传送消息，handler
			        handler.sendMessage(message);

			    	//stompClientActivity.addData(message.getContentAsString());
			    }
			});			 
		}
		catch(Exception e)
		{
			
		}
	
	}
}

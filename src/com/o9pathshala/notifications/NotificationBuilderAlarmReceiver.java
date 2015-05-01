package com.o9pathshala.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class NotificationBuilderAlarmReceiver extends BroadcastReceiver {
	 @Override
	    public void onReceive(Context context, Intent intent) {
	        generateNotification( context,  intent);
	    }
	    public void generateNotification(Context context, Intent intent)
	    {	
	    	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	    	if(null != sharedPreferences.getString("session", null)){
	    		GetNotificationAsynTask asynDemo = new GetNotificationAsynTask(context);
	    		asynDemo.execute();
	    	}
	    }

	}
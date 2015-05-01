package com.o9pathshala.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

	public class AlarmService{
	    private Context context;
	    private PendingIntent mAlarmSender;
	    
	    public AlarmService(Context context) {
	        this.context = context;
	        mAlarmSender = PendingIntent.getBroadcast(context, 0, new Intent(context, NotificationBuilderAlarmReceiver.class), 0);
	    }

	    public void startAlarm(){
	        //Set the alarm to 10 seconds from now
	        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	       // am.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstTime, 5, mAlarmSender);
	      //  am.set(AlarmManager.RTC_WAKEUP, firstTime, mAlarmSender);
	        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 5 * 60 * 1000, 5 * 60 * 1000, mAlarmSender);
	        
	    }
	}


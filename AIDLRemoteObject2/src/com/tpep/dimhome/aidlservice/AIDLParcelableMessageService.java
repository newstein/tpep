package com.tpep.dimhome.aidlservice;

import java.text.SimpleDateFormat;

//import com.tpep.dimhome.R;



//import com.tpep.dimhome.R.string;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AIDLParcelableMessageService extends Service {
	private static final String APPSOLUT_INTENT_ACTION_BIND_MESSAGE_SERVICE = "appsolut.intent.action.bindParcelableMessageService2";
//	private final static String LOG_TAG = AIDLParcelableMessageService.class.getCanonicalName();
	private final static String LOG_TAG = "AIDLParcelableMessageService";


	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(LOG_TAG,"The AIDLParcelableMessageService was created.");
        
	}

	@Override
	public void onDestroy() {
		Log.d(LOG_TAG,"The AIDLParcelableMessageService was destroyed.");
		super.onDestroy();
	}


	@Override
	public IBinder onBind(Intent intent) {
		if(APPSOLUT_INTENT_ACTION_BIND_MESSAGE_SERVICE.equals(intent.getAction())) {
			Log.d(LOG_TAG,"The AIDLParcelableMessageService was binded.");
			return new TimeParcelableMessageService(this);
		}
		return null;
	}

	String getStringForRemoteService() {
//	    return null;
	    Log.d(LOG_TAG,"getStringForRemoteService");
		return getString(R.string.time_message) + (new SimpleDateFormat(" hh:mm:ss").format(System.currentTimeMillis()));
//	   return getString(R.string.about_notices) + (new SimpleDateFormat(" hh:mm:ss").format(System.currentTimeMillis()));
	}

}

package com.tpep.dimhome.aidlservice;

//import com.tpep.dimhome.Launcher;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class TimeParcelableMessageService extends IRemoteParcelableMessageService.Stub {

    private static final String TAG = "TimeParcelableMessageService";
    private final static int MAX_FONT_SIZE_INCREASE = 40;
	private final static int MIN_FONT_SIZE = 10;
	
	private final AIDLParcelableMessageService service;
	


	public TimeParcelableMessageService(AIDLParcelableMessageService service) {
		this.service = service;
	}
	
	@Override
	public MyParcelableMessage getMessage() throws RemoteException {
		String message = service.getStringForRemoteService();
		int fontSize = (int)(Math.random()*MAX_FONT_SIZE_INCREASE) + MIN_FONT_SIZE;
		int textColor = (int)(Math.random()*Integer.MAX_VALUE);
		
		int randomTextStyleSelector = (int)(Math.random()*3);
		Log.v(TAG, "randomTextStyleSelector:=" + randomTextStyleSelector);
		int textStyle;
		switch (randomTextStyleSelector) {
		case 0:
			textStyle = Typeface.BOLD;
			
			// Start Login Activity Service
		  //    startActivity(new Intent(this, AuthenticatorActivity.class));
/*			    Context mContext=service.getApplicationContext();
		        Intent intent = new Intent(mContext, AuthenticatorActivity.class);
		        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        service.startActivity(intent);	*/	
					
			break;
		case 1:
			textStyle = Typeface.BOLD_ITALIC;
			break;
		case 2:
			textStyle = Typeface.ITALIC;
			break;
		default:
			textStyle = Typeface.NORMAL;
			break;
		}
		
		return new MyParcelableMessage(message, fontSize, textColor, Typeface.defaultFromStyle(textStyle));
	}

 

	
	
	
	
}

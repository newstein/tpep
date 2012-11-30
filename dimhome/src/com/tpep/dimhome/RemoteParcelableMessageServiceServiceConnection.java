package com.tpep.dimhome;


import com.tpep.dimhome.aidlservice.IRemoteParcelableMessageService;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class RemoteParcelableMessageServiceServiceConnection implements ServiceConnection {


	private static final String AIDL_MESSAGE_SERVICE_CLASS = ".AIDLParcelableMessageService";
	private static final String AIDL_MESSAGE_SERVICE_PACKAGE = "com.tpep.dimhome.aidlservice";
//   private static final String AIDL_MESSAGE_SERVICE_PACKAGE = "nl.sogeti.android.gpstracker";	
	private static final String APPSOLUT_INTENT_ACTION_BIND_MESSAGE_SERVICE = "appsolut.intent.action.bindParcelableMessageService2";
//	private final DisplayRemoteParcelableMessage parent;
   private final Launcher parent;	
	//GoogleLoggerMap
	private IRemoteParcelableMessageService service;

	public RemoteParcelableMessageServiceServiceConnection(
	        Launcher parent) {
		this.parent = parent;
	}

	private final static String LOG_TAG = "RemoteParcelableMessageServiceServiceConnection";
//	private final static String LOG_TAG = RemoteParcelableMessageServiceServiceConnection.class.getCanonicalName();
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		Log.d(LOG_TAG, "The service is now connected!");
		this.service = IRemoteParcelableMessageService.Stub.asInterface(service);
		Log.d(LOG_TAG, "Querying the message...");
		try {
			/*
			 * This call is required because the connect is an asynchronous call
			 * so the activity has to be notified that the connection is now 
			 * established and that the message was queried.
			 */
			parent.theMessageWasReceivedAsynchronously(this.service.getMessage());
			
		} catch (RemoteException e) {
			Log.e(LOG_TAG, "An error occured during the call.");
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		Log.d(LOG_TAG, "The connection to the service got disconnected unexpectedly!");
		service = null;
	}

	/**
	 * Method to disconnect the Service.
	 * This method is required because the onServiceDisconnected
	 * is only called when the connection got closed unexpectedly
	 * and not if the user requests to disconnect the service.
	 */
	public void safelyDisconnectTheService() {
		if(service != null) {
			service = null;
			parent.unbindService(this);
			Log.d(LOG_TAG, "The connection to the service was closed.!");
		}
	}

	/**
	 * Method to connect the Service.
	 */
	public void safelyConnectTheService() {
		if(service == null) {
			Intent bindIntent = new Intent(APPSOLUT_INTENT_ACTION_BIND_MESSAGE_SERVICE);
			bindIntent.setClassName(AIDL_MESSAGE_SERVICE_PACKAGE, AIDL_MESSAGE_SERVICE_PACKAGE + AIDL_MESSAGE_SERVICE_CLASS);
			parent.bindService(bindIntent, this, Context.BIND_AUTO_CREATE);
			Log.d(LOG_TAG, "The Service will be connected soon (asynchronus call)!");
		}
	}

	/**
	 * Method to safely query the message from the remote service
	 */
	public void safelyQueryMessage() {
		Log.d(LOG_TAG, "Trying to query the message from the Service.");
		if(service == null) {	// if the service is null the connection is not established.
			Log.d(LOG_TAG, "The service was not connected -> connecting.");
			safelyConnectTheService();
		} else {
			Log.d(LOG_TAG, "The Service is already connected -> querying the message.");
			try {
				parent.theMessageWasReceivedAsynchronously(service.getMessage());
			} catch (RemoteException e) {
				Log.e(LOG_TAG, "An error occured during the call.");
			}
		}
	}



}

/* The package where the aidl file is located */
package com.tpep.dimhome.aidlservice;

/* Import our Parcelable message */
import com.tpep.dimhome.aidlservice.MyParcelableMessage;

/* The name of the remote service */
interface IRemoteParcelableMessageService {

	/* A simple Method which will return a message
	 * The message object implements the Parcelable interface 
	 */
	MyParcelableMessage getMessage();


}
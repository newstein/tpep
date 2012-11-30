/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\AndroDev\\workspace3\\AIDLRemoteObject2\\src\\com\\tpep\\dimhome\\aidlservice\\IRemoteParcelableMessageService.aidl
 */
package com.tpep.dimhome.aidlservice;
/* The name of the remote service */
public interface IRemoteParcelableMessageService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.tpep.dimhome.aidlservice.IRemoteParcelableMessageService
{
private static final java.lang.String DESCRIPTOR = "com.tpep.dimhome.aidlservice.IRemoteParcelableMessageService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.tpep.dimhome.aidlservice.IRemoteParcelableMessageService interface,
 * generating a proxy if needed.
 */
public static com.tpep.dimhome.aidlservice.IRemoteParcelableMessageService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.tpep.dimhome.aidlservice.IRemoteParcelableMessageService))) {
return ((com.tpep.dimhome.aidlservice.IRemoteParcelableMessageService)iin);
}
return new com.tpep.dimhome.aidlservice.IRemoteParcelableMessageService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getMessage:
{
data.enforceInterface(DESCRIPTOR);
com.tpep.dimhome.aidlservice.MyParcelableMessage _result = this.getMessage();
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.tpep.dimhome.aidlservice.IRemoteParcelableMessageService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
/* A simple Method which will return a message
	 * The message object implements the Parcelable interface 
	 */
@Override public com.tpep.dimhome.aidlservice.MyParcelableMessage getMessage() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.tpep.dimhome.aidlservice.MyParcelableMessage _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getMessage, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = com.tpep.dimhome.aidlservice.MyParcelableMessage.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
/* A simple Method which will return a message
	 * The message object implements the Parcelable interface 
	 */
public com.tpep.dimhome.aidlservice.MyParcelableMessage getMessage() throws android.os.RemoteException;
}

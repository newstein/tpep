package com.tpep.dimhome.aidlservice;

import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.TextView;

public class MyParcelableMessage implements Parcelable {
	
	/**
	 * The text of the message.
	 */
	private final String message;
	
	/**
	 * The font size in which this message will be displayed.
	 */
	private final int textSize;
	
	/**
	 * The color of the message text.
	 */
	private final int textColor;
	
	/**
	 * The typeface of the message text.
	 */
	private final Typeface textTypeface;
	
	
	/**
	 * Constructor which initializes a Parcelable message object.
	 * @param message The message text
	 * @param textSize The size of the font
	 * @param textColor The color of the text
	 * @param textTypeface The typeface of the text
	 */
	public MyParcelableMessage(String message, int textSize,
			int textColor, Typeface textTypeface) {
		this.message = message;
		this.textSize = textSize;
		this.textColor = textColor;
		this.textTypeface = textTypeface;
	}
	
	/**
	 * Applys all message attributes to a given TextView.
	 * @param textView The TextView where the settings will be applied. 
	 */
	public void applyMessageToTextView(TextView textView) {
		textView.setText(message);
		textView.setTextSize(textSize);
		textView.setTextColor(textColor);
		textView.setTypeface(textTypeface);
	}

	/**
	 * Factory for creating instances of the Parcelable class.
	 */
	public static final Parcelable.Creator<MyParcelableMessage> CREATOR = new Parcelable.Creator<MyParcelableMessage>() {
		
		/**
		 * This method will be called to instantiate a MyParcelableMessage
		 * when a Parcel is received.
		 * All data fields which where written during the writeToParcel
		 * method should be read in the correct sequence during this method.
		 */
		@Override
		public MyParcelableMessage createFromParcel(Parcel in) {
			String message = in.readString();
			int fontSize = in.readInt();
			int textColor = in.readInt();
			Typeface typeface = Typeface.defaultFromStyle(in.readInt());
			return new MyParcelableMessage(message, fontSize, textColor, typeface);
		}

		/**
		 * Creates an array of our Parcelable object.
		 */
		@Override
		public MyParcelableMessage[] newArray(int size) {
			return new MyParcelableMessage[size];
		}
	};


	@Override
	/**
	 * Method which will give additional hints how to process
	 * the parcel. For example there could be multiple
	 * implementations of an Interface which extends the Parcelable
	 * Interface. When such a parcel is received you can use
	 * this to determine which object you need to instantiate.
	 */
	public int describeContents() {
		return 0;			// nothing special about our content
	}

	@Override
	/**
	 * Method which will be called when this object should be
	 * marshalled to a Parcelable object.
	 * Add all required data fields to the parcel in this
	 * method.
	 */
	public void writeToParcel(Parcel outParcel, int flags) {
		outParcel.writeString(message);
		outParcel.writeInt(textSize);
		outParcel.writeInt(textColor);
		outParcel.writeInt(textTypeface.getStyle());
	}

}

/*------------------------------------------------------------------------------
 **     Ident: Delivery Center Java
 **    Author: rene
 ** Copyright: (c) Feb 26, 2012 Sogeti Nederland B.V. All Rights Reserved.
 **------------------------------------------------------------------------------
 ** Sogeti Nederland B.V.            |  No part of this file may be reproduced  
 ** Distributed Software Engineering |  or transmitted in any form or by any        
 ** Lange Dreef 17                   |  means, electronic or mechanical, for the      
 ** 4131 NJ Vianen                   |  purpose, without the express written    
 ** The Netherlands                  |  permission of the copyright holder.
 *------------------------------------------------------------------------------
 */
package nl.sogeti.android.gpstracker.viewer.map;

import nl.sogeti.android.gpstracker.R;
import nl.sogeti.android.gpstracker.util.Constants;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * ????
 *
 * @version $Id:$
 * @author rene (c) Feb 26, 2012, Sogeti B.V.
 */
public class CommonLoggerMap extends Activity
{
   private static final String TAG = "OGT.CommonLoggerMap";

   
   
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      
      //hide status bar 
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
          WindowManager.LayoutParams.FLAG_FULLSCREEN);
      
 
      
      
      Intent myIntent = getIntent();
      Intent realIntent;

      Class<?> mapClass = GoogleLoggerMap.class;
      int provider = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.MAPPROVIDER, "" + Constants.GOOGLE)).intValue();
      switch (provider)
      {
         case Constants.GOOGLE:
            mapClass = GoogleLoggerMap.class;
            break;
         case Constants.OSM:
            mapClass = OsmLoggerMap.class;
            break;
         case Constants.MAPQUEST:
            mapClass = MapQuestLoggerMap.class;
            break;
         default:
            mapClass = GoogleLoggerMap.class;
            Log.e(TAG, "Fault in value " + provider + " as MapProvider, defaulting to Google Maps.");
            break;
      }
      if( myIntent != null )
      {
         realIntent = new Intent(myIntent.getAction(), myIntent.getData(), this, mapClass);
         realIntent.putExtras(myIntent);
      }
      else
      {
         realIntent = new Intent(this, mapClass);
         realIntent.putExtras(myIntent);
      }
      startActivity(realIntent);
      finish();
   }

   
   
 
   @Override
   public void onBackPressed()
   {
      // TODO Auto-generated method stub
      super.onBackPressed();
   }
   
 
     
   
  
}

/*------------------------------------------------------------------------------
 **     Ident: Sogeti Smart Mobile Solutions
 **    Author: rene
 ** Copyright: (c) Apr 24, 2011 Sogeti Nederland B.V. All Rights Reserved.
 **------------------------------------------------------------------------------
 ** Sogeti Nederland B.V.            |  No part of this file may be reproduced  
 ** Distributed Software Engineering |  or transmitted in any form or by any        
 ** Lange Dreef 17                   |  means, electronic or mechanical, for the      
 ** 4131 NJ Vianen                   |  purpose, without the express written    
 ** The Netherlands                  |  permission of the copyright holder.
 *------------------------------------------------------------------------------
 *
 *   This file is part of OpenGPSTracker.
 *
 *   OpenGPSTracker is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   OpenGPSTracker is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with OpenGPSTracker.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package nl.sogeti.android.gpstracker.viewer.map;

import java.util.List;

import nl.sogeti.android.gpstracker.R;
import nl.sogeti.android.gpstracker.util.Constants;
import nl.sogeti.android.gpstracker.util.SlidingIndicatorView;
import nl.sogeti.android.gpstracker.viewer.map.overlay.FixedMyLocationOverlay;
import nl.sogeti.android.gpstracker.viewer.map.overlay.OverlayProvider;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.SensorManager;
import android.media.audiofx.BassBoost.Settings;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.ActionProvider;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;


import com.tpep.dimhome.aidlservice.MyParcelableMessage;
/**
 * Main activity showing a track and allowing logging control
 * 
 * @version $Id$
 * @author rene (c) Jan 18, 2009, Sogeti B.V.
 */
public class GoogleLoggerMap extends MapActivity implements LoggerMap,View.OnClickListener
{
   private static final String TAG = "GoogleLoggerMap";
   LoggerMapHelper mHelper;
   private MapView mMapView;
   private TextView[] mSpeedtexts;
   private TextView mLastGPSSpeedView;
   private TextView mLastGPSAltitudeView;
   private TextView mDistanceView;
   private FixedMyLocationOverlay mMylocation;
   
   ///sean_20121122
   private ImageView hotseat1;
   private ImageView hotseat2;
   private TextView hotseat3;

   private static final Intent sSettingsIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);  
   
//   private static final Intent sMessageIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);     
   
   
 //  private final Context mContext;  
   private SensorManager mSensorManager;
   private PowerManager mPowerManager;   
   private WakeLock mWakeLock;
 
   private PackageManager pm;
   private Intent mIntent;
   
   
   private RemoteParcelableMessageServiceServiceConnection remoteServiceConnection;
   private TextView messageTextView;
   
   
   /**
    * Called when the activity is first created.
    */
   @Override
   protected void onCreate(Bundle load)
   {
      super.onCreate(load);
      
      //hide status bar 
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
          WindowManager.LayoutParams.FLAG_FULLSCREEN);
//      mContext=load;
      
      
      setContentView(R.layout.map_google);
      
      mHelper = new LoggerMapHelper(this);
      mMapView = (MapView) findViewById(R.id.myMapView);
      mMylocation = new FixedMyLocationOverlay(this, mMapView);
      mMapView.setBuiltInZoomControls(true);
      TextView[] speeds = { (TextView) findViewById(R.id.speedview05), (TextView) findViewById(R.id.speedview04), (TextView) findViewById(R.id.speedview03),
            (TextView) findViewById(R.id.speedview02), (TextView) findViewById(R.id.speedview01), (TextView) findViewById(R.id.speedview00) };
      mSpeedtexts = speeds;
      mLastGPSSpeedView = (TextView) findViewById(R.id.currentSpeed);
      mLastGPSAltitudeView = (TextView) findViewById(R.id.currentAltitude);
      mDistanceView = (TextView) findViewById(R.id.currentDistance);
      
      ///sean_20121122
      hotseat1 = (ImageView) findViewById(R.id.hotseat1);
      hotseat2 = (ImageView) findViewById(R.id.hotseat2);
      hotseat3 = (TextView) findViewById(R.id.hotseat3);
      
      hotseat1.setVisibility(ImageView.VISIBLE);
      hotseat2.setVisibility(ImageView.VISIBLE);
      hotseat3.setVisibility(TextView.VISIBLE);
      
      hotseat1.setOnClickListener(this);
      hotseat2.setOnClickListener(this);     
      hotseat3.setOnClickListener(this);      
 
      //mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

      // Get an instance of the PowerManager
      mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
      mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass().getName());

      pm = getPackageManager();
      
      remoteServiceConnection = new RemoteParcelableMessageServiceServiceConnection(this);
 //     remoteServiceConnection.safelyConnectTheService();
  
      hotseat3.setOnClickListener(new OnClickListener() {
         
         @Override
         public void onClick(View v) {
            remoteServiceConnection.safelyQueryMessage();
         }
      });
      
      
      
      mHelper.onCreate(load);
   }
   
   @Override
   protected void onResume()
   {
      super.onResume();
      mHelper.onResume();
      /*
       * when the activity is resumed, we acquire a wake-lock so that the
       * screen stays on, since the user will likely not be fiddling with the
       * screen or buttons.
       */
      mWakeLock.acquire();
   }
   
   @Override
   protected void onPause()
   {
      mHelper.onPause();
      // and release our wake-lock
      mWakeLock.release();
      super.onPause();
   }
   
   @Override
   protected void onDestroy()
   {
      mHelper.onDestroy();
      super.onDestroy();
   }
   
   @Override
   public void onNewIntent(Intent newIntent)
   {
      mHelper.onNewIntent(newIntent);
   }

   @Override
   protected void onRestoreInstanceState(Bundle load)
   {
      if (load != null)
      {
         super.onRestoreInstanceState(load);
      }
      mHelper.onRestoreInstanceState(load);
   }
   
   @Override
   protected void onSaveInstanceState(Bundle save)
   {
      super.onSaveInstanceState(save);
      mHelper.onSaveInstanceState(save);
   }
   
   @Override
   public boolean onCreateOptionsMenu(Menu menu)
   {
      boolean result = super.onCreateOptionsMenu(menu);
      
      //sean_121123
//     mHelper.onCreateOptionsMenu(menu);
//      getMenuInflater().inflate(R.menu.action_bar_settings_action_provider, menu); 
 //     return true;
      return result;
   }
   
   @Override
   public boolean onPrepareOptionsMenu(Menu menu)
   {
      
    //sean_121123
 //     mHelper.onPrepareOptionsMenu(menu);
      return super.onPrepareOptionsMenu(menu);
   }
   
   @Override
   public boolean onOptionsItemSelected(MenuItem item)
   {
      //sean_121123
      
      return false;
/*      boolean handled = mHelper.onOptionsItemSelected(item);
      if( !handled )
      {
         handled = super.onOptionsItemSelected(item);
      }
      return handled;*/

 
      
   }
   
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent intent)
   {
      super.onActivityResult(requestCode, resultCode, intent);
      mHelper.onActivityResult(requestCode, resultCode, intent);
   }
   
   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event)
   {
      boolean propagate = true;
      switch (keyCode)
      {
         case KeyEvent.KEYCODE_S:
            setSatelliteOverlay(!this.mMapView.isSatellite());
            propagate = false;
            break;
         case KeyEvent.KEYCODE_A:
            setTrafficOverlay(!this.mMapView.isTraffic());
            propagate = false;
            break;
         default:
            propagate = mHelper.onKeyDown(keyCode, event);
            if( propagate )
            {
               propagate = super.onKeyDown(keyCode, event);
            }
            break;
      }
      return propagate;
   }
   
   @Override
   protected Dialog onCreateDialog(int id)
   {
      Dialog dialog = mHelper.onCreateDialog(id);
      if( dialog == null )
      {
         dialog = super.onCreateDialog(id);
      }
      return dialog;
   }
   
   @Override
   protected void onPrepareDialog(int id, Dialog dialog)
   {
      mHelper.onPrepareDialog(id, dialog);
      super.onPrepareDialog(id, dialog);
   }
   
   /******************************/
   /** Own methods              **/ 
   /******************************/
   
   private void setTrafficOverlay(boolean b)
   {
      SharedPreferences sharedPreferences = mHelper.getPreferences();
      Editor editor = sharedPreferences.edit();
      editor.putBoolean(Constants.TRAFFIC, b);
      editor.commit();
   }

   private void setSatelliteOverlay(boolean b)
   {
      SharedPreferences sharedPreferences = mHelper.getPreferences();
      Editor editor = sharedPreferences.edit();
      editor.putBoolean(Constants.SATELLITE, b);
      editor.commit();
   }
   
   @Override
   protected boolean isRouteDisplayed()
   {
      return true;
   }
   
   @Override
   protected boolean isLocationDisplayed()
   {
      SharedPreferences sharedPreferences = mHelper.getPreferences();
      return sharedPreferences.getBoolean(Constants.LOCATION, false) || mHelper.isLogging();
   }

   /******************************/
   /** Loggermap methods        **/ 
   /******************************/
   
   @Override
   public void updateOverlays()
   {
      SharedPreferences sharedPreferences = mHelper.getPreferences();
      GoogleLoggerMap.this.mMapView.setSatellite(sharedPreferences.getBoolean(Constants.SATELLITE, false));
      GoogleLoggerMap.this.mMapView.setTraffic(sharedPreferences.getBoolean(Constants.TRAFFIC, false));
   }
   
   @Override
   public void setDrawingCacheEnabled(boolean b)
   {
      findViewById(R.id.mapScreen).setDrawingCacheEnabled(true);
   }
   
   @Override
   public Activity getActivity()
   {
      return this;
   }

   @Override
   public void onLayerCheckedChanged(int checkedId, boolean isChecked)
   {
      switch (checkedId)
      {
         case R.id.layer_google_satellite:
            setSatelliteOverlay(true);
            break;
         case R.id.layer_google_regular:
            setSatelliteOverlay(false);
            break;
         case R.id.layer_traffic:
            setTrafficOverlay(isChecked);
            break;
         default:
            break;
      }
   }

   @Override
   public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
   {
      if (key.equals(Constants.TRAFFIC))
      {
         updateOverlays();
      }
      else if (key.equals(Constants.SATELLITE))
      {
         updateOverlays();
      }
   }

   @Override
   public Bitmap getDrawingCache()
   {
      return findViewById(R.id.mapScreen).getDrawingCache();
   }

   @Override
   public void showMediaDialog(BaseAdapter mediaAdapter)
   {
      mHelper.showMediaDialog(mediaAdapter);
   }

   public void onDateOverlayChanged()
   {
      mMapView.postInvalidate();
   }
   
   @Override
   public String getDataSourceId()
   {
      return LoggerMapHelper.GOOGLE_PROVIDER;
   }

   @Override
   public boolean isOutsideScreen(GeoPoint lastPoint)
   {
      Point out = new Point();
      this.mMapView.getProjection().toPixels(lastPoint, out);
      int height = this.mMapView.getHeight();
      int width = this.mMapView.getWidth();
      return (out.x < 0 || out.y < 0 || out.y > height || out.x > width);
   }

   @Override
   public boolean isNearScreenEdge(GeoPoint lastPoint)
   {
      Point out = new Point();
      this.mMapView.getProjection().toPixels(lastPoint, out);
      int height = this.mMapView.getHeight();
      int width = this.mMapView.getWidth();
      return (out.x < width / 4 || out.y < height / 4 || out.x > (width / 4) * 3 || out.y > (height / 4) * 3);
   }

   @Override
   public void executePostponedActions()
   {
      // NOOP for Google Maps
   }
   
   @Override
   public void enableCompass()
   {
      mMylocation.enableCompass();
   }

   @Override
   public void enableMyLocation()
   {
      mMylocation.enableMyLocation(); 
   }
   @Override
   public void disableMyLocation()
   {
      mMylocation.disableMyLocation();
   }

   @Override
   public void disableCompass()
   {
      mMylocation.disableCompass();
   }

   @Override
   public void setZoom(int zoom)
   {
      mMapView.getController().setZoom(zoom);
   }

   @Override
   public void animateTo(GeoPoint storedPoint)
   {
      mMapView.getController().animateTo(storedPoint);
   }

   @Override
   public int getZoomLevel()
   {
      return mMapView.getZoomLevel();
   }

   @Override
   public GeoPoint getMapCenter()
   {
      return mMapView.getMapCenter();
   }

   @Override
   public boolean zoomOut()
   {
      return mMapView.getController().zoomOut();
   }

   @Override
   public boolean zoomIn()
   {
      return  mMapView.getController().zoomIn();
   }

   @Override
   public void postInvalidate()
   {
      mMapView.postInvalidate();
   }

   @Override
   public void clearAnimation()
   {
      mMapView.clearAnimation();
   }

   @Override
   public void setCenter(GeoPoint lastPoint)
   {
      mMapView.getController().setCenter(lastPoint);
   }

   @Override
   public int getMaxZoomLevel()
   {
      return mMapView.getMaxZoomLevel();
   }

   @Override
   public GeoPoint fromPixels(int x, int y)
   {
      return mMapView.getProjection().fromPixels(x, y);
   }

   @Override
   public boolean hasProjection()
   {
      return mMapView.getProjection() != null;
   }

   @Override
   public float metersToEquatorPixels(float float1)
   {
      return mMapView.getProjection().metersToEquatorPixels(float1);
   }

   @Override
   public void toPixels(GeoPoint geoPoint, Point screenPoint)
   {
      mMapView.getProjection().toPixels(geoPoint, screenPoint);
   }

   @Override
   public TextView[] getSpeedTextViews()
   {
      return mSpeedtexts;
   }

   @Override
   public TextView getAltitideTextView()
   {
      return mLastGPSAltitudeView;
   }

   @Override
   public TextView getSpeedTextView()
   {
      return mLastGPSSpeedView;
   }

   @Override
   public TextView getDistanceTextView()
   {
      return mDistanceView;
   }

   @Override
   public void addOverlay(OverlayProvider overlay)
   {
      mMapView.getOverlays().add(overlay.getGoogleOverlay());
   }

   @Override
   public void clearOverlays()
   { 
      mMapView.getOverlays().clear();
   }
   
   @Override
   public SlidingIndicatorView getScaleIndicatorView()
   {
      return (SlidingIndicatorView) findViewById(R.id.scaleindicator);
   }

   @Override
   public void onClick(View v)
   {
      // TODO Auto-generated method stub
      Intent intent = new Intent(Intent.ACTION_MAIN, null);
      intent.addCategory(Intent.CATEGORY_LAUNCHER);
      List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);
      int len = list.size();
      for (int i = 0; i < len; i++) {
         ResolveInfo info = list.get(i);
         //String A=info.activityInfo.applicationInfo.className;

         Log.v(TAG, "CName:=" + i);
         Log.v(TAG, "PName:=" + info.activityInfo.applicationInfo.packageName);
         Intent intent2=pm.getLaunchIntentForPackage(info.activityInfo.applicationInfo.packageName);
         String packagename=info.activityInfo.applicationInfo.packageName;
         Log.v(TAG, "packagename:=" + packagename);
         Log.v(TAG, "Intent2:=" + intent2.getPackage());
         Log.v(TAG, "className:=" + info.activityInfo.applicationInfo.className);
         Log.v(TAG, "CName:=" + info.activityInfo.name);
         if(packagename.equals("com.lge.message"))
         {
            mIntent=pm.getLaunchIntentForPackage(info.activityInfo.applicationInfo.packageName);
          
            break;
         }
         
         Log.v(TAG, "XXXX:=" + "====================================================");
     }
       

      switch (v.getId()) {
          case R.id.hotseat1:
              startActivity(mIntent);
 //            startActivity(sSettingsIntent);
              break;
          case R.id.hotseat2:
             startActivity(sSettingsIntent);
              break;
          case R.id.hotseat3:
             startActivity(sSettingsIntent);
              break;
 
      }

      
      
      
    
   }

   void theMessageWasReceivedAsynchronously(MyParcelableMessage message) {
      message.applyMessageToTextView(hotseat3);
      Log.v(TAG, "XXXX:=" + "====================================================");
    }
    
   

}
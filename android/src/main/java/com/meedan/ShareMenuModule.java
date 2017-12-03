package com.meedan;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import com.meedan.ShareMenuPackage;

import org.json.JSONObject;

import java.util.Map;
import java.util.Set;
import java.util.Iterator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class ShareMenuModule extends ReactContextBaseJavaModule {

  public ShareMenuModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "ShareMenu";
  }

  @ReactMethod
  public void getSharedText(Callback successCallback) {
    Activity mActivity = getCurrentActivity();
    Intent intent = mActivity.getIntent();
    String action = intent.getAction();
    String type = intent.getType();

    if (Intent.ACTION_SEND.equals(action) && type != null) {
      
      if ( "text/plain".equals( type ) ) {
        String input = intent.getStringExtra(Intent.EXTRA_TEXT);
        successCallback.invoke( input );
      }
      else if ( type.startsWith( "image/" ) ) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        successCallback.invoke( imageUri.toString() );
      }
      else if ( type.matches( "(?i).*zip.*" ) ) {
        Uri zipUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if ( zipUri != null ) {
          successCallback.invoke( zipUri.toString() );
        } else {
          successCallback.invoke( "" ); 
        }
      } 
      else if ( type.startsWith( "audio/" ) ) {
        Uri audioUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if( audioUri != null ) {
          successCallback.invoke( audioUri.toString() );
        } else {
          successCallback.invoke( "" );
        }
      } else {
        successCallback.invoke( "" );
      }
    } else {
      successCallback.invoke( "" );
    }
  }

  @ReactMethod
  public void clearSharedText() {
    Activity mActivity = getCurrentActivity();
    Intent intent = mActivity.getIntent();
    String type = intent.getType();
    if ( "text/plain".equals( type ) ) {
      intent.removeExtra( Intent.EXTRA_TEXT );
    } else if ( type.startsWith("image/" ) ) {
      intent.removeExtra( Intent.EXTRA_STREAM );
    } else if ( type.matches("(?i).*zip.*") ) {
      intent.removeExtra( Intent.EXTRA_STREAM );
    } else if( type.startsWith( "audio/" )) {
      intent.removeExtra( Intent.EXTRA_STREAM );
    }

  }

  @ReactMethod
  public void getSharedExtras(Callback successCallback) {
    Activity mActivity = getCurrentActivity();
    Intent intent = mActivity.getIntent();

    StringBuilder str = new StringBuilder();
    Bundle bundle = intent.getExtras();
    if (bundle != null) {
      Set keys = bundle.keySet();
      Iterator it = keys.iterator();
      str.append("{");
      while (it.hasNext()) {
        String key = (String) it.next();
        str.append("\"" + key + "\"");
        str.append(":");
        String fieldValue = "" + bundle.get(key);
        str.append("\"");
        str.append(fieldValue.replaceAll("\\\"", "\\\\\""));
        str.append("\"");

        if (it.hasNext()) str.append(",");
      }
      str.append("}");
    }

    successCallback.invoke(str.toString());
  }
}

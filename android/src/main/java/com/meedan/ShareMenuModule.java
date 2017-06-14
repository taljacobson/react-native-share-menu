package com.meedan;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import com.meedan.ShareMenuPackage;

import org.json.JSONObject;
import org.json.JSONException;

import java.util.Map;
import java.util.Set;
import java.util.Iterator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

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
    String inputText = intent.getStringExtra(Intent.EXTRA_TEXT);
    successCallback.invoke(inputText);
  }

  @ReactMethod
  public void clearSharedText() {
    Activity mActivity = getCurrentActivity();
    Intent intent = mActivity.getIntent();
    intent.removeExtra(Intent.EXTRA_TEXT);
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
        String escapedValue = fieldValue.replaceAll("\"", "\\\"");
        str.append("\"" + escapedValue + "\"");

        if (it.hasNext()) str.append(",");
      }
      str.append("}");

      try {
        JSONObject jsonObj = new JSONObject(str.toString());
        successCallback.invoke(jsonObj);
      } catch (JSONException e) {
        successCallback.invoke("{\"android.intent.extra.TEXT\":\"Error\"}");
      }
    } else {
      successCallback.invoke("");
    }
  }
}

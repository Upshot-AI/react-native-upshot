package com.upshotreactlibrary.upshot.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.brandkinesis.BrandKinesis;
import com.facebook.react.ReactInstanceManager;
import com.upshotreactlibrary.UpshotModule;
import com.facebook.react.ReactApplication;
import com.facebook.react.bridge.ReactContext;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by PurpleTalk on 7/6/16.
 */
public class UpshotPushAction extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = "";
        String appData = "";
        String deeplinkInfo = "";
        final Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String buttonOrImageDeeplink = intent.getStringExtra("clickInfo");
            Integer objectType = isJsonObjectOrJsonArray(buttonOrImageDeeplink);
            if (objectType == 2) {
                try {
                    JSONArray jsonArray = new JSONArray(buttonOrImageDeeplink);
                    JSONObject jsonObject = convertArrayToObject(jsonArray);
                    deeplinkInfo = jsonObject.toString();
                } catch (JSONException e) {
                    UpshotModule.logException(e);
                }
            } else if (objectType == 1) {
                deeplinkInfo = buttonOrImageDeeplink;
            }
            final String clickInfo = deeplinkInfo;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ReactApplication reactApplication = (ReactApplication) context.getApplicationContext();

                if (reactApplication != null) {
                    ReactInstanceManager reactInstanceManager = reactApplication.getReactNativeHost()
                            .getReactInstanceManager();
                    ReactContext reactContext = reactInstanceManager.getCurrentReactContext();

                    if (reactContext != null) {
                        UpshotModule.sendPushClickPayload(UpshotModule.bundleToJsonString(bundle));
                        if (deeplinkInfo != null && !deeplinkInfo.isEmpty()) {
                            UpshotModule.sendDeeplinkInfo(deeplinkInfo);
                        }
                    } else {
                        reactInstanceManager
                                .addReactInstanceEventListener(new ReactInstanceManager.ReactInstanceEventListener() {
                                    @Override
                                    public void onReactContextInitialized(ReactContext context) {
                                        // Use context here
                                        UpshotModule.sendPushClickPayload(UpshotModule.bundleToJsonString(bundle));
                                        if (clickInfo != null && !clickInfo.isEmpty()) {
                                            UpshotModule.sendDeeplinkInfo(clickInfo);
                                        }
                                        reactInstanceManager.removeReactInstanceEventListener(this);
                                    }
                                });
                    }
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ReactApplication reactApplication = (ReactApplication) context.getApplicationContext();
                            if (reactApplication != null) {
                                ReactInstanceManager reactInstanceManager = reactApplication.getReactNativeHost()
                                        .getReactInstanceManager();

                                ReactContext reactContext = reactInstanceManager.getCurrentReactContext();

                                if (reactContext != null) {
                                    UpshotModule.sendPushClickPayload(UpshotModule.bundleToJsonString(bundle));
                                } else {
                                    reactInstanceManager
                                            .addReactInstanceEventListener(
                                                    new ReactInstanceManager.ReactInstanceEventListener() {
                                                        @Override
                                                        public void onReactContextInitialized(ReactContext context) {
                                                            // Use context here
                                                            UpshotModule.sendPushClickPayload(
                                                                    UpshotModule.bundleToJsonString(bundle));
                                                            reactInstanceManager.removeReactInstanceEventListener(this);
                                                        }
                                                    });
                                }
                            }
                        }
                    }, 1000);
                }
            }
            action = bundle.getString("actionData");
            appData = intent.getStringExtra("appData");

            Class mainActivity = null;
            String packageName = context.getPackageName();
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            String className = launchIntent.getComponent().getClassName();
            try {
                mainActivity = Class.forName(className);
            } catch (Exception e) {
                UpshotModule.logException(e);
            }
            if (mainActivity == null) {
                return;
            }
            Intent i = new Intent(context, mainActivity);
            if (i == null) {
                return;
            }
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.putExtra("actionValue", action);
            i.putExtra("appData", appData);
            i.putExtra("deeplinkInfo", deeplinkInfo);
            context.startActivity(i);
            /**
             * This is to handle push with activity and we should call this method after
             * launching the application.
             */
            BrandKinesis bkInstance = BrandKinesis.getBKInstance();
            bkInstance.handlePushNotification(context, bundle);
        }
    }

    public JSONObject convertArrayToObject(JSONArray jsonArray) {
        JSONObject result = new JSONObject();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.optJSONObject(i);
                if (item != null) {
                    String key = item.optString("key");
                    String value = item.optString("android_value");
                    result.put(key, value);
                }
            }
        } catch (JSONException e) {
            UpshotModule.logException(e); // You can also log this or handle it more gracefully
        }

        return result;
    }
    public Integer isJsonObjectOrJsonArray(String jsonString) {
        try {
            new JSONObject(jsonString);  // Try to parse as JSONObject
            return 1;  // It's a valid JSONObject
        } catch (Exception e1) {
            try {
                new JSONArray(jsonString);  // Try to parse as JSONArray
                return 2;  // It's a valid JSONArray
            } catch (Exception e2) {
                return 0;  // It's neither a JSONObject nor a JSONArray
            }
        }
    }
}

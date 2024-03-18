package com.upshotreactlibrary.upshot.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.brandkinesis.BrandKinesis;
import com.facebook.react.ReactInstanceEventListener;
import com.facebook.react.ReactInstanceManager;
import com.upshotreactlibrary.UpshotModule;
import com.facebook.react.ReactApplication;
import com.facebook.react.bridge.ReactContext;
import android.os.Handler;

/**
 * Created by PurpleTalk on 7/6/16.
 */
public class UpshotPushAction extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = "";
        String appData = "";

        final Bundle bundle = intent.getExtras();
        if (bundle != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                ReactApplication reactApplication = (ReactApplication) context.getApplicationContext();
                if (reactApplication != null) {
                    ReactInstanceManager reactInstanceManager = reactApplication.getReactNativeHost()
                            .getReactInstanceManager();
                    ReactContext reactContext = reactInstanceManager.getCurrentReactContext();

                    if (reactContext != null) {
                        UpshotModule.sendPushClickPayload(UpshotModule.bundleToJsonString(bundle));
                    } else {
                        reactInstanceManager
                                .addReactInstanceEventListener(new ReactInstanceManager.ReactInstanceEventListener() {
                                    @Override
                                    public void onReactContextInitialized(ReactContext context) {
                                        // Use context here
                                        UpshotModule.sendPushClickPayload(UpshotModule.bundleToJsonString(bundle));
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
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("actionValue", action);
            i.putExtra("appData", appData);
            context.startActivity(i);
            /**
             * This is to handle push with activity and we should call this method after
             * launching the application.
             */
            BrandKinesis bkInstance = BrandKinesis.getBKInstance();
            bkInstance.handlePushNotification(context, bundle);
        }
    }
}

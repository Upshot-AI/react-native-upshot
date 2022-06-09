package com.upshotreactlibrary.upshot.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.brandkinesis.BrandKinesis;
import com.brandkinesis.utils.BKUtilLogger;
import com.upshotreactlibrary.UpshotModule;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by PurpleTalk on 7/6/16.
 */
public class UpshotPushAction extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.e("Bundle BKPushAction", "" + intent.getExtras());
        String action = "";
        String appData = "";        
        String bk = "";
        final Bundle bundle = intent.getExtras();
        if (bundle != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                UpshotModule.sendPushClickPayload(UpshotModule.bundleToJsonString(bundle));
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
                e.printStackTrace();
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
             * This is to handle push with activity and we should call this method after launching the application.
             */
            BrandKinesis bkInstance = BrandKinesis.getBKInstance();
            bkInstance.handlePushNotification(context, bundle);
        }
    }
}

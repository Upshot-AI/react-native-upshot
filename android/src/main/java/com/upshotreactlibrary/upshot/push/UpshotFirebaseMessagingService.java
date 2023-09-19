package com.upshotreactlibrary.upshot.push;

import android.content.Context;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;

import com.brandkinesis.BrandKinesis;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.upshotreactlibrary.UpshotModule;

import java.util.Map;

/**
 * Created by PurpleTalk on 30/12/16.
 */
public class UpshotFirebaseMessagingService extends FirebaseMessagingService {

    private final static String TAG = "BKFBaseMessagingService";
    private Context context;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        UpshotModule.sendRegistrationToServer(token);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Bundle bundle = new Bundle();
        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            bundle.putString(entry.getKey(), entry.getValue());
        }
        if (context == null) {
            context = getApplicationContext();
        }
        ApplicationInfo applicationInfo = null;
        String packageName = context.getPackageName();

        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
        }
        Bundle metaData = applicationInfo.metaData;

        if (bundle.containsKey("bk")) {

            String bkSmallNotificationIcon = null;
            Integer bkSmallNotificationIconColor = null;
            boolean allowForeground = false;

            if (metaData != null) {
                bkSmallNotificationIcon = metaData.getString("UpshotPushSmallIcon");
                bkSmallNotificationIconColor = metaData.getInt("UpshotPushSmallIconColor");
                allowForeground = metaData.getBoolean("UpshotAllowForegroundPush");
            }
            if (!TextUtils.isEmpty(bkSmallNotificationIcon)) {
                Resources resources = context.getResources();
                int resourceId = resources.getIdentifier(bkSmallNotificationIcon, "drawable", packageName);
                if (resourceId > 0) {
                    bundle.putInt(BrandKinesis.BK_LOLLIPOP_NOTIFICATION_ICON, resourceId);
                }

            }
            if (bkSmallNotificationIconColor != null) {
                bundle.putInt(BrandKinesis.BK_LOLLIPOP_NOTIFICATION_ICON_BG_COLOR, bkSmallNotificationIconColor);
            }
            sendPushBundletoBK(bundle, context, allowForeground);
        }
    }

    private void sendPushBundletoBK(final Bundle pushBundle, final Context mContext, boolean allowPushForeground) {

        BrandKinesis bkInstance1 = BrandKinesis.getBKInstance();
        bkInstance1.buildEnhancedPushNotification(mContext, pushBundle, allowPushForeground);
    }
}

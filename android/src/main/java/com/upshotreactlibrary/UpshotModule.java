package com.upshotreactlibrary;

import android.app.Activity;
import android.Manifest;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.content.Context;

import android.os.Handler;
import android.os.Looper;
import android.telecom.Call;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.brandkinesis.BKProperties;
import com.brandkinesis.BKUserInfo;
import com.brandkinesis.BrandKinesis;
import com.brandkinesis.activitymanager.BKActivityTypes;
import com.brandkinesis.callback.BKActivityCallback;
import com.brandkinesis.callback.BKBadgeAccessListener;
import com.brandkinesis.callback.BKDispatchCallback;
import com.brandkinesis.callback.BKInboxAccessListener;
import com.brandkinesis.callback.BKInboxActivityCallback;
import com.brandkinesis.callback.BKUserInfoCallback;
import com.brandkinesis.callback.BrandKinesisUserStateCompletion;
import com.brandkinesis.pushnotifications.BKNotificationsCountResponseListener;
import com.brandkinesis.pushnotifications.BKNotificationsResponseListener;
import com.brandkinesis.callback.BKMessageReadStatusListener;
import com.brandkinesis.rewards.BKRewardsResponseListener;
import com.brandkinesis.utils.BKUtilLogger;
import com.facebook.react.BuildConfig;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.Callback;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

public class UpshotModule extends ReactContextBaseJavaModule {

    public static final String REACT_CLASS = "UpshotReact";
    private static final String TAG = UpshotModule.class.getSimpleName();
    public static ReactApplicationContext reactContext;
    private static String pushClickPayload = "";
    private static String deeplinkInfo = "";
    private static View adsView = null;

    public UpshotModule(final ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    private static void emitDeviceEvent(final String eventName, @Nullable final WritableMap eventData) {
        // A method for emitting from the native side to JS
        // https://facebook.github.io/react-native/docs/native-modules-android.html#sending-events-to-javascript
        if (reactContext != null)
            reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, eventData);
    }

    /* Initialise Module */

    @ReactMethod
    public void initializeUpshot() {

        if (UpshotApplication.initType == null) {
            UpshotApplication.initUpshotUsingConfig();
            fetchTokenFromFirebaseSdk();
        }
        UpshotApplication.initType = "Config";
    }

    @ReactMethod
    public void initializeUpshotUsingOptions(final String options) {
        if (options == null) {
            return;
        }
        UpshotApplication.initType = "Options";
        if (UpshotApplication.options == null) {
            try {
                UpshotApplication.options = jsonToBundle(new JSONObject(options));
                UpshotApplication.initUpshotUsingOptions(jsonToBundle(new JSONObject(options)));
                fetchTokenFromFirebaseSdk();
            } catch (JSONException s) {
                logException(s);
            }
        }
    }

    /* Terminate Module */
    @ReactMethod
    private void terminate() {

    }

    @ReactMethod
    private void setDispatchInterval(final int interval) {

        final BrandKinesis bkInstance = BrandKinesis.getBKInstance();
        bkInstance.setDispatchEventTime(interval * 1000);
    }

    /* Events Module */

    @ReactMethod
    private void createPageViewEvent(final String PageName, final Callback callback) {

        try {
            if (PageName == null) {
                if (callback != null) {
                    callback.invoke("");
                }
                return;
            }
            final HashMap<String, Object> pageData = new HashMap<>();
            pageData.put(BrandKinesis.BK_CURRENT_PAGE, PageName);

            final BrandKinesis bkInstance = BrandKinesis.getBKInstance();
            final String eventID = bkInstance.createEvent(BKProperties.BKPageViewEvent.NATIVE, pageData, true);
            if (callback != null) {
                callback.invoke(eventID);
            }
        } catch (Exception e) {
            logException(e);
        }
    }

    @ReactMethod
    private void createCustomEvent(final String eventName, final String eventPayload, final boolean isTimed,
                                   final Callback callback) {

        try {
            if (eventName == null || eventPayload == null) {
                if (callback != null) {
                    callback.invoke("");
                }
                return;
            }
            final JSONObject jEventPayload = new JSONObject(eventPayload);
            final String eventID = BrandKinesis.getBKInstance().createEvent(eventName, jsonToHashMap(jEventPayload),
                    isTimed);
            if (callback != null) {
                callback.invoke(eventID);
            }
        } catch (final Exception e) {
            logException(e);
        }
    }

    @ReactMethod
    private void setValueAndClose(final String payload, final String eventId) {
        try {
            if (eventId == null) {
                return;
            }
            final JSONObject jeventPayload = new JSONObject(payload);
            BrandKinesis.getBKInstance().closeEvent(eventId, jsonToHashMap(jeventPayload));
        } catch (final Exception e) {
            logException(e);
        }
    }

    @ReactMethod
    private void closeEventForId(final String eventId) {
        try {
            if (eventId == null) {
                return;
            }
            BrandKinesis.getBKInstance().closeEvent(eventId);
        } catch (final Exception e) {
            logException(e);
        }
    }

    @ReactMethod
    private void dispatchEventsWithTimedEvents(final boolean timed, final Callback callback) {
        try {
            BrandKinesis.getBKInstance().dispatchNow(reactContext.getApplicationContext(), timed,
                    new BKDispatchCallback() {
                        @Override
                        public void onDispatchComplete(final boolean status) {
                            if (callback != null) {
                                callback.invoke(status);
                            }
                        }
                    });
        } catch (final Exception e) {
            logException(e);
        }
    }

    @ReactMethod
    private void createLocationEvent(final String latitude, final String longitude) {
        try {
            BrandKinesis.getBKInstance().createLocationEvent(Double.parseDouble(latitude),
                    Double.parseDouble(longitude));
        } catch (final Exception e) {
            logException(e);
        }
    }

    @ReactMethod
    private void createAttributionEvent(final String payload, final Callback callback) {

        try {
            if (payload == null) {
                return;
            }
            final JSONObject jeventPayload = new JSONObject(payload);
            String eventId = BrandKinesis.getBKInstance().createAttributionEvent(jsonToHashMapString(jeventPayload));
            if (callback != null) {
                callback.invoke(eventId);
            }
        } catch (final Exception e) {
            logException(e);
        }
    }

    /* Profile Module */
    @ReactMethod
    private void setUserProfile(final String userData, final Callback callback) {

        if (userData == null) {
            return;
        }

        Map<String, String> predefinedKeys = new HashMap<>();
        predefinedKeys.put("lastName", BKUserInfo.BKUserData.LAST_NAME);
        predefinedKeys.put("middleName", BKUserInfo.BKUserData.MIDDLE_NAME);
        predefinedKeys.put("firstName", BKUserInfo.BKUserData.FIRST_NAME);
        predefinedKeys.put("language", BKUserInfo.BKUserData.LANGUAGE);
        predefinedKeys.put("occupation", BKUserInfo.BKUserData.OCCUPATION);
        predefinedKeys.put("qualification", BKUserInfo.BKUserData.QUALIFICATION);
        predefinedKeys.put("phone", BKUserInfo.BKUserData.PHONE);
        predefinedKeys.put("localeCode", BKUserInfo.BKUserData.LOCALE_CODE);
        predefinedKeys.put("userName", BKUserInfo.BKUserData.USER_NAME);
        predefinedKeys.put("email", BKUserInfo.BKUserData.EMAIL);
        predefinedKeys.put("appuID", BKUserInfo.BKExternalIds.APPUID);
        predefinedKeys.put("facebookID", BKUserInfo.BKExternalIds.FACEBOOK);
        predefinedKeys.put("twitterID", BKUserInfo.BKExternalIds.TWITTER);
        predefinedKeys.put("foursquareID", BKUserInfo.BKExternalIds.FOURSQUARE);
        predefinedKeys.put("linkedinID", BKUserInfo.BKExternalIds.LINKEDIN);
        predefinedKeys.put("googleplusID", BKUserInfo.BKExternalIds.GOOGLEPLUS);
        predefinedKeys.put("enterpriseUID", BKUserInfo.BKExternalIds.ENTERPRISE_UID);
        predefinedKeys.put("advertisingID", BKUserInfo.BKExternalIds.ADVERTISING_ID);
        predefinedKeys.put("instagramID", BKUserInfo.BKExternalIds.INSTAGRAM);
        predefinedKeys.put("pinterest", BKUserInfo.BKExternalIds.PINTEREST);
        predefinedKeys.put("token", BKUserInfo.BKExternalIds.GCM);
        predefinedKeys.put("gender", BKUserInfo.BKUserData.GENDER);
        predefinedKeys.put("maritalStatus", BKUserInfo.BKUserData.MARITAL_STATUS);
        predefinedKeys.put("year", BKUserInfo.BKUserDOBdata.YEAR);
        predefinedKeys.put("month", BKUserInfo.BKUserDOBdata.MONTH);
        predefinedKeys.put("day", BKUserInfo.BKUserDOBdata.DAY);
        predefinedKeys.put("age", BKUserInfo.BKUserData.AGE);
        predefinedKeys.put("email_opt", BKUserInfo.BKUserData.EMAIL_OPT_OUT);
        predefinedKeys.put("push_opt", BKUserInfo.BKUserData.PUSH_OPT_OUT);
        predefinedKeys.put("sms_opt", BKUserInfo.BKUserData.SMS_OPT_OUT);
        predefinedKeys.put("data_opt", BKUserInfo.BKUserData.DATA_OPT_OUT);
        predefinedKeys.put("ip_opt", BKUserInfo.BKUserData.IP_OPT_OUT);
        try {
            JSONObject providedJson = new JSONObject(userData);
            JSONObject othersJson = new JSONObject();

            Bundle bundle = new Bundle();

            final Iterator<String> keys = providedJson.keys();
            while (keys.hasNext()) {

                final String key = keys.next();
                final Object value = providedJson.get(key);
                if (key == null || value == null) {
                    continue;
                }
                if (predefinedKeys.containsKey(key)) {
                    // predefined
                    String bkKey = predefinedKeys.get(key);
                    if (value instanceof Integer) {
                        bundle.putInt(bkKey, providedJson.optInt(key));
                    } else if (value instanceof Float || value instanceof Double) {
                        bundle.putFloat(bkKey, (float) providedJson.optDouble(key));
                    } else {
                        bundle.putString(bkKey, providedJson.optString(key));
                    }
                } else { // other
                    othersJson.put(key, value);
                }
            }
            if (othersJson.length() != 0) {
                bundle.putSerializable("others", (HashMap) jsonToHashMap(othersJson));
            }

            final BrandKinesis bkInstance = BrandKinesis.getBKInstance();
            bkInstance.setUserInfoBundle(bundle, new BKUserInfoCallback() {
                @Override
                public void onUserInfoUploaded(final boolean uploadSuccess) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.invoke(uploadSuccess);
                            }
                        }
                    });
                }
            });
        } catch (final JSONException e) {
            logException(e);
        }
    }

    @ReactMethod
    private void getUserDetails(final Callback callback) {

        final BrandKinesis bkInstance = BrandKinesis.getBKInstance();
        Set<String> keys = new HashSet<>();
        Map<String, Object> userInfo = bkInstance.getUserDetails(keys);
        JSONObject details = new JSONObject(userInfo);
        callback.invoke(details.toString());
    }

    /* Activity Module */
    @ReactMethod
    private void showActivityWithType(final int activityType, final String tagName) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                final BrandKinesis bkInstance = BrandKinesis.getBKInstance();

                BKActivityTypes type = BKActivityTypes.parse(activityType);
                if (activityType == -1) {
                    type = BKActivityTypes.ACTIVITY_ANY;
                }
                bkInstance.getActivity(reactContext.getApplicationContext(), type, tagName, new BKActivityCallback() {
                    @Override
                    public void onActivityError(final int i) {

                        upshotActivityError(i);
                    }

                    @Override
                    public void onActivityCreated(final BKActivityTypes bkActivityTypes) {
                        upshotActivityCreated(bkActivityTypes);
                    }

                    @Override
                    public void onActivityDestroyed(BKActivityTypes bkActivityTypes) {
                        upshotActivityDestroyed(bkActivityTypes);
                    }

                    @Override
                    public void onActivitySkipped(BKActivityTypes bkActivityTypes) {
                        upshotActivitySkipped(bkActivityTypes);
                    }

                    @Override
                    public void brandKinesisActivityPerformedActionWithParams(final BKActivityTypes bkActivityTypes,
                                                                              final Map<String, Object> map) {
                        upshotDeeplinkCallback(bkActivityTypes, map);
                    }
                });
            }
        });
    }

    @ReactMethod
    private void showInteractiveTutorial( final String tagName) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                final BrandKinesis bkInstance = BrandKinesis.getBKInstance();

                BKActivityTypes type = BKActivityTypes.ACTIVITY_ANY;
               View view = null;
                try {
                        view = reactContext.getCurrentActivity().getWindow().getDecorView();
                } catch (Exception e) {
                    logException(e);
                }
                bkInstance.getActivity(view, type, tagName, new BKActivityCallback() {
                    @Override
                    public void onActivityError(final int i) {

                        upshotActivityError(i);
                    }

                    @Override
                    public void onActivityCreated(final BKActivityTypes bkActivityTypes) {
                        upshotActivityCreated(bkActivityTypes);
                    }

                    @Override
                    public void onActivityDestroyed(BKActivityTypes bkActivityTypes) {
                        upshotActivityDestroyed(bkActivityTypes);
                    }

                    @Override
                    public void onActivitySkipped(BKActivityTypes bkActivityTypes) {
                        upshotActivitySkipped(bkActivityTypes);
                    }

                    @Override
                    public void brandKinesisActivityPerformedActionWithParams(final BKActivityTypes bkActivityTypes,
                                                                              final Map<String, Object> map) {
                        upshotDeeplinkCallback(bkActivityTypes, map);
                    }
                });
            }
        });
    }

    @ReactMethod
    private void showActivityWithId(final String activityId) {

        if (activityId == null) {
            return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                final BrandKinesis bkInstance = BrandKinesis.getBKInstance();
                bkInstance.getActivity(activityId, new BKActivityCallback() {
                    @Override
                    public void onActivityError(int i) {
                        upshotActivityError(i);
                    }

                    @Override
                    public void onActivityCreated(BKActivityTypes bkActivityTypes) {
                        upshotActivityCreated(bkActivityTypes);
                    }

                    @Override
                    public void onActivityDestroyed(BKActivityTypes bkActivityTypes) {
                        upshotActivityDestroyed(bkActivityTypes);
                    }

                    @Override
                    public void onActivitySkipped(BKActivityTypes bkActivityTypes) {
                        upshotActivitySkipped(bkActivityTypes);
                    }

                    @Override
                    public void brandKinesisActivityPerformedActionWithParams(BKActivityTypes bkActivityTypes,
                                                                              Map<String, Object> map) {
                        upshotDeeplinkCallback(bkActivityTypes, map);
                    }
                });
            }
        });
    }

    @ReactMethod
    private void removeTutorials() {

        final BrandKinesis bkInstance = BrandKinesis.getBKInstance();
        bkInstance.removeTutorial(reactContext.getApplicationContext());
    }

    @ReactMethod
    private void fetchInboxInfo(final Callback callback) {

        final BrandKinesis bkInstance = BrandKinesis.getBKInstance();
        bkInstance.fetchInboxInfo(new BKInboxAccessListener() {
            @Override
            public void onMessagesAvailable(List<HashMap<String, Object>> list) {

                final JSONArray messagesJson = new JSONArray();

                for (HashMap<String, Object> message : list) {

                    JSONObject messageJson = new JSONObject();

                    for (Map.Entry<String, Object> entry : message.entrySet()) {
                        try {
                            String jKey = entry.getKey();
                            if (jKey.equalsIgnoreCase("activities")) {
                                JSONArray jsonArray = new JSONArray();

                                List<HashMap<String, Object>> activityArrayList = (List<HashMap<String, Object>>) entry
                                        .getValue();

                                for (HashMap<String, Object> activityValue : activityArrayList) {

                                    JSONObject activityJson = new JSONObject();
                                    for (Map.Entry<String, Object> activityEntry : activityValue.entrySet()) {
                                        activityJson.put(activityEntry.getKey(), activityEntry.getValue());
                                    }
                                    jsonArray.put(activityJson);
                                }
                                messageJson.put(jKey, jsonArray);
                            } else {

                                messageJson.put(jKey, entry.getValue());
                            }
                        } catch (JSONException e) {
                            logException(e);
                        }
                    }
                    messagesJson.put(messageJson);
                }
                callback.invoke(messagesJson.toString());
            }
        });
    }

    @ReactMethod
    private void getUserBadges(final Callback callback) {

        final BrandKinesis bkInstance = BrandKinesis.getBKInstance();
        bkInstance.getBadges(new BKBadgeAccessListener() {
            @Override
            public void onBadgesAvailable(HashMap<String, List<HashMap<String, Object>>> badges) {

                final JSONArray activeBadgesArray = new JSONArray();
                final JSONArray inactiveBadgesArray = new JSONArray();

                for (Map.Entry<String, List<HashMap<String, Object>>> badgeMap : badges.entrySet()) {
                    String jKey = badgeMap.getKey();
                    List<HashMap<String, Object>> hashMapList = badges.get(jKey);

                    for (int i = 0; i < hashMapList.size(); i++) {
                        HashMap<String, Object> stringObjectHashMap = hashMapList.get(i);
                        JSONObject badgeJson = new JSONObject();

                        for (Map.Entry<String, Object> singleBadge : stringObjectHashMap.entrySet()) {
                            try {
                                String singleBadgeKey = singleBadge.getKey();
                                if (singleBadge.getValue() instanceof String) {

                                    String singleBadgeValue = (String) singleBadge.getValue();
                                    if (singleBadgeKey.equals("image")) {
                                        badgeJson.put(singleBadgeKey, "file://" + singleBadgeValue);
                                    } else {
                                        badgeJson.put(singleBadgeKey, singleBadgeValue);
                                    }

                                } else if (singleBadge.getValue() instanceof Double) {
                                    badgeJson.put(singleBadgeKey, singleBadge.getValue());
                                } else if (singleBadge.getValue() instanceof Long) {
                                    badgeJson.put(singleBadgeKey, singleBadge.getValue());
                                } else if (singleBadge.getValue() instanceof Integer) {
                                    badgeJson.put(singleBadgeKey, singleBadge.getValue());
                                } else {
                                }
                            } catch (JSONException e) {
                                logException(e);
                            }
                        }
                        if (jKey.equalsIgnoreCase("active_list")) {
                            activeBadgesArray.put(badgeJson);
                        } else {
                            inactiveBadgesArray.put(badgeJson);
                        }
                    }
                }

                try {
                    final JSONObject badge = new JSONObject();
                    badge.put("active_list", activeBadgesArray);
                    badge.put("inactive_list", inactiveBadgesArray);
                    callback.invoke(badge.toString());
                } catch (JSONException e) {
                    logException(e);
                }
            }
        });
    }

    /* Push Notification Module */
    @ReactMethod
    private void registerForPush() {
        requestForNotificationPermissions();
    }

    private static void requestForNotificationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && reactContext != null) {
            try {
                Activity currentActivity = reactContext.getCurrentActivity();
                if (currentActivity != null && ActivityCompat.checkSelfPermission(currentActivity,
                        Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    currentActivity
                            .requestPermissions(new String[] { Manifest.permission.POST_NOTIFICATIONS }, 1);
                }
            } catch (Exception e) {
                logException(e);
            }
        }
    }

    @ReactMethod
    private void sendDeviceToken(final String token) {

        final BrandKinesis bkInstance = BrandKinesis.getBKInstance();
        if (bkInstance == null || token == null) {
            return;
        }
        if(token.isEmpty()) {
            return;
        }

        final Bundle userInfo = new Bundle();
        userInfo.putString(BKUserInfo.BKExternalIds.GCM, token);

        bkInstance.setUserInfoBundle(userInfo, new BKUserInfoCallback() {
            @Override
            public void onUserInfoUploaded(final boolean uploadSuccess) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        });
    }

    private static void sendDeviceTokenToUpshot(final String token) {

        final BrandKinesis bkInstance = BrandKinesis.getBKInstance();
        if (bkInstance == null || token == null) {
            return;
        }
        if(token.isEmpty()) {
            return;
        }

        final Bundle userInfo = new Bundle();
        userInfo.putString(BKUserInfo.BKExternalIds.GCM, token);

        bkInstance.setUserInfoBundle(userInfo, new BKUserInfoCallback() {
            @Override
            public void onUserInfoUploaded(final boolean uploadSuccess) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        });
    }

    // For Creating Push Click Event
    @ReactMethod
    public void sendPushDataToUpshot(final String pushData) {

        BrandKinesis bkInstance = BrandKinesis.getBKInstance();
        try {
            bkInstance.handlePushNotification(reactContext.getApplicationContext(),
                    jsonToBundle(new JSONObject(pushData)));
        } catch (JSONException e) {
            logException(e);
        }
    }

    @ReactMethod
    public void displayNotification(final String pushData) {

        BrandKinesis bkInstance = BrandKinesis.getBKInstance();
        try {

            Bundle bundle = jsonToBundle(new JSONObject(pushData));
            if (!bundle.containsKey("bk")) {
                return;
            }

            Context context = null;
            if (reactContext == null) {
                context = UpshotApplication.get();
            } else {
                context = reactContext.getApplicationContext();
            }
            ApplicationInfo applicationInfo = null;
            String packageName = context.getPackageName();
            boolean allowPushForeground = false;
            try {
                applicationInfo = context.getPackageManager().getApplicationInfo(packageName,
                        PackageManager.GET_META_DATA);
                Bundle metaData = applicationInfo.metaData;
                String bkSmallNotificationIcon = null;
                Integer bkSmallNotificationIconColor = null;
                if (metaData != null) {
                    bkSmallNotificationIcon = metaData.getString("UpshotPushSmallIcon");
                    bkSmallNotificationIconColor = metaData.getInt("UpshotPushSmallIconColor");
                    allowPushForeground = metaData.getBoolean("UpshotAllowForegroundPush", false);
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
            } catch (PackageManager.NameNotFoundException e) {
                logException(e);
            }

            bkInstance.buildEnhancedPushNotification(context, bundle, allowPushForeground);
        } catch (Exception e) {
            logException(e);
        }
    }

    @ReactMethod
    private void getNotificationList(final int limit, final Boolean loadMore, final Callback successCallback,
                                     final Callback errorCallback) {

        BrandKinesis.getBKInstance().getNotifications(reactContext.getApplicationContext(), !loadMore, limit,
                new BKNotificationsResponseListener() {
                    @Override
                    public void notificationsResponse(Object response) {
                        successCallback.invoke(response);
                    }

                    @Override
                    public void onErrorReceived(Object errorResponse) {
                        errorCallback.invoke(errorResponse);
                    }
                });
    }

    @ReactMethod
    private void getUnreadNotificationsCount(final int inboxType, final Callback callback) {

        BrandKinesis.getBKInstance().getUnreadNotificationsCount(reactContext.getApplicationContext(), inboxType,
                new BKNotificationsCountResponseListener() {
                    @Override
                    public void notificationsCount(int i) {
                        callback.invoke(i);
                    }
                });
    }

    @ReactMethod
    private void updateNotificationReadStatus(final String notificationId, final Callback callback) {
        BrandKinesis.getBKInstance().setBrandkinesisMessageReadStatusCallback(new BKMessageReadStatusListener() {
            @Override
            public void onMessageStatus(boolean b, String s) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("status", b);
                    jsonObject.put("error", s);
                    callback.invoke(jsonObject.toString());
                } catch (JSONException e) {

                }
            }
        });
        BrandKinesis.getBKInstance().updatePushNotificationReadStatus(reactContext, notificationId);
    }

    @ReactMethod
    private void showInboxNotificationScreen(final String options) {

        try {

            JSONObject jsonObject = new JSONObject(options);

            jsonObject.put("bkInboxType", jsonObject.getInt("BKInboxType"));
            jsonObject.put("bkShowReadNotifications", jsonObject.getBoolean("BKShowReadNotifications"));
            jsonObject.put("BKEnableLoadMore", jsonObject.getBoolean("BKEnableLoadMore"));
            jsonObject.put("BKPushFetchLimit", jsonObject.getInt("BKPushFetchLimit"));
            jsonObject.put("BKDisplayMsgCount", jsonObject.getBoolean("BKDisplayMsgCount"));
            jsonObject.put("BKDisplayTime", jsonObject.getBoolean("BKDisplayTime"));

            BrandKinesis.getBKInstance().showInboxActivity(reactContext.getApplicationContext(),
                    jsonToBundle(jsonObject), new BKInboxActivityCallback() {
                        @Override
                        public void brandKinesisInboxActivityPresented() {

                        }

                        @Override
                        public void brandKinesisInboxActivityDismissed() {

                        }
                    });
        } catch (JSONException e) {
            logException(e);
        }
        ;
    }

    /* Streaks */
    @ReactMethod
    private void getStreaksData(final Callback successCallback, final Callback failCallback) {

        String streakData = BrandKinesis.getBKInstance().getStreakData();
        successCallback.invoke(streakData);
    }

    /* GDPR */
    @ReactMethod
    private void disableUser(final Callback callback) {

        final BrandKinesis bkInstance = BrandKinesis.getBKInstance();
        bkInstance.disableUser(reactContext.getApplicationContext(),
                new BrandKinesisUserStateCompletion() {
                    @Override
                    public void userStateCompletion(boolean b) {
                        if (callback != null) {
                            callback.invoke(b);
                        }
                    }
                });
    }

    @ReactMethod
    private void getUserId(final Callback callback) {

        final BrandKinesis bkInstance = BrandKinesis.getBKInstance();
        callback.invoke(bkInstance.getUserId(reactContext.getApplicationContext()));
    }

    @ReactMethod
    private void getSDKVersion(final Callback callback) {

        final BrandKinesis bkInstance = BrandKinesis.getBKInstance();
        callback.invoke(bkInstance.getSdkVersion());
    }

    /* Rewards Module */

    @ReactMethod
    public void getRewardsList(final Callback successCallback, final Callback errorCallback) {
        final BrandKinesis bkInstance = BrandKinesis.getBKInstance();

        bkInstance.getRewardsStatusWithCompletionBlock(reactContext.getApplicationContext(),
                new BKRewardsResponseListener() {
                    @Override
                    public void rewardsResponse(final Object response) {
                        successCallback.invoke(response);
                    }

                    @Override
                    public void onErrorReceived(final Object response) {
                        BKUtilLogger.showDebugLog("Test onErrorReceived", "response : " + response.toString());
                        errorCallback.invoke(response);
                    }
                });
    }

    @ReactMethod
    public void getRewardHistoryForProgram(final String programId, final int historyType,
                                           final Callback successCallback, final Callback failureCallback) {

        if (programId == null) {
            return;
        }
        final BrandKinesis bkInstance = BrandKinesis.getBKInstance();

        bkInstance.getRewardHistoryForProgramId(reactContext.getApplicationContext(), programId, historyType,
                new BKRewardsResponseListener() {
                    @Override
                    public void rewardsResponse(final Object response) {
                        successCallback.invoke(response);
                    }

                    @Override
                    public void onErrorReceived(final Object response) {
                        failureCallback.invoke(response);
                    }
                });
    }

    @ReactMethod
    public void redeemRewardsForProgram(final String programId, final int transactionAmount,
                                        final int redeemValue, final String tag, final Callback successCallback, final Callback failureCallback) {

        if (programId == null) {
            return;
        }
        final BrandKinesis bkInstance = BrandKinesis.getBKInstance();

        bkInstance.redeemRewardsWithProgramId(reactContext.getApplicationContext(), programId, transactionAmount,
                redeemValue, tag,
                new BKRewardsResponseListener() {
                    @Override
                    public void rewardsResponse(final Object response) {
                        successCallback.invoke(response);
                    }

                    @Override
                    public void onErrorReceived(final Object response) {
                        failureCallback.invoke(response);
                    }
                });
    }

    @ReactMethod
    public void getRewardRulesforProgram(final String programId, final Callback successCallback,
                                         final Callback failureCallback) {

        if (programId == null) {
            return;
        }
        final BrandKinesis bkInstance = BrandKinesis.getBKInstance();

        bkInstance.getRewardDetailsForProgramId(reactContext.getApplicationContext(), programId,
                new BKRewardsResponseListener() {
                    @Override
                    public void rewardsResponse(final Object response) {
                        successCallback.invoke(response);
                    }

                    @Override
                    public void onErrorReceived(final Object response) {
                        failureCallback.invoke(response);
                    }
                });
    }

    @ReactMethod
    public void getPushClickPayload(final Callback callback) {

        if (callback != null) {
            callback.invoke(pushClickPayload);
            pushClickPayload = "";
        }
    }

    @ReactMethod
    private void setFontStyles(final String fontStyles) {
        try {
            final JSONObject fontsJson = new JSONObject(fontStyles);
            final HashMap<String, Object> data = jsonToHashMap(fontsJson);

            HashMap<String, String> formattedFontData = new HashMap<>();

            for (HashMap.Entry<String, Object> entry : data.entrySet()) {
                String key = entry.getKey();
                try {

                    JSONObject keyDataMap = (JSONObject)data.get(key);
                    JSONObject jFormattedObj = new JSONObject();
                    Integer size = (Integer) keyDataMap.get("size");
                    jFormattedObj.put("size", size);

                    String formattedFontName = "";
                    String fontName = (String) keyDataMap.get("name");
                    if (!fontName.endsWith(".ttf")) {
                        formattedFontName = fontName + ".ttf";
                    } else {
                        formattedFontName = fontName;
                    }
                    jFormattedObj.put("name", formattedFontName);
                    formattedFontData.put(key, jFormattedObj.toString());
            } catch (Exception e) {
                logException(e);
            }            
        }
        BrandKinesis.getBKInstance().setFonts(formattedFontData, reactContext.getApplicationContext());
    } catch (JSONException e) {
        logException(e);
    }
}

    /* Utility Methods */
    public static void upshotInitStatus(boolean isSuccessCallback, String msg) {

        WritableMap payload = Arguments.createMap();
        payload.putBoolean("status", isSuccessCallback);
        payload.putString("error", msg);
        fetchTokenFromFirebaseSdk();
        emitDeviceEvent("UpshotAuthStatus", payload);
    }

    public static void upshotAdViewReceived(View adView, String adTag) {
        WritableMap payload = Arguments.createMap();
        payload.putString("Tag", adTag);
        emitDeviceEvent("UpshotAdReady", payload);
        adsView = adView;
    }

    private static WritableMap getWritableMapFromMap(Map<String, Object> map) {
        WritableMap payload = Arguments.createMap();

        for (String key : map.keySet()) {
            Object value = map.get(key);

            if (value instanceof Integer) {
                payload.putInt(key, (Integer) value);
            } else if (value instanceof Float || value instanceof Double) {
                payload.putDouble(key, (Double) value);
            } else if (value instanceof Boolean) {
                payload.putBoolean(key, (Boolean) value);
            } else {
                payload.putString(key, (String) value);
            }

        }
        return payload;
    }

    public static View getAdsView() {
        return adsView;
    }

    private static void fetchTokenFromFirebaseSdk() {

        try {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                return;
                            }
                            String token = task.getResult();
                            sendRegistrationToServer(token);
                        }
                    });
        } catch (Exception e) {
            logException(e);
        }
    }

    public static void upshotDeeplinkCallback(final BKActivityTypes bkActivityTypes,
                                              final Map<String, Object> map) {

        WritableMap payload = Arguments.createMap();
        if (map instanceof HashMap) {
            emitDeviceEvent("UpshotDeepLink", getWritableMapFromMap(map));
        } else {
            String data = (String) map.get("deepLink");
            payload.putInt("activityType", bkActivityTypes.getValue());
            if (TextUtils.isEmpty(data)) {
                try {
                    JSONObject deeplinkJSON = new JSONObject(data);
                    payload.putString("deepLink_keyValue", deeplinkJSON.toString());
                } catch (JSONException e) {
                    payload.putString("deepLink", data);
                }
            }
            emitDeviceEvent("UpshotDeepLink", payload);
        }
    }

    public static void upshotActivityCreated(final BKActivityTypes bkActivityTypes) {

        WritableMap payload = Arguments.createMap();
        payload.putInt("activityType", bkActivityTypes.getValue());
        emitDeviceEvent("UpshotActivityDidAppear", payload);
    }

    public static void upshotActivityDestroyed(final BKActivityTypes bkActivityTypes) {

        WritableMap payload = Arguments.createMap();
        payload.putInt("activityType", bkActivityTypes.getValue());
        // add activity payload
        emitDeviceEvent("UpshotActivityDidDismiss", payload);
    }

    public static void upshotActivitySkipped(final BKActivityTypes bkActivityTypes) {

        WritableMap payload = Arguments.createMap();
        payload.putInt("activityType", bkActivityTypes.getValue());
        emitDeviceEvent("UpshotActivitySkip", payload);
    }

    public static void upshotActivityError(int error) {

        String errorMessage = "";
        if (error == -1) {
            errorMessage = "No Activity Found";
        }
        WritableMap payload = Arguments.createMap();
        payload.putString("error", errorMessage);
        emitDeviceEvent("UpshotActivityError", payload);
    }

    public static void upshotCampaignDetailsLoaded() {

        WritableMap payload = Arguments.createMap();
        emitDeviceEvent("UpshotCampaignDetailsLoaded", payload);
    }

    public static void sendRegistrationToServer(String token) {

        try {
            if (token == null) {
                return;
            }

            if (token.isEmpty()) {
                return;
            }
            sendDeviceTokenToUpshot(token);
            WritableMap payload = Arguments.createMap();
            payload.putString("token", token);
            emitDeviceEvent("UpshotPushToken", payload);
        } catch (Exception e) {
            logException(e);
        }
    }

    public static void sendPushClickPayload(final String pushPayload) {

        pushClickPayload = pushPayload;
        final WritableMap payload = Arguments.createMap();
        payload.putString("payload", pushPayload);
        if (reactContext == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    emitDeviceEvent("UpshotPushPayload", payload);
                }
            }, 1000);
        } else {
            emitDeviceEvent("UpshotPushPayload", payload);
        }
    }

    public static void sendDeeplinkInfo(final String deeplinkInfo) {
        final WritableMap payload = Arguments.createMap();
        payload.putString("payload", deeplinkInfo);
        if (reactContext == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    emitDeviceEvent("UpshotOnPushClickInfo", payload);
                }
            }, 1000);
        } else {
            emitDeviceEvent("UpshotOnPushClickInfo", payload);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String bundleToJsonString(Bundle bundle) {

        JSONObject json = new JSONObject();
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            try {
                json.put(key, JSONObject.wrap(bundle.get(key)));
            } catch (JSONException e) {
                logException(e);
            }
        }
        return json.toString();
    }

    public static Bundle jsonToBundle(final JSONObject jsonObject) throws JSONException {

        final Bundle bundle = new Bundle();
        final Iterator iter = jsonObject.keys();
        while (iter.hasNext()) {
            try {
                final String key = (String) iter.next();
                if (key == null) {
                    continue;
                }
                final Object value = jsonObject.get(key);
                // predefined
                if (value != null) {
                    if (value instanceof Integer) {
                        bundle.putInt(key, jsonObject.optInt(key));
                    } else if (value instanceof Float || value instanceof Double) {
                        bundle.putFloat(key, (float) jsonObject.optDouble(key));
                    } else if (value instanceof Boolean) {
                        bundle.putBoolean(key, (boolean) jsonObject.optBoolean(key));
                    } else {
                        bundle.putString(key, jsonObject.optString(key));
                    }
                }
            } catch (JSONException e) {
                logException(e);
            }
        }
        return bundle;
    }

    public static HashMap<String, Object> jsonToHashMap(final JSONObject jsonObject) throws JSONException {
        final HashMap<String, Object> data = new HashMap<>();

        final Iterator jsonKeys = jsonObject.keys();
        while (jsonKeys.hasNext()) {
            final String key = (String) jsonKeys.next();
            if (key == null) {
                continue;
            }
            final Object value = jsonObject.get(key);
            if (value != null) {
                data.put(key, value);
            }
        }
        return data;
    }

    public static HashMap<String, String> jsonToHashMapString(final JSONObject jsonObject) throws JSONException {
        final HashMap<String, String> data = new HashMap<>();

        final Iterator jsonKeys = jsonObject.keys();
        while (jsonKeys.hasNext()) {
            final String key = (String) jsonKeys.next();
            if (key == null) {
                continue;
            }
            final String value = jsonObject.getString(key);
            if (value != null) {
                data.put(key, value);
            }
        }
        return data;
    }

    public static WritableMap convertJsonToMap(JSONObject jsonObject) throws JSONException {
        WritableMap map = new WritableNativeMap();

        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                map.putMap(key, convertJsonToMap((JSONObject) value));
            } else if (value instanceof Boolean) {
                map.putBoolean(key, (Boolean) value);
            } else if (value instanceof Integer) {
                map.putInt(key, (Integer) value);
            } else if (value instanceof Double) {
                map.putDouble(key, (Double) value);
            } else if (value instanceof String) {
                map.putString(key, (String) value);
            } else {
                map.putString(key, value.toString());
            }
        }
        return map;
    }

    public static void logException(Exception e) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace();
        }
    }
}

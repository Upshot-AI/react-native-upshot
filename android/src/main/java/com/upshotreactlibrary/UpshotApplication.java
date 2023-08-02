package com.upshotreactlibrary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

import com.brandkinesis.BrandKinesis;
import com.brandkinesis.activitymanager.BKActivityTypes;
import com.brandkinesis.callback.BKAuthCallback;
import com.brandkinesis.callback.BrandKinesisCallback;
import com.brandkinesis.utils.BKAppStatusUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpshotApplication extends Application implements BKAppStatusUtil.BKAppStatusListener {

    public static final String PRIMARY_CHANNEL = "default";
    private static Application application;
    public static Bundle options = null;
    public static String initType = null;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerChannel();
        }
        BKAppStatusUtil.getInstance().register(this, this);
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    public static Application get() {
        return application;
    }

    // BKAppStatusListener Callbacks
    @Override
    public void onAppComesForeground(Activity activity) {

        if (initType != null) {
            if (initType == "Config") {
                initUpshotUsingConfig();
            } else if (initType == "Options") {
                if (options != null) {
                    initUpshotUsingOptions(options);
                }
            }
        }
    }

    @Override
    public void onAppGoesBackground() {
        final BrandKinesis brandKinesis = BrandKinesis.getBKInstance();
        brandKinesis.terminate(getApplicationContext());
    }

    @Override
    public void onAppRemovedFromRecentsList() {
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void registerChannel() {
        try {
            String notificationsChannelId = "notifications";
            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    Context.NOTIFICATION_SERVICE);
            if (notificationManager == null) {
                return;
            }
            List<NotificationChannel> channels = notificationManager.getNotificationChannels();
            if (channels == null) {
                return;
            }
            NotificationChannel existingChanel = null;
            int count = 0;
            for (NotificationChannel channel : channels) {
                String fullId = channel.getId();
                if (fullId.contains(notificationsChannelId)) {
                    existingChanel = channel;
                    String[] numbers = extractRegexMatches(fullId, "\\d+");
                    if (numbers.length > 0) {
                        count = Integer.valueOf(numbers[0]);
                    }
                    break;
                }
            }
            if (existingChanel != null) {
                if (existingChanel.getImportance() < NotificationManager.IMPORTANCE_DEFAULT) {
                    notificationManager.deleteNotificationChannel(existingChanel.getId());
                }
            }

            String newId = existingChanel == null ? notificationsChannelId + '_' + (count + 1) : existingChanel.getId();

            NotificationChannel channel = new NotificationChannel(
                    newId, notificationsChannelId, NotificationManager.IMPORTANCE_HIGH);
            channel.setLightColor(Color.GREEN);
            channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            notificationManager.createNotificationChannel(channel);
        } catch (Exception e) {

        }
    }

    public String[] extractRegexMatches(String source, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);

        ArrayList<String> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(matcher.group());
        }

        String[] result = new String[matches.size()];
        matches.toArray(result);
        return result;
    }

    public static void initUpshotUsingConfig() {
        try {
            setUpshotGlobalCallbak();
            BrandKinesis.initialiseBrandKinesis(get(), new BKAuthCallback() {
                @Override
                public void onAuthenticationError(String errorMsg) {
                    UpshotModule.upshotInitStatus(false, errorMsg);
                }

                @Override
                public void onAuthenticationSuccess() {
                    UpshotModule.upshotInitStatus(true, "");
                }
            });
            setCustomizations();
        } catch (Exception e) {
        }
    }

    public static void initUpshotUsingOptions(Bundle options) {

        setUpshotGlobalCallbak();
        BrandKinesis.initialiseBrandKinesis(get(), options, null);
        setCustomizations();
    }

    private static void setCustomizations() {

        UpshotSurveyCustomization surveyCustomization = new UpshotSurveyCustomization(UpshotApplication.get());
        UpshotRatingCustomization ratingCustomization = new UpshotRatingCustomization(UpshotApplication.get());
        UpshotOpinionPollCustomization pollCustomization = new UpshotOpinionPollCustomization(UpshotApplication.get());
        UpshotTriviaCustomization triviaCustomization = new UpshotTriviaCustomization(UpshotApplication.get());

        BKUIPrefComponents components = new BKUIPrefComponents() {
            @Override
            public void setPreferencesForRelativeLayout(RelativeLayout relativeLayout, BKActivityTypes bkActivityTypes,
                    BKActivityRelativeLayoutTypes bkActivityRelativeLayoutTypes, boolean b) {
                switch (bkActivityTypes) {
                    case ACTIVITY_SURVEY:
                        surveyCustomization.customizeRelativeLayout(bkActivityRelativeLayoutTypes, relativeLayout, b);
                        break;
                    case ACTIVITY_RATINGS:
                        ratingCustomization.customizeRelativeLayout(bkActivityRelativeLayoutTypes, relativeLayout, b);
                        break;
                    case ACTIVITY_OPINION_POLL:
                        pollCustomization.customizeRelativeLayout(bkActivityRelativeLayoutTypes, relativeLayout, b);
                        break;
                    case ACTIVITY_TRIVIA:
                        triviaCustomization.customizeRelativeLayout(bkActivityRelativeLayoutTypes, relativeLayout, b);
                        break;
                }
            }

            @Override
            public void setPreferencesForImageButton(ImageButton imageButton, BKActivityTypes bkActivityTypes,
                    BKActivityImageButtonTypes bkActivityImageButtonTypes) {

                switch (bkActivityTypes) {
                    case ACTIVITY_SURVEY:
                        surveyCustomization.customizeImageButton(imageButton, bkActivityImageButtonTypes);
                        break;
                    case ACTIVITY_RATINGS:
                        ratingCustomization.customizeImageButton(imageButton, bkActivityImageButtonTypes);
                        break;
                    case ACTIVITY_OPINION_POLL:
                        pollCustomization.customizeImageButton(imageButton, bkActivityImageButtonTypes);
                        break;
                    case ACTIVITY_TRIVIA:
                        triviaCustomization.customizeImageButton(imageButton, bkActivityImageButtonTypes);
                        break;
                }
            }

            @Override
            public void setPreferencesForButton(Button button, BKActivityTypes bkActivityTypes,
                    BKActivityButtonTypes bkActivityButtonTypes) {

                switch (bkActivityTypes) {
                    case ACTIVITY_SURVEY:
                        surveyCustomization.customizeButton(button, bkActivityButtonTypes);
                        break;
                    case ACTIVITY_RATINGS:
                        ratingCustomization.customizeButton(button, bkActivityButtonTypes);
                        break;
                    case ACTIVITY_OPINION_POLL:
                        pollCustomization.customizeButton(button, bkActivityButtonTypes);
                        break;
                    case ACTIVITY_TRIVIA:
                        triviaCustomization.customizeButton(button, bkActivityButtonTypes);
                        break;
                }
            }

            @Override
            public void setPreferencesForTextView(TextView textView, BKActivityTypes bkActivityTypes,
                    BKActivityTextViewTypes bkActivityTextViewTypes) {
                switch (bkActivityTypes) {
                    case ACTIVITY_SURVEY:
                        surveyCustomization.customizeTextView(bkActivityTextViewTypes, textView);
                        break;
                    case ACTIVITY_RATINGS:
                        ratingCustomization.customizeTextView(bkActivityTextViewTypes, textView);
                        break;
                    case ACTIVITY_OPINION_POLL:
                        pollCustomization.customizeTextView(bkActivityTextViewTypes, textView);
                        break;
                    case ACTIVITY_TRIVIA:
                        triviaCustomization.customizeTextView(bkActivityTextViewTypes, textView);
                        break;
                }

            }

            @Override
            public void setPreferencesForImageView(ImageView imageView, BKActivityTypes bkActivityTypes,
                    BKActivityImageViewType bkActivityImageViewType) {

                switch (bkActivityTypes) {
                    case ACTIVITY_SURVEY:
                        surveyCustomization.customizeImageView(imageView, bkActivityImageViewType);
                        break;
                    case ACTIVITY_RATINGS:
                        ratingCustomization.customizeImageView(imageView, bkActivityImageViewType);
                        break;
                    case ACTIVITY_OPINION_POLL:
                        pollCustomization.customizeImageView(imageView, bkActivityImageViewType);
                        break;
                    case ACTIVITY_TRIVIA:
                        triviaCustomization.customizeImageView(imageView, bkActivityImageViewType);
                        break;
                }

            }

            @Override
            public void setPreferencesForOptionsSeparatorView(View view, BKActivityTypes bkActivityTypes) {

                switch (bkActivityTypes) {
                    case ACTIVITY_SURVEY:
                        surveyCustomization.customizeForOptionsSeparatorView(view);
                        break;
                    case ACTIVITY_RATINGS:
                        ratingCustomization.customizeForOptionsSeparatorView(view);
                        break;
                    case ACTIVITY_OPINION_POLL:
                        pollCustomization.customizeForOptionsSeparatorView(view);
                        break;
                    case ACTIVITY_TRIVIA:
                        triviaCustomization.customizeForOptionsSeparatorView(view);
                        break;
                }

            }

            @Override
            public void setCheckBoxRadioSelectorResource(BKUICheckBox bkuiCheckBox, BKActivityTypes bkActivityTypes,
                    boolean b) {

                switch (bkActivityTypes) {
                    case ACTIVITY_SURVEY:
                        surveyCustomization.customizeRadioButton(bkuiCheckBox, b);
                        break;
                    case ACTIVITY_RATINGS:
                        ratingCustomization.customizeRadioButton(bkuiCheckBox, b);
                        break;
                    case ACTIVITY_OPINION_POLL:
                        pollCustomization.customizeRadioButton(bkuiCheckBox, b);
                        break;
                    case ACTIVITY_TRIVIA:
                        triviaCustomization.customizeRadioButton(bkuiCheckBox, b);
                        break;
                }
            }

            @Override
            public void setRatingSelectorResource(List<Bitmap> list, List<Bitmap> list1,
                    BKActivityTypes bkActivityTypes, BKActivityRatingTypes bkActivityRatingTypes) {

                switch (bkActivityTypes) {
                    case ACTIVITY_SURVEY:
                        surveyCustomization.customizeRating(list, list1, bkActivityRatingTypes);
                        break;
                    case ACTIVITY_RATINGS:
                        ratingCustomization.customizeRating(list, list1, bkActivityRatingTypes);
                        break;
                    case ACTIVITY_OPINION_POLL:
                        pollCustomization.customizeRating(list, list1, bkActivityRatingTypes);
                        break;
                    case ACTIVITY_TRIVIA:
                        triviaCustomization.customizeRating(list, list1, bkActivityRatingTypes);
                        break;
                }
            }

            @Override
            public void setPreferencesForUIColor(BKBGColors bkbgColors, BKActivityTypes bkActivityTypes,
                    BKActivityColorTypes bkActivityColorTypes) {

                switch (bkActivityTypes) {
                    case ACTIVITY_SURVEY:
                        surveyCustomization.customizeBGColor(bkbgColors, bkActivityColorTypes);
                        break;
                    case ACTIVITY_RATINGS:
                        ratingCustomization.customizeBGColor(bkbgColors, bkActivityColorTypes);
                        break;
                    case ACTIVITY_OPINION_POLL:
                        pollCustomization.customizeBGColor(bkbgColors, bkActivityColorTypes);
                        break;
                    case ACTIVITY_TRIVIA:
                        triviaCustomization.customizeBGColor(bkbgColors, bkActivityColorTypes);
                        break;
                }
            }

            @Override
            public void setPreferencesForGraphColor(BKGraphType bkGraphType, List<Integer> list,
                    BKActivityTypes bkActivityTypes) {

                switch (bkActivityTypes) {
                    case ACTIVITY_SURVEY:
                        surveyCustomization.customizeForGraphColor(bkGraphType, list);
                        break;
                    case ACTIVITY_RATINGS:
                        ratingCustomization.customizeForGraphColor(bkGraphType, list);
                        break;
                    case ACTIVITY_OPINION_POLL:
                        pollCustomization.customizeForGraphColor(bkGraphType, list);
                        break;
                    case ACTIVITY_TRIVIA:
                        triviaCustomization.customizeForGraphColor(bkGraphType, list);
                        break;
                }
            }

            @Override
            public int getPositionPercentageFromBottom(BKActivityTypes bkActivityTypes, BKViewType bkViewType) {
                return 0;
            }

            @Override
            public void setPreferencesForSeekBar(SeekBar seekBar, BKActivityTypes bkActivityTypes,
                    BKActivitySeekBarTypes bkActivitySeekBarTypes) {
                switch (bkActivityTypes) {
                    case ACTIVITY_SURVEY:

                        surveyCustomization.customizeSeekBar(bkActivitySeekBarTypes, seekBar);
                        break;
                    case ACTIVITY_RATINGS:
                        ratingCustomization.customizeSeekBar(bkActivitySeekBarTypes, seekBar);
                        break;
                    case ACTIVITY_OPINION_POLL:
                        pollCustomization.customizeSeekBar(bkActivitySeekBarTypes, seekBar);
                        break;
                    case ACTIVITY_TRIVIA:
                        triviaCustomization.customizeSeekBar(bkActivitySeekBarTypes, seekBar);
                        break;
                }

            }

            @Override
            public void setPreferencesForEditText(EditText editText, BKActivityTypes bkActivityTypes,
                    BKActivityEditTextTypes bkActivityEditTextTypes) {

                switch (bkActivityTypes) {
                    case ACTIVITY_SURVEY:
                        surveyCustomization.customizeEditText(bkActivityEditTextTypes, editText);
                        break;
                    case ACTIVITY_RATINGS:
                        ratingCustomization.customizeEditText(bkActivityEditTextTypes, editText);
                        break;
                    case ACTIVITY_OPINION_POLL:
                        pollCustomization.customizeEditText(bkActivityEditTextTypes, editText);
                        break;
                    case ACTIVITY_TRIVIA:
                        triviaCustomization.customizeEditText(bkActivityEditTextTypes, editText);
                        break;
                }
            }

            @Override
            public void setPreferencesForLinearLayout(LinearLayout linearLayout, BKActivityTypes bkActivityTypes,
                    BKActivityLinearLayoutTypes bkActivityLinearLayoutTypes) {

                switch (bkActivityTypes) {
                    case ACTIVITY_SURVEY:
                        surveyCustomization.customizeForLinearLayout(linearLayout, bkActivityLinearLayoutTypes);
                        break;
                    case ACTIVITY_RATINGS:
                        ratingCustomization.customizeForLinearLayout(linearLayout, bkActivityLinearLayoutTypes);
                        break;
                    case ACTIVITY_OPINION_POLL:
                        pollCustomization.customizeForLinearLayout(linearLayout, bkActivityLinearLayoutTypes);
                        break;
                    case ACTIVITY_TRIVIA:
                        triviaCustomization.customizeForLinearLayout(linearLayout, bkActivityLinearLayoutTypes);
                        break;
                }
            }
        };
        BrandKinesis.getBKInstance().setUIPreferences(components);
    }

    private static void setUpshotGlobalCallbak() {

        BrandKinesis bkInstance = BrandKinesis.getBKInstance();
        bkInstance.setBrandkinesisCallback(new BrandKinesisCallback() {

            @Override
            public void brandKinesisInboxActivityPresented() {

            }

            @Override
            public void brandKinesisInboxActivityDismissed() {

            }

            @Override
            public void brandKinesisInteractiveTutorialInfoForPlugin(String s) {

            }

            @Override
            public void onActivityError(int i) {
                UpshotModule.upshotActivityError(i);
            }

            @Override
            public void brandkinesisCampaignDetailsLoaded() {
                UpshotModule.upshotCampaignDetailsLoaded();
            }

            @Override
            public void onActivityCreated(BKActivityTypes bkActivityTypes) {
                UpshotModule.upshotActivityCreated(bkActivityTypes);
            }

            @Override
            public void onActivityDestroyed(BKActivityTypes bkActivityTypes) {
                UpshotModule.upshotActivityDestroyed(bkActivityTypes);
            }

            @Override
            public void brandKinesisActivityPerformedActionWithParams(BKActivityTypes bkActivityTypes,
                    Map<String, Object> map) {
                UpshotModule.upshotDeeplinkCallback(bkActivityTypes, map);
            }

            @Override
            public void onAuthenticationError(String errorMsg) {
                UpshotModule.upshotInitStatus(false, errorMsg);
            }

            @Override
            public void onAuthenticationSuccess() {
                UpshotModule.upshotInitStatus(true, "");
            }

            @Override
            public void onBadgesAvailable(HashMap<String, List<HashMap<String, Object>>> hashMap) {

            }

            @Override
            public void getBannerView(View view, String s) {
                UpshotModule.upshotAdViewReceived(view, s);
            }

            @Override
            public void onErrorOccurred(int i) {

            }

            @Override
            public void onMessagesAvailable(List<HashMap<String, Object>> list) {

            }

            @Override
            public void onUserInfoUploaded(boolean b) {

            }

            @Override
            public void userStateCompletion(boolean b) {

            }
        });
    }
}

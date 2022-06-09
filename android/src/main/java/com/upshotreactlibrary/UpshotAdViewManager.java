package com.upshotreactlibrary;

import androidx.annotation.NonNull;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

public class UpshotAdViewManager extends SimpleViewManager<UpshotAdsView> {

    @NonNull
    @Override
    public String getName() {
        return "UpshotAdsView";
    }

    @NonNull
    @Override
    protected UpshotAdsView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new UpshotAdsView(reactContext);
    }
}

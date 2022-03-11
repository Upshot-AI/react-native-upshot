package com.upshotreactlibrary;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public class UpshotAdsView extends LinearLayout {

    public UpshotAdsView(Context context) {
        super(context);
        if (UpshotModule.getAdsView() != null) {
            this.addView(UpshotModule.getAdsView());
        }
    }
}

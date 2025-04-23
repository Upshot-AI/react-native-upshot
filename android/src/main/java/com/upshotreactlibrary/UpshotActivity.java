package com.upshotreactlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.facebook.react.ReactActivity;

public class UpshotActivity extends ReactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent currentIntent = getIntent();
        if (currentIntent != null) {
            String deeplinkInfo = currentIntent.getStringExtra("deeplinkInfo");
            if (deeplinkInfo != null && !deeplinkInfo.isEmpty()) {                
                Handler mHandler = new Handler();
                Runnable mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        UpshotModule.sendDeeplinkInfo(deeplinkInfo);
                    }
                };
                mHandler.postDelayed(mRunnable, 2000);
            }
        }
    }
    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String deeplinkInfo = intent.getStringExtra("deeplinkInfo");
        if (deeplinkInfo != null && !deeplinkInfo.isEmpty()) {
            UpshotModule.sendDeeplinkInfo(deeplinkInfo);
        }
    }
}

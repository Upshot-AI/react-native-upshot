package com.upshotreactlibrary.upshot.customization;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.brandkinesis.BKUIPrefComponents;
import com.brandkinesis.BKUIPrefComponents.BKActivityButtonTypes;
import com.brandkinesis.BKUIPrefComponents.BKActivityColorTypes;
import com.brandkinesis.BKUIPrefComponents.BKActivityImageViewType;
import com.brandkinesis.BKUIPrefComponents.BKActivityRatingTypes;
import com.brandkinesis.BKUIPrefComponents.BKActivityTextViewTypes;
import com.brandkinesis.BKUIPrefComponents.BKBGColors;
import com.brandkinesis.activitymanager.BKActivityTypes;
import com.upshotreactlibrary.UpshotApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.brandkinesis.BKUIPrefComponents.BKActivityImageButtonTypes;
import static com.brandkinesis.BKUIPrefComponents.BKUICheckBox;

import org.json.JSONException;
import org.json.JSONObject;

public class UpshotCustomization {

    public String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private int validateJsonInt(JSONObject json, String key) {

        try {
            if (json.has(key) && json.get(key) != null) {
                if (json.get(key) instanceof Integer) {
                    return json.getInt(key);
                } else if (json.get(key) instanceof String) {
                    try {
                        return Integer.parseInt(json.getString(key));
                    } catch (Exception e) {
                        return 0;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String validateJsonString(JSONObject json, String key) {

        try {
            if (json.has(key)) {
                if (json.get(key) == null || !(json.get(key) instanceof String)) {
                    return "";
                } else {
                    return json.getString(key);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void applyEditTextProperties(Context mContext, JSONObject submitButtonJsonObject, EditText editText) {
        applyFontAttribute(mContext, editText, submitButtonJsonObject);
        applyTextSizeAttribute(mContext, editText, submitButtonJsonObject);
        applyTextColorAttribute(mContext, editText, submitButtonJsonObject);
    }

    public void applyButtonProperties(Context context, JSONObject submitButtonJsonObject, Button button) {
        applyFontAttribute(context, button, submitButtonJsonObject);
        applyTextSizeAttribute(context, button, submitButtonJsonObject);
        applyTextColorAttribute(context, button, submitButtonJsonObject);
        applyBgColorAttribute(context, button, submitButtonJsonObject);
        applyBgImageAttribute(context, button, submitButtonJsonObject);
    }

    private void setImageResourceToView(Context context, String bgImage, View view) {
        if (!TextUtils.isEmpty(bgImage)) {

            int resourceId = getIdentifier(context, bgImage);
            if (resourceId > 0) {
                if (view instanceof ImageButton) {
                    ((ImageButton)view).setImageResource(resourceId);
                }else if (view instanceof ImageView) {
                    ((ImageView)view).setImageResource(resourceId);
                }
            }
        }
    }

    private void applyImageResourceAttribute(Context context, View view, JSONObject jsonObject) {
        String bgImage = validateJsonString(jsonObject, "image");
        setImageResourceToView(context, bgImage, view);
    }

    public void applyRelativeLayoutProperties(Context context, String imageName, RelativeLayout view) {
        setBgToView(context, imageName, view);
    }

    public void applyImageProperties(Context context, String imageName, ImageView view) {
        setBgToView(context, imageName, view);
    }

    public void applyImageButtonProperties(Context context, JSONObject submitButtonJsonObject, ImageButton button) {
        applyImageResourceAttribute(context, button, submitButtonJsonObject);
    }

    public void applyTextViewProperties(Context context, JSONObject jsonObject, TextView textView) {
        applyFontAttribute(context, textView, jsonObject);
        applyTextSizeAttribute(context, textView, jsonObject);
        applyTextColorAttribute(context, textView, jsonObject);
        applyBgColorAttribute(context, textView, jsonObject);
        applyBgImageAttribute(context, textView, jsonObject);
    }

    private void applyBgImageAttribute(Context context, View view, JSONObject jsonObject) {
        String bgImage = validateJsonString(jsonObject, "image");
        setBgToView(context, bgImage, view);
    }

    private void setBgToView(Context context, String bgImage, View view) {
        if (!TextUtils.isEmpty(bgImage)) {

            int resourceId = getIdentifier(context, bgImage);
            if (resourceId > 0) {
                view.setBackgroundResource(resourceId);
            }
        }
    }

    public int getIdentifier(Context context, String bgImage) {
        try {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier(bgImage, "drawable", context.getPackageName());
            return resourceId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void applyBgColorAttribute(Context context, View view, JSONObject jsonObject) {
        String bgcolor = validateJsonString(jsonObject, "bgcolor");
        if (!TextUtils.isEmpty(bgcolor)) {
            try {
                view.setBackgroundColor(Color.parseColor(bgcolor));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void applyTextColorAttribute(Context context, View view, JSONObject jsonObject) {
        String text_color = validateJsonString(jsonObject, "color");
        if (!TextUtils.isEmpty(text_color)) {
            try {
                if (view instanceof Button) {
                    ((Button) view).setTextColor(Color.parseColor(text_color));
                } else if (view instanceof TextView) {
                    ((TextView) view).setTextColor(Color.parseColor(text_color));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void applyTextSizeAttribute(Context context, View view, JSONObject jsonObject) {
        int font_size = validateJsonInt(jsonObject, "size");
        try {
            if (view instanceof Button) {
                ((Button) view).setTextSize(font_size);
            } else if (view instanceof TextView) {
                ((TextView) view).setTextSize(font_size);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyFontAttribute(Context context, View view, JSONObject jsonObject) {
        String font_name = validateJsonString(jsonObject, "font_name");
        if (!TextUtils.isEmpty(font_name)) {
            try {
                Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + font_name);
                if (view instanceof Button) {
                    ((Button) view).setTypeface(typeface);
                } else if (view instanceof TextView) {
                    ((TextView) view).setTypeface(typeface);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void customizeSeekBar(BKUIPrefComponents.BKActivitySeekBarTypes seekBarTypes, SeekBar seekBar) {

    }

    public void customizeRelativeLayout(BKUIPrefComponents.BKActivityRelativeLayoutTypes relativeLayoutTypes, RelativeLayout relativeLayout, boolean isFullScreen) {

    }

    public void customizeTextView(BKActivityTextViewTypes textViewType, TextView textView) {

    }

    public void customizeRating(List<Bitmap> selectedRatingList, List<Bitmap> unselectedRatingList, BKActivityRatingTypes ratingType) {

    }

    public void customizeImageView(ImageView imageView, BKActivityImageViewType imageType) {

    }

    public void customizeButton(Button button, BKActivityButtonTypes buttonType) {

    }

    public void customizeImageButton(ImageButton imageButton, BKActivityImageButtonTypes buttonType) {

    }

    public void customizeRadioButton(BKUICheckBox bkUiCheckBox, boolean isCheckBox) {

    }

    public void customizeBGColor(BKBGColors color, BKActivityColorTypes colorType) {

    }

    public void customizeEditText(BKUIPrefComponents.BKActivityEditTextTypes EditTextType, EditText editText) {

    }

    public void customizeForGraphColor(BKUIPrefComponents.BKGraphType graphType, List<Integer> colorsList) {

    }

    public void customizeForLinearLayout(LinearLayout linearLayout, BKUIPrefComponents.BKActivityLinearLayoutTypes linearLayoutTypes){

    }

    public void customizeForOptionsSeparatorView(View view){

    }
}

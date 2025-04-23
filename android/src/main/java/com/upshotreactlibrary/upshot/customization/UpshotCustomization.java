package com.upshotreactlibrary.upshot.customization;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import com.upshotreactlibrary.UpshotModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.brandkinesis.BKUIPrefComponents.BKActivityImageButtonTypes;
import static com.brandkinesis.BKUIPrefComponents.BKUICheckBox;

import androidx.core.content.res.ResourcesCompat;

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
            UpshotModule.logException(ex);
            return null;
        }
        return json;
    }

    private int getFontSize(JSONObject json, String key) {

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
            UpshotModule.logException(e);
        }
        return 14;
    }

    public String getImageName(JSONObject json, String key) {

        String imageName = validateJsonString(json, key);
        if (imageName.contains(".")) {
            String[] list = imageName.split("\\.");
            if (list.length > 0) {
                return list[0];
            }
        }
        return imageName;
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
            UpshotModule.logException(e);
        }
        return "";
    }

    public void applyEditTextProperties(Context mContext, JSONObject editTextJson, EditText editText) {

        GradientDrawable gd = new GradientDrawable();
        String borderColor = validateJsonString(editTextJson, "border_color");
        String bgColorValue = validateJsonString(editTextJson, "bgcolor");
        int bgColor = Color.TRANSPARENT;
        if (borderColor != null && !borderColor.isEmpty()) {
            gd.setStroke(3, Color.parseColor(borderColor));
        }

        if (bgColorValue != null && !bgColorValue.isEmpty()) {
            bgColor = Color.parseColor(bgColorValue);
        }
        gd.setCornerRadius(8);
        gd.setColor(bgColor);
        editText.setBackground(gd);

        applyFontAttribute(mContext, editText, editTextJson);
        applyTextSizeAttribute(mContext, editText, editTextJson);
        applyTextColorAttribute(mContext, editText, editTextJson);
    }

    public void applyButtonProperties(Context context, JSONObject submitButtonJsonObject, Button button) {
        applyFontAttribute(context, button, submitButtonJsonObject);

        String border_color = validateJsonString(submitButtonJsonObject, "border_color");
        String bgColor = validateJsonString(submitButtonJsonObject, "bgcolor");
        if (border_color != null && !border_color.isEmpty()) {
            applyBorderColorForButtons(button, border_color, bgColor);
        } else {
            applyBgColorAttribute(context, button, submitButtonJsonObject);
            applyBgImageAttribute(context, button, submitButtonJsonObject);
        }
        applyTextSizeAttribute(context, button, submitButtonJsonObject);
        applyTextColorAttribute(context, button, submitButtonJsonObject);
    }

    public void applyBorderColorForButtons(Button button, String border_color, String bg_color) {
        GradientDrawable borderDrawable = new GradientDrawable();
        borderDrawable.setCornerRadius(15);
        borderDrawable.setStroke(3, Color.parseColor(border_color));

        if (bg_color != null && !bg_color.isEmpty()) {
            borderDrawable.setColor(Color.parseColor(bg_color));
        }
        button.setBackground(borderDrawable);
    }

    private void setImageResourceToView(Context context, String bgImage, View view) {
        if (!TextUtils.isEmpty(bgImage)) {

            int resourceId = getIdentifier(context, bgImage);
            if (resourceId > 0) {
                if (view instanceof ImageButton) {
                    ((ImageButton) view).setImageResource(resourceId);
                } else if (view instanceof ImageView) {
                    ((ImageView) view).setImageResource(resourceId);
                }
            }
        }
    }

    private void applyImageResourceAttribute(Context context, View view, JSONObject jsonObject) {
        String bgImage = getImageName(jsonObject, "image");
        setImageResourceToView(context, bgImage, view);
    }

    public void applyRelativeLayoutProperties(Context context, String imageName, RelativeLayout view) {
        setBgToView(context, imageName, view);
    }

    public void applyBGImageForPopup(Context context, String imageName, RelativeLayout view) {

        if (!TextUtils.isEmpty(imageName)) {
            int resourceId = getIdentifier(context, imageName);
            if (resourceId > 0) {
                Bitmap bm = BitmapFactory.decodeResource(context.getResources(), resourceId);
                bm = Bitmap.createScaledBitmap(bm, 300, 250, true);

                Drawable d = new BitmapDrawable(context.getResources(), bm);
                view.setBackground(d);
            }
        }
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
        // applyBgColorAttribute(context, textView, jsonObject);
        // applyBgImageAttribute(context, textView, jsonObject);
    }

    private void applyBgImageAttribute(Context context, View view, JSONObject jsonObject) {
        String bgImage = getImageName(jsonObject, "image");
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
            UpshotModule.logException(e);
        }
        return 0;
    }

    public void applyBorderRadiusToButton(Button button, int bgColor, int borderColor) {

        ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) button.getLayoutParams();
                layoutParams.height = button.getHeight() - 7;
                button.setLayoutParams(layoutParams);
                button.setBackground(generateDrawableRectangle(bgColor, borderColor, true));

                // remove this layout listener - as it will run every time the view updates
                if (button.getViewTreeObserver().isAlive()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        button.getViewTreeObserver()
                                .removeOnGlobalLayoutListener(this);
                    } else {
                        button.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            }
        };
        button.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    private Drawable generateDrawableRectangle(int backgroundColor, int borderColor, boolean generateWithBorder) {
        // Default state
        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setColor(backgroundColor);
        if (generateWithBorder) {
            background.setStroke(2, borderColor);
        }
        return background;
    }

    private void applyBgColorAttribute(Context context, View view, JSONObject jsonObject) {
        String bgcolor = validateJsonString(jsonObject, "bgcolor");
        String borderColor = "";

        if (!TextUtils.isEmpty(borderColor)) {
            try {
                if (view instanceof Button) {
                    applyBorderRadiusToButton((Button) view, Color.parseColor(bgcolor), Color.parseColor(borderColor));
                }
            } catch (Exception e) {
                UpshotModule.logException(e);
            }
        } else {
            if (!TextUtils.isEmpty(bgcolor)) {
                try {
                    view.setBackgroundColor(Color.parseColor(bgcolor));
                } catch (Exception e) {
                    UpshotModule.logException(e);
                }
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
                UpshotModule.logException(e);
            }
        }
    }

    private void applyTextSizeAttribute(Context context, View view, JSONObject jsonObject) {
        int font_size = getFontSize(jsonObject, "size");
        try {
            if (view instanceof Button) {
                ((Button) view).setTextSize(font_size);
            } else if (view instanceof TextView) {
                ((TextView) view).setTextSize(font_size);
            }
        } catch (Exception e) {
            UpshotModule.logException(e);
        }
    }

    public static int getFontIdWithFontName(Context context, String fontName, String packageName) {
        int font = 0;
        try {
            font = context.getResources().getIdentifier(fontName, "font", packageName);
        } catch (Exception e) {
            UpshotModule.logException(e);
        }
        return font;
    }

    private void applyFontAttribute(Context context, View view, JSONObject jsonObject) {

        String font_name = validateJsonString(jsonObject, "font_name");
        if (!TextUtils.isEmpty(font_name)) {
            Typeface typeface = null;
            try {
                if (!font_name.contains(".ttf")) {
                    font_name += ".ttf";
                }
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + font_name);
            } catch (Exception e) {
                UpshotModule.logException(e);
            }

            if (typeface == null) {
                try {
                    typeface = ResourcesCompat.getFont(context,
                            getFontIdWithFontName(context, font_name, context.getPackageName()));
                } catch (Exception e) {
                    UpshotModule.logException(e);
                }
            }

            if (typeface != null) {
                if (view instanceof Button) {
                    ((Button) view).setTypeface(typeface);
                } else if (view instanceof TextView) {
                    ((TextView) view).setTypeface(typeface);
                }
            }
        }
    }

    public void customizeSeekBar(BKUIPrefComponents.BKActivitySeekBarTypes seekBarTypes, SeekBar seekBar) {

    }

    public void customizeRelativeLayout(BKUIPrefComponents.BKActivityRelativeLayoutTypes relativeLayoutTypes,
            RelativeLayout relativeLayout, boolean isFullScreen) {

    }

    public void customizeTextView(BKActivityTextViewTypes textViewType, TextView textView) {

    }

    public void customizeRating(List<Bitmap> selectedRatingList, List<Bitmap> unselectedRatingList,
            BKActivityRatingTypes ratingType) {

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

    public void customizeForLinearLayout(LinearLayout linearLayout,
            BKUIPrefComponents.BKActivityLinearLayoutTypes linearLayoutTypes) {

    }

    public void customizeForOptionsSeparatorView(View view) {

    }
}

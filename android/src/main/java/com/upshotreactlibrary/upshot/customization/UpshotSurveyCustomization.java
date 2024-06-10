package com.upshotreactlibrary.upshot.customization;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.brandkinesis.BKUIPrefComponents;
import com.brandkinesis.BKUIPrefComponents.BKActivityButtonTypes;
import com.brandkinesis.BKUIPrefComponents.BKActivityColorTypes;
import com.brandkinesis.BKUIPrefComponents.BKActivityTextViewTypes;
import com.brandkinesis.BKUIPrefComponents.BKBGColors;
import com.upshotreactlibrary.UpshotModule;

import org.json.JSONObject;

import java.util.List;

public class UpshotSurveyCustomization extends UpshotCustomization {
    Context mContext;
    private JSONObject mJsonObject = null;

    public UpshotSurveyCustomization(Context context) {
        mContext = context;
        try {
            mJsonObject = new JSONObject(loadJSONFromAsset(context, "UpshotSurveyTheme.json"));
        } catch (Exception e) {
            UpshotModule.logException(e);
        }
    }

    public void customizeRadioButton(BKUIPrefComponents.BKUICheckBox checkBox, boolean isCheckBox) {
        super.customizeRadioButton(checkBox, isCheckBox);
        if (mJsonObject != null) {
            try {
                JSONObject buttonJsonObject = (JSONObject) mJsonObject.get("image");

                if (isCheckBox) {
                    Bitmap check_select, default_select;
                    check_select = BitmapFactory.decodeResource(mContext.getResources(),
                            getIdentifier(mContext, getImageName(buttonJsonObject, "checkbox_sel")));
                    checkBox.setSelectedCheckBox(check_select);

                    default_select = BitmapFactory.decodeResource(mContext.getResources(),
                            getIdentifier(mContext, getImageName(buttonJsonObject, "checkbox_def")));
                    checkBox.setUnselectedCheckBox(default_select);
                } else {
                    Bitmap check_select, default_select;
                    check_select = BitmapFactory.decodeResource(mContext.getResources(),
                            getIdentifier(mContext, getImageName(buttonJsonObject, "radio_sel")));
                    checkBox.setSelectedCheckBox(check_select);

                    default_select = BitmapFactory.decodeResource(mContext.getResources(),
                            getIdentifier(mContext, getImageName(buttonJsonObject, "radio_def")));
                    checkBox.setUnselectedCheckBox(default_select);
                }

            } catch (Exception e) {
                UpshotModule.logException(e);
            }
        }
    }

    @Override
    public void customizeSeekBar(BKUIPrefComponents.BKActivitySeekBarTypes seekBarTypes, SeekBar seekBar) {
        super.customizeSeekBar(seekBarTypes, seekBar);

        if (mJsonObject != null) {

            try {
                JSONObject sliderJsonObject = (JSONObject) mJsonObject.get("slider");
                String minColor = validateJsonString(sliderJsonObject, "min_color");
                String maxColor = validateJsonString(sliderJsonObject, "max_color");
                Drawable bitmapDrawable;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    if (minColor != null && !minColor.isEmpty()) {
                        seekBar.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor(minColor)));
                    }
                    if (maxColor != null && !maxColor.isEmpty()) {
                        seekBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(maxColor)));
                    }
                    bitmapDrawable = ContextCompat.getDrawable(mContext,
                            getIdentifier(mContext, getImageName(sliderJsonObject, "thumb_image")));
                    if (bitmapDrawable != null) {
                        seekBar.setThumb(bitmapDrawable);
                    }
                }
            } catch (Exception e) {
                UpshotModule.logException(e);
            }
        }
    }

    @Override
    public void customizeEditText(BKUIPrefComponents.BKActivityEditTextTypes EditTextType, EditText editText) {
        super.customizeEditText(EditTextType, editText);

        if (mJsonObject != null) {
            try {
                JSONObject feedbackBoxJson = (JSONObject) mJsonObject.get("feedbackBox");

                switch (EditTextType) {
                    case BKACTIVITY_SURVEY_EDIT_TEXT:
                    default:
                        applyEditTextProperties(mContext, feedbackBoxJson, editText);
                        break;
                }
            } catch (Exception e) {
                UpshotModule.logException(e);
            }
        }
    }

    @Override
    public void customizeButton(Button button, BKActivityButtonTypes buttonType) {
        super.customizeButton(button, buttonType);

        if (mJsonObject != null) {

            try {
                JSONObject buttonJsonObject = (JSONObject) mJsonObject.get("button");

                switch (buttonType) {
                    case BKACTIVITY_SUBMIT_BUTTON:

                        JSONObject submitButtonJsonObject = (JSONObject) buttonJsonObject.get("submit");
                        applyButtonProperties(mContext, submitButtonJsonObject, button);
                        break;
                    case BKACTIVITY_SURVEY_CONTINUE_BUTTON:
                        JSONObject continueButtonJsonObject = (JSONObject) buttonJsonObject.get("continue");
                        applyButtonProperties(mContext, continueButtonJsonObject, button);
                        break;
                    case BKACTIVITY_SURVEY_NEXT_BUTTON:
                        JSONObject nextButtonJsonObject = (JSONObject) buttonJsonObject.get("next");
                        applyButtonProperties(mContext, nextButtonJsonObject, button);
                        break;
                    case BKACTIVITY_SURVEY_PREVIOUS_BUTTON:
                        JSONObject prevButtonJsonObject = (JSONObject) buttonJsonObject.get("prev");
                        applyButtonProperties(mContext, prevButtonJsonObject, button);
                        break;
                }

            } catch (Exception e) {
                UpshotModule.logException(e);
            }
        }
    }

    @Override
    public void customizeBGColor(BKBGColors color, BKActivityColorTypes colorType) {
        super.customizeBGColor(color, colorType);

        if (mJsonObject != null) {

            try {
                JSONObject jsonObject = (JSONObject) mJsonObject.get("color");

                switch (colorType) {
                    case BKACTIVITY_OPTION_DEF_BORDER:
                        String bgColor = validateJsonString(jsonObject, "option_def_border");
                        if (bgColor != null && !bgColor.isEmpty()) {
                            color.setColor(Color.parseColor(bgColor));
                        }

                        break;
                    case BKACTIVITY_OPTION_SEL_BORDER:
                        String bgColor_sel = validateJsonString(jsonObject, "option_sel_border");
                        if (bgColor_sel != null && !bgColor_sel.isEmpty()) {
                            color.setColor(Color.parseColor(bgColor_sel));
                        }

                        break;
                    case BKACTIVITY_BG_COLOR:
                        String bgColor_bg = validateJsonString(jsonObject, "background");
                        if (bgColor_bg != null && !bgColor_bg.isEmpty()) {
                            color.setColor(Color.parseColor(bgColor_bg));
                        }
                        break;
                    case BKACTIVITY_SURVEY_HEADER_COLOR:
                        String headerBG = validateJsonString(jsonObject, "headerBG");
                        if (headerBG != null && !headerBG.isEmpty()) {
                            color.setColor(Color.parseColor(headerBG));
                        }

                        break;
                    case BKACTIVITY_BOTTOM_COLOR:
                        String bottomBG = validateJsonString(jsonObject, "bottomBG");
                        if (bottomBG != null && !bottomBG.isEmpty()) {
                            color.setColor(Color.parseColor(bottomBG));
                        }
                        break;
                    case BKACTIVITY_PAGINATION_BORDER_COLOR:
                        String pagenationdots_current = validateJsonString(jsonObject, "pagenationdots_current");
                        if (pagenationdots_current != null && !pagenationdots_current.isEmpty()) {
                            color.setColor(Color.parseColor(pagenationdots_current));
                        }
                        break;
                    case BKACTIVITY_PAGINATION_ANSWERED_COLOR:
                        String pagenationdots_answered = validateJsonString(jsonObject, "pagenationdots_answered");
                        if (pagenationdots_answered != null && !pagenationdots_answered.isEmpty()) {
                            color.setColor(Color.parseColor(pagenationdots_answered));
                        }
                        break;
                    case BKACTIVITY_PAGINATION_DEFAULT_COLOR:
                        String pagenationdots_def = validateJsonString(jsonObject, "pagenationdots_def");
                        if (pagenationdots_def != null && !pagenationdots_def.isEmpty()) {
                            color.setColor(Color.parseColor(pagenationdots_def));
                        }
                        break;
                }

            } catch (Exception e) {
                UpshotModule.logException(e);
            }
        }
    }

    @Override
    public void customizeRating(List<Bitmap> selectedRatingList, List<Bitmap> unselectedRatingList,
            BKUIPrefComponents.BKActivityRatingTypes ratingType) {
        super.customizeRating(selectedRatingList, unselectedRatingList, ratingType);

        if (mJsonObject != null) {

            try {
                JSONObject ratingJsonObject = (JSONObject) mJsonObject.get("image");

                switch (ratingType) {
                    case BKACTIVITY_STAR_RATING:
                        Bitmap selected = BitmapFactory.decodeResource(mContext.getResources(),
                                getIdentifier(mContext, getImageName(ratingJsonObject, "star_sel")));
                        Bitmap unselected = BitmapFactory.decodeResource(mContext.getResources(),
                                getIdentifier(mContext, getImageName(ratingJsonObject, "star_def")));

                        if (selected != null && unselected != null) {
                            selectedRatingList.add(selected);
                            unselectedRatingList.add(unselected);
                        }
                        break;

                    case BKACTIVITY_EMOJI_RATING:

                        Bitmap veryBad_def = getBitmap(
                                getIdentifier(mContext, getImageName(ratingJsonObject, "smiley_vbad_def")));
                        Bitmap bad_def = getBitmap(
                                getIdentifier(mContext, getImageName(ratingJsonObject, "smiley_bad_def")));
                        Bitmap avg_def = getBitmap(
                                getIdentifier(mContext, getImageName(ratingJsonObject, "smiley_avg_def")));
                        Bitmap good_def = getBitmap(
                                getIdentifier(mContext, getImageName(ratingJsonObject, "smiley_good_def")));
                        Bitmap vGood_def = getBitmap(
                                getIdentifier(mContext, getImageName(ratingJsonObject, "smiley_Vgood_def")));

                        Bitmap veryBad_sel = getBitmap(
                                getIdentifier(mContext, getImageName(ratingJsonObject, "smiley_vbad_sel")));
                        Bitmap bad_sel = getBitmap(
                                getIdentifier(mContext, getImageName(ratingJsonObject, "smiley_bad_sel")));
                        Bitmap avg_sel = getBitmap(
                                getIdentifier(mContext, getImageName(ratingJsonObject, "smiley_avg_sel")));
                        Bitmap good_sel = getBitmap(
                                getIdentifier(mContext, getImageName(ratingJsonObject, "smiley_good_sel")));
                        Bitmap vGood_sel = getBitmap(
                                getIdentifier(mContext, getImageName(ratingJsonObject, "smiley_Vgood_sel")));

                        if (veryBad_sel != null && veryBad_def != null &&
                                bad_def != null && bad_sel != null &&
                                avg_sel != null && avg_def != null &&
                                good_sel != null && good_def != null &&
                                vGood_sel != null && vGood_def != null) {

                            unselectedRatingList.add(veryBad_def);
                            unselectedRatingList.add(bad_def);
                            unselectedRatingList.add(avg_def);
                            unselectedRatingList.add(good_def);
                            unselectedRatingList.add(vGood_def);

                            selectedRatingList.add(veryBad_sel);
                            selectedRatingList.add(bad_sel);
                            selectedRatingList.add(avg_sel);
                            selectedRatingList.add(good_sel);
                            selectedRatingList.add(vGood_sel);
                        }
                }
            } catch (Exception e) {
                UpshotModule.logException(e);
            }
        }
    }

    private Bitmap getBitmap(int id) {

        return BitmapFactory.decodeResource(mContext.getResources(), id);
    }

    @Override
    public void customizeTextView(BKActivityTextViewTypes textViewType, TextView textView) {
        super.customizeTextView(textViewType, textView);

        if (mJsonObject != null) {

            try {
                JSONObject label_textJsonObject = (JSONObject) mJsonObject.get("label_text");
                JSONObject sliderJsonObject = (JSONObject) mJsonObject.get("slider");
                switch (textViewType) {
                    case BKACTIVITY_HEADER_TV:
                        JSONObject header = (JSONObject) label_textJsonObject.get("header");
                        applyTextViewProperties(mContext, header, textView);
                        break;
                    case BKACTIVITY_SURVEY_DESC_TV:
                        JSONObject desc = (JSONObject) label_textJsonObject.get("desc");
                        applyTextViewProperties(mContext, desc, textView);
                        break;
                    case BKACTIVITY_SLIDE_TEXT_TV:
                        JSONObject slider_score = (JSONObject) sliderJsonObject.get("slider_score");
                        applyTextViewProperties(mContext, slider_score, textView);
                        break;
                    case BKACTIVITY_SLIDE_MAX_TV:
                        JSONObject slider_maxScore = (JSONObject) sliderJsonObject.get("slider_maxScore");
                        applyTextViewProperties(mContext, slider_maxScore, textView);
                        break;
                    case BKACTIVITY_SLIDE_MIN_TV:
                        JSONObject slider_minScore = (JSONObject) sliderJsonObject.get("slider_minScore");
                        applyTextViewProperties(mContext, slider_minScore, textView);
                        break;
                    case BKACTIVITY_SLIDE_MAX_LABEL_TV:
                        JSONObject slider_maxText = (JSONObject) sliderJsonObject.get("slider_maxText");
                        applyTextViewProperties(mContext, slider_maxText, textView);
                        break;
                    case BKACTIVITY_SLIDE_MIN_LABEL_TV:
                        JSONObject slider_minText = (JSONObject) sliderJsonObject.get("slider_minText");
                        applyTextViewProperties(mContext, slider_minText, textView);
                        break;
                    case BKACTIVITY_QUESTION_TV:
                        JSONObject question = (JSONObject) label_textJsonObject.get("question");
                        applyTextViewProperties(mContext, question, textView);
                        break;
                    case BKACTIVITY_THANK_YOU_TV:

                        JSONObject thanksJsonObject = (JSONObject) label_textJsonObject.get("thankyou");
                        applyTextViewProperties(mContext, thanksJsonObject, textView);

                        JSONObject jImageBg = (JSONObject) mJsonObject.get("image");
                        String bgData = getImageName(jImageBg, "background");

                        if (!bgData.isEmpty()) {
                            Resources resources = mContext.getResources();
                            int resourceId = resources.getIdentifier(bgData, "drawable", mContext.getPackageName());
                            textView.setBackgroundResource(resourceId);
                        } else {
                            JSONObject colorJsonObject = (JSONObject) mJsonObject.get("color");
                            if (colorJsonObject != null) {
                                String bgColor = validateJsonString(colorJsonObject, "background");
                                if (bgColor != null && !bgColor.isEmpty()) {
                                    textView.setBackgroundColor(Color.parseColor(bgColor));
                                }
                            }
                        }
                        break;
                    case BKACTIVITY_OPTION_TV:
                        JSONObject option = (JSONObject) label_textJsonObject.get("option");
                        applyTextViewProperties(mContext, option, textView);
                        break;
                }

            } catch (Exception e) {
                UpshotModule.logException(e);
            }
        }
    }

    public void customizeImageView(ImageView imageView, BKUIPrefComponents.BKActivityImageViewType imageType) {
        super.customizeImageView(imageView, imageType);

        if (mJsonObject != null) {
            try {
                JSONObject jImageBg = (JSONObject) mJsonObject.get("image");

                switch (imageType) {
                    case BKACTIVITY_PORTRAIT_LOGO:
                    case BKACTIVITY_LANDSCAPE_LOGO:
                        String bgData = getImageName(jImageBg, "logo");
                        applyImageProperties(mContext, bgData, imageView);
                        break;
                }
            } catch (Exception e) {
                UpshotModule.logException(e);
            }
        }
    }

    @Override
    public void customizeRelativeLayout(BKUIPrefComponents.BKActivityRelativeLayoutTypes relativeLayoutTypes,
            RelativeLayout relativeLayout, boolean isFullScreen) {
        super.customizeRelativeLayout(relativeLayoutTypes, relativeLayout, isFullScreen);

        if (mJsonObject != null) {

            try {
                JSONObject jImageBg = (JSONObject) mJsonObject.get("image");
                JSONObject colorJsonObject = (JSONObject) mJsonObject.get("color");
                if (colorJsonObject != null) {
                    String bgColor = validateJsonString(colorJsonObject, "background");
                    if (bgColor != null && !bgColor.isEmpty()) {
                        relativeLayout.setBackgroundColor(Color.parseColor(bgColor));
                    }
                }
                switch (relativeLayoutTypes) {
                    case BKACTIVITY_BACKGROUND_IMAGE:
                        String bgData = getImageName(jImageBg, "background");
                        if (isFullScreen) {
                            applyRelativeLayoutProperties(mContext, bgData, relativeLayout);
                        } else {
                            applyBGImageForPopup(mContext, bgData, relativeLayout);
                        }
                        break;
                }
            } catch (Exception e) {
                UpshotModule.logException(e);
            }
        }
    }

    @Override
    public void customizeImageButton(ImageButton button, BKUIPrefComponents.BKActivityImageButtonTypes buttonType) {
        super.customizeImageButton(button, buttonType);

        if (mJsonObject != null) {

            try {
                JSONObject buttonJsonObject = (JSONObject) mJsonObject.get("button");
                switch (buttonType) {
                    case BKACTIVITY_SKIP_BUTTON:
                        JSONObject submitButtonJsonObject = (JSONObject) buttonJsonObject.get("skip");
                        applyImageButtonProperties(mContext, submitButtonJsonObject, button);
                        break;
                }
            } catch (Exception e) {
                UpshotModule.logException(e);
            }
        }
    }
}
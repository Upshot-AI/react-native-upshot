package com.upshotreactlibrary.upshot.customization;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brandkinesis.BKUIPrefComponents;
import com.brandkinesis.BKUIPrefComponents.BKActivityButtonTypes;
import com.brandkinesis.BKUIPrefComponents.BKActivityTextViewTypes;
import com.upshotreactlibrary.UpshotModule;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class UpshotOpinionPollCustomization extends UpshotCustomization {

    private Context mContext;
    private JSONObject mJsonObject = null;

    public UpshotOpinionPollCustomization(Context context) {
        mContext = context;
        try {
            mJsonObject = new JSONObject(loadJSONFromAsset(context, "UpshotPollTheme.json"));
        } catch (Exception e) {
            UpshotModule.logException(e);
        }
    }

    public void customizeRadioButton(BKUIPrefComponents.BKUICheckBox checkBox, boolean isCheckBox) {
        super.customizeRadioButton(checkBox, isCheckBox);
        if (mJsonObject != null) {
            try {
                JSONObject imageJsonObject = (JSONObject) mJsonObject.get("image");
                Bitmap check_select, default_select;

                check_select = BitmapFactory.decodeResource(mContext.getResources(),
                        getIdentifier(mContext, getImageName(imageJsonObject, "radio_sel")));
                checkBox.setSelectedCheckBox(check_select);

                default_select = BitmapFactory.decodeResource(mContext.getResources(),
                        getIdentifier(mContext, getImageName(imageJsonObject, "radio_def")));
                checkBox.setUnselectedCheckBox(default_select);
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
                }

            } catch (Exception e) {
                UpshotModule.logException(e);
            }
        }
    }

    @Override
    public void customizeTextView(BKActivityTextViewTypes textViewType, TextView textView) {
        super.customizeTextView(textViewType, textView);

        if (mJsonObject != null) {
            try {
                JSONObject label_textJsonObject = (JSONObject) mJsonObject.get("label_text");
                JSONObject graphJsonObject = (JSONObject) mJsonObject.get("graph");
                switch (textViewType) {
                    case BKACTIVITY_QUESTION_TV:
                        JSONObject question = (JSONObject) label_textJsonObject.get("question");
                        applyTextViewProperties(mContext, question, textView);
                        break;

                    case BKACTIVITY_THANK_YOU_TV:

                        JSONObject thanksJsonObject = (JSONObject) label_textJsonObject.get("thankyou");
                        applyTextViewProperties(mContext, thanksJsonObject, textView);

                        JSONObject jImageBg = (JSONObject) mJsonObject.get("image");
                        String bgData = getImageName(jImageBg, "background");

                        if (bgData != null && !bgData.isEmpty()) {
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
                    case BKACTIVITY_QUESTION_OPTION_TV:
                        JSONObject option = (JSONObject) label_textJsonObject.get("option");
                        applyTextViewProperties(mContext, option, textView);
                        break;
                    case BKACTIVITY_LEGEND_TV:

                        String legendsColor = graphJsonObject.getString("legends");
                        if (legendsColor != null && !legendsColor.isEmpty()) {
                            textView.setTextColor(Color.parseColor(legendsColor));
                        }

                        break;
                    case BKACTIVITY_LEADER_BOARD_BAR_RESPONSES_TV:
                        String yAxis_HeaderColor = graphJsonObject.getString("yAxis_Header");
                        if (yAxis_HeaderColor != null && !yAxis_HeaderColor.isEmpty()) {
                            textView.setTextColor(Color.parseColor(yAxis_HeaderColor));
                        }

                        break;
                    case BKACTIVITY_LEADER_BOARD_BAR_GRADES_TV:
                        String xAxis_HeaderColor = graphJsonObject.getString("xAxis_Header");
                        if (xAxis_HeaderColor != null && !xAxis_HeaderColor.isEmpty()) {
                            textView.setTextColor(Color.parseColor(xAxis_HeaderColor));
                        }
                        break;
                }

            } catch (Exception e) {
                UpshotModule.logException(e);
            }
        }
    }

    @Override
    public void customizeBGColor(BKUIPrefComponents.BKBGColors color,
            BKUIPrefComponents.BKActivityColorTypes colorType) {
        super.customizeBGColor(color, colorType);

        if (mJsonObject != null) {
            try {
                JSONObject jsonObject = (JSONObject) mJsonObject.get("color");
                JSONObject graphJsonObject = (JSONObject) mJsonObject.get("graph");
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
                        color.setColor(Color.parseColor(bgColor_bg));
                        break;
                    case BKACTIVITY_SURVEY_HEADER_COLOR:
                        String headerBG = validateJsonString(jsonObject, "headerBG");
                        color.setColor(Color.parseColor(headerBG));
                        break;

                    case BKACTIVITY_YAXIS_TEXT_COLOR_COLOR:
                    case BKACTIVITY_XAXIS_TEXT_COLOR_COLOR: {
                        String percentageText = validateJsonString(graphJsonObject, "yAxis");
                        if (percentageText != null && !percentageText.isEmpty()) {
                            color.setColor(Color.parseColor(percentageText));
                        }
                    }
                        break;
                    case BKACTIVITY_YAXIS_COLOR:
                    case BKACTIVITY_XAXIS_COLOR:
                        String barGraphLine = validateJsonString(graphJsonObject, "bar_line");
                        if (barGraphLine != null && !barGraphLine.isEmpty()) {
                            color.setColor(Color.parseColor(barGraphLine));
                        }
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
    public void customizeForGraphColor(BKUIPrefComponents.BKGraphType graphType, List<Integer> colorsList) {
        super.customizeForGraphColor(graphType, colorsList);

        if (mJsonObject != null) {
            try {
                JSONObject buttonJsonObject = (JSONObject) mJsonObject.get("graph");
                switch (graphType) {
                    case BKACTIVITY_BAR_GRAPH:
                        colorsList.clear();

                        JSONArray jsonArray = buttonJsonObject.getJSONArray("barcolors");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            colorsList.add(Color.parseColor(jsonArray.getString(i)));
                        }
                        break;
                    case BKACTIVITY_PIE_GRAPH:
                        colorsList.clear();
                        JSONArray piecolors = buttonJsonObject.getJSONArray("piecolors");
                        for (int i = 0; i < piecolors.length(); i++) {
                            colorsList.add(Color.parseColor(piecolors.getString(i)));
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
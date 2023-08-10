package com.upshotreactlibrary.upshot.customization;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brandkinesis.BKUIPrefComponents;
import com.brandkinesis.BKUIPrefComponents.BKActivityButtonTypes;
import com.brandkinesis.BKUIPrefComponents.BKActivityColorTypes;
import com.brandkinesis.BKUIPrefComponents.BKActivityTextViewTypes;
import com.brandkinesis.BKUIPrefComponents.BKBGColors;

import java.util.List;

import static com.brandkinesis.BKUIPrefComponents.BKUICheckBox;

import org.json.JSONArray;
import org.json.JSONObject;

public class UpshotTriviaCustomization extends UpshotCustomization {
    private Context mContext;
    private JSONObject mJsonObject = null;

    public UpshotTriviaCustomization(Context context) {
        mContext = context;
        try {
            mJsonObject = new JSONObject(loadJSONFromAsset(context, "UpshotTriviaTheme.json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void customizeRadioButton(BKUICheckBox checkBox, boolean isCheckBox) {

        if (mJsonObject != null) {

            try {
                JSONObject imageJsonObject = (JSONObject) mJsonObject.get("image");
                Bitmap check_select, default_select;
                if (isCheckBox) {
                    check_select = BitmapFactory.decodeResource(mContext.getResources(),
                            getIdentifier(mContext, validateJsonString(imageJsonObject, "checkbox_sel")));
                    if (check_select != null) {
                        checkBox.setSelectedCheckBox(check_select);
                    }
                    default_select = BitmapFactory.decodeResource(mContext.getResources(),
                            getIdentifier(mContext, validateJsonString(imageJsonObject, "checkbox_def")));
                    if (default_select != null) {
                        checkBox.setUnselectedCheckBox(default_select);
                    }
                } else {
                    check_select = BitmapFactory.decodeResource(mContext.getResources(),
                            getIdentifier(mContext, validateJsonString(imageJsonObject, "radio_sel")));
                    default_select = BitmapFactory.decodeResource(mContext.getResources(),
                            getIdentifier(mContext, validateJsonString(imageJsonObject, "radio_def")));
                    if (check_select != null) {
                        checkBox.setSelectedCheckBox(check_select);
                    }
                    if (default_select != null) {
                        checkBox.setUnselectedCheckBox(default_select);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** customizeButton is used to customize Buttons */
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
                    case BKACTIVITY_TRIVIA_CONTINUE_BUTTON:
                        JSONObject continueButtonJsonObject = (JSONObject) buttonJsonObject.get("continue");
                        applyButtonProperties(mContext, continueButtonJsonObject, button);
                        break;
                    case BKACTIVITY_TRIVIA_NEXT_BUTTON:
                        JSONObject nextButtonJsonObject = (JSONObject) buttonJsonObject.get("next");
                        applyButtonProperties(mContext, nextButtonJsonObject, button);
                        break;
                    case BKACTIVITY_TRIVIA_PREVIOUS_BUTTON:
                        JSONObject prevButtonJsonObject = (JSONObject) buttonJsonObject.get("prev");
                        applyButtonProperties(mContext, prevButtonJsonObject, button);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * customizeRelativeLayout is used to customize Background Color and BG Image
     */
    @Override
    public void customizeRelativeLayout(BKUIPrefComponents.BKActivityRelativeLayoutTypes relativeLayoutTypes,
            RelativeLayout relativeLayout, boolean isFullScreen) {
        super.customizeRelativeLayout(relativeLayoutTypes, relativeLayout, isFullScreen);

        if (mJsonObject != null) {
            try {
                JSONObject colorJsonObject = (JSONObject) mJsonObject.get("color");
                if (colorJsonObject != null) {
                    String bgColor = validateJsonString(colorJsonObject, "background");
                    if (bgColor != null && !bgColor.isEmpty()) {
                        relativeLayout.setBackgroundColor(Color.parseColor(bgColor));
                    }
                }
                switch (relativeLayoutTypes) {
                    case BKACTIVITY_BACKGROUND_IMAGE:
                        JSONObject jImageBg = (JSONObject) mJsonObject.get("image");
                        String bgData = validateJsonString(jImageBg, "background");
                        applyRelativeLayoutProperties(mContext, bgData, relativeLayout);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
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
                e.printStackTrace();
            }
        }
    }

    @Override
    public void customizeForLinearLayout(LinearLayout linearLayout,
            BKUIPrefComponents.BKActivityLinearLayoutTypes linearLayoutTypes) {
        super.customizeForLinearLayout(linearLayout, linearLayoutTypes);
        switch (linearLayoutTypes) {
            case BKACTIVITY_BACKGROUND_IMAGE:
                linearLayout.setBackgroundColor(Color.TRANSPARENT);
                break;
        }
    }

    @Override
    public void customizeForOptionsSeparatorView(View view) {
        super.customizeForOptionsSeparatorView(view);
        view.setBackgroundColor(Color.RED);
    }

    @Override
    public void customizeBGColor(BKBGColors color, BKActivityColorTypes colorType) {
        super.customizeBGColor(color, colorType);

        if (mJsonObject != null) {
            try {
                JSONObject jsonObject = (JSONObject) mJsonObject.get("color");
                JSONObject graphJson = (JSONObject) mJsonObject.get("graph");

                switch (colorType) {
                    case BKACTIVITY_TRIVIA_GRADE_COLOR:
                        JSONObject label_textJsonObject = (JSONObject) mJsonObject.get("label_text");
                        JSONObject header = (JSONObject) label_textJsonObject.get("tabular_response");

                        String optionColor = validateJsonString(header, "color");
                        if (optionColor != null && !optionColor.isEmpty()) {
                            color.setColor(Color.parseColor(optionColor));
                        }
                        break;

                    case BKACTIVITY_OPTION_DEF_BORDER:
                        String option_borderColor = validateJsonString(jsonObject, "option_def_border");
                        if (option_borderColor != null && !option_borderColor.isEmpty()) {
                            color.setColor(Color.parseColor(option_borderColor));
                        }
                        break;

                    case BKACTIVITY_BG_COLOR:
                        String bgColor = validateJsonString(jsonObject, "background");
                        if (bgColor != null && !bgColor.isEmpty()) {
                            color.setColor(Color.parseColor(bgColor));
                        }
                        break;

                    case BKACTIVITY_TRIVIA_HEADER_COLOR:
                    case BKACTIVITY_TRIVIA_TITLE_COLOR:
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

                    case BKACTIVITY_YAXIS_TEXT_COLOR_COLOR:
                    case BKACTIVITY_XAXIS_TEXT_COLOR_COLOR: {
                        String percentageText = validateJsonString(graphJson, "yAxis");
                        if (percentageText != null && !percentageText.isEmpty()) {
                            color.setColor(Color.parseColor(percentageText));
                        }
                    }
                        break;
                    case BKACTIVITY_YAXIS_COLOR:
                    case BKACTIVITY_XAXIS_COLOR:
                        String barGraphLine = validateJsonString(graphJson, "bar_line");
                        if (barGraphLine != null && !barGraphLine.isEmpty()) {
                            color.setColor(Color.parseColor(barGraphLine));
                        }
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void customizeTextView(BKActivityTextViewTypes textViewType, TextView textView) {
        super.customizeTextView(textViewType, textView);

        if (mJsonObject != null) {

            try {
                JSONObject label_textJsonObject = (JSONObject) mJsonObject.get("label_text");
                JSONObject graphJson = (JSONObject) mJsonObject.get("graph");
                JSONObject leaderBoardJson = (JSONObject) mJsonObject.get("leaderBoard");

                switch (textViewType) {
                    case BKACTIVITY_HEADER_TV:
                        JSONObject header = (JSONObject) label_textJsonObject.get("header");
                        applyTextViewProperties(mContext, header, textView);
                        break;
                    case BKACTIVITY_TRIVIA_DESC_TV:
                        JSONObject desc = (JSONObject) label_textJsonObject.get("desc");
                        applyTextViewProperties(mContext, desc, textView);
                        break;
                    case BKACTIVITY_QUESTION_TV:
                        JSONObject question = (JSONObject) label_textJsonObject.get("question");
                        applyTextViewProperties(mContext, question, textView);
                        break;
                    case BKACTIVITY_THANK_YOU_TV:
                        JSONObject thanksJsonObject = (JSONObject) label_textJsonObject.get("thankyou");
                        applyTextViewProperties(mContext, thanksJsonObject, textView);
                        JSONObject colorJsonObject = (JSONObject) mJsonObject.get("color");
                        if (colorJsonObject != null) {
                            String bgColor = validateJsonString(colorJsonObject, "background");
                            if (bgColor != null && !bgColor.isEmpty()) {
                                textView.setBackgroundColor(Color.parseColor(bgColor));
                            }
                        }
                        break;
                    case BKACTIVITY_QUESTION_OPTION_TV:
                    case BKACTIVITY_OPTION_TV:
                        JSONObject option = (JSONObject) label_textJsonObject.get("option");
                        applyTextViewProperties(mContext, option, textView);
                        break;

                    case BKACTIVITY_LEADER_BOARD_SCORE_TV:
                        JSONObject option_userScore = (JSONObject) leaderBoardJson.get("yourScore");
                        applyTextViewProperties(mContext, option_userScore, textView);
                        break;

                    case BKACTIVITY_LEADER_BOARD_GRADE_TV: {
                        JSONObject option_userGrade = (JSONObject) leaderBoardJson.get("yourGrade");
                        applyTextViewProperties(mContext, option_userGrade, textView);
                    }
                        break;
                    case BKACTIVITY_SCORE_TV:
                        JSONObject option_score = (JSONObject) label_textJsonObject.get("score");
                        applyTextViewProperties(mContext, option_score, textView);
                        break;
                    case BKACTIVITY_LEADER_BOARD_GRADE_VALUE_TV:
                        JSONObject option_yourGrade = (JSONObject) leaderBoardJson.get("userGrade");
                        applyTextViewProperties(mContext, option_yourGrade, textView);
                        break;
                    case BKACTIVITY_LEADER_BOARD_SCORE_VALUE_TV: {
                        JSONObject option_result = (JSONObject) leaderBoardJson.get("userScore");
                        applyTextViewProperties(mContext, option_result, textView);
                    }
                        break;
                    case BKACTIVITY_LEADER_BOARD_TITLE_TV:
                        JSONObject option_result = (JSONObject) leaderBoardJson.get("result");
                        applyTextViewProperties(mContext, option_result, textView);
                        break;
                    case BKACTIVITY_TRIVIA_GRADE_HEADER_TABLE_TV: {
                        JSONObject tabularResponseHeader = (JSONObject) leaderBoardJson
                                .get("tabular_grade_header");
                        applyTextViewProperties(mContext, tabularResponseHeader, textView);
                    }
                        break;
                    case BKACTIVITY_TRIVIA_RESPONSE_HEADER_TABLE_TV:
                        JSONObject tabularResponseHeader = (JSONObject) leaderBoardJson
                                .get("tabular_response_header");
                        applyTextViewProperties(mContext, tabularResponseHeader, textView);
                        break;
                    case BKACTIVITY_LEADER_BOARD_BAR_RESPONSES_TV: {
                        String yAxis_HeaderColor = graphJson.getString("yAxis_Header");
                        if (!yAxis_HeaderColor.isEmpty()) {
                            textView.setTextColor(Color.parseColor(yAxis_HeaderColor));
                        }
                    }
                        break;
                    case BKACTIVITY_LEADER_BOARD_BAR_GRADES_TV: {
                        String xAxis_HeaderColor = graphJson.getString("xAxis_Header");
                        if (!xAxis_HeaderColor.isEmpty()) {
                            textView.setTextColor(Color.parseColor(xAxis_HeaderColor));
                        }
                    }
                        break;
                    case BKACTIVITY_LEGEND_TV:
                        String legendColor = graphJson.getString("legends");
                        if (!legendColor.isEmpty()) {
                            textView.setTextColor(Color.parseColor(legendColor));
                        }
                        break;
                    case BKACTIVITY_TRIVIA_GRADE_DATA_TABLE_TV:
                        JSONObject tabular_grade_header = (JSONObject) leaderBoardJson.get("tabular_grade_header");
                        applyTextViewProperties(mContext, tabular_grade_header, textView);
                        break;
                    case BKACTIVITY_TRIVIA_RESPONSE_DATA_TABLE_TV: {
                        JSONObject tabular_response_header = (JSONObject) leaderBoardJson
                                .get("tabular_response_header");
                        applyTextViewProperties(mContext, tabular_response_header, textView);
                    }
                        break;

                }

            } catch (Exception e) {
                e.printStackTrace();
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
                        JSONArray jsonArray = buttonJsonObject.getJSONArray("bar");
                        if (jsonArray.length() == 5) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                colorsList.add(Color.parseColor(jsonArray.getString(i)));
                            }
                        }
                        break;
                    case BKACTIVITY_PIE_GRAPH:
                        colorsList.clear();
                        JSONArray piecolors = buttonJsonObject.getJSONArray("pie");
                        if (piecolors.length() == 5) {
                            for (int i = 0; i < piecolors.length(); i++) {
                                colorsList.add(Color.parseColor(piecolors.getString(i)));
                            }
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** customizeImageView is used to customize Logo */
    @Override
    public void customizeImageView(ImageView imageView, BKUIPrefComponents.BKActivityImageViewType imageType) {
        super.customizeImageView(imageView, imageType);

        switch (imageType) {
            case BKACTIVITY_PORTRAIT_LOGO:
                if (mJsonObject != null) {
                    {
                        try {
                            JSONObject imageJsonObject = (JSONObject) mJsonObject.get("image");
                            applyImageProperties(mContext, validateJsonString(imageJsonObject, "logo"), imageView);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        }
    }
}

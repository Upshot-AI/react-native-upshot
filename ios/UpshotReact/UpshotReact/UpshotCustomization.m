//
//  UpshotCustomization.m
//  Upshot-Objc
//
//  Created by Vinod on 15/07/19.
//  Copyright © 2019 Upshot. All rights reserved.
//

#import "UpshotCustomization.h"
#import "UpshotUtility.h"

#define kUpshotSurveyTheme @"UpshotSurveyTheme"
#define kUpshotRatingTheme @"UpshotRatingTheme"
#define kUpshotPollTheme @"UpshotPollTheme"
#define kUpshotTriviaTheme @"UpshotTriviaTheme"

@implementation UpshotCustomization

/*This method is used to customize the images based on Activity Type
 RadioImage
 CheckBox Image
 BackgroundImage
 Logo
 */

- (void)preferencesForUIImageView:(UIImageView *)imageView ofActivity:(BKActivityType)activityType andType:(BKActivityImageType)activityImage {
    
    switch (activityType) {
        case BKActivityTypeSurvey: {
            
            NSDictionary *s_imageData = [UpshotUtility getObjectForKey:@"image" withFileName:kUpshotSurveyTheme];
            
            if (s_imageData != nil && [s_imageData isKindOfClass:[NSDictionary class]]) {
                
                UIImage *bgImage = [UIImage imageNamed:[UpshotUtility validateString:s_imageData[@"background"]]];
                UIImage *logo = [UIImage imageNamed:[UpshotUtility validateString:s_imageData[@"logo"]]];
                
                UIImage *radio_def = [UIImage imageNamed:[UpshotUtility validateString:s_imageData[@"radio_def"]]];
                UIImage *radio_sel = [UIImage imageNamed:[UpshotUtility validateString:s_imageData[@"radio_sel"]]];
                
                UIImage *checkbox_def = [UIImage imageNamed:[UpshotUtility validateString:s_imageData[@"checkbox_def"]]];
                UIImage *checkbox_sel = [UIImage imageNamed:[UpshotUtility validateString:s_imageData[@"checkbox_sel"]]];
                
                switch (activityImage) {
                    case BKActivityBackgroundImage:
                        if (bgImage != nil) {
                            [imageView setImage:bgImage];
                        }
                        break;
                    case BKActivityRadioImage:
                        if (radio_def != nil) {
                            [imageView setImage:radio_def];
                        }
                        if (radio_sel != nil) {
                            [imageView setHighlightedImage:radio_sel];
                        }
                        break;
                    case BKActivityCheckboxImage:
                        
                        if (checkbox_def != nil) {
                            [imageView setImage:checkbox_def];
                        }
                        if (checkbox_sel != nil) {
                            [imageView setHighlightedImage:checkbox_sel];
                        }
                        break;
                    case BKActivityPortraitLogo:
                    case BKActivityLandscapeLogo:
                        
                        if (logo != nil) {
                            [imageView setImage:logo];
                        }
                        break;
                    default:
                        break;
                }
            }
        }
            break;
        case BKActivityTypeRating: {
            
            NSDictionary *r_imageData = [UpshotUtility getObjectForKey:@"image" withFileName:kUpshotRatingTheme];
            
            if (r_imageData != nil && [r_imageData isKindOfClass:[NSDictionary class]]) {
                
                UIImage *bgImage = [UIImage imageNamed:[UpshotUtility validateString:r_imageData[@"background"]]];
                UIImage *logo = [UIImage imageNamed:[UpshotUtility validateString:r_imageData[@"logo"]]];
                
                switch (activityImage) {
                    case BKActivityBackgroundImage:
                        if (bgImage != nil) {
                            [imageView setImage:bgImage];
                        }
                        break;
                    case BKActivityPortraitLogo:
                    case BKActivityLandscapeLogo:
                        if (logo != nil) {
                            [imageView setImage:logo];
                        }
                        break;
                    default:
                        break;
                }
            }
        }
            break;
        case BKActivityTypeOpinionPoll: {
            
            NSDictionary *p_imageData = [UpshotUtility getObjectForKey:@"image" withFileName:kUpshotPollTheme];
            
            if (p_imageData != nil && [p_imageData isKindOfClass:[NSDictionary class]]) {
                
                UIImage *bgImage = [UIImage imageNamed:[UpshotUtility validateString:p_imageData[@"background"]]];
                UIImage *logo = [UIImage imageNamed:[UpshotUtility validateString:p_imageData[@"logo"]]];
                
                UIImage *radio_def = [UIImage imageNamed:[UpshotUtility validateString:p_imageData[@"radio_def"]]];
                UIImage *radio_sel = [UIImage imageNamed:[UpshotUtility validateString:p_imageData[@"radio_sel"]]];
                
                switch (activityImage) {
                    case BKActivityBackgroundImage:
                        if (bgImage != nil) {
                            [imageView setImage:bgImage];
                        }
                        break;
                    case BKActivityRadioImage:
                        if (radio_def != nil) {
                            [imageView setImage:radio_def];
                        }
                        if (radio_sel != nil) {
                            [imageView setHighlightedImage:radio_sel];
                        }
                        break;
                        
                    case BKActivityPortraitLogo:
                    case BKActivityLandscapeLogo:
                        if (logo != nil) {
                            [imageView setImage:logo];
                        }
                        break;
                    default:
                        break;
                }
            }
        }
            break;
        case BKActivityTypeTrivia: {
            
            NSDictionary *t_imageData = [UpshotUtility getObjectForKey:@"image" withFileName:kUpshotTriviaTheme];
            
            if (t_imageData != nil && [t_imageData isKindOfClass:[NSDictionary class]]) {
                
                UIImage *bgImage = [UIImage imageNamed:[UpshotUtility validateString:t_imageData[@"background"]]];
                UIImage *logo = [UIImage imageNamed:[UpshotUtility validateString:t_imageData[@"logo"]]];
                
                UIImage *radio_def = [UIImage imageNamed:[UpshotUtility validateString:t_imageData[@"radio_def"]]];
                UIImage *radio_sel = [UIImage imageNamed:[UpshotUtility validateString:t_imageData[@"radio_sel"]]];
                
                UIImage *checkbox_def = [UIImage imageNamed:[UpshotUtility validateString:t_imageData[@"checkbox_def"]]];
                UIImage *checkbox_sel = [UIImage imageNamed:[UpshotUtility validateString:t_imageData[@"checkbox_sel"]]];
                
                switch (activityImage) {
                    case BKActivityBackgroundImage:
                        if (bgImage != nil) {
                            [imageView setImage:bgImage];
                        }
                        break;
                    case BKActivityRadioImage:
                        if (radio_def != nil) {
                            [imageView setImage:radio_def];
                        }
                        if (radio_sel != nil) {
                            [imageView setHighlightedImage:radio_sel];
                        }
                        break;
                    case BKActivityCheckboxImage:
                        if (checkbox_def != nil) {
                            [imageView setImage:checkbox_def];
                        }
                        if (checkbox_sel != nil) {
                            [imageView setHighlightedImage:checkbox_sel];
                        }
                        break;
                    case BKActivityPortraitLogo:
                    case BKActivityLandscapeLogo:
                        if (logo != nil) {
                            [imageView setImage:logo];
                        }
                        break;
                    default:
                        break;
                }
            }
        }
            break;
        default:
            break;
    }
}

/* This method is used to customize the Slider based on Activity Type */

- (void)preferencesForSlider:(UISlider *)slider ofActivity:(BKActivityType)activityType {

    NSDictionary *sliderData = [UpshotUtility getObjectForKey:@"slider" withFileName:kUpshotSurveyTheme];
    
    if (activityType == BKActivityTypeRating) {
        sliderData = [UpshotUtility getObjectForKey:@"slider" withFileName:kUpshotRatingTheme];
    }
    
    if (sliderData != nil && [sliderData isKindOfClass:[NSDictionary class]]) {
        
        UIImage *minTrackImage = [[UIImage imageNamed:[UpshotUtility validateString:sliderData[@"min_image"]]] resizableImageWithCapInsets:UIEdgeInsetsMake(0, 4, 0, 0) resizingMode:UIImageResizingModeStretch];

        UIImage *maxTrackImage = [[UIImage imageNamed:[UpshotUtility validateString:sliderData[@"max_image"]]] resizableImageWithCapInsets:UIEdgeInsetsMake(0, 0, 0, 5) resizingMode:UIImageResizingModeStretch];

        UIImage *thumbImage = [UIImage imageNamed:[UpshotUtility validateString:sliderData[@"thumb_image"]]];

        if (thumbImage != nil) {
            [slider setThumbImage:thumbImage forState:UIControlStateNormal];
        }
        if (minTrackImage != nil) {
            [slider setMinimumTrackImage:minTrackImage forState:UIControlStateNormal];
        }
        if (maxTrackImage != nil) {
            [slider setMaximumTrackImage:maxTrackImage forState:UIControlStateNormal];
        }
    }
}

/*This method is used to customize the graph colors based on graph type
 BarGraph,
 PieGraph
 */

- (void)preferencesForGraphColor:(BKGraphType)graphType graphColors:(void (^)(NSArray *))block {
    
    NSDictionary *pollColorData = [UpshotUtility getObjectForKey:@"graph" withFileName:kUpshotPollTheme];
    NSDictionary *triviaColorData = [UpshotUtility getObjectForKey:@"graph" withFileName:kUpshotTriviaTheme];
    
    BOOL colorsAssigned = NO;
    
    if (pollColorData != nil && [pollColorData isKindOfClass:[NSDictionary class]]) {
        NSArray *poll_barColors = pollColorData[@"barcolors"];
        NSArray *poll_pieColors = pollColorData[@"piecolors"];
        
        if (graphType == BKActivityBarGraph && poll_barColors != nil && poll_barColors.count > 4) {
            
            NSMutableArray *barColors = [[NSMutableArray alloc] init];
            for (NSString *colorStr in poll_barColors) {
                UIColor *color = [UpshotUtility colorFromHex:colorStr];
                if (color != nil) {
                    [barColors addObject:color];
                }
            }
            if (barColors.count > 4) {
                colorsAssigned = YES;
                block(barColors);
            }
        } else {
            if (poll_pieColors != nil && poll_pieColors.count > 4) {
                
                NSMutableArray *pieColors = [[NSMutableArray alloc] init];
                for (NSString *colorStr in poll_pieColors) {
                    UIColor *color = [UpshotUtility colorFromHex:colorStr];
                    if (color != nil) {
                        [pieColors addObject:color];
                    }
                }
                if (pieColors.count > 4) {
                    colorsAssigned = YES;
                    block(pieColors);
                }
            }
        }
    }
    
    if (!colorsAssigned) {
        if (triviaColorData != nil && [triviaColorData isKindOfClass:[NSDictionary class]]) {
            
            NSArray *trivia_barColors = triviaColorData[@"barcolors"];
            NSArray *trivia_pieColors = triviaColorData[@"piecolors"];
            
            if (graphType == BKActivityBarGraph && trivia_barColors != nil && trivia_barColors.count > 4) {
                
                NSMutableArray *barColors = [[NSMutableArray alloc] init];
                for (NSString *colorStr in trivia_barColors) {
                    UIColor *color = [UpshotUtility colorFromHex:colorStr];
                    if (color != nil) {
                        [barColors addObject:color];
                    }
                }
                if (barColors.count > 4) {
                    block(barColors);
                }
            } else {
                if (trivia_pieColors != nil && trivia_pieColors.count > 4) {
                    
                    NSMutableArray *pieColors = [[NSMutableArray alloc] init];
                    for (NSString *colorStr in trivia_pieColors) {
                        UIColor *color = [UpshotUtility colorFromHex:colorStr];
                        if (color != nil) {
                            [pieColors addObject:color];
                        }
                    }
                    if (pieColors.count > 4) {
                        block(pieColors);
                    }
                }
            }
        }
    }
}

/* This method is used to customize label text color, font style and font size based on activity type*/

- (void)preferencesForUILabel:(UILabel *)label ofActivity:(BKActivityType)activityType andType:(BKActivityLabelType)activityLabel {
    
    switch (activityType) {
        case BKActivityTypeSurvey: {
            
            NSDictionary *s_labelData = [UpshotUtility getObjectForKey:@"label_text" withFileName:kUpshotSurveyTheme];
            
            switch (activityLabel) {
                case BKActivityHeaderLabel:
                    [UpshotUtility customizeLabel:label withData:s_labelData[@"header"]];
                    break;
                case BKActivityDescriptionLabel:
                    [UpshotUtility customizeLabel:label withData:s_labelData[@"desc"]];
                    break;
                case BKActivityQuestionLabel:
                    [UpshotUtility customizeLabel:label withData:s_labelData[@"question"]];
                    break;
                case BKActivityOptionLabel:
                    [UpshotUtility customizeLabel:label withData:s_labelData[@"option"]];
                    break;
                case BKActivityThankyouLabel:
                    [UpshotUtility customizeLabel:label withData:s_labelData[@"thankyou"]];
                    break;
                case BKActivitySliderMaxValueLabel:
                    [UpshotUtility customizeLabel:label withData:s_labelData[@"slider_maxScore"]];
                    break;
                case BKActivitySliderMinValueLabel:
                    [UpshotUtility customizeLabel:label withData:s_labelData[@"slider_minScore"]];
                    break;
                case BKActivitySliderScoreLabel:
                    [UpshotUtility customizeLabel:label withData:s_labelData[@"slider_score"]];
                    break;
                case BKActivityMaxValueTitleLabel:
                    [UpshotUtility customizeLabel:label withData:s_labelData[@"slider_maxText"]];
                    break;
                case BKActivityMinValueTitleLabel:
                    [UpshotUtility customizeLabel:label withData:s_labelData[@"slider_minText"]];
                    break;
                default:
                    break;
            }
        }
            break;
        case BKActivityTypeRating: {
            
            NSDictionary *r_labelData = [UpshotUtility getObjectForKey:@"label_text" withFileName:kUpshotRatingTheme];
            
            switch (activityLabel) {
                case BKActivityHeaderLabel:
                    [UpshotUtility customizeLabel:label withData:r_labelData[@"feedback_header"]];
                    break;
                case BKActivityQuestionLabel:
                    [UpshotUtility customizeLabel:label withData:r_labelData[@"question"]];
                    break;
                case BKActivityThankyouLabel:
                    [UpshotUtility customizeLabel:label withData:r_labelData[@"thankyou"]];
                    break;
                case BKActivitySliderMaxValueLabel:
                    [UpshotUtility customizeLabel:label withData:r_labelData[@"slider_maxScore"]];
                    break;
                case BKActivitySliderMinValueLabel:
                    [UpshotUtility customizeLabel:label withData:r_labelData[@"slider_minScore"]];
                    break;
                case BKActivitySliderScoreLabel:
                    [UpshotUtility customizeLabel:label withData:r_labelData[@"slider_score"]];
                    break;
                case BKActivityMaxValueTitleLabel:
                    [UpshotUtility customizeLabel:label withData:r_labelData[@"slider_maxText"]];
                    break;
                case BKActivityMinValueTitleLabel:
                    [UpshotUtility customizeLabel:label withData:r_labelData[@"slider_minText"]];
                    break;
                case BKActivityThankyouAppStoreHint:
                    [UpshotUtility customizeLabel:label withData:r_labelData[@"appStoreHint"]];
                    break;
                default:
                    break;
            }
        }
            break;
        case BKActivityTypeOpinionPoll:{
            
            NSDictionary *p_labelData = [UpshotUtility getObjectForKey:@"label_text" withFileName:kUpshotPollTheme];
            
            switch (activityLabel) {
                case BKActivityQuestionLabel:
                    [UpshotUtility customizeLabel:label withData:p_labelData[@"question"]];
                    break;
                case BKActivityThankyouLabel:
                    [UpshotUtility customizeLabel:label withData:p_labelData[@"thankyou"]];
                    break;
                case BKActivityOptionLabel:
                    [UpshotUtility customizeLabel:label withData:p_labelData[@"option"]];
                    break;
                case BKActivityLegendLabel:
                    [UpshotUtility customizeLabel:label withData:p_labelData[@"graph_legends"]];
                    break;
                case BKActivityBarGraphUserLabel:
                    [UpshotUtility customizeLabel:label withData:p_labelData[@"graph_users_text"]];
                    break;
                case BKActivityBarGraphOptionsLabel:
                    [UpshotUtility customizeLabel:label withData:p_labelData[@"graph_options_text"]];
                    break;
                default:
                    break;
            }
        }
            break;
        case BKActivityTypeTrivia: {
            
            NSDictionary *t_labelData = [UpshotUtility getObjectForKey:@"label_text" withFileName:kUpshotTriviaTheme];
            
            switch (activityLabel) {
                case BKActivityHeaderLabel:
                    [UpshotUtility customizeLabel:label withData:t_labelData[@"header"]];
                    break;
                case BKActivityDescriptionLabel:
                    [UpshotUtility customizeLabel:label withData:t_labelData[@"desc"]];
                    break;
                case BKActivityQuestionLabel:
                    [UpshotUtility customizeLabel:label withData:t_labelData[@"question"]];
                    break;
                case BKActivityOptionLabel:
                    [UpshotUtility customizeLabel:label withData:t_labelData[@"option"]];
                    break;
                case BKActivityThankyouLabel:
                    [UpshotUtility customizeLabel:label withData:t_labelData[@"thankyou"]];
                    break;
                case BKActivityTriviaGraphGradeLabel:
                    [UpshotUtility customizeLabel:label withData:t_labelData[@"graph_grade_text"]];
                    break;
                case BKActivityTriviaGraphCountLabel:
                    [UpshotUtility customizeLabel:label withData:t_labelData[@"graph_res_count"]];
                    break;
                case BKActivityYourScoreLabel:
                    [UpshotUtility customizeLabel:label withData:t_labelData[@"yourScore"]];
                    break;
                case BKActivityTriviaResultsLabel:
                    [UpshotUtility customizeLabel:label withData:t_labelData[@"result"]];
                    break;
                case BKActivityYourGradeLabel:
                    [UpshotUtility customizeLabel:label withData:t_labelData[@"yourGrade"]];
                    break;
                case BKActivityUserGradeLabel:
                    [UpshotUtility customizeLabel:label withData:t_labelData[@"userGrade"]];
                    break;
                case BKActivityUserScoreLabel:
                    [UpshotUtility customizeLabel:label withData:t_labelData[@"userScore"]];
                    break;
                case BKActivityTriviaTabularResponsesLabel:
                    [UpshotUtility customizeLabel:label withData:t_labelData[@"tabular_response"]];
                    break;
                case BKActivityTriviaTabularGradeLabel:
                    [UpshotUtility customizeLabel:label withData:t_labelData[@"tabular_grade"]];
                    break;
                case BKActivityScoreLabel:
                    [UpshotUtility customizeLabel:label withData:t_labelData[@"score"]];
                    break;
                case BKActivityBarGraphUserLabel:
                    [UpshotUtility customizeLabel:label withData:t_labelData[@"graph_users_text"]];
                    break;
                case BKActivityBarGraphOptionsLabel:
                    [UpshotUtility customizeLabel:label withData:t_labelData[@"graph_options_text"]];
                    break;
                default:
                    break;
            }
        }
            break;
        default:
            break;
    }
}


/*This method is used to customize the TextView based on viewType and activityType
FreeFormText,
CommentField
 */

- (void)preferencesForTextView:(UITextView *)textView ofActivity:(BKActivityType)activityType andType:(BKActivityViewType)viewType {
    NSDictionary *labelData = [UpshotUtility getObjectForKey:@"label_text" withFileName:kUpshotSurveyTheme];
    if (activityType == BKActivityTypeRating) {
        labelData = [UpshotUtility getObjectForKey:@"label_text" withFileName:kUpshotRatingTheme];
    }
    
    if (labelData != nil && [labelData isKindOfClass:[NSDictionary class]]) {
        
        NSDictionary *data = labelData[@"feedback_box"];
        if (data != nil) {
            UIColor *textcolor = [UpshotUtility colorFromHex:[UpshotUtility validateString:data[@"color"]]];
            UIColor *border_color = [UpshotUtility colorFromHex:[UpshotUtility validateString:data[@"border_color"]]];
            CGFloat size = [data[@"size"] floatValue];
            UIFont *font = [UIFont fontWithName:[UpshotUtility validateString:data[@"font_name"]] size:size];
            
            if (textcolor != nil) {
                [textView setTextColor:textcolor];
            }
            if (font != nil) {
                [textView setFont:font];
            }
            if (border_color != nil) {
                textView.layer.borderColor = border_color.CGColor;
            }
        }
    }
}

/*This method is used to customize buttons based on activity type */

- (void)preferencesForUIButton:(UIButton *)button ofActivity:(BKActivityType)activityType andType:(BKActivityButtonType)activityButton {
            
    switch (activityType) {
        case BKActivityTypeSurvey: {
            
            NSDictionary *s_buttonData = [UpshotUtility getObjectForKey:@"button" withFileName:kUpshotSurveyTheme];
            switch (activityButton) {

                case BKActivityContinueButton:
                    [UpshotUtility customizeButton:button withData:s_buttonData[@"continue"]];
                    break;
                case BKActivityNextButton:
                    [UpshotUtility customizeButton:button withData:s_buttonData[@"next"]];

                    break;
                case BKActivityPreviousButton:
                    [UpshotUtility customizeButton:button withData:s_buttonData[@"prev"]];
                    break;
                case BKActivitySubmitButton:
                    [UpshotUtility customizeButton:button withData:s_buttonData[@"submit"]];
                    break;
                case BKActivitySkipButton:
                    [UpshotUtility customizeButton:button withData:s_buttonData[@"skip"]];
                    break;
                default:
                    break;
            }
        }
            break;
        case BKActivityTypeRating: {
            
            NSDictionary *r_buttonData = [UpshotUtility getObjectForKey:@"button" withFileName:kUpshotRatingTheme];
            NSDictionary *r_ImageData = [UpshotUtility getObjectForKey:@"image" withFileName:kUpshotRatingTheme];
            switch (activityButton) {
                    
                case BKActivityRatingYesButton:
                    [UpshotUtility customizeButton:button withData:r_buttonData[@"yes"]];
                    break;
                case BKActivityRatingNoButton:
                    [UpshotUtility customizeButton:button withData:r_buttonData[@"no"]];
                    break;
                case BKActivitySubmitButton:
                    [UpshotUtility customizeButton:button withData:r_buttonData[@"submit"]];
                    break;
                case BKActivitySkipButton:
                    [UpshotUtility customizeButton:button withData:r_buttonData[@"skip"]];
                    break;
                case BKActivityRatingLikeButton:
                    if (r_ImageData != nil) {
                        UIImage *likeSel = [UIImage imageNamed:[UpshotUtility validateString:r_ImageData[@"like_sel"]]];
                        UIImage *likeDef = [UIImage imageNamed:[UpshotUtility validateString:r_ImageData[@"like_def"]]];
                        
                        if (likeSel != nil  && likeDef != nil) {
                            [button setImage:likeDef forState:UIControlStateNormal];
                            [button setImage:likeSel forState:UIControlStateSelected];
                            [button setImage:likeSel forState:UIControlStateHighlighted];
                        }
                    }
                    break;
                case BKActivityRatingDislikeButton: {
                    
                    if (r_ImageData != nil) {
                        UIImage *dislikeSel = [UIImage imageNamed:[UpshotUtility validateString:r_ImageData[@"disLike_sel"]]];
                        UIImage *dislikeDef = [UIImage imageNamed:[UpshotUtility validateString:r_ImageData[@"disLike_def"]]];
                        
                        if (dislikeSel != nil && dislikeDef != nil) {
                            [button setImage:dislikeDef forState:UIControlStateNormal];
                            [button setImage:dislikeSel forState:UIControlStateSelected];
                            [button setImage:dislikeSel forState:UIControlStateHighlighted];
                        }
                    }
                }
                default:
                    break;
            }
        }
            break;
        case BKActivityTypeOpinionPoll: {
            
            NSDictionary *p_buttonData = [UpshotUtility getObjectForKey:@"button" withFileName:kUpshotPollTheme];
            switch (activityButton) {
                case BKActivitySubmitButton:
                    [UpshotUtility customizeButton:button withData:p_buttonData[@"submit"]];
                    break;
                case BKActivitySkipButton:
                    [UpshotUtility customizeButton:button withData:p_buttonData[@"skip"]];
                    break;
                default:
                    break;
            }
        }
            break;
        case BKActivityTypeTrivia: {

            NSDictionary *t_buttonData = [UpshotUtility getObjectForKey:@"button" withFileName:kUpshotTriviaTheme];
            switch (activityButton) {

                case BKActivityContinueButton:
                    [UpshotUtility customizeButton:button withData:t_buttonData[@"continue"]];
                    break;
                case BKActivityNextButton:
                    [UpshotUtility customizeButton:button withData:t_buttonData[@"next"]];

                    break;
                case BKActivityPreviousButton:
                    [UpshotUtility customizeButton:button withData:t_buttonData[@"prev"]];
                    break;
                case BKActivitySubmitButton:
                    [UpshotUtility customizeButton:button withData:t_buttonData[@"submit"]];
                    break;
                case BKActivitySkipButton:
                    [UpshotUtility customizeButton:button withData:t_buttonData[@"skip"]];
                    break;
                default:
                    break;
            }
        }
        default:
            break;
    }
}


/*This method is used to customize the UITextField based on viewType and activityType
 FreeFormText,
 CommentField
 */

- (void)preferencesForTextField:(UITextField *)textField ofActivity:(BKActivityType)activityType andType:(BKActivityViewType)viewType {

    NSDictionary *labelData = [UpshotUtility getObjectForKey:@"label_text" withFileName:kUpshotSurveyTheme];
    if (activityType == BKActivityTypeRating) {
        labelData = [UpshotUtility getObjectForKey:@"label_text" withFileName:kUpshotRatingTheme];
    }
    
    if (labelData != nil && [labelData isKindOfClass:[NSDictionary class]]) {
        
        NSDictionary *data = labelData[@"feedback_box"];
        if (data != nil) {
            UIColor *textcolor = [UpshotUtility colorFromHex:[UpshotUtility validateString:data[@"color"]]];
            UIColor *border_color = [UpshotUtility colorFromHex:[UpshotUtility validateString:data[@"border_color"]]];
            CGFloat size = [data[@"size"] floatValue];
            UIFont *font = [UIFont fontWithName:[UpshotUtility validateString:data[@"font_name"]] size:size];
            
            if (textcolor != nil) {
                [textField setTextColor:textcolor];
            }
            if (font != nil) {
                [textField setFont:font];
            }
            if (border_color != nil) {
                textField.layer.borderColor = border_color.CGColor;
                textField.layer.borderWidth = 1.0;
                textField.layer.cornerRadius = 3.0;
            }
        }
    }
}

/*This method is used to customize the Star and Smiley Images based on activityType
 StarRating,
 EmojiRating,
 */

- (void)preferencesForRatingActivity:(BKActivityType)activityType andType:(BKActivityRatingType)activityRating withImages:(void (^)(NSArray *, NSArray *))block {

    NSDictionary *imageData = [UpshotUtility getObjectForKey:@"image" withFileName:kUpshotSurveyTheme];
    
    if (activityType == BKActivityTypeRating) {
        imageData = [UpshotUtility getObjectForKey:@"image" withFileName:kUpshotRatingTheme];
    }
    
    if (imageData != nil && [imageData isKindOfClass:[NSDictionary class]]) {
        
        switch (activityRating) {
                
            case BKActivityStarRating: {

                UIImage *star_sel = [UIImage imageNamed:[UpshotUtility validateString:imageData[@"star_sel"]]];
                UIImage *star_def = [UIImage imageNamed:[UpshotUtility validateString:imageData[@"star_def"]]];
                                
                if (star_sel != nil && star_def != nil) {
                    NSArray *active = @[star_sel,star_sel,star_sel,star_sel,star_sel];
                    NSArray *inactive = @[star_def,star_def,star_def,star_def,star_def];
                    block(inactive,active);
                }
            }
                break;
            case BKActivityEmojiRating: {

                UIImage *defVerybadImage = [UIImage imageNamed:[UpshotUtility validateString:imageData[@"smiley_vbad_def"]]];
                UIImage *defbadImage = [UIImage imageNamed:[UpshotUtility validateString:imageData[@"smiley_bad_def"]]];
                UIImage *defavgImage = [UIImage imageNamed:[UpshotUtility validateString:imageData[@"smiley_avg_def"]]];
                UIImage *defgoodImage = [UIImage imageNamed:[UpshotUtility validateString:imageData[@"smiley_good_def"]]];
                UIImage *defveryGoodImage = [UIImage imageNamed:[UpshotUtility validateString:imageData[@"smiley_Vgood_def"]]];

                UIImage *selVerybadImage = [UIImage imageNamed:[UpshotUtility validateString:imageData[@"smiley_vbad_sel"]]];
                UIImage *selbadImage = [UIImage imageNamed:[UpshotUtility validateString:imageData[@"smiley_bad_sel"]]];
                UIImage *selavgImage = [UIImage imageNamed:[UpshotUtility validateString:imageData[@"smiley_avg_sel"]]];
                UIImage *selgoodImage = [UIImage imageNamed:[UpshotUtility validateString:imageData[@"smiley_good_sel"]]];
                UIImage *selveryGoodImage = [UIImage imageNamed:[UpshotUtility validateString:imageData[@"smiley_Vgood_sel"]]];

                if (defVerybadImage != nil && defbadImage != nil && defavgImage != nil && defgoodImage!= nil &&
                    defveryGoodImage != nil && selVerybadImage != nil && selbadImage != nil && selavgImage != nil && selgoodImage != nil && selveryGoodImage != nil) {
                    NSArray *active = @[selVerybadImage,selbadImage,selavgImage,selgoodImage,selveryGoodImage];
                    NSArray *inactive = @[defVerybadImage,defbadImage,defavgImage,defgoodImage,defveryGoodImage];
                    block(inactive,active);
                }
            }
            default:
                break;
        }
    }
}

/*This method is used to customize the Background Colors of View based on activityColor and activityType */

- (void)preferencesForUIColor:(BKBGColor *)color ofActivity:(BKActivityType)activityType colorType:(BKActivityColorType)activityColor andButtonType:(BOOL)isFloatButton {

    switch (activityType) {
        case BKActivityTypeSurvey: {
            
            NSDictionary *s_colorData = [UpshotUtility getObjectForKey:@"color" withFileName:kUpshotSurveyTheme];
            switch (activityColor) {
                case BKActivityBGColor:
                    [UpshotUtility customizeColor:color withData:s_colorData[@"background"]];
                    break;
                case BKActivityHeaderBGColor:
                    [UpshotUtility customizeColor:color withData:s_colorData[@"headerBG"]];
                    break;
                case BKActivityBottomBGColor:
                    [UpshotUtility customizeColor:color withData:s_colorData[@"bottomBG"]];
                    break;
                case BKActivityPageTintColor:
                    [UpshotUtility customizeColor:color withData:s_colorData[@"pagenationdots_def"]];
                    break;
                case BKActivityCurrentPageTintColor:
                    [UpshotUtility customizeColor:color withData:s_colorData[@"pagenationdots_current"]];
                    break;
                case BKActivityAnsweredPageTintColor:
                    [UpshotUtility customizeColor:color withData:s_colorData[@"pagenationdots_answered"]];
                    break;
                case BKActivityOptionDefaultBorderColor:
                    [UpshotUtility customizeColor:color withData:s_colorData[@"option_def_border"]];
                    break;
                case BKActivityOptionSelectedBorderColor:
                    [UpshotUtility customizeColor:color withData:s_colorData[@"option_sel_border"]];
                    break;
                default:
                    break;
            }
        }

            break;
        case BKActivityTypeRating: {
            NSDictionary *r_colorData = [UpshotUtility getObjectForKey:@"color" withFileName:kUpshotRatingTheme];
            switch (activityColor) {
                case BKActivityBGColor:
                    [UpshotUtility customizeColor:color withData:r_colorData[@"background"]];
                    break;
                case BKActivityHeaderBGColor:
                    [UpshotUtility customizeColor:color withData:r_colorData[@"headerBG"]];
                    break;
                case BKActivityBottomBGColor:
                    [UpshotUtility customizeColor:color withData:r_colorData[@"bottomBG"]];
                    break;
                default:
                    break;
            }
        }
            break;
        case BKActivityTypeOpinionPoll: {
            
            NSDictionary *p_colorData = [UpshotUtility getObjectForKey:@"color" withFileName:kUpshotPollTheme];
            switch (activityColor) {

                case BKActivityBGColor:
                    [UpshotUtility customizeColor:color withData:p_colorData[@"background"]];
                    break;
                case BKActivityHeaderBGColor:
                    [UpshotUtility customizeColor:color withData:p_colorData[@"headerBG"]];
                    break;
                case BKActivityBottomBGColor:
                    [UpshotUtility customizeColor:color withData:p_colorData[@"bottomBG"]];
                    break;
                case BKActivityPercentageColor:
                    [UpshotUtility customizeColor:color withData:p_colorData[@"percentageText"]];
                    break;
                case BKActivityGraphLabelColor:
                    [UpshotUtility customizeColor:color withData:p_colorData[@"barGraphNumberText"]];
                    break;
                case BKActivityStrokeColor:
                    [UpshotUtility customizeColor:color withData:p_colorData[@"barGraphLine"]];
                    break;
                case BKActivityOptionDefaultBorderColor:
                    [UpshotUtility customizeColor:color withData:p_colorData[@"option_def_border"]];
                    break;
                case BKActivityOptionSelectedBorderColor:
                    [UpshotUtility customizeColor:color withData:p_colorData[@"option_sel_border"]];
                    break;
                default:
                    break;
            }
        }
            break;
        case BKActivityTypeTrivia: {

            NSDictionary *t_colorData = [UpshotUtility getObjectForKey:@"color" withFileName:kUpshotTriviaTheme];
            switch (activityColor) {
                case BKActivityBGColor:
                    [UpshotUtility customizeColor:color withData:t_colorData[@"background"]];
                    break;
                case BKActivityHeaderBGColor:
                    [UpshotUtility customizeColor:color withData:t_colorData[@"headerBG"]];
                    break;
                case BKActivityBottomBGColor:
                    [UpshotUtility customizeColor:color withData:t_colorData[@"bottomBG"]];
                    break;
                case BKActivityPercentageColor:
                    [UpshotUtility customizeColor:color withData:t_colorData[@"percentageText"]];
                    break;
                case BKActivityGraphLabelColor:
                    [UpshotUtility customizeColor:color withData:t_colorData[@"barGraphNumberText"]];
                    break;
                case BKActivityStrokeColor:
                    [UpshotUtility customizeColor:color withData:t_colorData[@"barGraphLine"]];
                    break;
                case BKActivityOptionDefaultBorderColor:
                    [UpshotUtility customizeColor:color withData:t_colorData[@"option_def_border"]];
                    break;
                case BKActivityOptionSelectedBorderColor:
                    [UpshotUtility customizeColor:color withData:t_colorData[@"option_sel_border"]];
                    break;
                case BKActivityPageTintColor:
                    [UpshotUtility customizeColor:color withData:t_colorData[@"pagenationdots_def"]];
                    break;
                case BKActivityCurrentPageTintColor:
                    [UpshotUtility customizeColor:color withData:t_colorData[@"pagenationdots_current"]];
                    break;
                case BKActivityAnsweredPageTintColor:
                    [UpshotUtility customizeColor:color withData:t_colorData[@"pagenationdots_answered"]];
                    break;
                    
                default:
                    break;
            }
        }
            break;
        default:
            break;
    }
}

@end

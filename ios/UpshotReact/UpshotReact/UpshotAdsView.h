//
//  UpshotAdsView.h
//  react-native-upshotsdk
//
//  Created by Vinod Kottamsu on 20/01/22.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UpshotAdsView : UIView

@property (nonatomic, assign) BOOL updateFrame;
@property (nonatomic, strong) UIView* upshotAdView;

- (void)update;

@end

NS_ASSUME_NONNULL_END

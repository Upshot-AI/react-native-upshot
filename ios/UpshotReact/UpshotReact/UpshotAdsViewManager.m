//
//  NSObject+UpshotAdsViewManager.m
//  react-native-upshotsdk
//
//  Created by Vinod Kottamsu on 20/01/22.
//

#import <React/RCTViewManager.h>
#import "UpshotAdsView.h"

@interface UpshotAdsViewManager: RCTViewManager
@end

@implementation UpshotAdsViewManager

RCT_EXPORT_MODULE(UpshotAdsView)

- (UIView *) view {
    
    UpshotAdsView *view = [[UpshotAdsView alloc] init];
    [view update];
    return view;
}

+ (BOOL)requiresMainQueueSetup {
    return YES;
}

RCT_EXPORT_VIEW_PROPERTY(updateFrame, BOOL)

@end

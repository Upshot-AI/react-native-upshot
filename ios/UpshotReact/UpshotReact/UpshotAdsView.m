//
//  UpshotAdsView.m
//  react-native-upshotsdk
//
//  Created by Vinod Kottamsu on 20/01/22.
//

#import "UpshotAdsView.h"
#import "UpshotReact.h"

@implementation UpshotAdsView

- (void)update {
    
    UIView *adView = [UpshotReact getAdView];
    [adView setFrame:self.bounds];
    [self addSubview:adView];
    
}

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self != nil) {        
        
    }
    return self;
}

@end

//
//  UpshotReact.h
//  UpshotReact
//
//  Created by Vinod on 07/01/20.
//  Copyright © 2020 [x]cubeLabs. All rights reserved.
//

#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

#import <Upshot/Upshot.h>

@interface UpshotReact : RCTEventEmitter <RCTBridgeModule, BrandKinesisDelegate, UNUserNotificationCenterDelegate>

+ (UIView *)getAdView;

@end

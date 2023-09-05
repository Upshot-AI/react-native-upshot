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

//@property(nonatomic, strong) UIView *adsView;

@property (nonatomic, strong) NSDictionary *pushPayload;
@property (nonatomic, strong) NSString *pushToken;
@property (nonatomic, assign) BOOL hasStartObserving;


+ (UIView *)getAdView;

- (void)applicationDidRegisterWithDeviceToken:(NSData *)deviceToken;

- (void)didReceivePushNotifcationWithResponse:(NSDictionary *)notification;

@end

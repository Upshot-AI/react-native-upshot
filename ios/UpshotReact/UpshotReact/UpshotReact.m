//
//  UpshotReact.m
//  UpshotReact
//
//  Created by Vinod on 07/01/20.
//  Copyright © 2020 [x]cubeLabs. All rights reserved.
//

#import "UpshotReact.h"
#import "UpshotCustomization.h"
#import <React/RCTBridge.h>
#import <React/RCTEventDispatcher.h>
#import "UpshotUtility.h"

@interface UpshotReact () <UpshotUtilityDelegate>

@end

@implementation UpshotReact {
    
    NSMutableArray *missedEvents;
    NSDictionary *deeplinkInfo;
    bool hasStartObserving;
    bool enableCustomization;
}

static UIView *_adsView = nil;

RCT_EXPORT_MODULE();


- (instancetype)init
{
    self = [super init];
    if (self) {
        missedEvents = [NSMutableArray array];
        [UpshotUtility sharedUtility].delegate = self;
    }
    return self;
}

- (void)addObservers {
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(handleNotification:)
                                                 name:@"UpshotDidReceiveDeviceToken"
                                               object:nil];
}

- (void)handleNotification:(NSNotification *)notification {
    NSLog(@"Notification received: %@", notification.userInfo);
}
#pragma mark SupportedEvents

- (NSArray<NSString *> *)supportedEvents {
    
  return @[@"UpshotDeepLink",
           @"UpshotActivityDidAppear",           
           @"UpshotActivityDidDismiss",
           @"UpshotActivitySkip",
           @"UpshotActivityError",
           @"UpshotAuthStatus",
           @"UpshotPushToken",
           @"UpshotPushPayload",
           @"UpshotCampaignDetailsLoaded",
           @"UpshotOnPushClickInfo",
           @"UpshotAdReady"];
}

#pragma mark Initialize Upshot
 
RCT_EXPORT_METHOD(initializeUpshot) {
    
    [[BrandKinesis sharedInstance] initializeWithDelegate:self];
    UpshotCustomization *customization = [[UpshotCustomization alloc] init];
    [[BKUIPreferences preferences] setDelegate:customization];
}

RCT_EXPORT_METHOD(initializeUpshotUsingOptions:(NSString *)options) {
    
    NSDictionary *json = [UpshotUtility convertJsonStringToJson:options];
    if(json == nil) {
        return;
    }
    NSMutableDictionary *initOptions = [[NSMutableDictionary alloc] init];
    if ([json valueForKey:@"bkApplicationID"]) {
        [initOptions setValue:json[@"bkApplicationID"] forKey:BKApplicationID];
    }
    if ([json valueForKey:@"bkApplicationOwnerID"]) {
        [initOptions setValue:json[@"bkApplicationOwnerID"] forKey:BKApplicationOwnerID];
    }
    if ([json valueForKey:@"bkFetchLocation"]) {
        [initOptions setValue:[NSNumber numberWithBool:[json[@"bkFetchLocation"] boolValue]] forKey:BKFetchLocation];
    }
    if ([json valueForKey:@"bkExceptionHandler"]) {
        [initOptions setValue:[NSNumber numberWithBool:[json[@"bkExceptionHandler"] boolValue]] forKey:BKExceptionHandler];
    }
    if ([json valueForKey:@"appuid"]) {
        [initOptions setValue:json[@"appuid"] forKey:BKAppuID];
    }
     if ([json valueForKey:@"bkEnableCustomization"]) {
        enableCustomization = [json[@"bkEnableCustomization"] boolValue];
    }
    [[BrandKinesis sharedInstance] initializeWithOptions:initOptions delegate:self];
    if(enableCustomization) {
        UpshotCustomization *customization = [[UpshotCustomization alloc] init];
        [[BKUIPreferences preferences] setDelegate:customization];
    }
}

#pragma mark Terminate

RCT_EXPORT_METHOD(terminate) {
    
    [[BrandKinesis sharedInstance] terminate];
}

#pragma mark Events

RCT_EXPORT_METHOD(setDispatchInterval:(NSInteger)interval) {
    
    [[BrandKinesis sharedInstance] setDispatchInterval:interval];
}

RCT_EXPORT_METHOD(createPageViewEvent:(NSString *)currentPage callback:(RCTResponseSenderBlock)callback) {
    
    NSString *eventId = nil;
    if ((currentPage != nil) && ![currentPage isEqualToString:@""]) {
        eventId = [[BrandKinesis sharedInstance] createEvent:BKPageViewNative params:@{BKCurrentPage: currentPage} isTimed:YES];
    }
    if (callback != nil) {
        if (eventId != nil) {
            callback(@[eventId]);
        } else{
            callback(@[[NSNull null]]);
        }
    }
}

RCT_EXPORT_METHOD(createCustomEvent:(NSString *)eventName payload:(NSString *)payload timed:(BOOL)isTimed callback:(RCTResponseSenderBlock)callback) {
    
    NSString *eventId = nil;
    if ((eventName != nil) && ![eventName isEqualToString: @""]) {
        NSDictionary *eventPayload = [UpshotUtility convertJsonStringToJson:payload];
        eventId = [[BrandKinesis sharedInstance] createEvent:eventName params:eventPayload isTimed:isTimed];
    }
    if (callback != nil) {
        if (eventId != nil) {
            callback(@[eventId]);
        } else{
            callback(@[[NSNull null]]);
        }
    }
}

RCT_EXPORT_METHOD(setValueAndClose:(NSString *)payload forEvent:(NSString *)eventId) {
        
    NSDictionary *eventPayload = [UpshotUtility convertJsonStringToJson:payload];
    if(eventId != nil && eventPayload != nil) {
        [[BrandKinesis sharedInstance] setValueAndClose:eventPayload forEvent:eventId];
    }
}

RCT_EXPORT_METHOD(closeEventForId:(NSString *)eventId) {
    
    if(eventId != nil) {
        [[BrandKinesis sharedInstance] closeEventForID:eventId];
    }
}

RCT_EXPORT_METHOD(dispatchEventsWithTimedEvents:(BOOL)timed callback:(RCTResponseSenderBlock)callback) {
    
    [[BrandKinesis sharedInstance] dispatchEventsWithTimedEvents:timed completionBlock:^(BOOL dispatched) {
        if (callback != nil) {
            callback(@[[NSNumber numberWithBool:dispatched]]);
        }
    }];
}

RCT_EXPORT_METHOD(createLocationEvent:(NSString *)latitude longitude:(NSString *)longitude) {
    
    CGFloat lat = [latitude floatValue];
    CGFloat lon = [longitude floatValue];
    [[BrandKinesis sharedInstance] createLocationEvent:lat longitude:lon];
}


RCT_EXPORT_METHOD(createAttributionEvent:(NSString *)payload callback:(RCTResponseSenderBlock)callback) {
    
    NSString *eventId = nil;
    if (payload && ![payload isEqualToString: @""]) {
        NSDictionary *eventPayload = [UpshotUtility convertJsonStringToJson:payload];
        if(eventPayload != nil) {
            eventId = [[BrandKinesis sharedInstance] createAttributionEvent:eventPayload];
        }
    }
    if (callback != nil) {
        if (eventId != nil) {
            callback(@[eventId]);
        } else{
            callback(@[[NSNull null]]);
        }
    }
}

#pragma mark UserProfile

RCT_EXPORT_METHOD(setUserProfile:(NSString *)userData callback:(RCTResponseSenderBlock)callback) {
     
    NSDictionary *userDict = [UpshotUtility convertJsonStringToJson:userData];
        if(userDict != nil && userDict.allKeys.count > 0) {
            [self buildUserInfoForParams:userDict completionBlock:^(BOOL success, NSError * _Nullable error) {
              callback(@[[NSNumber numberWithBool:success]]);
            }];
        }
}

#pragma mark GetUserDetails

RCT_EXPORT_METHOD(getUserDetails:(RCTResponseSenderBlock)callback) {
        
    NSDictionary *userDetails = [[BrandKinesis sharedInstance] getUserDetails:@[]];
    NSString *jsonString = [UpshotUtility convertJsonObjToJsonString:userDetails];
    callback(@[jsonString]);
}

#pragma mark Upshot Actions

RCT_EXPORT_METHOD(showActivityWithType:(NSInteger)type andTag:(NSString *)tag) {
  
  if(enableCustomization) {
     UpshotCustomization *customization = [[UpshotCustomization alloc] init];
    [[BKUIPreferences preferences] setDelegate:customization];
  }
    
    BKActivityType activityType = (BKActivityType)type;
    [[BrandKinesis sharedInstance] setDelegate:self];
    [[BrandKinesis sharedInstance] showActivityWithType:activityType andTag:tag];
}

RCT_EXPORT_METHOD(showInteractiveTutorial:(NSString *)tag) {
  
  if(enableCustomization) {
     UpshotCustomization *customization = [[UpshotCustomization alloc] init];
    [[BKUIPreferences preferences] setDelegate:customization];
  }
    
    BKActivityType activityType = BKActivityTypeTutorials;
    [[BrandKinesis sharedInstance] setDelegate:self];
    [[BrandKinesis sharedInstance] showActivityWithType:activityType andTag:tag];
}

RCT_EXPORT_METHOD(showActivityWithId:(NSString *)activityId) {
    
    if(activityId != nil) {
        if(enableCustomization) {
            UpshotCustomization *customization = [[UpshotCustomization alloc] init];
            [[BKUIPreferences preferences] setDelegate:customization];
        }        
        [[BrandKinesis sharedInstance] setDelegate:self];
        [[BrandKinesis sharedInstance] showActivityWithActivityId:activityId];
    }
}

RCT_EXPORT_METHOD(removeTutorials) {
    
    [[BrandKinesis sharedInstance] removeTutorials];
}

RCT_EXPORT_METHOD(fetchInboxInfo:(RCTResponseSenderBlock)callback) {
    
    [[BrandKinesis sharedInstance] fetchInboxInfoWithCompletionBlock:^(NSArray * inbox) {
           
        NSMutableArray *inboxInfo = [[NSMutableArray alloc] init];
       if(inbox != nil) {
           
           for (id object in inbox) {
               
               NSMutableDictionary *dict = [[NSMutableDictionary alloc] init];
               [dict setObject:object[@"name"] ? object[@"name"] : @"" forKey:@"name"];
               
               NSArray *activities = object[@"activities"];
               if(activities.count > 0) {
                   NSMutableDictionary *activityInfo = activities.firstObject;
                   if([activityInfo valueForKey:@"date"]) {
                       NSDate *date = activityInfo[@"date"];
                       [activityInfo setValue:[NSNumber numberWithLongLong:[date timeIntervalSince1970]] forKey:@"date"];
                   }
                   if([activityInfo valueForKey:@"expiry"]) {
                       NSDate *date = activityInfo[@"expiry"];
                       [activityInfo setValue:[NSNumber numberWithLongLong:[date timeIntervalSince1970]] forKey:@"expiry"];
                   }
               }
               [dict setValue:activities forKey:@"activities"];
               [inboxInfo addObject:dict];
           }
            NSString *jsonString = [UpshotUtility convertJsonObjToJsonString:inboxInfo];
              if([jsonString length] > 0) {
                  callback(@[jsonString]);
              }
        }
    }];
}

RCT_EXPORT_METHOD(getUserBadges:(RCTResponseSenderBlock)callback) {
    
    NSMutableDictionary *badges = [[[BrandKinesis sharedInstance] getUserBadges] mutableCopy];
    NSString *jsonString = [UpshotUtility getBadgesData:badges];
    callback(@[jsonString]);
}

#pragma mark PushNotifications

RCT_EXPORT_METHOD(registerForPush) {
  
   [self registerForPushWithCallback];
}

RCT_EXPORT_METHOD(sendDeviceToken:(NSString *)token) {
    
    [self updateDeviceToken:token];
}

RCT_EXPORT_METHOD(sendPushDataToUpshot:(NSString *)pushDetails) {
    
    NSDictionary *payload = [UpshotUtility convertJsonStringToJson:pushDetails];
    [self updatePushResponse:payload];
}

RCT_EXPORT_METHOD(getNotificationList:(NSInteger)limit enable:(BOOL)loadMore response:(RCTResponseSenderBlock)successCallback error:(RCTResponseSenderBlock)failureCallback) {
    
   [[BrandKinesis sharedInstance] getNotificationsWith:limit loadmore:loadMore onCompletion:^(NSDictionary * _Nullable response, NSString * _Nullable errorMessage) {
        
        if(errorMessage == nil) {
            NSArray *data = response[@"data"];
            if (data != nil) {
                NSString *jsonString = [UpshotUtility convertJsonObjToJsonString:data];
                if(jsonString != nil) {
                    successCallback(@[jsonString]);
                } else {
                    failureCallback(@[@"Something went wrong"]);
                }
            } else {
                failureCallback(@[@"Something went wrong"]);
            }
        } else {
            failureCallback(@[errorMessage]);
        }
    }];
}

RCT_EXPORT_METHOD(getUnreadNotificationsCount:(NSInteger)inboxType response:(RCTResponseSenderBlock)callback) {
    
    BKInboxMessageType type = (BKInboxMessageType)inboxType;
    [[BrandKinesis sharedInstance] getUnreadNotificationsCountWithType:type onCompletion:^(NSInteger pushCount) {
        callback(@[[NSNumber numberWithInteger:pushCount]]);
    }];
}

RCT_EXPORT_METHOD(updateNotificationReadStatus:(NSString *)notificationId response:(RCTResponseSenderBlock)callback) {
    
    [[BrandKinesis sharedInstance] updatePushNotificationReadStatus:notificationId onCompletion:^(BOOL status, NSString * _Nullable error) {
        NSDictionary *jsonResponse = @{@"status": [NSNumber numberWithBool:status],@"error":error ? error: @""};
        NSString *jsonString = [UpshotUtility convertJsonObjToJsonString:jsonResponse];
        callback(@[jsonString]);
    }];
}

RCT_EXPORT_METHOD(showInboxNotificationScreen:(NSString *)options) {
    
    if(options != nil) {
        NSDictionary *json = [UpshotUtility convertJsonStringToJson:options];
        if(json != nil) {
            [[BrandKinesis sharedInstance] showInboxController:json];
        }
    }
}

#pragma mark Streaks

RCT_EXPORT_METHOD(getStreaksData:(RCTResponseSenderBlock)successCallback error:(RCTResponseSenderBlock)errorCallback) {
    
     [[BrandKinesis sharedInstance] getStreaksDataWithCompletionBlock:^(NSDictionary * _Nullable response, NSString * _Nullable errorMessage) {
            
            NSArray *data = response[@"data"];
            NSDictionary *jsonResponse = @{@"streakData": data};
        if(errorMessage != nil) {
            errorCallback(@[errorMessage]);
        } else {
             NSString *jsonString = [UpshotUtility convertJsonObjToJsonString:jsonResponse];
            successCallback(@[jsonString]);
        }
    }];
}

#pragma mark GDPR

RCT_EXPORT_METHOD(disableUser:(RCTResponseSenderBlock)callback) {
        
    [[BrandKinesis sharedInstance] disableUser:^(BOOL status, NSError * _Nullable error) {
        callback(@[[NSNumber numberWithBool:status]]);
    }];
}

#pragma mark Upshot UserId

RCT_EXPORT_METHOD(getUserId:(RCTResponseSenderBlock)callback) {
    
    NSString *userId = [[BrandKinesis sharedInstance] getUserId];
    callback(@[userId]);
}

#pragma mark SDK Vesrion

RCT_EXPORT_METHOD(getSDKVersion:(RCTResponseSenderBlock)callback) {
    
    NSString *version = [BrandKinesis sharedInstance].version;
    callback(@[version]);
}

#pragma mark Rewards

RCT_EXPORT_METHOD(getRewardsList:(RCTResponseSenderBlock)successCallback error:(RCTResponseSenderBlock)failureCallback) {
    
    [[BrandKinesis sharedInstance] getRewardsStatusWithCompletionBlock:^(NSDictionary * _Nullable response, NSString * _Nullable errorMessage) {
        
        NSString *error = errorMessage ? errorMessage : @"";
        if (errorMessage.length > 0) {
            failureCallback(@[error]);
        } else {
          NSDictionary *result = response ? response : @{};
          NSString *jsonString = [UpshotUtility convertJsonObjToJsonString:result];
          successCallback(@[jsonString]);
        }
    }];
}

RCT_EXPORT_METHOD(getRewardHistoryForProgram:(NSString *)programId historyType:(NSInteger)type callback:(RCTResponseSenderBlock)successCallback error:(RCTResponseSenderBlock)failureCallback) {
    
    if(programId == nil) {
        failureCallback(@[@"Invalid Program Id"]);
        return;
    }
  BKRewardHistoryType historyType = (BKRewardHistoryType)type;
    [[BrandKinesis sharedInstance] getRewardHistoryForProgramId:programId withHistoryType:historyType withCompletionBlock:^(NSDictionary * _Nullable response, NSString * _Nullable errorMessage) {
      
        NSString *error = errorMessage ? errorMessage : @"";
        if (errorMessage.length > 0) {
            failureCallback(@[error]);
        } else {
          NSDictionary *result = response ? response : @{};
          NSString *jsonString = [UpshotUtility convertJsonObjToJsonString:result];
          successCallback(@[jsonString]);
        }
    }];
}

RCT_EXPORT_METHOD(getRewardRulesforProgram:(NSString *)programId callback:(RCTResponseSenderBlock)successCallback error:(RCTResponseSenderBlock)failureCallback) {
    
    if(programId == nil) {
        failureCallback(@[@"Invalid Program Id"]);
        return;
    }
    [[BrandKinesis sharedInstance] getRewardDetailsForProgramId:programId
                                            withCompletionblock:^(NSDictionary * _Nullable response, NSString * _Nullable errorMessage) {
        
        NSString *error = errorMessage ? errorMessage : @"";
        if (errorMessage.length > 0) {
            failureCallback(@[error]);
        } else {
          NSDictionary *result = response ? response : @{};
          NSString *jsonString = [UpshotUtility convertJsonObjToJsonString:result];
          successCallback(@[jsonString]);
        }
    }];
}

RCT_EXPORT_METHOD(redeemRewardsForProgram:(NSString *)programId transactionAmount:(NSInteger)amount redeemValue:(NSInteger)value tag:(NSString *)tag callback:(RCTResponseSenderBlock)successCallback error:(RCTResponseSenderBlock)failureCallback) {
    
    if(programId == nil) {
        failureCallback(@[@"Invalid Program Id"]);
        return;
    }
    [[BrandKinesis sharedInstance] redeemRewardsWithProgramId:programId transactionValue:amount redeemAmout:value tag:tag withCompletionblock:^(NSDictionary * _Nullable response, NSString * _Nullable errorMessage) {
        
        NSString *error = errorMessage ? errorMessage : @"";
        if (errorMessage.length > 0) {
            failureCallback(@[error]);
        } else {
          NSDictionary *result = response ? response : @{};
          NSString *jsonString = [UpshotUtility convertJsonObjToJsonString:result];
          successCallback(@[jsonString]);
        }
    }];
}

RCT_EXPORT_METHOD(setFontStyles:(NSString *)fontStyles) {
    
    if(fontStyles != nil) {
        NSDictionary *fonts = [UpshotUtility convertJsonStringToJson:fontStyles];
        if(fonts != nil) {
            [[BrandKinesis sharedInstance] setFontStyles:fonts];
        }
    }
}

- (void)startObserving {
        
    hasStartObserving = YES;
    [self sendMissedEvents];
}

- (void)stopObserving {
    hasStartObserving = NO;
}

- (void)sendEvent:(NSDictionary *)eventPayload {
    if (missedEvents == nil) {
        missedEvents = [NSMutableArray array];
    }
    if (hasStartObserving) {
        
        [self updatePushResponse:eventPayload];
        [self sendEventWithName:@"UpshotPushPayload" body:@{@"payload": [UpshotUtility convertJsonObjToJsonString:eventPayload]}];
    } else {
        [missedEvents addObject:eventPayload];
    }
}

- (void)sendMissedEvents {
        
    [[BrandKinesis sharedInstance] setDelegate:self];
    if ([[UpshotUtility sharedUtility] getDeviceToken] != nil) {
        
        [self sendEventWithName:@"UpshotPushToken" body:[[UpshotUtility sharedUtility] getDeviceToken]];
    }
    
    if (deeplinkInfo != nil && deeplinkInfo.allKeys.count > 0) {
        [self sendEventWithName:@"UpshotOnPushClickInfo" body:@{@"payload": [UpshotUtility convertJsonObjToJsonString:deeplinkInfo]}];
        deeplinkInfo = @{};
    }
        
    [missedEvents addObjectsFromArray:[[UpshotUtility sharedUtility] getPushPayloads]];
    for (NSDictionary *eventPayload in missedEvents) {
        [self updatePushResponse:eventPayload];
        [self sendEventWithName:@"UpshotPushPayload" body:@{@"payload": [UpshotUtility convertJsonObjToJsonString:eventPayload]}];
    }
    [missedEvents removeAllObjects];
    [[UpshotUtility sharedUtility] removeAllObjects];
}

+ (UIView *)getAdView {
    
    return _adsView;
}

#pragma mark Upshot Internal Methods

- (void)registerForPushWithCallback {
      
    if (@available(iOS 10.0, *) ) {
        
        id delegate = [UIApplication sharedApplication].delegate;
        if (delegate != nil) {
            
            UNUserNotificationCenter *notificationCenter = [UNUserNotificationCenter currentNotificationCenter];
            [notificationCenter requestAuthorizationWithOptions:(UNAuthorizationOptionAlert | UNAuthorizationOptionBadge | UNAuthorizationOptionSound ) completionHandler:^(BOOL granted, NSError * _Nullable error) {
                if (granted) {
                    [notificationCenter setDelegate:delegate];
                }
            }];
            dispatch_async(dispatch_get_main_queue(), ^{
                [[UIApplication sharedApplication] registerForRemoteNotifications];
            });
        }
    }
}

- (void)updateDeviceToken:(NSString *)token {
    
    BKUserInfo *userInfo = [[BKUserInfo alloc] init];
    BKExternalId *externalId = [[BKExternalId alloc] init];
    externalId.apnsID = token;
    userInfo.externalId = externalId;
    [userInfo buildUserInfoWithCompletionBlock:nil];
}

- (void)updatePushResponse:(NSDictionary *)response {
 
    [[BrandKinesis sharedInstance] handlePushNotificationWithParams:response withCompletionBlock:nil];
}

- (void)buildUserInfoForParams:(NSDictionary *)dict completionBlock:(void(^)(BOOL success,  NSError * _Nullable error))block {
  
  if (dict == nil || dict.allKeys.count < 1) {
    return;
  }
  BKUserInfo *userInfo     = [[BKUserInfo alloc] init];
  BKDob *dob               = [[BKDob alloc] init];
  BKExternalId *externalId = [[BKExternalId alloc] init];
  
  NSMutableDictionary *mutDict = [[NSMutableDictionary alloc] initWithDictionary:dict];
  
  NSMutableDictionary *others = [[NSMutableDictionary alloc] init];
  
  if (dict[@"lat"] && dict[@"lng"]) {
    CLLocation *location = [[CLLocation alloc] initWithLatitude:[dict[@"lat"] floatValue] longitude:[dict[@"lng"] floatValue]];
    [userInfo setLocation:location];
    [mutDict removeObjectForKey:@"lat"];
    [mutDict removeObjectForKey:@"lng"];
  }
  
  if (dict[@"token"]) {
    [externalId setApnsID:dict[@"token"]];
    [mutDict removeObjectForKey:@"token"];
  }
  
    for (NSString *key in [mutDict allKeys]) {
        
        if(key == nil) {
            continue;
        }
        NSString *type = [UpshotUtility getInfoTypeForKey:key];
        
        id object = dict[key];
        if(object == nil || [object isKindOfClass:[NSNull class]]) {
            continue;
        }
        if ([object isKindOfClass:[NSNumber class]]) {
            object = [NSNumber numberWithInt:[object intValue]];
        }
        if ([key isEqualToString:@"data_opt"] && [object isKindOfClass:[NSNumber class]]) {
            userInfo.dataOptout = object;
            continue;
        }
        if ([key isEqualToString:@"email_opt"] && [object isKindOfClass:[NSNumber class]]) {
            userInfo.emailOptout = object;
            continue;
        }
        if ([key isEqualToString:@"sms_opt"] && [object isKindOfClass:[NSNumber class]]) {
            userInfo.smsOptout = object;
            continue;
        }
        if ([key isEqualToString:@"push_opt"] && [object isKindOfClass:[NSNumber class]]) {
            userInfo.pushOptout = object;
            continue;
        }
        if ([key isEqualToString:@"ip_opt"] && [object isKindOfClass:[NSNumber class]]) {
            userInfo.ipOptout = object;
            continue;
        }
        if ([type  isEqual:@"BKDob"]) {
            
            [dob setValue:object forKey:key];
            
        }else if ([type isEqual:@"BKExternalId"]){
            
            [externalId setValue:object forKey:key];
            
        } else if ([type isEqual:@"UserInfo"]){
            
            [userInfo setValue:object forKey:key];
            
        }else{
            [others setObject:object forKey:key];
        }
    }
  [userInfo setOthers:others];
  [userInfo setExternalId:externalId];
  [userInfo setDateOfBirth:dob];
  
  [userInfo buildUserInfoWithCompletionBlock:^(BOOL success, NSError *error) {
      block(success, error);
  }];
}


#pragma mark Upshot Events

- (void)brandKinesisAuthentication:(BrandKinesis *)brandKinesis withStatus:(BOOL)status error:(NSError *)error {
    
    [self sendEventWithName:@"UpshotAuthStatus" body:@{@"status": [NSNumber numberWithBool:status], @"error": error ? error : [NSNull null]}];
}

- (void)brandKinesisActivity:(BKActivityType)activityType performedActionWithParams:(NSDictionary *)params {
    
    NSString *deeplinkValue = params[@"deepLink"];
    NSDictionary *deeplinkKeyValue = params[@"deepLink_keyValue"];
    if (deeplinkValue != nil && params.count == 1) {
        [self sendEventWithName:@"UpshotDeepLink" body:@{@"deepLink": params[@"deepLink"]}];
    } else if (deeplinkKeyValue != nil && [deeplinkKeyValue allKeys].count > 0) {
        [self sendEventWithName:@"UpshotDeepLink" body:@{@"deepLink_keyValue": [UpshotUtility convertJsonObjToJsonString:deeplinkKeyValue]}];
    } else if (params != nil && [params allKeys].count > 0) {
        [self sendEventWithName:@"UpshotDeepLink" body:params];
    }
}

- (void)brandKinesisActivityDidAppear:(BrandKinesis *)brandKinesis forActivityType:(BKActivityType)activityType {
    
    [self sendEventWithName:@"UpshotActivityDidAppear" body:@{@"activityType": [NSNumber numberWithInteger:activityType]}];
}

- (void)brandKinesisActivityDidDismiss:(BrandKinesis *)brandKinesis forActivityType:(BKActivityType)activityType {
    
    [self sendEventWithName:@"UpshotActivityDidDismiss" body:@{@"activityType": [NSNumber numberWithInteger:activityType]}];
}

- (void)brandKinesisActivitySkipped:(BrandKinesis *)brandKinesis forActivityType:(BKActivityType)activityType {

    [self sendEventWithName:@"UpshotActivitySkip" body:@{@"activityType": [NSNumber numberWithInteger:activityType]}];
}


- (void)brandkinesisErrorLoadingActivity:(BrandKinesis *)brandkinesis withError:(NSError *)error {
    
    [self sendEventWithName:@"UpshotActivityError" body:@{@"error": error.localizedDescription ? error.localizedDescription : @""}];
}

- (void)brandKinesisDidReceiveView:(UIView *)adsView forTag:(NSString *)tag {
    
    _adsView = adsView;
    [self sendEventWithName:@"UpshotAdReady" body:@{@"Tag": tag ? tag : @""}];
}

- (void)brandKinesisOnPushClickInfo:(NSDictionary *)payload {
    if (hasStartObserving) {
        [self sendEventWithName:@"UpshotOnPushClickInfo" body:@{@"payload": [UpshotUtility convertJsonObjToJsonString:payload]}];
    } else {
        deeplinkInfo = payload;
    }
}

- (void)brandkinesisCampaignDetailsLoaded {

    [self sendEventWithName:@"UpshotCampaignDetailsLoaded" body:@{}];
}

- (dispatch_queue_t)methodQueue {
  return dispatch_get_main_queue();
}

- (void)didReceivePushPayload:(nonnull NSDictionary *)userInfo {
    [[BrandKinesis sharedInstance] setDelegate:self];
    [self sendEvent: userInfo];
}

- (void)didReceivePushToken:(NSString *)token {
    [self updateDeviceToken:token];
    [self sendEventWithName:@"UpshotPushToken" body:token];
}

@end

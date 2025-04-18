//
//  UpshotUtility.h
//  UpshotReact
//
//  Created by Vinod K on 9/3/20.
//  Copyright © 2020 [x]cubeLabs. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <Upshot/Upshot.h>

NS_ASSUME_NONNULL_BEGIN

@protocol UpshotUtilityDelegate <NSObject>

-(void)didReceivePushPayload:(NSDictionary *)userInfo;

@end


@interface UpshotUtility : NSObject

@property(nonatomic, weak, nullable) id delegate;

+(instancetype)sharedUtility;

+ (NSDictionary *)convertJsonStringToJson:(NSString *)jsonString;

+ (NSString *)convertJsonObjToJsonString:(id)json;

+ (NSString *)writeImageToTemp:(UIImage *)image withName:(NSString *)name;

+ (NSString *)getInfoTypeForKey:(NSString *)key;

+ (NSString *)getBadgesData:(NSDictionary *)badgesPayload;

+ (NSDictionary *)getObjectForKey:(NSString *)key withFileName:(NSString *)fileName;

+ (NSString *)validateString:(NSString *)value;

+ (UIColor *)colorFromHex:(NSString *)hexString;

+ (void)customizeLabel:(UILabel *)label withData:(NSDictionary *)data;

+ (void)customizeButton:(UIButton *)button withData:(NSDictionary *)data;

+ (void)customizeColor:(BKBGColor *)color withData:(NSString *)colorStr;

- (void)applicationDidRegisterWithDeviceToken:(NSData *)deviceToken;

- (void)didReceivePushNotifcationWithResponse:(NSDictionary *)userInfo;

- (NSString *)getDeviceToken;

- (NSArray *)getPushPayloads;

- (void)removeAllObjects;

@end

NS_ASSUME_NONNULL_END

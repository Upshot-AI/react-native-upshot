//
//  UpshotUtility.m
//  UpshotReact
//
//  Created by Vinod K on 9/3/20.
//  Copyright © 2020 [x]cubeLabs. All rights reserved.
//

#import "UpshotUtility.h"

@implementation UpshotUtility {
    
    NSString *device_token;
    NSMutableArray *pushPayloads;
}

+(instancetype)sharedUtility {
    
    static dispatch_once_t onceToken;
    static UpshotUtility *sharedUtility = nil;
       
    dispatch_once(&onceToken, ^{
        sharedUtility = [[UpshotUtility alloc] init];
    });
    return sharedUtility;
}

- (void)applicationDidRegisterWithDeviceToken:(NSData *)deviceToken {
    
    device_token = [self getTokenFromdata:deviceToken];
}

- (void)didReceivePushNotifcationWithResponse:(NSDictionary *)userInfo {
    
    if (pushPayloads == nil) {
        pushPayloads = [NSMutableArray array];
    }
    if (self.delegate && [self.delegate respondsToSelector:@selector(didReceivePushPayload:)]) {
        [self.delegate didReceivePushPayload:userInfo];
    } else {
        [pushPayloads addObject:userInfo];
    }
}

- (NSString *)getDeviceToken {
    return device_token;
}

- (NSArray *)getPushPayloads {        
    return pushPayloads;
}

- (void)removeAllObjects {
    [pushPayloads removeAllObjects];
}



+ (NSDictionary *)convertJsonStringToJson:(NSString *)jsonString {
  
  if(jsonString == nil) {
        return nil;
    }
  NSData *data = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
  NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableLeaves error:nil];
  return jsonDict;
}

+ (NSString *)convertJsonObjToJsonString:(id)json {
  
  NSError *error = nil;
  if(([json isKindOfClass:[NSDictionary class]] || [json isKindOfClass:[NSArray class]]) && json != nil) {
        
        NSData *data = [NSJSONSerialization dataWithJSONObject:json options:NSJSONWritingPrettyPrinted error:&error];
        if (error == nil) {
            NSString *jsonString = [[NSString alloc] initWithData:data encoding:kCFStringEncodingUTF8];
            return jsonString;
        }
    }
  return @"";
}

- (NSString *)getTokenFromdata:(NSData *)data {

  NSUInteger dataLength = data.length;
  if (dataLength == 0) {
    return nil;
  }

  const unsigned char *dataBuffer = (const unsigned char *)data.bytes;
  NSMutableString *hexString  = [NSMutableString stringWithCapacity:(dataLength * 2)];
  for (int i = 0; i < dataLength; ++i) {
    [hexString appendFormat:@"%02x", dataBuffer[i]];
  }
  return [hexString copy];
}

+ (NSString *)writeImageToTemp:(UIImage *)image withName:(NSString *)name {
    
    if (!image) {
        return nil;
    }
    NSString* tempPath = [NSSearchPathForDirectoriesInDomains(NSCachesDirectory,
                                                              NSUserDomainMask,
                                                              YES) lastObject];
  
    NSString *filePath = [tempPath stringByAppendingPathComponent:[NSString stringWithFormat:@"%@%@",name,@".png"]];
    NSData *pngData = UIImagePNGRepresentation(image);
    [pngData writeToFile:filePath atomically:YES];
    return filePath;
}

+ (NSString *)getInfoTypeForKey:(NSString *)key {
  
  NSArray *externalIdKeys = @[@"appuID",
                              @"facebookID",
                              @"twitterID",
                              @"foursquareID",
                              @"linkedinID",
                              @"googleplusID",
                              @"enterpriseUID",
                              @"advertisingID",
                              @"instagramID",
                              @"pinterest",
                              ];
  
  if ([externalIdKeys containsObject:key]) {
    return @"BKExternalId";
  }
  
  NSArray *dobKeys = @[@"year",
                       @"month",
                       @"day"];
  
  if ([dobKeys containsObject:key]) {
    return @"BKDob";
  }
  
  NSArray *userInfoKeys = @[@"lastName",
                            @"middleName",
                            @"firstName",
                            @"language",
                            @"occupation",
                            @"qualification",
                            @"maritalStatus",
                            @"phone",
                            @"localeCode",
                            @"userName",
                            @"email",
                            @"age",
                            @"gender",
                            @"email_opt",
                            @"sms_opt",
                            @"push_opt",
                            @"data_opt",
                            @"ip_opt"];
  
  if ([userInfoKeys containsObject:key]) {
    return @"UserInfo";
  }
  
  return  @"Others";
}

+ (NSString *)getBadgesData:(NSDictionary *)badgesPayload {
        
    NSMutableArray *activeBadges = badgesPayload[@"active_list"];
    NSMutableArray *inactiveBadges = badgesPayload[@"inactive_list"];

    for (int index = 0; index < [activeBadges count]; index++) {
        
        NSMutableDictionary *badge = [[NSMutableDictionary alloc] initWithDictionary:[activeBadges objectAtIndex:index]];
        [badge setObject:[UpshotUtility writeImageToTemp:badge[@"badgeImage"] withName:badge[@"badge"]] forKey:@"image"];
        [badge setObject:badge[@"badgeDesc"] forKey:@"desc"];
        [badge removeObjectForKey:@"badgeImage"];
        [badge removeObjectForKey:@"badgeDesc"];
        [activeBadges replaceObjectAtIndex:index withObject:badge];
    }
    
    for (int index = 0; index < [inactiveBadges count]; index++) {
        
        NSMutableDictionary *badge = [[NSMutableDictionary alloc] initWithDictionary:[inactiveBadges objectAtIndex:index]];
        [badge setObject:[UpshotUtility writeImageToTemp:badge[@"badgeImage"] withName:badge[@"badge"]] forKey:@"image"];
        [badge setObject:badge[@"badgeDesc"] forKey:@"desc"];
        [badge removeObjectForKey:@"badgeImage"];
        [badge removeObjectForKey:@"badgeDesc"];
        [inactiveBadges replaceObjectAtIndex:index withObject:badge];
    }
    
    NSDictionary *badges = @{@"active_list": activeBadges, @"inactive_list": inactiveBadges};
    NSString *jsonString = [UpshotUtility convertJsonObjToJsonString:badges];
    return jsonString;
}

+ (NSDictionary *)getObjectForKey:(NSString *)key withFileName:(NSString *)fileName {
    
    NSString *filePath =  [[NSBundle mainBundle] pathForResource:fileName ofType:@"json"];
    if (filePath != nil) {
        
        NSData *jsonData = [NSData dataWithContentsOfFile:filePath];
        if (jsonData != nil) {
            NSDictionary *response = [NSJSONSerialization JSONObjectWithData:jsonData options:NSJSONReadingMutableLeaves error:nil];
            
            if (response != nil && [response isKindOfClass:[NSDictionary class]]) {
                return response[key];
            }
            return nil;
        }
        return nil;
    }
    return  nil;
}

+ (NSString *)validateString:(NSString *)value {
    
    return value ? value : @"";
}

+ (UIColor *)colorFromHex:(NSString *)hexString {
    
    unsigned rgbValue = 0;
    NSString *colorValue = [hexString stringByTrimmingCharactersInSet:NSCharacterSet.whitespaceAndNewlineCharacterSet];
    colorValue = [colorValue stringByReplacingOccurrencesOfString:@"#" withString:@""];
    if (![colorValue length] || [colorValue isEqualToString:@""]) {
        return nil;
    }
    
    if([colorValue length] == 6) {
        NSScanner *scanner = [NSScanner scannerWithString:hexString];
        [scanner setScanLocation:1];
        [scanner scanHexInt:&rgbValue];
        return [UIColor colorWithRed:((rgbValue & 0XFF0000) >> 16)/255.0 green:((rgbValue & 0XFF00) >> 8)/255.0 blue:(rgbValue & 0XFF)/255.0 alpha:1.0];
    }
    
    if([colorValue length] == 8) {
        
        NSScanner *scanner = [NSScanner scannerWithString:hexString];
        [scanner setScanLocation:1];
        [scanner scanHexInt:&rgbValue];
        return [UIColor colorWithRed:((rgbValue & 0xff000000) >> 24)/255.0 green:((rgbValue & 0x00ff0000) >> 16)/255.0 blue:((rgbValue & 0x0000ff00) >> 8)/255.0 alpha: (rgbValue & 0x000000ff) / 255];

    }
    
    return nil;
    
   
}

+ (void)customizeLabel:(UILabel *)label withData:(NSDictionary *)data {
    
    if (data != nil && [data isKindOfClass:[NSDictionary class]]) {
        CGFloat size = [data[@"size"] floatValue];
        UIColor *textColor = [self colorFromHex:[self validateString:data[@"color"]]];
        UIFont *font = [UIFont fontWithName:[self validateString:data[@"font_name"]] size:size];
        
        if (textColor != nil) {
            [label setTextColor:textColor];
        }
        if (font != nil) {
            [label setFont:font];
        }
    }
}

+ (void)customizeButton:(UIButton *)button withData:(NSDictionary *)data {
    
    if (data != nil && [data isKindOfClass:[NSDictionary class]]) {
        
        CGFloat size = [data[@"size"] floatValue];
        UIColor *textColor = [self colorFromHex:[self validateString:data[@"color"]]];
        UIColor *border_color = [self colorFromHex:[self validateString:data[@"border_color"]]];
        UIColor *bgColor = [self colorFromHex:[self validateString:data[@"bgcolor"]]];
        UIFont *font = [UIFont fontWithName:[self validateString:data[@"font_name"]] size:size];
        UIImage *image = [UIImage imageNamed:[self validateString:data[@"image"]]];
        
        if (textColor != nil) {
            [button setTitleColor:textColor forState:UIControlStateNormal];
            [button setTitleColor:textColor forState:UIControlStateSelected];
            [button setTitleColor:textColor forState:UIControlStateHighlighted];
        }
        if (font != nil) {
            [button.titleLabel setFont:font];
        }
        if (bgColor != nil) {
            [button setBackgroundColor:bgColor];
        }
        
        if(border_color != nil) {
            [button.layer setBorderColor:border_color.CGColor];
            [button.layer setBorderWidth:1.5];
        }
        
        if (image != nil) {
            [button setImage:image forState:UIControlStateNormal];
            [button setImage:image forState:UIControlStateSelected];
            [button setImage:image forState:UIControlStateHighlighted];
        }                
    }
}

+ (void)customizeColor:(BKBGColor *)color withData:(NSString *)colorStr {
    
    UIColor *clr = [UpshotUtility colorFromHex:[UpshotUtility validateString:colorStr]];
    if (clr != nil) {
        [color setBackgroundColor:clr];
    }
}

@end

//
//  SEReferences.h
//  Bruin Swipes
//
//  Created by Matthew DeCoste on 11/6/13.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SEReferences : NSObject

+ (UIColor *)mainColor;
+ (UIColor *)altColor;
+ (NSString *)ratingForValue:(int)value;
+ (NSString *)ratingForUps:(int)ups downs:(int)downs;
+ (NSString *)localUserID; // TODO: replace with persistent storage of user

@end

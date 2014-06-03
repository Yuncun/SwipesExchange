//
//  SEAppDelegate.h
//  SwipesExchange
//
//  Created by Matthew DeCoste on 11/1.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SEAppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

// Google Backend
// Device token from APNS in string format
@property(nonatomic, copy) NSString *tokenString;

@end

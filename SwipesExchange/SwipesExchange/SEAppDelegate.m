//
//  SEAppDelegate.m
//  SwipesExchange
//
//  Created by Matthew DeCoste on 11/1.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import "SEAppDelegate.h"
#import "SEReferences.h"

// Google Backend
#import "CloudEntityCollection.h"
#import "CloudControllerHelper.h"

@implementation SEAppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    // Override point for customization after application launch.
	[self.window setTintColor:[SEReferences mainColor]];
	
	// Google Backend
#if (TARGET_IPHONE_SIMULATOR)
	NSLog(@"This application uses the push notification functionality. %@",
		  @"It has to be executed on a physical device instead of a simulator.");
	
	return NO;
#endif
	
	// Register for push notification
	UIRemoteNotificationType types =
	(UIRemoteNotificationTypeSound | UIRemoteNotificationTypeAlert);
	[[UIApplication sharedApplication] registerForRemoteNotificationTypes:types];
	
    return YES;
}

// Start Google Backend

- (void)application:(UIApplication *)application
didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
	NSString *token = [self hexStringFromData:deviceToken];
	NSLog(@"content---%@", token);
	
	// Save the token
	self.tokenString = token;
	
	// Notify controller that device token is received
	[[NSNotificationCenter defaultCenter]
	 postNotificationName:kCloudControllerDeviceTokenNotification
	 object:self];
}

// Returns an NSString object that contains a hexadecimal representation of the
// receiver’s contents.
- (NSString *)hexStringFromData:(NSData *)data {
	NSUInteger dataLength = [data length];
	NSMutableString *stringBuffer =
	[NSMutableString stringWithCapacity:dataLength * 2];
	const unsigned char *dataBuffer = [data bytes];
	for (int i=0; i<dataLength; i++) {
		[stringBuffer appendFormat:@"%02x", (NSUInteger)dataBuffer[i]];
	}
	
	return stringBuffer;
}

- (void)application:(UIApplication *)app
didFailToRegisterForRemoteNotificationsWithError:(NSError *)err {
	NSLog(@"%@", err);
}

- (void)application:(UIApplication *)application
didReceiveRemoteNotification:(NSDictionary *)userInfo {
	NSLog(@"Alert: %@", userInfo);
	
	NSString *message = userInfo[@"hiddenMessage"];
	// message is in the format of "<regId>:query:<clientSubId>" based on the
	// backend
	NSArray *tokens = [message componentsSeparatedByString:@":"];
	
	// Tokens are not expected, do nothing
	if ([tokens count] != 3) {
		NSLog(@"Message doesn't conform to the subId format at the backend: %@",
			  message);
		return;
	}
	
	// Token type isn't "query", do nothing
	if (![tokens[1] isEqual: @"query"]) {
		NSLog(@"Message is not in type QUERY: %@", message);
		return;
	}
	
	// Handle this push notification based on this topicID
	NSString *topicID = tokens[2]; // clientSubId
	CloudEntityCollection *entityCollection =
	[CloudEntityCollection sharedInstance];
	[entityCollection handlePushNotificationWithTopicID:topicID];
}

// End Google Backend




- (void)applicationWillResignActive:(UIApplication *)application
{
	// Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
	// Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
	// Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
	// If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
	// Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
	// Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
	// Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

@end

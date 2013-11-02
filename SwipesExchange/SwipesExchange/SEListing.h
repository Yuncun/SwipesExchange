//
//  SEListing.h
//  SwipesExchange
//
//  Created by Matthew DeCoste on 11/1.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SEUser.h"
#import "SEHalls.h"

@interface SEListing : NSObject

//@property (nonatomic) NSInteger *height;	// constant once set

@property (nonatomic) SEUser *user;
//@property (nonatomic) NSString *price;	// per ticket
@property (nonatomic) NSInteger *count;	// number of tickets
@property (nonatomic) SEHalls *halls;	// names of the halls.
										// TODO: Consider using utility class.

- (UIView *)listing44pt;
- (UIView *)listing55pt;
- (UIView *)listing66pt;

//- (UIView *)buyerListingWithHeight:(CGFloat)height;
//- (UIView *)sellerListingWithHeight:(CGFloat)height;

@end

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

@property (nonatomic) SEUser *user;
//@property (nonatomic) NSString *price;	// per ticket
@property (nonatomic) NSInteger count;	// number of tickets
@property (nonatomic) SEHalls *halls;	// names of the halls.
										// TODO: Consider using utility class.
@property (nonatomic) NSString *time;	// TODO: determine how to generate this. Should be of format "5:30pm - 7:00pm"
@property (nonatomic) NSString *startTime;
@property (nonatomic) NSString *endTime;

- (UIView *)listing;

- (UILabel *)topLeft;
- (UILabel *)bottomLeft;
- (UILabel *)topRight;
- (UILabel *)bottomRight;

@end

//
//  SEConversation.h
//  Bruin Swipes
//
//  Created by Matthew DeCoste on 12/6/13.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SEListing.h"
#import "SEUser.h"

@interface SEConversation : NSObject

@property (nonatomic, strong) SEListing *listing;
@property (nonatomic, strong) NSArray *messages;

@property (nonatomic, strong) SEUser *seller;
@property (nonatomic, strong) SEUser *buyer;

- (UILabel *)topLeft;
- (UILabel *)bottomLeft;
- (UILabel *)topRight;
- (UILabel *)bottomRight;

@end

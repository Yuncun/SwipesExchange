//
//  SEUser.h
//  Bruin Swipes
//
//  Created by Matthew DeCoste on 11/1/13.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SEUser : NSObject

@property (nonatomic) NSString *name;
@property (nonatomic) NSString *idNumber;
@property (nonatomic) NSArray *connections;
@property (nonatomic) NSArray *listings;

// TODO: figure out how to do this best
@property (nonatomic) NSString *rating;

@end

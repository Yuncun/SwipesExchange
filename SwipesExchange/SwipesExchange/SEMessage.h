//
//  SEMessage.h
//  Bruin Swipes
//
//  Created by Matthew DeCoste on 11/3/13.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import "SESendable.h"
#import "SEUser.h"

@interface SEMessage : SESendable

@property (nonatomic, strong) SEUser *user;
@property (nonatomic, strong) NSString *content;

@end

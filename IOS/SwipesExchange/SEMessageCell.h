//
//  SEMessageCell.h
//  Bruin Swipes
//
//  Created by Matthew DeCoste on 12/6/13.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SEMessage.h"

@interface SEMessageCell : UITableViewCell

@property (nonatomic, strong) SEMessage *message;
//@property (nonatomic) BOOL sentByLocalUser;

@end

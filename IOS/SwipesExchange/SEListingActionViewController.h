//
//  SEListingActionViewController.h
//  Bruin Swipes
//
//  Created by Matthew DeCoste on 11/11/13.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SEListing.h"

@interface SEListingActionViewController : UITableViewController

// this class is for the page where a user can issue a feeler

@property (nonatomic, strong) SEListing *listing;

@end

//
//  SEListing.m
//  SwipesExchange
//
//  Created by Matthew DeCoste on 11/1.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import "SEListing.h"

@implementation SEListing

@synthesize user = _user;
@synthesize price = _price;
@synthesize count = _count;
@synthesize halls = _halls;

- (UIView *)buyerListingWithHeight:(CGFloat)height
{
	// height should be 55 or 66
	UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320.f, height)];
	
	// buy listings will show dining halls, 
	
	return view;
}

- (UIView *)sellerListingWithHeight:(CGFloat)height
{
	// height should be 55 or 66
	UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320.f, height)];
	
	
	
	return view;
}

@end

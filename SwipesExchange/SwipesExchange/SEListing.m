//
//  SEListing.m
//  SwipesExchange
//
//  Created by Matthew DeCoste on 11/1.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import "SEListing.h"

@implementation SEListing

//@synthesize height = _height;

@synthesize user = _user;
//@synthesize price = _price;
@synthesize count = _count;
@synthesize halls = _halls;

- (instancetype)init
{
	self = [super init];
	
	if (self)
	{
//		_height = 44; // or 55 or 66?
	}
	
	return self;
}

- (UIView *)listing44pt
{
	return [[UIView alloc] initWithFrame:CGRectZero];
}

- (UIView *)listing55pt
{
	return [[UIView alloc] initWithFrame:CGRectZero];
}

- (UIView *)listing66pt
{
	return [[UIView alloc] initWithFrame:CGRectZero];
}

/*
- (UIView *)buyerListingWithHeight:(CGFloat)height
{
	// height should be 55 or 66
	UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320.f, height)];
	
	// buy listings will show user name/rating, time, and count
	// Name			   Count
	// Rating			Time
	
	
	
	return view;
}

- (UIView *)sellerListingWithHeight:(CGFloat)height
{
	// height should be 44, 55, or 66
	UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320.f, height)];
	
	// sell listings will show user name/rating, time, count, and price
	// Name		Count, Price
	// Rating			Time
	
	
	
	return view;
}
 */

@end

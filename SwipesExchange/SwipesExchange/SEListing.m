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
		
	}
	
	return self;
}

- (UIView *)listing
{
	return [[UIView alloc] initWithFrame:CGRectZero];
}

@end

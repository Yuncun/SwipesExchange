//
//  SEBuyListing.m
//  Bruin Swipes
//
//  Created by Matthew DeCoste on 11/2/13.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import "SEBuyListing.h"
#import "SEReferences.h"

#define TOP_ROW_SIZE	18.f
#define BOTTOM_ROW_SIZE	12.f

@implementation SEBuyListing

- (instancetype)init
{
	self = [super init];
	
	if (self)
	{
		// go crazy to make a demo
	}
	
	return self;
}

#pragma mark - Listing functions

- (UILabel *)topRight
{
	UILabel *num = [[UILabel alloc] initWithFrame:CGRectMake(180.f, 0.f, 105.f, 30.f)];
	[num setText:[NSString stringWithFormat:@"%d requested", (int)self.count]];
	[num setTextAlignment:NSTextAlignmentRight];
	[num setFont:[UIFont systemFontOfSize:TOP_ROW_SIZE]];
	
	return num;
}

@end

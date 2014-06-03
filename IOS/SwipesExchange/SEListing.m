//
//  SEListing.m
//  SwipesExchange
//
//  Created by Matthew DeCoste on 11/1.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import "SEListing.h"
#import "SEReferences.h"

#define TOP_ROW_SIZE	18.f
#define BOTTOM_ROW_SIZE	12.f

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
		// go crazy to make a demo
		SEUser *demoUser = [[SEUser alloc] init];
		[demoUser setName:@"Matthew DeCoste"];
		[demoUser setRating:[SEReferences ratingForUps:4 downs:1]];
		self.user = demoUser;
		self.startTime = @"4:00pm";
		self.endTime = @"7:00pm";
		self.count = 5;
	}
	
	return self;
}

#pragma mark - Time functions

- (NSString *)time
{
	return [NSString stringWithFormat:@"%@ - %@", self.startTime, self.endTime];
}

#pragma mark - Listing functions

- (UIView *)listing
{
	UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0.f, 0.f, 320.f, 44.f)];
	
	[view addSubview:[self topLeft]];
	[view addSubview:[self topRight]];
	[view addSubview:[self bottomLeft]];
	[view addSubview:[self bottomRight]];
	
	return view;
}

- (UILabel *)topLeft
{
	UILabel *name = [[UILabel alloc] initWithFrame:CGRectMake(15.f, 0.f, 160.f, 30.f)];
	[name setText:self.user.name];
	[name setTextAlignment:NSTextAlignmentLeft];
	[name setFont:[UIFont systemFontOfSize:TOP_ROW_SIZE]];
	
	return name;
}

- (UILabel *)bottomLeft
{
	UILabel *rating = [[UILabel alloc] initWithFrame:CGRectMake(15.f, 24.f, 160.f, 20.f)];
	[rating setText:self.user.rating];
	[rating setTextAlignment:NSTextAlignmentLeft];
	[rating setFont:[UIFont systemFontOfSize:BOTTOM_ROW_SIZE]];
	
	return rating;
}

- (UILabel *)topRight
{
	return [[UILabel alloc] initWithFrame:CGRectZero];
}

- (UILabel *)bottomRight
{
	UILabel *time = [[UILabel alloc] initWithFrame:CGRectMake(170.f, 24.f, 115.f, 20.f)];
	[time setText:[NSString stringWithFormat:@"%@ - %@", self.startTime, self.endTime]];
	[time setTextAlignment:NSTextAlignmentRight];
	[time setFont:[UIFont systemFontOfSize:BOTTOM_ROW_SIZE]];
	
	return time;
}

@end

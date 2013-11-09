//
//  SESellListing.m
//  Bruin Swipes
//
//  Created by Matthew DeCoste on 11/2/13.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import "SESellListing.h"
#import "SEReferences.h"

#define TOP_ROW_SIZE	18.f
#define BOTTOM_ROW_SIZE	12.f

@implementation SESellListing

@synthesize price = _price;

- (instancetype)init
{
	self = [super init];
	
	if (self)
	{
		//		_height = 44; // or 55 or 66?
		
		// go crazy to make a demo
		SEUser *demoUser = [[SEUser alloc] init];
		[demoUser setName:@"Matthew DeCoste"];
		[demoUser setRating:[SEReferences ratingForValue:4]];
		self.user = demoUser;
		self.startTime = @"4:00pm";
		self.endTime = @"7:00pm";
		self.count = 5;
		self.price = @"4.00";
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
	UILabel *num = [[UILabel alloc] initWithFrame:CGRectMake(180.f, 0.f, 105.f, 30.f)];
	[num setText:[NSString stringWithFormat:@"%d at $%@", (int)self.count, self.price]];
	[num setTextAlignment:NSTextAlignmentRight];
	[num setFont:[UIFont systemFontOfSize:TOP_ROW_SIZE]];
	
	return num;
}

- (UILabel *)bottomRight
{
	UILabel *time = [[UILabel alloc] initWithFrame:CGRectMake(170.f, 24.f, 115.f, 20.f)];
	[time setText:self.time];
	[time setTextAlignment:NSTextAlignmentRight];
	[time setFont:[UIFont systemFontOfSize:BOTTOM_ROW_SIZE]];
	
	return time;
}
@end

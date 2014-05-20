//
//  SEConversation.m
//  Bruin Swipes
//
//  Created by Matthew DeCoste on 12/6/13.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import "SEConversation.h"
#import "SEReferences.h"

#define TOP_ROW_SIZE	18.f
#define BOTTOM_ROW_SIZE	12.f

#define ACTIVE	@"Active"
#define DONE	@"Done"

@implementation SEConversation

@synthesize listing = _listing;
@synthesize messages = _messages;
@synthesize seller = _seller;
@synthesize buyer = _buyer;

- (SEUser *)otherUser
{
	if ([_seller.idNumber isEqualToString:[SEReferences localUserID]])
	{
		return _buyer;
	} else
	{
		return _seller;
	}
}

- (UILabel *)topLeft
{
	UILabel *name = [[UILabel alloc] initWithFrame:CGRectMake(15.f, 0.f, 160.f, 30.f)];
	[name setText:[self otherUser].name];
	[name setTextAlignment:NSTextAlignmentLeft];
	[name setFont:[UIFont systemFontOfSize:TOP_ROW_SIZE]];
	
	return name;
}

- (UILabel *)bottomLeft
{
	UILabel *rating = [[UILabel alloc] initWithFrame:CGRectMake(15.f, 24.f, 160.f, 20.f)];
	[rating setText:[self otherUser].rating];
	[rating setTextAlignment:NSTextAlignmentLeft];
	[rating setFont:[UIFont systemFontOfSize:BOTTOM_ROW_SIZE]];
	
	return rating;
}

- (UILabel *)topRight
{
	UILabel *num = [[UILabel alloc] initWithFrame:CGRectMake(180.f, 0.f, 105.f, 30.f)];
	int count = (int)_messages.count;
	if (count == 1) [num setText:[NSString stringWithFormat:@"%d message", count]];
	else [num setText:[NSString stringWithFormat:@"%d messages", count]];
	[num setTextAlignment:NSTextAlignmentRight];
	[num setFont:[UIFont systemFontOfSize:TOP_ROW_SIZE]];
	
	return num;
}

- (UILabel *)bottomRight
{
	UILabel *time = [[UILabel alloc] initWithFrame:CGRectMake(170.f, 24.f, 115.f, 20.f)];
	[time setText:ACTIVE];
	[time setTextAlignment:NSTextAlignmentRight];
	[time setFont:[UIFont systemFontOfSize:BOTTOM_ROW_SIZE]];
	
	return time;
}

@end

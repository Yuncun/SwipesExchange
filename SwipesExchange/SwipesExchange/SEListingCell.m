//
//  SEListingCell.m
//  Bruin Swipes
//
//  Created by Matthew DeCoste on 11/8/13.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import "SEListingCell.h"

@interface SEListingCell ()

@property (nonatomic) BOOL subviewed;

@property (nonatomic, strong) UILabel *topLeft;
@property (nonatomic, strong) UILabel *topRight;
@property (nonatomic, strong) UILabel *bottomLeft;
@property (nonatomic, strong) UILabel *bottomRight;

@property (nonatomic, strong) UIView *view;

@end

@implementation SEListingCell

@synthesize listing = _listing;

//- (SEListing *)listing
//{
//	if (_listing)
//	{
//		// lazy instantiation
//	}
//	return _listing;
//}

- (void)setListing:(SEListing *)listing
{
	if ([self.topLeft superview] != self)
	{
		NSLog(@"Initialized");
		[self addSubview:self.topLeft];
		[self addSubview:self.topRight];
		[self addSubview:self.bottomLeft];
		[self addSubview:self.bottomRight];
	}
	
	NSLog(@"Updated");
	
	self.topLeft = _listing.topLeft;
	self.topRight = _listing.topRight;
	self.bottomLeft = _listing.bottomLeft;
	self.bottomRight = _listing.bottomRight;
	
	self.view = _listing.listing;
}

- (void)setView:(UIView *)view
{
	if (!self.subviewed)
	{
		NSLog(@"View subbed in");
		[self addSubview:self.view];
	}
	NSLog(@"Set");
}

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
		NSLog(@"Initialized");
		[self addSubview:self.topLeft];
		[self addSubview:self.topRight];
		[self addSubview:self.bottomLeft];
		[self addSubview:self.bottomRight];
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end

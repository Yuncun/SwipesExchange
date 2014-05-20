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

@end

@implementation SEListingCell

@synthesize listing = _listing;

- (void)setListing:(SEListing *)listing
{
	_listing = listing;
	
	if (self.topLeft == nil)
	{
		self.topLeft = _listing.topLeft;
		self.topRight = _listing.topRight;
		self.bottomLeft = _listing.bottomLeft;
		self.bottomRight = _listing.bottomRight;
		
		[self addSubview:self.topLeft];
		[self addSubview:self.topRight];
		[self addSubview:self.bottomLeft];
		[self addSubview:self.bottomRight];
	}
	
	else
	{
		[self.topLeft setText:_listing.topLeft.text];
		[self.topRight setText:_listing.topRight.text];
		[self.bottomLeft setText:_listing.bottomLeft.text];
		[self.bottomRight setText:_listing.bottomRight.text];
	}
}

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
	}
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end

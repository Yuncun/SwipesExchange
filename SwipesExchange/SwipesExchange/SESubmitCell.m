//
//  SESubmitCell.m
//  Bruin Swipes
//
//  Created by Matthew DeCoste on 11/13/13.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import "SESubmitCell.h"
#import "SEReferences.h"

@interface SESubmitCell ()

@property (nonatomic, strong) UILabel *label;

@end

@implementation SESubmitCell

@synthesize title = _title;

- (void)setTitle:(NSString *)title
{
	_title = title;
	
	if (self.label == nil)
	{
		self.label = [[UILabel alloc] initWithFrame:CGRectMake(100, 5, 120, 34)];
		[self.label setText:_title];
		
		[self.label setFont:[UIFont systemFontOfSize:20.f]];
		[self.label setTextAlignment:NSTextAlignmentCenter];
		[self.label setTextColor:[SEReferences mainColor]];
		
		[self addSubview:self.label];
	}
	else
	{
		[self.label setText:_title];
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

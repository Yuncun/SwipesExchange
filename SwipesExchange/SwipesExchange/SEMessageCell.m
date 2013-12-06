//
//  SEMessageCell.m
//  Bruin Swipes
//
//  Created by Matthew DeCoste on 12/6/13.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import "SEMessageCell.h"
#import "SEReferences.h"

@interface SEMessageCell ()

@property (nonatomic, strong) UILabel *label;

@end

@implementation SEMessageCell

@synthesize message = _message;


- (void)setMessage:(SEMessage *)message
{
	_message = message;
	
	if (self.label == nil)
	{
		self.label = [[UILabel alloc] initWithFrame:CGRectMake(15, 5, 290, 34)];
		[self.label setText:_message.content];
		
		[self.label setFont:[UIFont systemFontOfSize:14.f]];
		[self modifyLabel];
		
		[self addSubview:self.label];
	}
	else
	{
		[self.label setText:_message.content];
	}
}

// ideally this does not exist
- (void)modifyLabel
{
	if ([self.message.user.idNumber isEqualToString:[SEReferences localUserID]])
	{
		[self.label setTextAlignment:NSTextAlignmentRight];
		[self.label setTextColor:[SEReferences mainColor]];
	}
	else
	{
		[self.label setTextAlignment:NSTextAlignmentLeft];
		[self.label setTextColor:[UIColor colorWithWhite:0.4f alpha:1.f]];
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

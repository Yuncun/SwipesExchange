//
//  SEConversationCell.m
//  Bruin Swipes
//
//  Created by Matthew DeCoste on 12/6/13.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import "SEConversationCell.h"

@interface SEConversationCell ()

@property (nonatomic, strong) UILabel *topLeft;
@property (nonatomic, strong) UILabel *topRight;
@property (nonatomic, strong) UILabel *bottomLeft;
@property (nonatomic, strong) UILabel *bottomRight;

@end

@implementation SEConversationCell

@synthesize conversation = _conversation;

- (void)setConversation:(SEConversation *)conversation
{
	_conversation = conversation;
	
	if (self.topLeft == nil)
	{
		self.topLeft = _conversation.topLeft;
		self.topRight = _conversation.topRight;
		self.bottomLeft = _conversation.bottomLeft;
		self.bottomRight = _conversation.bottomRight;
		
		[self addSubview:self.topLeft];
		[self addSubview:self.topRight];
		[self addSubview:self.bottomLeft];
		[self addSubview:self.bottomRight];
	}
	
	else
	{
		[self.topLeft setText:_conversation.topLeft.text];
		[self.topRight setText:_conversation.topRight.text];
		[self.bottomLeft setText:_conversation.bottomLeft.text];
		[self.bottomRight setText:_conversation.bottomRight.text];
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

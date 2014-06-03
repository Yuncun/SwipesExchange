//
//  SESIgnInViewController.m
//  Bruin Swipes
//
//  Created by Matthew DeCoste on 11/15/13.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import "SESIgnInViewController.h"
#import "SEListingCell.h"
#import "SESellListing.h"
#import "SEUser.h"
#import "SEReferences.h"

@interface SESIgnInViewController ()

@property (weak, nonatomic) IBOutlet UITextField *textField;
@property (weak, nonatomic) IBOutlet SEListingCell *listingCell;

@property (strong, nonatomic) SEUser *user;
@property (strong, nonatomic) SESellListing *listing;

@end

@implementation SESIgnInViewController

// hit the done button and things are done
- (BOOL)textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    return YES;
}

- (void)textFieldDidEndEditing:(UITextField *)textField
{
	NSString *name = textField.text;
	if (textField == nil || [name isEqualToString:@""]) name = @"Name here";
	
	[self.user setName:name];
	[self.listing setUser:self.user];
	[self.listingCell setListing:self.listing];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
	
	self.user = [[SEUser alloc] init];
	[self.user setName:@""];
	[self.user setRating:[SEReferences ratingForValue:5]];
	
	self.listing = [[SESellListing alloc] init];
	[self.listing setCount:1];
	
	[self textFieldDidEndEditing:nil];
}

- (void)viewDidAppear:(BOOL)animated
{
	[super viewDidAppear:animated];
	
	[self.textField becomeFirstResponder];
}

- (void)viewWillDisappear:(BOOL)animated
{
	[super viewWillDisappear:animated];
	
	[self.textField resignFirstResponder];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end

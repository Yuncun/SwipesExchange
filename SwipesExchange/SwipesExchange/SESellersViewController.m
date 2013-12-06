//
//  SESellersViewController.m
//  SwipesExchange
//
//  Created by Matthew DeCoste on 11/1.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import "SESellersViewController.h"
#import "SESellListing.h"
#import "SEReferences.h"
#import "SEListingCell.h"
#import "SEListingActionViewController.h"

@interface SESellersViewController ()

@property (nonatomic, strong) NSArray *headerArray;
@property (nonatomic, strong) NSArray *dataArray;
@property (nonatomic, strong) SESellListing *destinationListing;

@end

@implementation SESellersViewController

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];

    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
 
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
	
	// set tint
	self.navigationController.navigationBar.barTintColor = [SEReferences altColor];
//    self.navigationController.navigationBar.translucent = NO;

	self.headerArray = @[@"De Neve", @"Covel", @"Feast", @"Hedrick", @"Sproul"];
	
	SESellListing *nd1 = [[SESellListing alloc] init];
	[nd1.user setName:@"Kate Linton"];
	[nd1.user setRating:[SEReferences ratingForUps:7 downs:0]];
	[nd1 setCount:1];
	[nd1 setStartTime:@"12:00pm"];
	[nd1 setEndTime:@"2:00pm"];
	[nd1 setPrice:@"4.00"];
	SESellListing *nd2 = [[SESellListing alloc] init];
	[nd2.user setName:@"Eli Rockwell"];
	[nd2.user setRating:[SEReferences ratingForUps:4 downs:2]];
	[nd2 setCount:2];
	[nd2 setStartTime:@"1:00pm"];
	[nd2 setEndTime:@"2:30pm"];
	[nd2 setPrice:@"3.00"];
	
	SESellListing *c1 = [[SESellListing alloc] init];
	[c1.user setName:@"Joe Bruin"];
	[c1.user setRating:[SEReferences ratingForUps:6 downs:0]];
	[c1 setCount:1];
	[c1 setStartTime:@"6:00pm"];
	[c1 setEndTime:@"7:00pm"];
	[c1 setPrice:@"4.00"];
	
	SESellListing *f1 = [[SESellListing alloc] init];
	[f1.user setName:@"Holly Hill"];
	[f1.user setRating:[SEReferences ratingForUps:8 downs:1]];
	[f1 setCount:1];
	[f1 setStartTime:@"5:00pm"];
	[f1 setEndTime:@"8:00pm"];
	[f1 setPrice:@"4.00"];
	SESellListing *f2 = [[SESellListing alloc] init];
	[f2.user setName:@"Andrew Abraham"];
	[f2.user setRating:[SEReferences ratingForUps:1 downs:2]];
	[f2 setCount:1];
	[f2 setStartTime:@"5:45pm"];
	[f2 setEndTime:@"7:00pm"];
	[f2 setPrice:@"3.00"];
	SESellListing *f3 = [[SESellListing alloc] init];
	[f3.user setName:@"Valerie Katz"];
	[f3.user setRating:[SEReferences ratingForUps:5 downs:1]];
	[f3 setCount:3];
	[f3 setStartTime:@"7:00pm"];
	[f3 setEndTime:@"7:45pm"];
	[f3 setPrice:@"4.00"];
	
	SESellListing *h1 = [[SESellListing alloc] init];
	[h1.user setName:@"Parker Lane"];
	[h1.user setRating:[SEReferences ratingForUps:4 downs:0]];
	[h1 setCount:1];
	[h1 setStartTime:@"6:00pm"];
	[h1 setEndTime:@"7:15pm"];
	[h1 setPrice:@"5.00"];
	
	self.dataArray = @[@[h1], @[nd2], @[nd1, f3], @[f2, f1], @[c1]];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (SESellListing *)dataObjectAtIndexPath:(NSIndexPath *)ip
{
	return [[self.dataArray objectAtIndex:ip.section] objectAtIndex:ip.row];
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return self.headerArray.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    return [[self.dataArray objectAtIndex:section] count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    SEListingCell *cell = [tableView dequeueReusableCellWithIdentifier:@"Cell" forIndexPath:indexPath];
	
	// Configure the cell...
	[cell setListing:[self dataObjectAtIndexPath:indexPath]];
	[cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
	
	return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
	return 24.f;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
	UIView *header = [[UIView alloc] initWithFrame:CGRectMake(15, 4, 305, 20)];
	UILabel *label = [[UILabel alloc] initWithFrame:header.frame];
	[label setFont:[UIFont systemFontOfSize:15]];
	[label setText:[self.headerArray objectAtIndex:section]];
	[label setTextColor:[UIColor colorWithWhite:0.f alpha:0.65f]];
	
	[header addSubview:label];
	
	return header;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	// set destination listing
	self.destinationListing = [self dataObjectAtIndexPath:indexPath];
	
	// perform segue
	[self performSegueWithIdentifier:@"followListing" sender:self];
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
	if ([segue.identifier isEqualToString:@"followListing"])
	{
		SEListingActionViewController *dest = [segue destinationViewController];
		[dest setListing:self.destinationListing];
	}
}

/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    }   
    else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
{
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

/*
#pragma mark - Navigation

// In a story board-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}

 */

@end

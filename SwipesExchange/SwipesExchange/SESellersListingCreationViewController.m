//
//  SESellersListingCreationViewController.m
//  Bruin Swipes
//
//  Created by Matthew DeCoste on 11/6/13.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import "SESellersListingCreationViewController.h"
#import "SESellListing.h"
#import "SEUser.h"
#import "SEReferences.h"

@interface SESellersListingCreationViewController ()

@property (nonatomic, strong) NSArray *sectionTitles;
@property (nonatomic, strong) NSArray *cellsPerSection;

@property (nonatomic, strong) SESellListing *sellListing;

@end

@implementation SESellersListingCreationViewController

@synthesize sellListing = _sellListing;

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
	
	self.sectionTitles = @[@"Time and place", @"How many?", @"Preview", @""];
	self.cellsPerSection = @[@[@"Start Time", @"End Time", @"Place"], @[@"Count", @"Price"], @[@"Preview"], @[@"Submit"]];
	
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
 
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Sell Listing functions

- (SESellListing *)sellListing
{
	if (_sellListing == nil)
	{
		// Lazy instantiation!
		
		_sellListing = [[SESellListing alloc] init];
		[_sellListing setStartTime:@"5:00pm"];
		[_sellListing setEndTime:@"8:00pm"];
//		[_sellListing setUser:<#(SEUser *)#>];
		[_sellListing setCount:1];
//		[_sellListing setHalls:<#(SEHalls *)#>];
		[_sellListing setPrice:@"4.00"];
	}
	
	return _sellListing;
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return self.sectionTitles.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    return [[self.cellsPerSection objectAtIndex:section] count];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSArray *arr = [self.cellsPerSection objectAtIndex:indexPath.section];
	NSString *cellIden = [arr objectAtIndex:indexPath.row];
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIden forIndexPath:indexPath];
    
	// clear out old stuff
#warning There has got to be a better way..
	id shortcut = [[[cell subviews] objectAtIndex:0] subviews];
	for (int i = 0; i < (int)[shortcut count]; i++)
	{
		NSString *className = [[[shortcut objectAtIndex:i] class] description];
		if ([className isEqualToString:@"UIView"])
			[[shortcut objectAtIndex:i] removeFromSuperview];
	}
	
    // Configure the cell...
    
	// remove all previous subviews? .............
	
	if ([cellIden isEqualToString:@"Preview"])
	{
		// take device user information
		// for now this is default code:
		SEUser *defUser = [[SEUser alloc] init];
		[defUser setName:@"Joe Bruin"];
		[defUser setRating:[SEReferences ratingForValue:4]];
		
		// take count information
		
		// take time information
		
		// take place information
		
		// create the preview, add it as subview
		
		[self.sellListing setUser:defUser];
		
		UIView *view = [self.sellListing listing];
		
		[cell addSubview:view];
		[cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
	} else if ([cellIden isEqualToString:@"Submit"])
	{
		UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0.f, 0.f, 320.f, 44.f)];
		
		UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(100, 5, 120, 34)];
		[label setText:@"Submit"];
		
		[label setFont:[UIFont systemFontOfSize:20.f]];
		[label setTextAlignment:NSTextAlignmentCenter];
		[label setTextColor:[SEReferences mainColor]];
		
		[view addSubview:label];
		[cell addSubview:view];
	} else
	{
		[cell.textLabel setText:cellIden];
		NSString *detailText;
		
		if ([cellIden isEqualToString:@"Start Time"])
		{
			detailText = self.sellListing.startTime;
		}
		
		if ([cellIden isEqualToString:@"End Time"])
		{
			detailText = self.sellListing.endTime;
		}
		
		if ([cellIden isEqualToString:@"Place"])
		{
			detailText = @"All dining halls";
			// TODO: plug this in
		}
		
		if ([cellIden isEqualToString:@"Count"])
		{
			int count = (int)self.sellListing.count;
			if (count == 1) detailText = @"1 swipe";
			else detailText = [NSString stringWithFormat:@"%d swipes", count];
		}
		
		if ([cellIden isEqualToString:@"Price"])
		{
			detailText = [NSString stringWithFormat:@"$%@ each", self.sellListing.price];
		}
		
		[cell.detailTextLabel setText:detailText];
	}
	
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
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

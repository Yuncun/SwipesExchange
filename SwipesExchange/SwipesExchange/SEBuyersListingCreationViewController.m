//
//  SEBuyersListingCreationViewController.m
//  Bruin Swipes
//
//  Created by Matthew DeCoste on 11/4/13.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import "SEBuyersListingCreationViewController.h"
#import "SEBuyListing.h"
#import "SEUser.h"

@interface SEBuyersListingCreationViewController ()

@property (nonatomic, strong) NSArray *sectionTitles;
@property (nonatomic, strong) NSArray *cellsPerSection;

@end

@implementation SEBuyersListingCreationViewController

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
	self.cellsPerSection = @[@[@"Time", @"Place"], @[@"Count"], @[@"Preview"], @[@"Submit"]];

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

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
	NSLog(@"%d", (int)self.sectionTitles.count);
    return self.sectionTitles.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
	NSLog(@"%d \t %d", (int)section, (int)[[self.cellsPerSection objectAtIndex:section] count]);
    return [[self.cellsPerSection objectAtIndex:section] count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSArray *arr = [self.cellsPerSection objectAtIndex:indexPath.section];
	NSString *cellIden = [arr objectAtIndex:indexPath.row];
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIden forIndexPath:indexPath];
    
	// clear out old stuff
#warning There has got to be a better way..	NSArray *array = [cell subviews];
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
		
		// take count information
		
		// take time information
		
		// take place information
		
		// create the preview, add it as subview
		
		SEBuyListing *sbl = [[SEBuyListing alloc] init];
		[sbl setUser:defUser];
		[sbl setTime:@"4:00pm - 7:00pm"];
		[sbl setCount:5];
		
		UIView *view = [sbl listing44pt];
		
		[cell addSubview:view];
	}
	
	
//	UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(0.f, 0.f, 320.f, 44.f)];
//	[label setText:cellIden];
//	[label setFont:[UIFont systemFontOfSize:20.f]];
//	
//	[cell addSubview:label];
	
    return cell;
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

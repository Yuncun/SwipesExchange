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

@end

@implementation SESellersListingCreationViewController

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
	self.cellsPerSection = @[@[@"Time", @"Place"], @[@"Count", @"Price"], @[@"Preview"], @[@"Submit"]];
	
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
		[defUser setName:@"Joe Bruin"];
		[defUser setRating:@"★★★★☆"];
		
		// take count information
		
		// take time information
		
		// take place information
		
		// create the preview, add it as subview
		
		SESellListing *ssl = [[SESellListing alloc] init];
		[ssl setUser:defUser];
		[ssl setTime:@"4:00pm - 7:00pm"];
		[ssl setCount:5];
		[ssl setPrice:@"4.00"];
		
		UIView *view = [ssl listing];
		
		[cell addSubview:view];
		[cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
	}
	
	if ([cellIden isEqualToString:@"Submit"])
	{
		UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0.f, 0.f, 320.f, 44.f)];
		
		UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(100, 5, 120, 34)];
		[label setText:@"Submit"];
		
		[label setFont:[UIFont systemFontOfSize:20.f]];
		[label setTextAlignment:NSTextAlignmentCenter];
		[label setTextColor:[SEReferences mainColor]];
		
		[view addSubview:label];
		[cell addSubview:view];
	}
	
	if ([cellIden isEqualToString:@"Time"])
	{
		UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0.f, 0.f, 320.f, 44.f)];
		
		UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(15.f, 0.f, 100.f, 43.f)];
		[label setText:@"Time"];
		[label setFont:[UIFont systemFontOfSize:18.f]];
		[label setTextAlignment:NSTextAlignmentLeft];
		
		UILabel *detail = [[UILabel alloc] initWithFrame:CGRectMake(155.f, 11.f, 150.f, 21.f)];
		[detail setText:@"4:00pm - 7:00pm"];
		[detail setFont:[UIFont systemFontOfSize:17.f]];
		[detail setTextColor:[UIColor colorWithWhite:0.56f alpha:1.f]];
		[detail setTextAlignment:NSTextAlignmentRight];
		
		[view addSubview:label];
		[view addSubview:detail];
		[cell addSubview:view];
	}
	
	if ([cellIden isEqualToString:@"Place"])
	{
		UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0.f, 0.f, 320.f, 44.f)];
		
		UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(15.f, 0.f, 100.f, 43.f)];
		[label setText:@"Place"];
		[label setFont:[UIFont systemFontOfSize:18.f]];
		[label setTextAlignment:NSTextAlignmentLeft];
		
		UILabel *detail = [[UILabel alloc] initWithFrame:CGRectMake(155.f, 11.f, 150.f, 21.f)];
		[detail setText:@"All dining halls"];
		[detail setFont:[UIFont systemFontOfSize:17.f]];
		[detail setTextColor:[UIColor colorWithWhite:0.56f alpha:1.f]];
		[detail setTextAlignment:NSTextAlignmentRight];
		
		[view addSubview:label];
		[view addSubview:detail];
		[cell addSubview:view];
	}
	
	if ([cellIden isEqualToString:@"Count"])
	{
		UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0.f, 0.f, 320.f, 44.f)];
		
		UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(15.f, 0.f, 100.f, 43.f)];
		[label setText:@"Count"];
		[label setFont:[UIFont systemFontOfSize:18.f]];
		[label setTextAlignment:NSTextAlignmentLeft];
		
		UILabel *detail = [[UILabel alloc] initWithFrame:CGRectMake(155.f, 11.f, 150.f, 21.f)];
		[detail setText:@"5 swipes"];
		[detail setFont:[UIFont systemFontOfSize:17.f]];
		[detail setTextColor:[UIColor colorWithWhite:0.56f alpha:1.f]];
		[detail setTextAlignment:NSTextAlignmentRight];
		
		[view addSubview:label];
		[view addSubview:detail];
		[cell addSubview:view];
	}
	
	if ([cellIden isEqualToString:@"Price"])
	{
		UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0.f, 0.f, 320.f, 44.f)];
		
		UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(15.f, 0.f, 100.f, 43.f)];
		[label setText:@"Price"];
		[label setFont:[UIFont systemFontOfSize:18.f]];
		[label setTextAlignment:NSTextAlignmentLeft];
		
		UILabel *detail = [[UILabel alloc] initWithFrame:CGRectMake(155.f, 11.f, 150.f, 21.f)];
		[detail setText:@"$4.00 each"];
		[detail setFont:[UIFont systemFontOfSize:17.f]];
		[detail setTextColor:[UIColor colorWithWhite:0.56f alpha:1.f]];
		[detail setTextAlignment:NSTextAlignmentRight];
		
		[view addSubview:label];
		[view addSubview:detail];
		[cell addSubview:view];
	}
	
	//	UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(0.f, 0.f, 320.f, 44.f)];
	//	[label setText:cellIden];
	//	[label setFont:[UIFont systemFontOfSize:20.f]];
	//
	//	[cell addSubview:label];
	
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

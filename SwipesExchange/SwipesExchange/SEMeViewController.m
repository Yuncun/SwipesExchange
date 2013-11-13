//
//  SEMeViewController.m
//  Bruin Swipes
//
//  Created by Matthew DeCoste on 11/10/13.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import "SEMeViewController.h"
#import "SEReferences.h"

#define kCellIden		@"cellIdentifier"
#define kCellTitle		@"cellTitle"
#define kCellDetail		@"cellDetail"
#define kCellAcc		@"cellAccessory"

#define kCellRightDet	@"detail"
#define kCellCustom		@"custom"

#define kCellAccNone	@0
#define kCellAccDisc	@1

@interface SEMeViewController ()

@property (nonatomic, strong) NSArray *cellArray;

@end

@implementation SEMeViewController

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
	
	NSDictionary *dictOne = @{kCellIden		: @"detail",
							  kCellTitle	: @"Name",
							  kCellDetail	: @"Joe Bruin",
							  kCellAcc		: @1 };
	
	NSDictionary *dictTwo = @{kCellIden		: @"detail",
							  kCellTitle	: @"Rating",
							  kCellDetail	: @"None yet",
							  kCellAcc		: @0 };
	
	NSDictionary *dictThree = @{kCellIden	: @"detail",
							  kCellTitle	: @"ID Number",
							  kCellDetail	: @"None yet",
							  kCellAcc		: @0 };
	
	self.cellArray = @[@[dictOne, dictTwo], @[dictThree]];
	
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
    return self.cellArray.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    return [[self.cellArray objectAtIndex:section] count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSDictionary *dict = [[self.cellArray objectAtIndex:indexPath.section] objectAtIndex:indexPath.row];
	
	NSString *cellIdentifier = [dict objectForKey:kCellIden];
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier forIndexPath:indexPath];
    
    // Configure the cell...
	[cell.textLabel setText:[dict objectForKey:kCellTitle]];
	[cell.detailTextLabel setText:[dict objectForKey:kCellDetail]];
	
	NSNumber *num = [dict objectForKey:kCellAcc];
	switch ([num intValue]) {
		case 0:
			[cell setAccessoryType:UITableViewCellAccessoryNone];
			break;
		case 1:
			[cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
			break;
		default:
			break;
	}
    
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

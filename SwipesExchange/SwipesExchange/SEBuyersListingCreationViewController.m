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
#import "SEReferences.h"
#import "SEListingCell.h"

#define kPickerAnimationDuration    0.40   // duration for the animation to slide the date picker into view
#define kPickerTag              99     // view tag identifiying the date picker view

#define kTitleKey	@"title"	// key for obtaining the data source item's title
#define kTimeKey	@"time"		// key for obtaining the data source item's time
#define kStringKey	@"string"	// key for obtaining the data source item's string
#define kHallKey	@"halls"	// key for obtaining the data source item's hall(s)

// keep track of which rows have date cells
#define kDateStartRow   1
#define kDateEndRow     2

static NSString *kDateCellID = @"stringCell";		// the cells with the start or end date
static NSString *kPickerID = @"stringPicker";	// the cell containing the date picker
static NSString *kHallCellID = @"hallCell";			// opens new page to pick halls
static NSString *kOtherCell = @"otherCell";			// the remaining cells at the end

@interface SEBuyersListingCreationViewController ()

@property (nonatomic, strong) NSArray *dataArray;

@property (nonatomic, strong) NSArray *sectionTitles;
@property (nonatomic, strong) NSArray *cellsPerSection;

// keep track which indexPath points to the cell with UIDatePicker
@property (nonatomic, strong) NSIndexPath *pickerIndexPath;
@property (assign) NSInteger pickerCellRowHeight;
@property (weak, nonatomic) IBOutlet UIPickerView *pickerView;

@property (nonatomic, strong) SEBuyListing *buyListing; // use this to store values

@end

@implementation SEBuyersListingCreationViewController

@synthesize buyListing = _buyListing;

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
	
	// setup our data source
    NSMutableDictionary *itemOne = [@{ kTitleKey : @"Start Time",
									   kStringKey : [NSDate date] } mutableCopy];
    NSMutableDictionary *itemTwo = [@{ kTitleKey : @"End Time",
                                       kStringKey : [NSDate date] } mutableCopy];
    NSMutableDictionary *itemThree = [@{ kTitleKey : @"Place" } mutableCopy];
    NSMutableDictionary *itemFour = [@{ kTitleKey : @"Count" } mutableCopy];
    NSMutableDictionary *itemFive = [@{ kTitleKey : @"Price" } mutableCopy];
	
	// also preview and submit
	
    self.dataArray = @[itemOne, itemTwo, itemThree, itemFour, itemFive];
	
	// obtain the picker view cell's height, works because the cell was pre-defined in our storyboard
    UITableViewCell *pickerViewCellToCheck = [self.tableView dequeueReusableCellWithIdentifier:kPickerID];
    self.pickerCellRowHeight = pickerViewCellToCheck.frame.size.height;
	
	
	self.sectionTitles = @[@"Time and place", @"How many?", @"Preview", @""];
	self.cellsPerSection = @[@[@"Start Time", @"End Time", @"Place"], @[@"Count"], @[@"Preview"], @[@"Submit"]];

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

#pragma mark - Buy Listing functions

- (SEBuyListing *)buyListing
{
	if (_buyListing == nil)
	{
		// lazy instantiation with default data!
		
		_buyListing = [[SEBuyListing alloc] init];
		[_buyListing setStartTime:@"5:00pm"];
		[_buyListing setEndTime:@"8:00pm"];
//		[_buyListing setUser:<#(SEUser *)#>]; set with default user
		[_buyListing setCount:1];
//		[_buyListing setHalls:<#(SEHalls *)#>];
	}
	
	
	return _buyListing;
}

/*! Determines if the given indexPath has a cell below it with a UIDatePicker.
 
 @param indexPath The indexPath to check if its cell has a UIDatePicker below it.
 */
- (BOOL)hasPickerForIndexPath:(NSIndexPath *)indexPath
{
    BOOL hasDatePicker = NO;
    
    NSInteger targetedRow = indexPath.row;
    targetedRow++;
    
    UITableViewCell *checkDatePickerCell =
	[self.tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:targetedRow inSection:0]];
    UIDatePicker *checkDatePicker = (UIDatePicker *)[checkDatePickerCell viewWithTag:kPickerTag];
    
    hasDatePicker = (checkDatePicker != nil);
    return hasDatePicker;
}

/*! Updates the UIDatePicker's value to match with the date of the cell above it.
 */
- (void)updateDatePicker
{
    if (self.pickerIndexPath != nil)
    {
        UITableViewCell *associatedDatePickerCell = [self.tableView cellForRowAtIndexPath:self.pickerIndexPath];
        
        UIDatePicker *targetedDatePicker = (UIDatePicker *)[associatedDatePickerCell viewWithTag:kPickerTag];
        if (targetedDatePicker != nil)
        {
            // we found a UIDatePicker in this cell, so update it's date value
            //
            NSDictionary *itemData = self.dataArray[self.pickerIndexPath.row - 1];
			// TODO: convert in data!
//            [targetedDatePicker setDate:[itemData valueForKey:kDateKey] animated:NO];
        }
    }
}

/*! Determines if the UITableViewController has a UIDatePicker in any of its cells.
 */
- (BOOL)hasInlineDatePicker
{
    return (self.pickerIndexPath != nil);
}

/*! Determines if the given indexPath points to a cell that contains the UIDatePicker.
 
 @param indexPath The indexPath to check if it represents a cell with the UIDatePicker.
 */
- (BOOL)indexPathHasPicker:(NSIndexPath *)indexPath
{
    return ([self hasInlineDatePicker] && self.pickerIndexPath.row == indexPath.row);
}

/*! Determines if the given indexPath points to a cell that contains the start/end dates.
 
 @param indexPath The indexPath to check if it represents start/end date cell.
 */
- (BOOL)indexPathHasDate:(NSIndexPath *)indexPath
{
    BOOL hasDate = NO;
    
    if ((indexPath.row == kDateStartRow) ||
        (indexPath.row == kDateEndRow || ([self hasInlineDatePicker] && (indexPath.row == kDateEndRow + 1))))
    {
        hasDate = YES;
    }
    
    return hasDate;
}


#pragma mark - Table view data source

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return ([self indexPathHasPicker:indexPath] ? self.pickerCellRowHeight : self.tableView.rowHeight);
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return self.sectionTitles.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
//    if ([self hasInlineDatePicker])
//    {
//        // we have a date picker, so allow for it in the number of rows in this section
//        NSInteger numRows = self.dataArray.count;
//        return ++numRows;
//    }
//    
//    return self.dataArray.count;
	
	// Return the number of rows in the section.
    return [[self.cellsPerSection objectAtIndex:section] count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    // new?
	/*
	{
		UITableViewCell *cell = nil;
		
		NSString *cellID = kOtherCell;
		
		if ([self indexPathHasPicker:indexPath])
		{
			// the indexPath is the one containing the inline date picker
			cellID = kPickerID;     // the current/opened date picker cell
		}
		else if ([self indexPathHasDate:indexPath])
		{
			// the indexPath is one that contains the date information
			cellID = kDateCellID;       // the start/end date cells
		}
		
		NSLog(@"%@", cellID);
		
		cell = [tableView dequeueReusableCellWithIdentifier:cellID];
		
		if (indexPath.row == 0)
		{
			// we decide here that first cell in the table is not selectable (it's just an indicator)
			cell.selectionStyle = UITableViewCellSelectionStyleNone;
		}
		
		// if we have a date picker open whose cell is above the cell we want to update,
		// then we have one more cell than the model allows
		//
		NSInteger modelRow = indexPath.row;
		if (self.pickerIndexPath != nil && self.pickerIndexPath.row < indexPath.row)
		{
			modelRow--;
		}
		
		NSDictionary *itemData = self.dataArray[modelRow];
		
		// proceed to configure our cell
		if ([cellID isEqualToString:kDateCellID])
		{
			// we have either start or end date cells, populate their date field
			//
			cell.textLabel.text = [itemData valueForKey:kTitleKey];
//			cell.detailTextLabel.text = [self.dateFormatter stringFromDate:[itemData valueForKey:kDateKey]];
		}
		else if ([cellID isEqualToString:kOtherCell])
		{
			// this cell is a non-date cell, just assign it's text label
			//
			cell.textLabel.text = [itemData valueForKey:kTitleKey];
		}
		
		return cell;
	}
	*/
	
	// old?
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
			//		SEListingCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIden forIndexPath:indexPath];
			
			// take device user information
			// for now this is default code:
			SEUser *defUser = [[SEUser alloc] init];
			[defUser setName:@"Joe Bruin"];
			[defUser setRating:[SEReferences ratingForValue:4]];
			
			// take count information
			
			// take time information
			
			// take place information
			
			// create the preview, add it as subview
			
			[self.buyListing setUser:defUser];
			
			UIView *view = [self.buyListing listing];
			
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
				detailText = self.buyListing.startTime;
			}
			
			if ([cellIden isEqualToString:@"End Time"])
			{
				detailText = self.buyListing.endTime;
			}
			
			if ([cellIden isEqualToString:@"Place"])
			{
				detailText = @"All dining halls";
				// TODO: plug this in
			}
			
			if ([cellIden isEqualToString:@"Count"])
			{
				int count = (int)self.buyListing.count;
				if (count == 1) detailText = @"1 swipe";
				else detailText = [NSString stringWithFormat:@"%d swipes", count];
			}
			
			[cell.detailTextLabel setText:detailText];
		}
		
		return cell;
	}
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

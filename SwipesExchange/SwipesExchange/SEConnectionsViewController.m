//
//  SEConnectionsViewController.m
//  SwipesExchange
//
//  Created by Matthew DeCoste on 11/1.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import "SEConnectionsViewController.h"
#import "SEReferences.h"
#import "SEConversation.h"
#import "SEConversationCell.h"
#import "SEConversationViewController.h"

#import "SEMessage.h"
#import "SESellListing.h"

@interface SEConnectionsViewController ()

@property (nonatomic, strong) NSArray *dataArray;
@property (nonatomic, strong) SEConversation *destinationConversation; // for selecting

@end

@implementation SEConnectionsViewController

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
    self.navigationItem.rightBarButtonItem = self.editButtonItem;
	
	// set tint
	self.navigationController.navigationBar.barTintColor = [SEReferences altColor];
	//    self.navigationController.navigationBar.translucent = NO;
	
	// put in data
	
	SEUser *buyer = [[SEUser alloc] init]; // is me
	[buyer setName:@"Josephine Bruin"];
	[buyer setRating:[SEReferences ratingForUps:8 downs:0]];
	[buyer setIdNumber:@"Nope"];
	
	SEUser *me = [[SEUser alloc] init];
	[me setName:@"Joe Bruin"];
	[me setRating:[SEReferences ratingForUps:5 downs:0]];
	[me setIdNumber:[SEReferences localUserID]];
	
	SESellListing *listing = [[SESellListing alloc] init];
	[listing setUser:me];
	[listing setCount:1];
	[listing setStartTime:@"6:00pm"];
	[listing setEndTime:@"7:15pm"];
	[listing setPrice:@"$4.00"];
	
	SEMessage *m1 = [[SEMessage alloc] init];
	[m1 setUser:buyer];
	[m1 setContent:@"I only have $3. That okay?"];
	
	SEMessage *m2 = [[SEMessage alloc] init];
	[m2 setUser:me];
	[m2 setContent:@"That's fine. Meet by the benches at 6:30?"];
	
	SEMessage *m3 = [[SEMessage alloc] init];
	[m3 setUser:buyer];
	[m3 setContent:@"Sounds good!"];
	
	SEConversation *c = [[SEConversation alloc] init];
	[c setSeller:me];
	[c setBuyer:buyer];
	[c setListing:listing];
	[c setMessages:@[m1, m2, m3]];
	
	self.dataArray = @[c];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (SEConversation *)dataObjectAtIndexPath:(NSIndexPath *)ip
{
	return [self.dataArray objectAtIndex:ip.row];
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    return self.dataArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    SEConversationCell *cell = [tableView dequeueReusableCellWithIdentifier:@"Cell" forIndexPath:indexPath];
	
	// Configure the cell...
	[cell setConversation:[self dataObjectAtIndexPath:indexPath]];
	[cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	self.destinationConversation = [self dataObjectAtIndexPath:indexPath];
	
	// perform segue
	[self performSegueWithIdentifier:@"followConversation" sender:self];
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
	if ([segue.identifier isEqualToString:@"followConversation"])
	{
		SEConversationViewController *dest = [segue destinationViewController];
		[dest setConversation:self.destinationConversation];
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

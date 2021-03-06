//
//  SEReferences.m
//  Bruin Swipes
//
//  Created by Matthew DeCoste on 11/6/13.
//  Copyright (c) 2013 CS130SwipesExchange. All rights reserved.
//

#import "SEReferences.h"

@implementation SEReferences

+ (UIColor *)mainColor
{
	return [UIColor colorWithRed:39.f/255.f green:100.f/255.f blue:247.f/255.f alpha:1.f];
}

+ (UIColor *)altColor
{
//	return [UIColor colorWithRed:255.f/255.f green:247.f/255.f blue:125.f/255.f alpha:1.f]; // original
	return [UIColor colorWithRed:255.f/255.f green:249.f/255.f blue:154.f/255.f alpha:1.f];
//	return [UIColor colorWithRed:255.f/255.f green:251.f/255.f blue:184.f/255.f alpha:1.f]; // subtler
}

+ (NSString *)ratingForValue:(int)value
{
	NSString *toReturn = @"";
	for (int i = 0; i < 5; i++)
	{
		if (value > i) toReturn = [toReturn stringByAppendingString:@"★"];
		else toReturn = [toReturn stringByAppendingString:@"☆"];
	}
	return toReturn;
	
	// consider also just returning like 1.4 ★ or thumbs up thumbs down
}

+ (NSString *)ratingForUps:(int)ups downs:(int)downs
{
	return [NSString stringWithFormat:@"%d✓\t%d✗", ups, downs];
}

+ (NSString *)localUserID
{
	return @"MatthewDeCoste";
}

@end

CS 144 Project 2
Team Last Minute
Andrew Yoon, Jiexi Luan



**************************
*    PART B Questions    *
**************************
1. List your relations. Please specify all keys that hold on each relation. You need not specify attribute types at this stage.

Items
{ItemID, Name, Categories, Currently, Buy_Price, First_Bid, Number_of_Bids, Bids, Location, Latitude, Longitude, Country, Started, Ends, Seller, Description}

Bids
{BidID, Bidder, Time, Amount, Rating}

Categories
{Category Name, ItemID}

Users
{UserID, Location, Country, BidderRating, SellerRating}

2. List all completely nontrivial functional dependencies that hold on each relation, excluding those that effectively specify keys.

ItemID -> Name, Categories, Currently, Buy_Price, First_Bid, Number_of_Bids, Bids, Location(ID), Country, Started, Ends, Seller(UserID), Description

Location(ID) -> Latitude, Longitude

BidID -> Bidder(UserID), Time, Amount

UserID -> SellerRating, BidderRating, Location(ID), Country

3. Are all of your relations in Boyce-Codd Normal Form (BCNF)? If not, either redesign them and start over, or explain why you feel it is advantageous to use non-BCNF relations.

No, our database structure does not have a Location table like the functional dependency recommends. We feel this method is advantageous because only Items ever have a latitude and longitude associated with their Location. Additionally, Locations do not make good keys because things like “Cleveland,OH” and “Cleveland Ohio” both have the same longitude and latitude, but their location ‘key’ is not the same and would be stored in separate tuples if we had a Location table. If this happens, we would not be saving more space in the database anyway.

4. Are all of your relations in Fourth Normal Form (4NF)? If not, either redesign them and start over, or explain why you feel it is advantageous to use non-4NF relations.

We were unable to find any multivariable dependencies in the schema.





**************************
*  Additional Comments   *
**************************
We spent a total of about 7.5 hours on this project, and both of us collaborated on each part of the lab. The lab was straightforward for the most part; the biggest difficulty we had was with escaping special characters in the data, so that our parser wouldn't get confused with processing fields containing commas or double quotes. In particular, we had to deal with the case of truncating an Item's description when the last character is a '\'. Eventually we were able to figure out this issue so that our data was parsed successfully. The only known issue with our code is that the last query is off by -1. We believe this is because of an unknown bug in one of our tables/parser outputs that is causing a single tuple to be left out, possibly due to a special character not being escaped.


WE FAILED TO MENTION THIS IN OUR ORIGINAL PROJECT 2. (Otherwise, our files are the same as our submission)
NOTE: We escaped commas, newlines, and quotes using html entities &#44;, &quote;, &#10; respectively!
      This may affect the grading script if it is directly comparing the string contents.

#1
SELECT COUNT(*) FROM Users;

#2
SELECT COUNT(*) FROM Items WHERE BINARY Location="New York";

#3
SELECT COUNT(*) FROM
  (SELECT COUNT(*) AS count
    FROM Categories
    GROUP BY ItemID)
  AS a
  WHERE a.count = 4;

#4
SELECT i.id FROM (SELECT id, Currently FROM Items
    WHERE (Currently < Buy_Price || (Buy_Price=0 && Number_of_Bids>0))
      && Ends > '2001-12-20 00:00:01') as i,
  (SELECT Currently FROM Items
    WHERE (Currently < Buy_Price || (Buy_Price=0 && Number_of_Bids>0))
      && Ends > '2001-12-20 00:00:01'
    ORDER BY Currently DESC
    LIMIT 1) AS max_price WHERE i.Currently = max_price.Currently;

#5
SELECT COUNT(*) FROM Users
  WHERE SellerRating > 1000;

#6
SELECT COUNT(*) FROM
  (SELECT DISTINCT(SellerID) FROM Items) as i,
  (SELECT DISTINCT(UserID) FROM Bids) as b
  WHERE i.SellerID = b.UserID;

#7
SELECT COUNT(DISTINCT(c.Name))
  FROM Categories as c,
  (SELECT DISTINCT(ItemID) as ItemID 
    FROM Bids 
    WHERE Amount > 100) as b 
  WHERE c.ItemID = b.ItemID;
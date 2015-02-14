CREATE TABLE Locations (
  ItemID INT NOT NULL PRIMARY KEY,
  Location POINT NOT NULL,
  SPATIAL INDEX(Location)
)
ENGINE = MyISAM;

INSERT INTO Locations (ItemID, Location)
SELECT id,Point(Latitude, Longitude)
FROM Items;
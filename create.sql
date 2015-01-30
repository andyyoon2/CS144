CREATE TABLE Users(
  id VARCHAR(100) NOT NULL PRIMARY KEY,
  Location VARCHAR(100),
  Country VARCHAR(100),
  BidderRating INT DEFAULT 0,
  SellerRating INT DEFAULT 0
) ENGINE = InnoDB;

CREATE TABLE Items(
  id INT NOT NULL PRIMARY KEY,
  Name VARCHAR(100) NOT NULL,
  Currently DECIMAL(8, 2) NOT NULL,
  Buy_Price DECIMAL(8, 2),
  First_Bid DECIMAL(8, 2) NOT NULL,
  Number_of_Bids INT DEFAULT 0,
  Location VARCHAR(100) NOT NULL,
  Latitude DECIMAL(9,6),
  Longitude DECIMAL(9,6),
  Country VARCHAR(100) NOT NULL,
  Started TIMESTAMP NOT NULL,
  Ends TIMESTAMP NOT NULL,
  SellerID VARCHAR(100) NOT NULL,
  FOREIGN KEY (SellerID)
    REFERENCES Users(id),
  Description VARCHAR(4000)
) ENGINE = InnoDB;

CREATE TABLE Categories(
  ItemID INT NOT NULL,
  FOREIGN KEY (ItemID)
    REFERENCES Items(id),
  Name VARCHAR(100) NOT NULL
) ENGINE = InnoDB;

CREATE TABLE Bids(
  ItemID INT NOT NULL,
  FOREIGN KEY (ItemID)
    REFERENCES Items(id),
  UserID VARCHAR(100) NOT NULL,
  FOREIGN KEY (UserID)
    REFERENCES Users(id),
  Time TIMESTAMP NOT NULL,
  Amount DECIMAL(8,2) NOT NULL
) ENGINE = InnoDB;
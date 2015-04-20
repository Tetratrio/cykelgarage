set foreign_key_checks = 0;

drop table if exists User;
drop table if exists Bike;

CREATE TABLE User (
  username CHAR(10),
  info TEXT,
  password CHAR(4),
  checkedIn TIMESTAMP DEFAULT 0,
    PRIMARY KEY (username)
);

CREATE TABLE Bike (
  bikeId INT(13) AUTO_INCREMENT,
  username CHAR(10),
    PRIMARY KEY (bikeId),
    FOREIGN KEY (username) REFERENCES User (username)
);


set foreign_key_checks = 1;

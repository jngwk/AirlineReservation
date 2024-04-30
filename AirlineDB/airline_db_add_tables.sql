USE airlinedb;

CREATE TABLE flights(
id	int	AUTO_INCREMENT,
flightNum varchar(7),
origin varchar(5),
destination varchar(5),
dptDate Datetime,
arrDate Datetime,
seatsLeft int,
PRIMARY KEY(id));

-- Insert mock data into flights table
INSERT INTO flights (flightNum, origin, destination, dptDate, arrDate, seatsLeft) VALUES
('KE5075', 'USA', 'KOR', '2024-03-01 08:00:00', '2024-03-02 01:00:00', 350),
('UA202', 'USA', 'CAN', '2024-03-02 10:30:00', '2024-03-02 13:45:00', 400),
('LH112', 'GER', 'FRA', '2024-03-03 12:45:00', '2024-03-03 14:30:00', 380),
('BA415', 'FRA', 'UK', '2024-03-04 14:15:00', '2024-03-04 16:00:00', 390),
('AF780', 'FRA', 'KOR', '2024-03-05 16:45:00', '2024-03-06 10:30:00', 360),
('EK512', 'USA', 'UAE', '2024-03-06 18:30:00', '2024-03-07 23:45:00', 410),
('QF303', 'USA', 'AUS', '2024-03-07 20:15:00', '2024-03-07 05:30:00', 375),
('SQ106', 'CHN', 'SGP', '2024-03-08 22:00:00', '2024-03-09 02:30:00', 420),
('DL409', 'IND', 'USA', '2024-03-09 23:45:00', '2024-03-10 06:15:00', 390),
('TK212', 'FRA', 'TUR', '2024-03-10 01:30:00', '2024-03-10 03:45:00', 380),
('AA607', 'CAN', 'USA', '2024-03-11 03:15:00', '2024-03-11 07:00:00', 400),
('CX708', 'USA', 'HKG', '2024-03-12 05:00:00', '2024-03-12 09:30:00', 370),
('IB205', 'FRA', 'ESP', '2024-03-13 06:45:00', '2024-03-13 10:15:00', 360),
('EY814', 'IND', 'UAE', '2024-03-14 08:30:00', '2024-03-14 13:00:00', 380),
('KL609', 'FRA', 'NLD', '2024-03-15 10:15:00', '2024-03-15 12:45:00', 375),
('JL815', 'USA', 'JPN', '2024-03-16 12:00:00', '2024-03-16 19:30:00', 400),
('UA717', 'AUS', 'USA', '2024-03-17 14:30:00', '2024-03-17 22:45:00', 390),
('LX321', 'FRA', 'CHE', '2024-03-18 16:15:00', '2024-03-18 18:30:00', 395),
('TG102', 'USA', 'THA', '2024-03-19 18:00:00', '2024-03-19 23:15:00', 405),
('BA901', 'GER', 'UK', '2024-03-20 19:45:00', '2024-03-21 00:30:00', 385),
('AF607', 'CAN', 'FRA', '2024-03-21 21:30:00', '2024-03-22 02:45:00', 375),
('EK401', 'USA', 'UAE', '2024-03-22 23:15:00', '2024-03-23 04:30:00', 395),
('QF607', 'USA', 'AUS', '2024-03-23 01:00:00', '2024-03-23 09:15:00', 410),
('UA508', 'GER', 'USA', '2024-03-24 02:45:00', '2024-03-24 08:00:00', 385),
('LH901', 'FRA', 'GER', '2024-03-25 04:30:00', '2024-03-25 07:45:00', 380),
('BA303', 'USA', 'UK', '2024-03-26 06:15:00', '2024-03-26 11:30:00', 395),
('CX509', 'FRA', 'HKG', '2024-03-27 08:00:00', '2024-03-27 12:15:00', 400),
('AA303', 'CAN', 'USA', '2024-03-28 09:45:00', '2024-03-28 14:00:00', 390),
('EK815', 'IND', 'UAE', '2024-03-29 11:30:00', '2024-03-29 17:45:00', 380),
('KL909', 'FRA', 'NLD', '2024-03-30 13:15:00', '2024-03-30 16:30:00', 385),
('JL601', 'USA', 'JPN', '2024-03-31 15:00:00', '2024-03-31 22:15:00', 400),
('UA405', 'AUS', 'USA', '2024-04-01 16:45:00', '2024-04-01 01:00:00', 395),
('LX608', 'FRA', 'CHE', '2024-04-02 18:30:00', '2024-04-02 20:45:00', 390),
('TG812', 'USA', 'THA', '2024-04-03 20:15:00', '2024-04-03 01:30:00', 405),
('BA207', 'GER', 'UK', '2024-04-04 22:00:00', '2024-04-05 03:15:00', 380);

SELECT * FROM flights;

CREATE TABLE bookings(
reservationId int DEFAULT (NOW() + 0),
flightId int,
memberId varchar(30),
PRIMARY KEY(reservationId),
FOREIGN KEY(flightId) REFERENCES flights(id)
); 

ALTER TABLE bookings
RENAME COLUMN reservationId TO bookingId;

SELECT * FROM bookings;
DESC bookings;
CREATE TABLE members(
id varchar(20),
pwd varchar(20),
firstName varchar(20),
lastName varchar(20),
passport varchar(20),
sex char(1) CHECK (sex = 'M' OR sex = 'F'),
PRIMARY KEY(id)
);

UPDATE flights SET seatsLeft = seatsLeft-1 WHERE id = ;

SELECT * FROM flights;

use airlinedb;

CREATE VIEW bookingsWithSeats AS (SELECT 

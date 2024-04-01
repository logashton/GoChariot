CREATE DATABASE gochariot;

\c gochariot;

CREATE TABLE Users (
    UserId SERIAL PRIMARY KEY,
    Email VARCHAR(255) UNIQUE NOT NULL,
    PasswordHash VARCHAR(255) NOT NULL,
    PasswordSalt VARCHAR(255) NOT NULL,
    CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Driver (
    DriverId SERIAL PRIMARY KEY,
    HoursClocked INTEGER DEFAULT 0,
    Rides INTEGER DEFAULT 0,
    FullName VARCHAR(255) NOT NULL
);

CREATE TABLE Admin (
    AdminId SERIAL PRIMARY KEY,
    FullName VARCHAR(255) NOT NULL
);

-- Create Student table
CREATE TABLE Student (
    StudentId SERIAL PRIMARY KEY,
    Rides INTEGER DEFAULT 0,
    FullName VARCHAR(255) NOT NULL
);

CREATE TABLE Logins (
    LoginId SERIAL PRIMARY KEY,
    UserId INTEGER REFERENCES Users(UserId),
    LoginTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    IPAddress VARCHAR(50),
    UserAgent VARCHAR(255)
);

CREATE TABLE Reviews (
    ReviewId SERIAL PRIMARY KEY,
    UserId INTEGER REFERENCES Users(UserId),
    DriverId INTEGER REFERENCES Driver(DriverId),
    Rating DECIMAL(3, 2) NOT NULL,
    Content TEXT,
    CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Routes (
    RouteId SERIAL PRIMARY KEY,
    DriverId INTEGER REFERENCES Driver(DriverId),
    Name VARCHAR(255) NOT NULL,
    StartPoint VARCHAR(255) NOT NULL,
    EndPoint VARCHAR(255) NOT NULL,
    StartTime TIMESTAMP NOT NULL,
    EndTime TIMESTAMP NOT NULL
);

-- Create Alerts table
CREATE TYPE TargetAudience AS ENUM ('Driver', 'Admin', 'Student');

CREATE TABLE Alerts (
    AlertId SERIAL PRIMARY KEY,
    UserId INTEGER REFERENCES Users(UserId),
    Content TEXT,
    Title TEXT,
    TargetAudience TargetAudience,
    CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create Request table
CREATE TABLE Request (
    RequestId SERIAL PRIMARY KEY,
    PickUp VARCHAR(255) NOT NULL,
    DropOff VARCHAR(255) NOT NULL,
    UserId INTEGER REFERENCES Users(UserId),
    requestTime TIMESTAMP NOT NULL
);

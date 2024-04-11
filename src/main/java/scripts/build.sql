CREATE DATABASE test_gochariot;

\c test_gochariot;

CREATE TABLE Users (
                       UserId SERIAL PRIMARY KEY,
                       Email VARCHAR(255) UNIQUE NOT NULL,
                       PasswordHash VARCHAR(255) NOT NULL,
                       PasswordSalt VARCHAR(255) NOT NULL,
                       UserName VARCHAR(255) UNIQUE NOT NULL,
                       FirstName VARCHAR(255) NOT NULL,
                       LastName VARCHAR(255) NOT NULL,
                       CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       UpdatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Roles (
                       RoleId SERIAL PRIMARY KEY,
                       RoleName VARCHAR(50) UNIQUE NOT NULL
);

INSERT INTO Roles (RoleName) VALUES ('Student'), ('Driver'), ('Admin');

CREATE TABLE UserRoles (
                           UserId INTEGER REFERENCES Users(UserId),
                           RoleId INTEGER REFERENCES Roles(RoleId),
                           PRIMARY KEY (UserId, RoleId)
);

CREATE TABLE Drivers (
                        DriverId SERIAL PRIMARY KEY,
                        UserId INTEGER UNIQUE REFERENCES Users(UserId),
                        HoursClocked INTEGER DEFAULT 0,
                        Rides INTEGER DEFAULT 0,
                        FullName VARCHAR(255) NOT NULL
);

CREATE TABLE Admins (
                       AdminId SERIAL PRIMARY KEY,
                       UserId INTEGER UNIQUE REFERENCES Users(UserId),
                       FullName VARCHAR(255) NOT NULL
);

CREATE TABLE Students (
                         StudentId SERIAL PRIMARY KEY,
                         UserId INTEGER UNIQUE REFERENCES Users(UserId),
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
                         DriverId INTEGER REFERENCES Drivers(DriverId),
                         Rating DECIMAL(3, 2) NOT NULL,
                         Content TEXT,
                         CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Routes (
                        RouteId SERIAL PRIMARY KEY,
                        DriverId INTEGER REFERENCES Drivers(DriverId),
                        Name VARCHAR(255) NOT NULL,
                        StartPoint VARCHAR(255) NOT NULL,
                        EndPoint VARCHAR(255) NOT NULL,
                        StartTime TIMESTAMP NOT NULL,
                        EndTime TIMESTAMP NOT NULL
);

CREATE TYPE TargetAudience AS ENUM ('Driver', 'Admin', 'Student');

CREATE TABLE Alerts (
                        AlertId SERIAL PRIMARY KEY,
                        UserId INTEGER REFERENCES Users(UserId),
                        Content TEXT,
                        Title TEXT,
                        TargetAudience TargetAudience,
                        CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Requests (
                         RequestId SERIAL PRIMARY KEY,
                         PickUp VARCHAR(255) NOT NULL,
                         DropOff VARCHAR(255) NOT NULL,
                         UserId INTEGER REFERENCES Users(UserId),
                         RequestTime TIMESTAMP NOT NULL
);

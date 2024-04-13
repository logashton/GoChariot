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
                       UpdatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       AccountNonExpired BOOLEAN NOT NULL DEFAULT TRUE,
                       AccountNonLocked BOOLEAN NOT NULL DEFAULT TRUE,
                       CredentialsNonExpired BOOLEAN NOT NULL DEFAULT TRUE,
                       Enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE Role (
                       RoleId SERIAL PRIMARY KEY,
                       RoleName VARCHAR(50) UNIQUE NOT NULL
);

INSERT INTO Role (RoleName) VALUES ('Student'), ('Driver'), ('Admin');

CREATE TABLE UserRole (
                           UserId INTEGER REFERENCES Users(UserId),
                           RoleId INTEGER REFERENCES Role(RoleId),
                           PRIMARY KEY (UserId, RoleId)
);

CREATE TABLE Driver (
                        DriverId SERIAL PRIMARY KEY,
                        UserId INTEGER UNIQUE REFERENCES Users(UserId),
                        HoursClocked INTEGER DEFAULT 0,
                        Rides INTEGER DEFAULT 0
);

CREATE TABLE Admin (
                       AdminId SERIAL PRIMARY KEY,
                       UserId INTEGER UNIQUE REFERENCES Users(UserId)
);

CREATE TABLE Student (
                         StudentId SERIAL PRIMARY KEY,
                         UserId INTEGER UNIQUE REFERENCES Users(UserId),
                         Rides INTEGER DEFAULT 0
);

CREATE TABLE Login (
                        LoginId SERIAL PRIMARY KEY,
                        UserId INTEGER REFERENCES Users(UserId),
                        LoginTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        IPAddress VARCHAR(50),
                        UserAgent VARCHAR(255)
);

CREATE TABLE Review (
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

CREATE TYPE TargetAudience AS ENUM ('Driver', 'Admin', 'Student');

CREATE TABLE Alert (
                        AlertId SERIAL PRIMARY KEY,
                        UserId INTEGER REFERENCES Users(UserId),
                        Content TEXT,
                        Title TEXT,
                        TargetAudience TargetAudience,
                        CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Request (
                         RequestId SERIAL PRIMARY KEY,
                         PickUp VARCHAR(255) NOT NULL,
                         DropOff VARCHAR(255) NOT NULL,
                         UserId INTEGER REFERENCES Users(UserId),
                         RequestTime TIMESTAMP NOT NULL
);

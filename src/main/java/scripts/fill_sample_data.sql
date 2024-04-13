\c test_gochariot;

INSERT INTO Users (Email, PasswordHash, PasswordSalt, UserName, FirstName, LastName, CreatedAt, UpdatedAt) VALUES
                                                                                                               ('student@email.com', '$2a$12$TIvVq2oqroUVOGHRE1FZ4.mQhABSQvU4xDZSJzoAWn6mHUN4C1qty', 'a', 'student1', 'John', 'Doe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                               ('driver@email.com', '$2a$12$TIvVq2oqroUVOGHRE1FZ4.mQhABSQvU4xDZSJzoAWn6mHUN4C1qty', 'a', 'driver1', 'Jane', 'Doe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                               ('admin@email.com', '$2a$12$TIvVq2oqroUVOGHRE1FZ4.mQhABSQvU4xDZSJzoAWn6mHUN4C1qty', 'a', 'admin1', 'Admin', 'User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO UserRole (UserId, RoleId) VALUES
                                           (1, 1),
                                           (2, 2),
                                           (3, 3);

INSERT INTO Student (UserId, Rides) VALUES (1, 5);

INSERT INTO Driver (UserId, HoursClocked, Rides) VALUES (2, 5, 15);

INSERT INTO Admin (UserId) VALUES (3);

INSERT INTO Routes (DriverId, Name, StartPoint, EndPoint, StartTime, EndTime) VALUES
                                                                                  (1, 'Example loop A', 'Point A', 'Point B', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (1, 'Example loop B', 'Point C', 'Point D', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (1, 'Example loop C', 'Point E', 'Point F', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO Review (UserId, DriverId, Rating, Content, CreatedAt) VALUES
    (1, 1, 4.5, 'Example review', CURRENT_TIMESTAMP);

INSERT INTO Request (PickUp, DropOff, UserId, RequestTime) VALUES
    ('Point A', 'Point B', 1, CURRENT_TIMESTAMP);

INSERT INTO Login (UserId, LoginTime, IPAddress, UserAgent) VALUES
    (1, CURRENT_TIMESTAMP, '1.1.1.1', 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko)');

INSERT INTO Alert (UserId, Content, Title, TargetAudience, CreatedAt) VALUES
    (2, 'Example alert', 'Example alert title', 'Student', CURRENT_TIMESTAMP);

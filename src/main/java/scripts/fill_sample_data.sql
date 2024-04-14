\c test_gochariot;

INSERT INTO Users (Email, PasswordHash, PasswordSalt, UserName, FirstName, LastName, CreatedAt, UpdatedAt) VALUES
                                                                                                               ('student@email.com', '$2a$12$TIvVq2oqroUVOGHRE1FZ4.mQhABSQvU4xDZSJzoAWn6mHUN4C1qty', 'a', 'student1', 'John', 'Doe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                               ('driver@email.com', '$2a$12$TIvVq2oqroUVOGHRE1FZ4.mQhABSQvU4xDZSJzoAWn6mHUN4C1qty', 'a', 'driver1', 'Jane', 'Doe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                               ('admin@email.com', '$2a$12$TIvVq2oqroUVOGHRE1FZ4.mQhABSQvU4xDZSJzoAWn6mHUN4C1qty', 'a', 'admin1', 'Admin', 'User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                               ('student2@email.com', '$2a$12$TIvVq2oqroUVOGHRE1FZ4.mQhABSQvU4xDZSJzoAWn6mHUN4C1qty', 'a', 'student2', 'Jason', 'Smith', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                               ('driver2@email.com', '$2a$12$TIvVq2oqroUVOGHRE1FZ4.mQhABSQvU4xDZSJzoAWn6mHUN4C1qty', 'a', 'driver2', 'Jon', 'Snow', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO UserRole (UserId, RoleId) VALUES
                                           (1, 1),
                                           (2, 2),
                                           (3, 3),
                                           (4, 1),
                                           (5, 2);

INSERT INTO Student (UserId, Rides) VALUES (1, 5), (4, 3);

INSERT INTO Driver (UserId, HoursClocked, Rides) VALUES (2, 5, 15), (5, 7, 25);

INSERT INTO Admin (UserId) VALUES (3);

INSERT INTO Routes (DriverId, Name, StartPoint, EndPoint, StartTime, EndTime) VALUES
                                                                                  (1, 'Example loop A', 'Point A', 'Point B', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (1, 'Example loop B', 'Point C', 'Point D', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                  (1, 'Example loop C', 'Point E', 'Point F', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO Review (UserId, DriverFirstName, DriverLastName, Rating, Content, CreatedAt) VALUES
    (1, 'Jane', 'Doe', 4.5, 'Example review', CURRENT_TIMESTAMP),
    (4, 'Jon', '/><script>alert(''xss test'');</script>', 1, 'Epic review', CURRENT_TIMESTAMP),
    (4, 'Jane', 'Doe', 0, 'Awesome review', CURRENT_TIMESTAMP),
    (1, 'Jane', 'Doe', 5, 'Cool review asdfklj', CURRENT_TIMESTAMP),
    (4, 'Jon', 'Snow', 3.5, 'Slightly cooler review', CURRENT_TIMESTAMP);



INSERT INTO Request (PickUp, DropOff, UserId, RequestTime) VALUES
    ('Point A', 'Point B', 1, CURRENT_TIMESTAMP);

INSERT INTO Login (UserId, LoginTime, IPAddress, UserAgent) VALUES
    (1, CURRENT_TIMESTAMP, '1.1.1.1', 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko)');

INSERT INTO Alert (UserId, Content, Title, TargetAudience, CreatedAt) VALUES
    (2, 'Example alert', 'Example alert title', 'Student', CURRENT_TIMESTAMP);

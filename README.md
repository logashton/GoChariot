## GoChariot

## Team Members
>Logan Phillips, Hasan Hashim, Madelyn Good

## Nickname
>GC

## Description
>GoChariot (GC) is a web application, accessible via a web browser, that is used to track and interact with UNCG's Spartan Chariots.
>GC is designed to reimplement UNCG's Chariot tracking system and expand upon it by adding features that are useful for the UNCG community as a whole.
>The motivation behind GC is due to the fact that UNCG's current Spartan Chariot tracking is clunky and lacks any meaningful features.
>The goal of GC is to create a web app that is useful, easy to use, and increases safety for both students and drivers.
>This goal will be accomplished by numerous features for students and drivers. Students will be able to request rides, view/add driver reviews, and track bus routes. Drivers will be able to send out safety alerts, view their reviews, and view ride requests.


## How to run
You will have to install PostgreSQL and set up a local server.
After setting up the PostgreSQL server, you will open a psql shell and import the build.sql located in /scripts to run it (see instructions.txt in /scripts if necessary).
Also, import the fill_sample_data.sql file so you have some sample data to work with. If you do not want to use psql, you could use pgadmin4 and execute the queries from the .sql scripts in the query execution section.
Once everything is set up with the PostgreSQL server, set up your application.properties for the project so that it can connect to your database.
Open a terminal in the root directory of the project and run the command `mvnw exec:java -Dexec.mainClass="mhl.gochariot.GochariotApplication"` to start the program -- or you can open the project in an IDE and start it from there.
If you get any dependency errors, please try `mvnw clean install`. If you ran the fill_sample_data.sql script, you will have a few users to test with:

1. username: student1 password: password
2. username: driver1 password: password
3. username: admin1 password: password

If you want to test requesting rides as a student and accepting/declining requests as a driver, there are a few ways to do this:

The first way, which requires buses to currently be in operation but will give a more realistic experience, is to hit the endpoint /api/bus/all on your local server and find a busId.
Take that busId and then hit the endpoint /api/bus/id/bus_id_here to get the driverId of the Passio GO! tracked driver.
You can now impersonate that driver, for testing, by registering as a driver with that Passio GO! id and verifying it as an admin. This will result in the tracked driver showing up as verified on the tracking map and allow you to request a ride. You can also log in as the impersonated driver to accept/decline the request. 

The second way is to sign up as a driver with some made up id for the Passio GO! driver id field, log in as admin to verify the driver, and then login as student and navigate to the tracker page.
On the tracker page, open your developer tools console on your browser, and enter `requestModal("Campus Loop", "FirstName LastName", passio_go_id_you_made)` -- this will open a modal to submit a ride request, which you can then check in /requests for student and login as the driver you made to accept/decline it.

Both ways of testing will work, but the first way will be more realistic.



![Use Case Diagram](https://github.com/logashton/GoChariot/blob/main/WMS%20Use%20Case%203.jpg)



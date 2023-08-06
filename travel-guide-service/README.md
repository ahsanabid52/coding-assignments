# Travel Guide Service

Travel Guide service is a case study that is implemented to implement the coding challenge setup by element.

## Prerequisites

Please make sure that docker is installed on the machine.

* jdk 11
* kotlin 1.4.20
* gradle (or you can use `./gradlew` on linux/mac, `gradlew.bat` on windows)
* docker

## Useful commands

* Run the tests: `./gradlew clean test -i`
* Build the application: `./gradlew clean build -i`
* Check the application test coverage: `./gradlew test jacocoTestReport jacocoTestCoverageVerification`  
* Run the application: `./gradlew clean build -x test && docker-compose up --build`
* Test the application is healthy `curl localhost:8080/actuator/health`

## Steps to test the application
* Please use the postman collection present in the (project folder/postman.collection) folder with the environment.
   - Import it in postman.
* Run the application: `./gradlew clean build -x test && docker-compose up --build`

* Test the application is healthy by making a request in postman for `HealthCheck`

* Get all the hikes by making a request in postman for `Get all the hike trails`

* Get a specific hike trail by making a request in postman for `Get specific hike trails`

* Book a selected hike trail by making a request in postman for `Book a selected hike trail`

* View a booking by making a request in postman for `View a booking`

* View all booking by making a request in postman for `View all bookings`

* Cancel a booking by making a request in postman for `Cancel a booking`

I have set the environment variables like this that you dont have to edit the postman requests for adding booking/hike id.

Note: Please use the open api specs `travel-guide-service-openapi.yaml` located in the project folder for API guidelines.


## Things to improve

* Test coverage for the code is right now at 75 percent. It should be increased to 90 percent.
* Security should be added to the application so only authorized clients can use this.
* Cross Origin Requests should be enabled for only allowed origins. 
* Logging/Metric Alerts should be added in the application in case we have a lot of requests that are failing.

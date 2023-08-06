
# ReBalancing Service

ReBalancing service is a case study that is implemented for the coding challenge setup by Vanguard.

## Prerequisites

Please make sure that docker is installed on the machine and it is up and running.

* jdk 11
* kotlin 1.5.31
* gradle (or you can use `./gradlew` on linux/mac, `gradlew.bat` on windows)
* docker

## Useful commands

* Build the application: `./gradlew clean build -i`
* Run the tests: `./gradlew clean test -i`
* Check the application test coverage: `./gradlew test jacocoTestReport jacocoTestCoverageVerification`  
* Run the application mac: `./gradlew clean build -x test && docker-compose up --build`
* Run the application windows: `./gradlew clean build -x test "&&" docker-compose up --build`
* Test the application is healthy `curl localhost:8080/actuator/health`

## Steps to test the application
* Please use the postman collection present in the (project folder/postman.collection) folder.
   - Import it in postman.
   - Please note that I have added an example mock server for FPS. You can use this as an example and make your own 
      mock server. Please update the fps.api.url in the application.yaml, so you can run the application and use postman
      to test the application. 
* Run the application mac: `./gradlew clean build -x test && docker-compose up --build`
* Run the application windows: `./gradlew clean build -x test "&&" docker-compose up --build`

* Test the application is healthy by making a request in postman for `HealthCheck`

* Load all the customers and strategies by making a request in postman for `Load Daily Customers and Strategies`

* Get All the customers by making a request in postman for `Get All Customers`

* Get All the strategies by making a request in postman for `Get All Strategies`

* Get strategies for a specific customer by making a request in postman for `Get Strategies For A Specific Customer`

* Select strategy for a specific customer by making a request in postman for `Select Strategy For A Customer`

* Get portfolio for a customer by making a request in postman for `Get Portfolio For A Customer`
 
* Get trades for re-balancing customer portfolio by making a request in postman for `Get Trades for Rebalancing Customer Portfolio`

* Execute trades for re-balancing customers portfolio by making a request in postman for `Execute Trades for Rebalancing Customers Portfolio`


## Things to improve

* Loading customers and strategies data should be changed to handle for different data types and also for invalid values.
* Security should be added to the application so only authorized clients can use this.
* Test coverage for the code is right now at 76 percent. It should be increased.
* Logging should be added in the application.
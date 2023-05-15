# Enterprise Grade City List

Enterprise-grade "city list" application
which allows the user to do the following: 
* browse through the paginated list of cities with the corresponding photos
* search by the name
* edit the city (both name and photo) - Only by Admin

This app is Developed using Springboot 2.7.11 with JAVA 17 for BackEnd and Angular 16 for FrontEnd
___
## Pre-requests to run the application

### ` Java 17 & Angular 16`

Please make sure you have above versions installed before running the app
___
## How to run the application

### For Mac/Linux Users: 
use following script to run both springboot and angular
### `./start_city_list_app.sh `
to stop the running app
### `./stop_city_list_app.sh`
___

### For Windows Users:
use following script to run both springboot and angular
### `start_city_list_app.bat`
to stop the running app
### `stop_city_list_app.bat`
___
### To run manually
Have to run the backend and frontend separately 
* to run springboot app

`cd citylist.be
 gradlew build -x test && gradlew bootRun --args='--spring.profiles.active=dev'` 
* to run front end app

`cd citylist-ui
npm install && ng serve --open`
___
App can be accessed via http://localhost:4200/
___
**TestUsers:**
* Normal user : 
  * userName : selva
  * password : selva123
* Admin User :
  * userName : kuehne
  * password : kuehne123
# Stringinator
Find the most popular, longest string the server has been seen and frequent characters in the given input

#To Build Spring Boot Application use command: gradlew clean build -x test

#To Run Spring Boot Application Test Cases use command: gradlew clean build

#To Run Spring Boot Application Test Cases use command: gradlew bootRun

#To check for the rest endpoint use after application started :

curl -i -X POST -H "Content-Type:application/json" -d "{\"input\": \"Comcast is best place to work!\" }" http://localhost:8080/stringinate

curl -i -X GET http://localhost:8080/stringinate?input=Comcast%20is%20best%20place

curl -i -X GET http://localhost:8080/stats

curl -i -X GET http://localhost:8080/

#Github Repo for the application is: git clone https://github.com/ramadevi15/stringinator.git

# Stocks
Stocks Demo app

1) install Apache Kafka, Zookeeper, Zipkin, Couchbase if not already installed
2) create dir: D:/stockData/arch/ and D:/stockData/unprocessed/ (you can change the directory from Application.properties of application stock-uploader-service)
3) Create bucket 'Stock' in Couchbase 
4) Start Zookeeper and Kafka on default ports(or change ports in application.properties in uploader and login app)
5) Start zipkin(if want to trace request withing applications)
6) import login-mvc, Stock-reactive-service, stock-uploader-service into eclipse as maven project
7) run applications (start login-service at last as it is dependent on other services)
8) open url http://localhost:8099/ and click on signup to create user
9) Place inputfile in dir D:/stockData/live/ 
10) login with singed up user



This is a sample app for stock view and purchase demo real time.

Technologies:
1. Java8
2. Spring Boot
3. Spring Webflux
4. Sping MVC
5. Spring Schedular 
6. Apache Kafka
7. Java Script (UI)
8. Ajax (UI)
9. thymeleaf (UI)
10. Reactive Couchbase DB(Database)
11. WatchService

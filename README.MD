# Words Service

## Description:
This application is a simple REST service that has two endpoints:
* The first endpoint accepts a string of comma-separated words.
* The second endpoint returns stats about the frequency of all the words received in total:
  * Top 5 recurring words
  * Minimum frequency among all words
  * The median frequency

The application is thread safe and can handle multiple requests at the same time.
## Prerequisites:
- Maven 3
- JDK 17

## How to build:
    mvn clean install
Note: this command will run tests as well.

## Start application:
    java -jar target/words-stream-0.0.1-SNAPSHOT.jar

## Swagger interface:
    http://localhost:9080/swagger-ui.html

## Useful CURL commands

### Add words
    curl -X 'POST' \
    'http://localhost:9080/api/v1/words' \
    -H 'accept: */*' \
    -H 'Content-Type: application/json' \
    -d '{
    "words": "string"
    }'

### Get stats
    curl -X 'GET' 
    'http://localhost:9080/api/v1/wordsStatistics' 
    -H 'accept: */*'

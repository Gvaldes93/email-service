## Email Service
API that allows sending emails in SpringBoot and Java 8. 

### Running tests
`./gradlew test`
### Build and run with gradle
`./gradlew build`

Export environment variable `SENDGRID_API_KEY={InsertYourApiKEy}` 
and run `java -jar build/libs/email.microservice-1.0.0.jar`
application can be find at `localhost:8080`
 
### or Build and run with docker
`docker build . -t emailservice/emailservice`
then 
`docker run -p 8080:8080 -e SENDGRID_API_KEY={InsertYourApiKEy} emailservice/emailservice`

## API 
endpoint: __/email__ 

- POST
```js
{
    "to": [],
    "subject": ""
    "body": "",
    "cc": [],
    "bcc": [],
    
}
```
__to__: recipients emails separated by comma - REQUIRED. <br />
__subject__: the email subject. <br />
__body__: email body, can be plain text of html <br />
__cc__: cc emails separated by comma <br />
__bcc__: bcc emails separated by comma <br />

curl example:
```
curl -X POST \
  http://localhost:8080/email \
  -H 'Accept: application/json' \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: a53e3653-4069-4828-9c4f-4dc81dc08007' \
  -d '{
    "to": ["test@mailinator.com"],
    "subject": "Test subject",
    "body": "Hello there",
    "cc": ["test2@mailinator.com", "test2@mailinator.com"],
    "bcc": ["test3@mailinator.com"]
}'
```
response for this request: 200 OK - `Email sent successfully`

- You can also add the parameter enrich=true
which will add a quote of the day from https://quotes.rest/
e.g:
```
curl -X POST \
  'http://localhost:8080/email?enrich=true' \
  -H 'Accept: application/json' \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: fb42a52d-d43f-47dc-9a81-42a524210cd6' \
  -H 'cache-control: no-cache' \
  -d '{
        "to": ["test@mailinator.com"],
        "subject": "Test subject",
        "body": "Hello there",
      }'
``` 
response for this request: 200 OK - `Email sent successfully with quote of day` 

TODO:
- API documentation with Swagger
- Quote of the day request cached daily and served from memory
- API discoverability (HATEOAS impl) and more specific error handling with proper status codes and messages
- Validation of body when is html. API should reply with detailed errors encountered i
- Logging library and better logging in general
- CI / CD pipeline


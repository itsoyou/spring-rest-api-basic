# Rest API with Spring Boot Basics

## how are the requests handled?
DispatcherServlet: Front Controller Pattern

DispatcherServlet is mapped to root url and send request to correspond url

log: Mapping servlets: dispatcherServlet urls=[/]

Who is configuring DispatcherServlet? Auto Configuration.(DispatcherServletAutoConfiguration)

## how does HelloWorldBean Object get converted to JSON?
@ResponseBody + JacksonHttpMessageConverters

@RestController -> @ResponseBody
JacksonHttpMessageConverters -> Auto configuration.(JacksonHttpMessageConvertersConfiguration)

## who is configuring Error Mapping?
Auto configuration.(ErrorMvcAutoConfiguration)

## how are all jars(Spring MVC, Jackson, Tomcat) available?
Starter project. Spring Boot Starter Web.

## Rest API structure with User and Post

GET: retrieve details of a resource
POST: create a new resource
PUT: update an existing resource
PATCH: update part of an existing resource
DELETE: delete a resource

400 bad request/validation error
401 unauthorized
404 not found

200 success
201 created
204 no content

500 server exception

=> set by @ResponseEntity


GET /users -> retrieve all users
GET /users/{id} -> retrieve single user
POST /users
DELETE /users/{id}

GET /users/{id}/posts -> retrieve all posts of a user
GET /users/{id}/posts/{post_id} -> retrieve single post of a user
POST /users/{id}/posts

## DAO Object: Data Access Object

## Swagger 
2011. Swagger specification and tools introduced.

## Open API
2016. created based on Swagger Spec. Swagger tools(UI) continue to exist.
Standard, language-agnostic interface. Swagger UI: Visualize & interact

## Content Negotiation

Same resource but Different Representations are possible

### How customer tell the REST API provider what they want?
Via Accept header.(MIME types - application/xml, application/json, ...)

library for this: jackson-dataformat-xml

## Internationalization - i18n
Accept-Language header (en, nl, fr,...)
configure this language information in src/main/resources/messages.properties
and use "MessageSource"

## Versioning REST API

if you need to implement breaking change, you better version your REST API. 
this ensures flexibility for customers to update whenever they want. 

options
- URL : v1/ v2
- Request Parameter 
- Request Header
- Media Type

## Versioning REST API Factors to Consider
- URI Pollution
- Misuse of HTTP Headers (they are never intended for versioning tool)
- Caching- typically caching is done based on URL. so might need to implement caching based on header.
- Can we execute the request on the browser? 
	- if versions are in header, browser need command line utility or REST API client.
- API Documentation

Summary: No Perfect Solution. Think about versioning even before you need it. 
One Enterprise - One Versioning Approach


## HATEOAS: Hypermedia As The Engine Of Application State

Websites allow you to see the data and perform action.
Enhancing REST API to tell consumers how to perform subsequent actions.

How to implement?
1. Custom format and implementation
2. Use Standard implementation ( HAL: JSON Hypertext Application Language): simple format that gives
a consistent and easy way to hyperlink between resources in you API.

Dependency: spring-boot-starter-hateoas
Use "EntityModel" and "WebMvcLinkBuilder"

## Serialization: Convert object to stream e.g. JSON

Jackson: most popular JSON Serialization in Java

To customize return object:

1. Customize field names in response: @JSONProperty
2. Return only selected fields - Filtering
	- Static filtering: same filtering for a bean across different API => @JsonIgnoreProperties, @JsonIgnore
	- Dynamic filtering: customize filtering for a bean for specific API. => @JsonFilter + FilterProvider

## Spring Boot Actuator

provides production ready features. e.g. Monitor and manage your app in prod.
provides number of endpoints.
/actuator/beans
/actuator/health
/actuator/metrics
/actuator/mappings

dependency: srping-boot-starter-actuator
to see more than health endpoint, configure this in application.properties
management.endpoints.web.exposure.include=*

## Explore Rest API using HAL(JSON Hypertext Application Language) Explorer
"_links" : { ... }
simple format that gives a consistent and easy way to hyperlink between resources in you API.

### HAL Explorer
enable non-technical teams to play with API.
dependency: spring-data-rest-hal-explorer

If this is enabled, localhost:8080 will redirected to http://localhost:8080/explorer/index.html#uri=/


# Use mysqlsh

mysqlsh
\connect social-media-user@localhost:3306
\use social-media-database
select * from user_details;
select * from post;
\quit

# Spring Security

intercept the request and executes series of filters(Filter Chains)

1. All requests should be authenticated
2. if a request is not authenticated, a web page is shown for log in
3. CSRF-> POST, PUT

## HTTP authentication

We will create configuration file where we define FilterChain Bean.

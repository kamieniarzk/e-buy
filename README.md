# e-buy
[e-buy](http://ebuy.eu-central-1.elasticbeanstalk.com/) <----------  
A Spring Boot application, imitating basic functionality of consumer-to-consumer and business-to-consumer sales service.
Stack: 
* `Spring Boot`,
* `Spring Data JPA` with `Hibernate` coupled with H2 for development and PostgreSQL in production,
* `Spring Security`,
* `Thymeleaf`,
* `Bootstrap`,
* [Cloudinary Upload API](https://cloudinary.com/documentation/image_upload_api_reference) for images upload,
* `Maven`,


## Overview
The main assumption for this application was for it to be a simple sales service. The main functionality requirements were: CRUD operations on products by authorized users, adding products to the shopping cart, checkout of the shopping cart - which saves the order details in the database, user profile edition. 

## Domain
At this stage, the domain of this application is quite generic. Depending on the needs, the domain could be easily adjusted (for example by implementing different `Product` classes for each category) but the accuracy was not the priority in the process of making this application. 
### `Product`
Product is an entity class for storing the product for sale. It contains a few generic characteristics like:
* price, 
* quantity, 
* mass, 
* category,  
* imageUrl,
for the sake of simplicity, at this stage the product categories are groceries. 
### `UserProfile`
User profile is a class implementing `UserDetails` interface. It contains security credentials and some personal info that a user would have like:
* name,
* surname,  
* about me,
* imageUrl (for profile picture),
### `OrderDetails`
OrderDetails is an entity class for storing the information about each order that a user completes (checks out in the shopping cart). It contains:
* timestamp (creation date and time),
* list of product id's,
* total,  
A user can access their previous orders by visiting `Orders history`.

## Security
User authentication is implemented using a login form. User's credentials are stored in the database and passwords are encrypted using BCrypt algorihtm. There are 3 user roles: `ADMIN`, `SELLER` and `CUSTOMER` and 4 user permissions: `PRODUCT_READ`, `PRODUCT_WRITE`, `TRANSACTION_READ`, `TRANSACTION_WRITE`. `ADMIN` has all permissions.

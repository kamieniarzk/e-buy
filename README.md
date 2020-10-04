[e-buy](https://ebuy-app.herokuapp.com/) <----------  *Please be aware, the page may load for up to 15 seconds, because I'm running it on a free dyno hours on heroku. (if there's no web traffic in 30 minutes, the app shutsdown and it only boots up when a new request comes).*<br><br>
# e-buy
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
User authentication is implemented using a login form. User's credentials are stored in the database and passwords are encrypted using BCrypt algorihtm. There are 3 user roles: `ADMIN`, `SELLER` and `CUSTOMER` and 4 user permissions: `PRODUCT_READ`, `PRODUCT_WRITE`, `TRANSACTION_READ`, `TRANSACTION_WRITE`. Endpoints are secured using `@PreAuthorize` annotation.

## User interface
### Main page
The main page displays a list of products, by default from all the categories. The user can either select from the categories on the left or try the search bar above the list. There are also sorting options to be chosen - by name, price or the user who offers the product. Product queries have pagination implemented - each query shows up to 5 products per page. On the right hand side of the page there is a shopping cart widget which displays current state of user's products added to the cart.<br>
![](https://res.cloudinary.com/ddd3ldsj2/image/upload/v1600980647/Screenshot_2_ymtrag.jpg)<br>
### Product page
The product page simply presents the details of a product.<br>
![](https://res.cloudinary.com/ddd3ldsj2/image/upload/v1600981168/Screenshot_3_cvjofx.jpg)<br>
### User page
User page presents user details along with list of products offered by them.<br>
![](https://res.cloudinary.com/ddd3ldsj2/image/upload/v1600983227/Screenshot_5_jvwdrj.jpg)<br>
### Order page
Order page presents the list of products from the order with date and order total.<br>
![](https://res.cloudinary.com/ddd3ldsj2/image/upload/v1600983303/Screenshot_6_uqdos7.jpg)<br>
### Orders list
Orders list page presents a list of user's past orders.<br>
![](https://res.cloudinary.com/ddd3ldsj2/image/upload/v1600983424/Screenshot_7_f4pndh.jpg)
### User form
User form is used for either new user sign up or editing existing user's profile.<br>
![](https://res.cloudinary.com/ddd3ldsj2/image/upload/v1600983555/Screenshot_8_xauwk1.jpg)<br>
### Product form
Product form is used for either adding a new product or editing an existing one.<br>
![](https://res.cloudinary.com/ddd3ldsj2/image/upload/v1600983631/Screenshot_9_je1hrf.jpg)<br>



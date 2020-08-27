# e-buy
A Spring Boot application, imitating basic functionality of consumer-to-consumer and business-to-consumer sales service.
Stack: 
* `Spring Boot`,
* `Spring Data JPA` with `Hibernate` coupled with H2 database (for simplicity of development),
* `Spring Security`,
* `Thymeleaf`,
* [Cloudinary Upload API](https://cloudinary.com/documentation/image_upload_api_reference) for images upload,
* `Maven`,

## mandatory TODOs
* add database mapping for `products` in `OrderDetails` 
* finish html for detailed product view, 
* make endpoint for editing product,
* make changes to home html,
* plan the whole routing from scratch - now it's a mess,
* make html and controller for orders history and single order (`OrderDetails`),
* make the navbar usable,
* make html and controller for editing user profile,
* improve data validation for user sign up and product creation.

## possible TODOs for the future
* make html and controller for user profile (to view other user profiles),
* enable editing of categories for users with `ROLE_ADMIN',
* implement pagination,
* implement Jason Web Token for user authentication,

## far fetched TODOs
* implement rating of products and users,

 
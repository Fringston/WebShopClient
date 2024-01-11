# ClientWebservice

## Description

This is a client webservice for the ServerWebservice: Webshop. 
It's a Spring Boot application that uses Spring Security to authenticate users and OAuth2 to authorize them. 
The application is connected to a MySQL database and uses Spring Data JPA to access the data. The application is also connected to the ServerWebservice: Webshop to get the data from the database.
The application features secure user registration and login functionality, and allows users to browse through products, add them to their shopping cart and place orders.


## Functionality
### User functionality
- **All users can:**
  - Browse through articles.
  - Register an account.
  - Login to their account.
  
- **Logged-in users can:**
  - Browse through articles, add them to their shopping cart and place orders.
  - See their order history.
  

### Admin functionality
- **Admins users can:** 
  - Do everything a regular user can do.
  - Create, read, update and delete articles.
  - See all historic shopping carts.
  - See all current users.
  
## Installation

+ Download and install IntelliJ IDEA or your preferred IDE.
+ Clone this repository to your local machine.

## Usage

- Open your IDE and run the Main class to start the application.
- Follow the instructions in the console to use the web-shop.

## Dependencies:

- [jackson-core](https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core)
- [jackson-annotations](https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-annotations)
- [httpclient5](https://mvnrepository.com/artifact/org.apache.httpcomponents.client5/httpclient5)
- [jackson-databind](https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind)
- [org.json](https://mvnrepository.com/artifact/org.json/json)


## Credits

### Collaborators in this project:
* [Clara Brorson](https://github.com/clarabrorson)
* [Fredrik Rinstad](https://github.com/Fringston)
* [Jafar Hussein](https://github.com/Jafar-Hussein)

## License

**[MIT License](https://choosealicense.com/licenses/mit/)**

## Badges
![badmath](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white)
![badmath](https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![badmath](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![badmath](https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)


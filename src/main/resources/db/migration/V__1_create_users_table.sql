CREATE TABLE users (
   id INT NOT NULL AUTO_INCREMENT,
   name varchar(255) NOT NULL ,
   email varchar(255) NOT NULL UNIQUE ,
   password varchar(255) NOT NULL ,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
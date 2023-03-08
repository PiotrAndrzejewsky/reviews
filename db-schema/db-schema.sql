CREATE DATABASE reviews;
use reviews;

CREATE TABLE users (
	id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    active BIT DEFAULT 0
);

CREATE TABLE spotify_reviews (
	id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    grade INT NOT NULL,
    CHECK (0<=grade<=10),
	spotify_id VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
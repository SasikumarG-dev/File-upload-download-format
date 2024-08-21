create database file;

use file;

CREATE TABLE image (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    type VARCHAR(50),
    data LONGBLOB
);
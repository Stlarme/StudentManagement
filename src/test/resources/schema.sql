CREATE TABLE IF NOT EXISTS students
 (
       id INT AUTO_INCREMENT PRIMARY KEY ,
       name VARCHAR(100) NOT NULL,
       kanaName VARCHAR(100) NOT NULL,
       nickname VARCHAR(50),
       email VARCHAR(100) NOT NULL,
       region VARCHAR(100),
       age INT,
       gender VARCHAR(50),
       remark VARCHAR(255),
       isDeleted boolean
);

CREATE TABLE IF NOT EXISTS students_courses
(
       id INT AUTO_INCREMENT PRIMARY KEY,
       student_id INT NOT NULL,
       course_name VARCHAR(100),
       start_date TIMESTAMP,
       end_date TIMESTAMP
);
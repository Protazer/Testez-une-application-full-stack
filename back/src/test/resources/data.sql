ALTER TABLE `SESSIONS` ADD FOREIGN KEY (`teacher_id`) REFERENCES `TEACHERS` (`id`);
ALTER TABLE `PARTICIPATE` ADD FOREIGN KEY (`user_id`) REFERENCES `USERS` (`id`);
ALTER TABLE `PARTICIPATE` ADD FOREIGN KEY (`session_id`) REFERENCES `SESSIONS` (`id`);



INSERT INTO TEACHERS (first_name, last_name)
VALUES ('John', 'DOE'),
       ('Jane', 'DOE');


INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES ('Admin', 'Admin', true, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq');


INSERT INTO SESSIONS (name, description, date)
VALUES ('TEST1', 'This is the first session description', '2025-05-30 14:40:00'),
('TEST2', 'This is the second session description', '2025-05-30 14:42:00');

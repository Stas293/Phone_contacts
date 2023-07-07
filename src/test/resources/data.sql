-- Insert sample data into the "user_list" table
INSERT INTO public.user_list (id, username, password, role_id)
VALUES (1, 'john_doe', 'password123', 1),
       (2, 'jane_smith', 'securepwd456', 2);
SELECT SETVAL('public.user_list_id_seq', 2);

-- Insert sample data into the "contact" table
INSERT INTO public.contact (id, first_name, middle_name, last_name, user_id, image_path)
VALUES (1,'John', '', 'Doe', 1, '/images/john_doe.jpg'),
       (2, 'Jane', '', 'Smith', 2, '/images/jane_smith.jpg');
SELECT SETVAL('public.contact_id_seq', 2);

-- Insert sample data into the "email" table
INSERT INTO public.email (id, username, domain, contact_id)
VALUES (1, 'john.doe', 'example.com', 1),
       (2, 'jane.smith', 'example.com', 2);
SELECT SETVAL('public.email_id_seq', 2);

-- Insert sample data into the "phone" table
INSERT INTO public.phone (id, country_code, area_code, number, contact_id)
VALUES (1, '+380', '50', '1234567', 1),
       (2, '+380', '50', '7654321', 2);
SELECT SETVAL('public.phone_id_seq', 2);

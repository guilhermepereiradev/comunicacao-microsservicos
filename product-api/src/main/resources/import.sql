INSERT INTO category (id, description) VALUES (1000, 'Comic Books');
INSERT INTO category (id, description) VALUES (1002, 'Movies');
INSERT INTO category (id, description) VALUES (1003, 'Books');

INSERT INTO supplier (id, name) VALUES (1000, 'Panini Comics');
INSERT INTO supplier (id, name) VALUES (1002, 'Amazon');

INSERT INTO product (id, name, fk_supplier, fk_category, quantity_available, created_at) VALUES (1000, 'Batman: The Killing Joke', 1000, 1000, 10, CURRENT_TIMESTAMP);
INSERT INTO product (id, name, fk_supplier, fk_category, quantity_available, created_at) VALUES (1002, 'Cassino', 1002, 1002, 5, CURRENT_TIMESTAMP);
INSERT INTO product (id, name, fk_supplier, fk_category, quantity_available, created_at) VALUES (1003, 'Harry Potter', 1002, 1003, 3, CURRENT_TIMESTAMP);
-- Allergens
INSERT INTO allergens (id, name)
VALUES
    (nextval('allergens_seq'), 'gluten (tarwe, rogge, gerst, haver en spelt)'),
    (nextval('allergens_seq'), 'ei'),
    (nextval('allergens_seq'), 'vis'),
    (nextval('allergens_seq'), 'pinda'),
    (nextval('allergens_seq'), 'noten'),
    (nextval('allergens_seq'), 'soja'),
    (nextval('allergens_seq'), 'melk (lactose)'),
    (nextval('allergens_seq'), 'schaaldieren'),
    (nextval('allergens_seq'), 'weekdieren'),
    (nextval('allergens_seq'), 'selderij'),
    (nextval('allergens_seq'), 'mosterd'),
    (nextval('allergens_seq'), 'sesamzaad'),
    (nextval('allergens_seq'), 'sulfiet'),
    (nextval('allergens_seq'), 'lupine');

-- Supplements
INSERT INTO supplements (id, name, brand, description, price, stock, average_rating, contains)
VALUES
    (nextval('products_seq'), 'B12 Combi 6000', 'Vitakruid', 'De actieve vormen', 34.90, 15, null, '60 smelttabletten'),
    (nextval('products_seq'), 'Magnesium Malaat', 'Vitakruid', 'Voor energie', 24.90, 6, null, '120 gram'),
    (nextval('products_seq'), 'Magnesium Tauraat', 'Vitakruid', 'Voor slapen', 26.90, 1, null, '100 vega capsules'),
    (nextval('products_seq'), 'Vitamine D3 & K2', 'Vitakruid', 'Actieve vormen', 24.90, 5, null, '10 milliliter'),
    (nextval('products_seq'), 'Creatine Monohydraat', 'Gym Essentials', 'Pure vorm', 26.99, 10, null, '500 gram'),
    (nextval('products_seq'), 'Protein Bar Killa Fudged Up', 'Grenade', 'Niet teveel eten ivm flatulentie', 3.29, 4, null, '60 gram'),
    (nextval('products_seq'), 'Protein Bar White Chocolate Salted Peanut', 'Grenade', 'Absurd lekker', 3.29, 3, null, '60 gram'),
    (nextval('products_seq'), 'Magnesium Bisglycinaat', 'Vitakruid', 'voor algemeen gebruik', 23.90, 0, null, '100 vega capsules');

-- Supplement and Allergens relations
INSERT INTO supplement_allergens (supplement_id, allergen_id)
VALUES
    (1011147, 1006),
    (1011147, 1007),
    (1013284, 1005),
    (1013284, 1006),
    (1013284, 1007);

-- Garments
INSERT INTO garments (id, name, brand, description, price, stock, average_rating, size, color)
VALUES
    (100101, 'Hoody xs', 'Energize', 'Legendarische trui', 49.99, 5, null, 'XS', 'zwart'),
    (100102, 'Hoody s', 'Energize', 'Legendarische trui', 49.99, 3, null, 'S', 'zwart'),
    (100103, 'Hoody m', 'Energize', 'Legendarische trui', 49.99, 3, null, 'M', 'zwart'),
    (100104, 'Hoody l', 'Energize', 'Legendarische trui', 49.99, 0, null, 'L', 'zwart'),
    (100105, 'Hoody xl', 'Energize', 'Legendarische trui', 49.99, 1, null, 'XL', 'zwart'),
    (100201, 'Shirt xs zwart', 'Energize', 'Legendarisch shirt', 29.99, 2, null, 'XS', 'zwart'),
    (100202, 'Shirt s zwart', 'Energize', 'Legendarisch shirt', 29.99, 8, null, 'X', 'zwart'),
    (100203, 'Shirt m zwart', 'Energize', 'Legendarisch shirt', 29.99, 12, null, 'M', 'zwart'),
    (100204, 'Shirt l zwart', 'Energize', 'Legendarisch shirt', 29.99, 20, null, 'L', 'zwart'),
    (100205, 'Shirt xl zwart', 'Energize', 'Legendarisch shirt', 29.99, 0, null, 'XL', 'zwart'),
    (100211, 'Shirt xs groen', 'Energize', 'Legendarisch shirt', 29.99, 15, null, 'XS', 'groen'),
    (100212, 'Shirt s groen', 'Energize', 'Legendarisch shirt', 29.99, 1, null, 'S', 'groen'),
    (100213, 'Shirt m groen', 'Energize', 'Legendarisch shirt', 29.99, 3, null, 'M', 'groen'),
    (100214, 'Shirt l groen', 'Energize', 'Legendarisch shirt', 29.99, 22, null, 'L', 'groen'),
    (100215, 'Shirt xl groen', 'Energize', 'Legendarisch shirt', 29.99, 5, null, 'XL', 'groen');

-- Users
INSERT INTO users (username, password, email)
VALUES ('mmesander', '$2a$12$RfLgBdcAj/9o/XkMZNm.Zerka9oTm3WRp5nm5rkPg/G5mwVQftbzq', 'mark@test.nl'),
       ('rplooij', '$2a$12$RfLgBdcAj/9o/XkMZNm.Zerka9oTm3WRp5nm5rkPg/G5mwVQftbzq', 'rowan@test.nl');

-- Shipping Details
INSERT INTO shipping_details (id, details_name, name, country, city, zip_code, street, house_number, email, user_shipping_details)
VALUES
    (nextval('shipping_details_seq'), 'THUISADRES', 'Mark Mesander', 'Nederland', 'Haarlem', '1234AB', 'Test de Testlaan', 28, 'mark@test.nl', 'mmesander'),
    (nextval('shipping_details_seq'), 'WERKADRES', 'Mark Mesander', 'Nederland', 'Noordwijk', '3456CD', 'Test de Teststraat', 42, 'mark@test.nl', 'mmesander'),
    (nextval('shipping_details_seq'), 'NIETTOEKOMSTADRES', 'Rowan Plooij', 'Nederland', 'Friesland', '3333DE', 'Vriendinstraat 101', '26E', 'rowan@test.nl', 'rplooij');

-- Discounts
INSERT INTO discounts (id, name, value)
VALUES
    (nextval('discounts_seq'), 'markgaateen8krijgen', 10),
    (nextval('discounts_seq'), 'markgaateen9krijgen', 80),
    (nextval('discounts_seq'), 'markgaateen10krijgen', 90);

INSERT INTO discounts_users (discount_id, username)
VALUES
    (2, 'mmesander'),
    (3, 'mmesander'),
    (3, 'rplooij');

INSERT INTO authorities (username, authority)
VALUES ('mmesander', 'ROLE_ADMIN'),
       ('mmesander', 'ROLE_USER'),
       ('rplooij', 'ROLE_USER');

INSERT INTO orders (order_number, order_date, status, is_paid, discount_code, total_amount, user_orders, shipping_details)
VALUES (nextval('orders_seq'), CURRENT_DATE, 'delivered', TRUE, 'markgaateen10krijgen', 200.00, 'mmesander', 1),
       (nextval('orders_seq'), CURRENT_DATE, 'delivered', FALSE, NULL, 50.00, 'mmesander', 1);

INSERT INTO order_garments (order_id, garment_id)
VALUES (101, 100101),
       (101, 100101),
       (101, 100102),
       (101, 100102),
       (101, 100102),
       (101, 100103),
       (101, 100103),
       (101, 100103),
       (101, 100103),
       (101, 100103),
       (101, 100103),
       (102, 100215),
       (102, 100215),
       (102, 100215),
       (102, 100215),
       (102, 100215),
       (102, 100215),
       (102, 100215),
       (102, 100215),
       (102, 100215),
       (102, 100215),
       (102, 100215),
       (102, 100215),
       (102, 100215);

INSERT INTO reviews (id, review, rating, date, garment_id, supplement_id, user_id)
VALUES (nextval('reviews_seq'), 'inderdaad legendarisch', 10, CURRENT_DATE, 100101, NULL, 'mmesander'),
       (nextval('reviews_seq'), 'ik ben wat kritischer', 8, CURRENT_DATE, 100101, NULL, 'rplooij'),
       (nextval('reviews_seq'), 'mega lekker', 9, CURRENT_DATE, NULL, 1013284, 'mmesander');
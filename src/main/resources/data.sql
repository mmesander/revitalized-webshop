-- Products
ALTER SEQUENCE products_seq RESTART 100462 INCREMENT BY 2137;

-- Supplements
INSERT INTO supplements (id, name, brand, description, price, average_rating, contains)
VALUES
    (nextval('products_seq'), 'B12 Combi 6000', 'Vitakruid', 'De actieve vormen', 34.90, null, '60 smelttabletten'),
    (nextval('products_seq'), 'Magnesium Malaat', 'Vitakruid', 'Voor energie', 24.90, null, '120 gram'),
    (nextval('products_seq'), 'Magnesium Tauraat', 'Vitakruid', 'Voor slapen', 26.90, null, '100 vega capsules'),
    (nextval('products_seq'), 'Vitamine D3 & K2', 'Vitakruid', 'Actieve vormen', 24.90, null, '10 milliliter'),
    (nextval('products_seq'), 'Creatine Monohydraat', 'Gym Essentials', 'Pure vorm', 26.99, null, '500 gram'),
    (nextval('products_seq'), 'Protein Bar Killa Fudged Up', 'Grenade', 'Niet teveel eten ivm flatulentie', 3.29, null, '60 gram'),
    (nextval('products_seq'), 'Protein Bar White Chocolate Salted Peanut', 'Grenade', 'Absurd lekker', 3.29, null, '60 gram');

-- Allergens
ALTER SEQUENCE allergens_seq RESTART 1001 INCREMENT BY 1;
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

-- Supplement and Allergens relations
INSERT INTO supplement_allergens (supplement_id, allergen_id)
VALUES
    (111147, 1006),
    (111147, 1007),
    (113284, 1005),
    (113284, 1006),
    (113284, 1007);

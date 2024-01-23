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

-- Garments
INSERT INTO garments (id, name, brand, description, price, average_rating, size, color)
VALUES
    (100101, 'Hoody xs', 'Energize', 'Legendarische trui', 49.99, null, 'XS', 'zwart'),
    (100102, 'Hoody s', 'Energize', 'Legendarische trui', 49.99, null, 'S', 'zwart'),
    (100103, 'Hoody m', 'Energize', 'Legendarische trui', 49.99, null, 'M', 'zwart'),
    (100104, 'Hoody l', 'Energize', 'Legendarische trui', 49.99, null, 'L', 'zwart'),
    (100105, 'Hoody xl', 'Energize', 'Legendarische trui', 49.99, null, 'XL', 'zwart'),
    (100201, 'Shirt xs zwart', 'Energize', 'Legendarisch shirt', 29.99, null, 'XS', 'zwart'),
    (100202, 'Shirt s zwart', 'Energize', 'Legendarisch shirt', 29.99, null, 'X', 'zwart'),
    (100203, 'Shirt m zwart', 'Energize', 'Legendarisch shirt', 29.99, null, 'M', 'zwart'),
    (100204, 'Shirt l zwart', 'Energize', 'Legendarisch shirt', 29.99, null, 'L', 'zwart'),
    (100205, 'Shirt xl zwart', 'Energize', 'Legendarisch shirt', 29.99, null, 'XL', 'zwart'),
    (100211, 'Shirt xs groen', 'Energize', 'Legendarisch shirt', 29.99, null, 'XS', 'groen'),
    (100212, 'Shirt s groen', 'Energize', 'Legendarisch shirt', 29.99, null, 'S', 'groen'),
    (100213, 'Shirt m groen', 'Energize', 'Legendarisch shirt', 29.99, null, 'M', 'groen'),
    (100214, 'Shirt l groen', 'Energize', 'Legendarisch shirt', 29.99, null, 'L', 'groen'),
    (100215, 'Shirt xl groen', 'Energize', 'Legendarisch shirt', 29.99, null, 'XL', 'groen');

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

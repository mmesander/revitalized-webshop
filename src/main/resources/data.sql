-- Products
ALTER SEQUENCE products_seq INCREMENT BY 1;

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
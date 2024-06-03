CREATE TABLE Brand
(
    brand_id   INT AUTO_INCREMENT PRIMARY KEY,
    brand_name VARCHAR(255) NOT NULL,
    price_sum DECIMAL DEFAULT 0
);

CREATE TABLE Product
(
    product_id    INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL,
    brand_name VARCHAR(255) NOT NULL,
    price         DECIMAL
);

-- Indexes
CREATE INDEX idx_product_category_price_brand ON Product (category_name, price, brand_name);
CREATE INDEX idx_product_brand_category_price ON Product (brand_name, category_name, price);
CREATE INDEX idx_brand_price_sum ON Brand (price_sum, brand_name);

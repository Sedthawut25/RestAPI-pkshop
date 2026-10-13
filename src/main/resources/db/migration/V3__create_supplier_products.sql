CREATE TABLE supplier_products (
    id BIGINT NOT NULL AUTO_INCREMENT,
    supplier_user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    supplier_sku VARCHAR(100) NULL,
    min_order_qty INT NOT NULL DEFAULT 1,
    is_active TINYINT(1) NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_supplier_product (supplier_user_id, product_id),
    KEY idx_supplier_products_product (product_id),
    CONSTRAINT fk_supplier_products_user FOREIGN KEY (supplier_user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_supplier_products_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
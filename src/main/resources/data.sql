INSERT INTO sc_category (id, title) VALUES (1, 'CLOTHING')
INSERT INTO sc_category (id, title, parent_id) VALUES (2, 'SWEATER', 1)
INSERT INTO sc_category (id, title) VALUES (3, 'HOME')

INSERT INTO sc_product (id, title, price, category_id) VALUES (1, 'MILLA HODIE SWEATER', 119.50, 2)
INSERT INTO sc_product (id, title, price, category_id) VALUES (2, 'LAMB', 50.90, 3)

INSERT INTO sc_campaign (id, title, category_id, min_cart_product_number, discount_type, discount_amount) VALUES (1, '%50 SALE', 1, 1, 'rate', 50)

INSERT INTO sc_coupon (id, title, min_cart_amount, discount_type, discount_amount) VALUES (1, '250 TL GIFT!', 280, 'amount', 250)
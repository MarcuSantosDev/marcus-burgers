-- =========================================
-- 0. LIMPAR TODAS AS TABELAS
--    (ordem importa: filhos antes dos pais, por causa das chaves estrangeiras)
-- =========================================

DELETE FROM order_item;
DELETE FROM orders;
DELETE FROM address;
DELETE FROM product;
DELETE FROM category;
DELETE FROM users;


-- =========================================
-- 1. CATEGORIAS
-- =========================================

INSERT INTO category (name, description) VALUES
                                             ('Hambúrgueres', 'Hambúrgueres artesanais e tradicionais'),
                                             ('Bebidas', 'Refrigerantes, sucos e cervejas'),
                                             ('Acompanhamentos', 'Batatas fritas, onion rings e porções');


-- =========================================
-- 2. PRODUTOS
-- =========================================

INSERT INTO product (name, description, price, stock, category_id)
VALUES
    ('X-Bacon', 'Pão brioche, blend 180g, queijo cheddar e bacon crocante.',
     32.90, 50, (SELECT category_id FROM category WHERE name = 'Hambúrgueres')),

    ('X-Salada', 'Pão brioche, blend 180g, queijo prato, alface, tomate e molho especial.',
     28.90, 40, (SELECT category_id FROM category WHERE name = 'Hambúrgueres')),

    ('X-Tudo', 'Pão brioche, blend 180g, queijo, presunto, bacon, ovo, alface, tomate e molho especial.',
     36.90, 25, (SELECT category_id FROM category WHERE name = 'Hambúrgueres')),

    ('Coca-Cola Lata 350ml', 'Refrigerante Coca-Cola tradicional, lata de 350ml.',
     6.00, 100, (SELECT category_id FROM category WHERE name = 'Bebidas')),

    ('Guaraná Antarctica 350ml', 'Refrigerante de guaraná, lata de 350ml.',
     5.50, 80, (SELECT category_id FROM category WHERE name = 'Bebidas')),

    ('Batata Frita P', 'Porção pequena de batatas fritas crocantes.',
     12.00, 40, (SELECT category_id FROM category WHERE name = 'Acompanhamentos')),

    ('Batata Frita G', 'Porção grande de batatas fritas crocantes.',
     20.00, 30, (SELECT category_id FROM category WHERE name = 'Acompanhamentos'));


-- =========================================
-- 3. USUÁRIOS
-- =========================================

INSERT INTO users (name, email, password, role)
VALUES
    ('Admin', 'admin@email.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'ADMIN'),

    ('Marcus', 'marcus@email.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'CLIENT');


-- =========================================
-- 4. ENDEREÇOS
--    (number agora é texto; zipcode virou zip_code)
-- =========================================

INSERT INTO address (
    name, street, number, complement, neighborhood,
    city, state, zip_code, user_id
)
VALUES
    ('Casa', 'Rua das Flores', '120', 'Apto 101',
     'Centro', 'João Pessoa', 'PB', '58010-000',
     (SELECT user_id FROM users WHERE email = 'marcus@email.com')),

    ('Trabalho', 'Avenida Epitácio Pessoa', '1500', 'Sala 304',
     'Bairro dos Estados', 'João Pessoa', 'PB', '58030-001',
     (SELECT user_id FROM users WHERE email = 'marcus@email.com')),

    ('Casa dos Pais', 'Rua Sete de Setembro', '45', NULL,
     'Centro', 'Campina Grande', 'PB', '58400-100',
     (SELECT user_id FROM users WHERE email = 'marcus@email.com')),

    ('Apartamento', 'Avenida Paulista', '1000', 'Bloco B, Apto 52',
     'Bela Vista', 'São Paulo', 'SP', '01310-100',
     (SELECT user_id FROM users WHERE email = 'marcus@email.com')),

    ('Casa de Praia', 'Rua dos Coqueiros', 'S/N', 'Casa 7',
     'Praia do Sol', 'Recife', 'PE', '51020-040',
     (SELECT user_id FROM users WHERE email = 'marcus@email.com'));


-- =========================================
-- 5. PEDIDOS
--    (total_amount começa em 0 e é calculado no passo 7)
-- =========================================

INSERT INTO orders (order_date, status, total_amount, user_id, address_id)
VALUES
    ('2026-11-10 19:30:00', 'DELIVERED', 0,
     (SELECT user_id FROM users WHERE email = 'marcus@email.com'),
     (SELECT address_id FROM address WHERE name = 'Casa')),

    ('2026-11-19 20:15:00', 'PENDING', 0,
     (SELECT user_id FROM users WHERE email = 'marcus@email.com'),
     (SELECT address_id FROM address WHERE name = 'Trabalho'));


-- =========================================
-- 6. ITENS DOS PEDIDOS
--    (unit_price copia o preço do produto no momento da compra)
-- =========================================

INSERT INTO order_item (order_id, product_id, quantity, unit_price)
VALUES
    -- Pedido de 10/11: 2x X-Bacon, 1x Coca-Cola, 1x Batata G
    ((SELECT order_id FROM orders WHERE order_date = '2026-11-10 19:30:00'),
     (SELECT product_id FROM product WHERE name = 'X-Bacon'),
     2, (SELECT price FROM product WHERE name = 'X-Bacon')),

    ((SELECT order_id FROM orders WHERE order_date = '2026-11-10 19:30:00'),
     (SELECT product_id FROM product WHERE name = 'Coca-Cola Lata 350ml'),
     1, (SELECT price FROM product WHERE name = 'Coca-Cola Lata 350ml')),

    ((SELECT order_id FROM orders WHERE order_date = '2026-11-10 19:30:00'),
     (SELECT product_id FROM product WHERE name = 'Batata Frita G'),
     1, (SELECT price FROM product WHERE name = 'Batata Frita G')),

    -- Pedido de 19/11: 1x X-Tudo, 1x Guaraná
    ((SELECT order_id FROM orders WHERE order_date = '2026-11-19 20:15:00'),
     (SELECT product_id FROM product WHERE name = 'X-Tudo'),
     1, (SELECT price FROM product WHERE name = 'X-Tudo')),

    ((SELECT order_id FROM orders WHERE order_date = '2026-11-19 20:15:00'),
     (SELECT product_id FROM product WHERE name = 'Guaraná Antarctica 350ml'),
     1, (SELECT price FROM product WHERE name = 'Guaraná Antarctica 350ml'));


-- =========================================
-- 7. CALCULAR O TOTAL DE CADA PEDIDO
-- =========================================

UPDATE orders
SET total_amount = (
    SELECT SUM(oi.unit_price * oi.quantity)
    FROM order_item oi
    WHERE oi.order_id = orders.order_id
);


-- =========================================
-- 8. CONFERIR OS DADOS
-- =========================================

SELECT * FROM category;
SELECT * FROM product;
SELECT * FROM users;
SELECT * FROM address;
SELECT * FROM orders;
SELECT * FROM order_item;
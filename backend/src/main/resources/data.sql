
-- =========================================
-- 0. LIMPAR TODAS AS TABELAS
-- =========================================

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

INSERT INTO users (name, email, password_hash, role)
VALUES
    ('Admin', 'admin@email.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'ADMIN'),

    ('Marcus', 'marcus@email.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'CLIENT');


-- =========================================
-- 4. ENDEREÇOS
-- =========================================

INSERT INTO address (
    name, street, number, complement, neighborhood,
    city, state, zipcode, user_id
)
VALUES
    ('Casa', 'Rua das Flores', 120, 'Apto 101',
     'Centro', 'João Pessoa', 'PB', '58010-000',
     (SELECT user_id FROM users WHERE email = 'marcus@email.com')),

    ('Trabalho', 'Avenida Epitácio Pessoa', 1500, 'Sala 304',
     'Bairro dos Estados', 'João Pessoa', 'PB', '58030-001',
     (SELECT user_id FROM users WHERE email = 'marcus@email.com')),

    ('Casa dos Pais', 'Rua Sete de Setembro', 45, NULL,
     'Centro', 'Campina Grande', 'PB', '58400-100',
     (SELECT user_id FROM users WHERE email = 'marcus@email.com')),

    ('Apartamento', 'Avenida Paulista', 1000, 'Bloco B, Apto 52',
     'Bela Vista', 'São Paulo', 'SP', '01310-100',
     (SELECT user_id FROM users WHERE email = 'marcus@email.com')),

    ('Casa de Praia', 'Rua dos Coqueiros', 0, 'Casa 7',
     'Praia do Sol', 'Recife', 'PE', '51020040',
     (SELECT user_id FROM users WHERE email = 'marcus@email.com'));


-- =========================================
-- 5. CONFERIR OS DADOS
-- =========================================

SELECT * FROM category;
SELECT * FROM product;
SELECT * FROM users;
SELECT * FROM address;

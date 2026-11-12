INSERT INTO category (name, description) VALUES ('Hambúrgueres', 'Hambúrgueres artesanais e tradicionais');
INSERT INTO category (name, description) VALUES ('Bebidas', 'Refrigerantes, sucos e cervejas');
INSERT INTO category (name, description) VALUES ('Acompanhamentos', 'Batatas fritas, onion rings e porções');

INSERT INTO product (name, description, price, stock, category_id) VALUES ('X-Bacon', 'Pão brioche, blend 180g, queijo cheddar e bacon crocante.', 32.90, 50, 1);
INSERT INTO product (name, description, price, stock, category_id) VALUES ('X-Salada', 'Pão brioche, blend 180g, queijo prato, alface, tomate e molho especial.', 28.90, 40, 1);
INSERT INTO product (name, description, price, stock, category_id) VALUES ('X-Tudo', 'Pão brioche, blend 180g, queijo, presunto, bacon, ovo, alface, tomate e molho especial.', 36.90, 25, 1);
INSERT INTO product (name, description, price, stock, category_id) VALUES ('Coca-Cola Lata 350ml', 'Refrigerante Coca-Cola tradicional, lata de 350ml.', 6.00, 100, 2);
INSERT INTO product (name, description, price, stock, category_id) VALUES ('Guaraná Antarctica 350ml', 'Refrigerante de guaraná, lata de 350ml.', 5.50, 80, 2);
INSERT INTO product (name, description, price, stock, category_id) VALUES ('Batata Frita P', 'Porção pequena de batatas fritas crocantes.', 12.00, 40, 3);
INSERT INTO product (name, description, price, stock, category_id) VALUES ('Batata Frita G', 'Porção grande de batatas fritas crocantes.', 20.00, 30, 3);

INSERT INTO users (name, email, password_hash) VALUES ('Admin', 'admin@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');
INSERT INTO users (name, email, password_hash) VALUES ('Marcus', 'marcus@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');

INSERT INTO address (name, street, number, complement, neighborhood, city, state, zipcode) VALUES ('Casa', 'Rua das Flores', 120, 'Apto 101', 'Centro', 'João Pessoa', 'PB', '58010-000');
INSERT INTO address (name, street, number, complement, neighborhood, city, state, zipcode) VALUES ('Trabalho', 'Avenida Epitácio Pessoa', 1500, 'Sala 304', 'Bairro dos Estados', 'João Pessoa', 'PB', '58030-001');
INSERT INTO address (name, street, number, complement, neighborhood, city, state, zipcode) VALUES ('Casa dos Pais', 'Rua Sete de Setembro', 45, NULL, 'Centro', 'Campina Grande', 'PB', '58400-100');
INSERT INTO address (name, street, number, complement, neighborhood, city, state, zipcode) VALUES ('Apartamento', 'Avenida Paulista', 1000, 'Bloco B, Apto 52', 'Bela Vista', 'São Paulo', 'SP', '01310-100');
INSERT INTO address (name, street, number, complement, neighborhood, city, state, zipcode) VALUES ('Casa de Praia', 'Rua dos Coqueiros', 0, 'Casa 7', 'Praia do Sol', 'Recife', 'PE', '51020040');
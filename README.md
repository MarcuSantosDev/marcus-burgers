# 🍔 Marcus's Burgers

Aplicação full-stack para uma hamburgueria, com catálogo por categorias, carrinho persistido, autenticação JWT, endereços, checkout e acompanhamento de pedidos.

## ✨ Funcionalidades

- Catálogo de produtos agrupado por categoria.
- Imagens individuais dos produtos vindas do backend.
- Carrinho com quantidade, remoção, limpeza e persistência no navegador.
- Cadastro e login com autenticação JWT.
- Cadastro e seleção de endereços do usuário.
- Criação e acompanhamento de pedidos.
- Controle de acesso para usuário `CLIENT` e administrador `ADMIN`.
- Layout responsivo para computador e dispositivos móveis.

## 🧱 Tecnologias

### Backend

- Java 25
- Spring Boot 4.1.2
- Spring Web MVC
- Spring Data JPA / Hibernate
- Spring Security
- OAuth2 Resource Server / JWT
- Bean Validation
- PostgreSQL
- H2 para o perfil de teste
- Maven
- Lombok

### Frontend

- React 19
- JavaScript ES6+
- Vite 8
- React Router DOM
- ESLint
- npm

## 📁 Estrutura

```text
ProjetoHamburgueria/
├── backend/
│   ├── src/main/java/
│   ├── src/main/resources/
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── hooks/
│   │   ├── pages/
│   │   ├── services/
│   │   ├── styles/
│   │   └── utils/
│   ├── package.json
│   └── vite.config.js
└── README.md
```

## 🚀 Como executar

Abra dois terminais no VS Code.

### 1. Backend

No diretório do backend:

```powershell
cd backend
```

Configure o segredo JWT em uma sessão do PowerShell:

```powershell
$env:JWT_SECRET="uma-chave-secreta-com-pelo-menos-32-caracteres"
```

Execute a aplicação:

```powershell
.\mvnw.cmd spring-boot:run
```

Também é possível executar pelo Maven instalado:

```bash
mvn spring-boot:run
```

O backend iniciará em:

```text
http://localhost:8080
```

### 2. Frontend

Em outro terminal:

```powershell
cd frontend
npm install
npm run dev
```

O frontend iniciará em:

```text
http://localhost:5173
```

## 🧪 Perfis e banco de dados

O projeto possui estas configurações principais:

- `test`: banco H2 em memória e dados iniciais carregados por `data.sql`.
- `dev`: PostgreSQL através das variáveis de ambiente.

O perfil ativo é definido em `application.properties` e pode ser sobrescrito ao iniciar:

```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=test
```

## 🔐 Variáveis de ambiente

### Necessárias

| Variável     | Uso                             |
| ------------ | ------------------------------- |
| `JWT_SECRET` | Assinar e validar os tokens JWT |

### Perfil de desenvolvimento PostgreSQL

| Variável      | Valor padrão   |
| ------------- | -------------- |
| `DB_HOST`     | `localhost`    |
| `DB_PORT`     | `5432`         |
| `DB_NAME`     | `hamburgueria` |
| `DB_USERNAME` | obrigatório    |
| `DB_PASSWORD` | obrigatório    |

Exemplo no Windows:

```powershell
$env:JWT_SECRET="uma-chave-secreta-com-pelo-menos-32-caracteres"
$env:DB_HOST="localhost"
$env:DB_PORT="5432"
$env:DB_NAME="hamburgueria"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="sua-senha"
```

> Não adicione senhas ou chaves reais ao repositório.

Para gerar uma chave em formato Base64:

```powershell
$bytes = New-Object byte[] 32
[System.Security.Cryptography.RandomNumberGenerator]::Create().GetBytes($bytes)
[Convert]::ToBase64String($bytes)
```

## 🌐 Configuração da API no frontend

Por padrão, o frontend usa:

```text
http://localhost:8080
```

Para usar outra URL, crie um arquivo `.env` dentro de `frontend`:

```env
VITE_API_URL=http://localhost:8080
```

Depois, reinicie o servidor Vite.

## 📡 Principais endpoints

| Método | Endpoint         | Descrição                                                | Autenticação |
| ------ | ---------------- | -------------------------------------------------------- | ------------ |
| `POST` | `/auth/register` | Cadastra um cliente                                      | Não          |
| `POST` | `/auth/login`    | Autentica e retorna JWT                                  | Não          |
| `GET`  | `/products`      | Lista produtos                                           | Não          |
| `GET`  | `/products/{id}` | Busca um produto                                         | Não          |
| `GET`  | `/categories`    | Lista categorias                                         | Não          |
| `GET`  | `/addresses`     | Lista endereços do usuário                               | Sim          |
| `POST` | `/addresses`     | Cadastra endereço                                        | Sim          |
| `GET`  | `/orders`        | Lista pedidos do usuário ou de todos, para administrador | Sim          |
| `POST` | `/orders`        | Cria pedido                                              | Sim          |
| `GET`  | `/orders/{id}`   | Busca pedido                                             | Sim          |

As requisições autenticadas devem enviar:

```http
Authorization: Bearer <token>
```

## 🍔 Dados iniciais

O arquivo `backend/src/main/resources/data.sql` popula o banco com:

- Categorias de hambúrgueres, bebidas e acompanhamentos.
- Produtos com nome, descrição, preço, estoque, categoria e URL da imagem.
- Usuários administrativos e clientes.
- Endereços e pedidos de exemplo.

Usuário inicial de exemplo:

```text
E-mail: admin@email.com
Perfil: ADMIN
```

> Os usuários possuem hashes BCrypt no `data.sql`. Use a senha configurada no ambiente ou cadastre uma nova conta pela interface.

## 🖥️ Páginas do frontend

- `/` — página inicial e cardápio por categoria.
- `/login` — login.
- `/register` — cadastro.
- `/checkout` — endereços e confirmação do pedido.
- `/pedidos` — pedidos do usuário.
- `/pedidos/{id}` — confirmação e detalhes de um pedido.

## 🛠️ Comandos úteis

### Frontend

```powershell
npm run dev
npm run build
npm run lint
npm run preview
```

### Backend

```powershell
.\mvnw.cmd test
.\mvnw.cmd clean package
.\mvnw.cmd spring-boot:run
```

## 📦 Build de produção

Frontend:

```powershell
cd frontend
npm run build
```

Os arquivos finais serão gerados em `frontend/dist`.

Backend:

```powershell
cd backend
.\mvnw.cmd clean package
```

O arquivo executável será gerado em `backend/target`.

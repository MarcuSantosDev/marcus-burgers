# 🍔 Marcus's Burgers

Aplicação full-stack para uma hamburgueria, desenvolvida com **React + Vite** no frontend e **Java + Spring Boot** no backend.

O projeto inclui catálogo por categorias, carrinho persistido, autenticação JWT, endereços com consulta ViaCEP, checkout, controle de estoque e painel administrativo.

## 🚀 Comece aqui

| Objetivo                | Comando ou endereço                         |
| ----------------------- | ------------------------------------------- |
| Iniciar tudo com Docker | `docker compose up --build`                 |
| Abrir a loja            | http://localhost:5173                       |
| Abrir o backend         | http://localhost:8080                       |
| Abrir o Swagger         | http://localhost:8080/swagger-ui/index.html |
| Abrir o console do H2   | http://localhost:8080/h2-console            |

### Primeiro uso

1. Na raiz do projeto, copie o arquivo de exemplo:

   ```powershell
   Copy-Item .env.example .env
   ```

2. Inicie a aplicação:

   ```powershell
   docker compose up --build
   ```

3. Acesse http://localhost:5173.

   > O Docker já está configurado para iniciar o backend no perfil `test`, com o banco H2 persistido no volume Docker `h2_data`. Use `jdbc:h2:~/test` no console. Não é necessário configurar PostgreSQL nem trocar a URL manualmente.

### Parar a aplicação

```powershell
# Para os containers e mantém os dados
docker compose down

# Para os containers e apaga os volumes também
docker compose down -v
```

## ✨ Funcionalidades

- Catálogo de produtos agrupado por categoria.
- Imagens individuais dos produtos através de URLs.
- Carrinho com quantidade, remoção, limpeza e persistência no navegador.
- Cadastro e login com autenticação JWT.
- Cadastro e seleção de endereços do usuário.
- Busca automática de endereço por CEP integrada à API ViaCEP no backend.
- Limite de quantidade no carrinho conforme o estoque disponível.
- Controle de estoque com bloqueio pessimista, garantindo que dois pedidos simultâneos não vendam o mesmo último item (coberto por teste de concorrência).
- Atualização automática do catálogo e estoque após finalizar pedidos.
- Perfil do usuário com nome e e-mail no cabeçalho.
- Criação e acompanhamento de pedidos.
- Painel administrativo para acompanhar e alterar status dos pedidos.
- Cadastro, edição e exclusão de categorias pelo administrador.
- Cadastro, edição e exclusão de produtos pelo administrador.
- Administrador pode navegar pelo cardápio e acessar o checkout, mas não possui carrinho nem pode confirmar pedidos.
- Navbar personalizada por perfil: clientes veem pedidos; administradores veem categorias, produtos e pedidos.
- Controle de acesso para usuários `CLIENT` e administradores `ADMIN`.
- Validação de dados com Bean Validation.
- Documentação interativa da API com Swagger UI / OpenAPI.
- Execução completa (banco, backend e frontend) com um único comando via Docker Compose.
- Testes automatizados e integração contínua com GitHub Actions CI.
- Layout responsivo para computadores e dispositivos móveis.

## 📸 Screenshots

### Checkout

![Checkout](docs/checkout.png)

### Pedidos do Cliente

![Pedidos do Cliente](docs/pedidos-cliente.png)

### Admin

![Admin](docs/admin.png)

## 🧱 Tecnologias

### Backend

- Java 25
- Spring Boot 4.1.2
- Spring Web MVC
- Spring Data JPA / Hibernate
- Spring Security
- OAuth2 Resource Server / JWT
- Springdoc OpenAPI / Swagger UI
- ViaCEP API com `RestClient`
- Bean Validation
- PostgreSQL
- H2 para o perfil de teste
- Maven
- Lombok
- JUnit 6.0.3

### Frontend

- React 19
- JavaScript ES6+
- Vite 8
- React Router DOM
- ESLint
- npm

### Infraestrutura

- Docker
- Docker Compose

### CI

- GitHub Actions
- GitHub Flow

## 📁 Estrutura

```text
ProjetoHamburgueria/
├── .github/
│   └── workflows/
│       └── ci.yml
├── backend/
│   ├── src/main/java/
│   ├── src/main/resources/
│   ├── src/test/java/
│   ├── .dockerignore
│   ├── Dockerfile
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── hooks/
│   │   ├── pages/
│   │   ├── services/
│   │   ├── styles/
│   │   └── utils/
│   ├── .dockerignore
│   ├── Dockerfile
│   ├── package.json
│   └── vite.config.js
├── docs/
├── .env.example
├── docker-compose.yml
└── README.md
```

## 📋 Pré-requisitos

Escolha **uma** das formas de execução:

| Forma de execução                    | Necessário                                       |
| ------------------------------------ | ------------------------------------------------ |
| Docker (banco + backend + frontend)  | Docker e Docker Compose, Git                     |
| Local, perfil `test` (H2 em arquivo) | Java 25, Node.js 22 ou superior, Git             |
| Local, perfil `dev` (PostgreSQL)     | Java 25, Node.js 22 ou superior, PostgreSQL, Git |

> Os comandos deste README assumem **Windows com PowerShell**. No Linux/macOS, use `./mvnw` no lugar de `.\mvnw.cmd` e `cp` no lugar de `Copy-Item`.

## 🔐 Variáveis de ambiente

Todas as variáveis ficam em **um único arquivo `.env`**, na raiz do projeto. Não é necessário editar nenhum arquivo `application*.properties`: eles apenas referenciam as variáveis (por exemplo, `${DB_USERNAME}`), e o Spring as preenche ao iniciar.

Clone o projeto e crie o seu `.env` a partir do exemplo:

```powershell
Copy-Item .env.example .env
```

Depois, preencha os valores:

```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=hamburgueria
DB_USERNAME=seu_usuario
DB_PASSWORD=sua_senha
JWT_SECRET=sua-chave-base64
```

| Variável      | Descrição                                                           | Padrão                             |
| ------------- | ------------------------------------------------------------------- | ---------------------------------- |
| `JWT_SECRET`  | Chave Base64 (mínimo 32 bytes) para assinar e validar os tokens JWT | obrigatória no `dev` e em produção |
| `DB_HOST`     | Host do PostgreSQL                                                  | `localhost`                        |
| `DB_PORT`     | Porta do PostgreSQL                                                 | `5432`                             |
| `DB_NAME`     | Nome do banco                                                       | `hamburgueria`                     |
| `DB_USERNAME` | Usuário do banco                                                    | obrigatória                        |
| `DB_PASSWORD` | Senha do banco                                                      | obrigatória                        |

> O perfil `test` (padrão ao rodar localmente) **não precisa de nenhuma variável**: usa H2 em `jdbc:h2:~/test` e uma chave JWT própria definida em `application-test.properties`.

### Como o `.env` é usado

- **Docker Compose:** o `.env` é lido automaticamente e repassado ao container do backend. Dentro do Docker, o `DB_HOST` é sobrescrito para `db` (nome do serviço do PostgreSQL), então o valor `localhost` do `.env` não interfere.
- **Execução local:** o `application.properties` importa o arquivo com `spring.config.import=optional:file:.env[.properties]`. Execute o backend a partir da raiz do projeto ou de dentro da pasta `backend`. Como o import é `optional`, a ausência do `.env` (como no perfil `test` e no CI) não gera erro.

### Gerar a chave JWT

A chave deve estar em **Base64** e ter pelo menos 32 bytes após a decodificação, conforme a configuração HS256 utilizada pelo projeto. Para gerar uma:

```powershell
$bytes = New-Object byte[] 32
[System.Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
[Convert]::ToBase64String($bytes)
```

Ou, no Linux/macOS:

```bash
openssl rand -base64 32
```

O token expira em 3600 segundos (1 hora), valor configurado em `security.jwt.expiration`.

> ⚠️ **Nunca commite o `.env`.** Ele está no `.gitignore`; apenas o `.env.example` (com valores fictícios) vai para o repositório. Utilize uma chave JWT exclusiva para cada ambiente.

## 🚀 Como executar

### Opção 1: Docker (recomendado)

Na raiz do projeto, execute:

```powershell
docker compose up --build
```

| Serviço           | Endereço                                    |
| ----------------- | ------------------------------------------- |
| Loja (frontend)   | http://localhost:5173                       |
| API do backend    | http://localhost:8080                       |
| Swagger / OpenAPI | http://localhost:8080/swagger-ui/index.html |
| Console do H2     | http://localhost:8080/h2-console            |
| PostgreSQL        | `localhost:5432`                            |

Por padrão, o Compose inicia o backend com o perfil `test` e o banco H2. O banco fica persistido no volume Docker `h2_data`; os dados não são apagados quando o container reinicia.

Comandos úteis:

```powershell
# Executar em segundo plano
docker compose up -d --build

# Acompanhar os logs
docker compose logs -f backend

# Parar os containers, mantendo os dados
docker compose down

# Parar e apagar todos os volumes/dados
docker compose down -v
```

#### Trocar o banco usado pelo Docker

No serviço `backend` do `docker-compose.yml`, altere somente:

```yaml
SPRING_PROFILES_ACTIVE: dev
```

Valores disponíveis:

| Valor  | Banco                       |
| ------ | --------------------------- |
| `test` | H2 persistente em `h2_data` |
| `dev`  | PostgreSQL no serviço `db`  |

Depois recrie o backend:

```powershell
docker compose up -d --build backend
```

Não é necessário remover nem adicionar campos de conexão JDBC: o perfil selecionado define automaticamente o banco correto.

| Perfil no Docker | Banco      | Persistência           | Dados de demonstração          |
| ---------------- | ---------- | ---------------------- | ------------------------------ |
| `test` (padrão)  | H2         | Volume `h2_data`       | Carregados pelo `data.sql`     |
| `dev`            | PostgreSQL | Volume `postgres_data` | Não carregados automaticamente |

No perfil `test`, a chave JWT definida em `application-test.properties` é usada e o valor de `JWT_SECRET` do `.env` não é necessário.

### Opção 2: Execução local (perfil `test`, sem instalar banco)

Abra dois terminais no VS Code.

#### Backend

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

Também é possível executar pelo Maven instalado:

```bash
mvn spring-boot:run
```

O backend iniciará em http://localhost:8080.

> O perfil padrão é `test`, que usa **H2 em arquivo**. Não é necessário instalar PostgreSQL para experimentar o projeto, e os dados de exemplo do `data.sql` são carregados a cada inicialização.

#### Frontend

Em outro terminal:

```powershell
cd frontend
npm install
npm run dev
```

O frontend iniciará em http://localhost:5173.

### Opção 3: Execução local com PostgreSQL (perfil `dev`)

Com o PostgreSQL instalado e rodando, e o `.env` preenchido:

```powershell
cd backend
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev"
```

O frontend é iniciado como na Opção 2.

## 🧪 Perfis e banco de dados

O projeto possui configurações para diferentes ambientes:

| Perfil              | Banco         | Uso                                             |
| ------------------- | ------------- | ----------------------------------------------- |
| `test` (**padrão**) | H2 em arquivo | Execução local rápida e testes automatizados    |
| `dev`               | PostgreSQL    | Docker Compose e desenvolvimento com banco real |

O perfil ativo é definido em `application.properties` (`spring.profiles.active=test`) e pode ser sobrescrito:

- **Maven:** `-Dspring-boot.run.profiles=dev`
- **Docker:** altere `SPRING_PROFILES_ACTIVE` no serviço `backend` do `docker-compose.yml` e recrie o backend; veja [Trocar o banco usado pelo Docker](#trocar-o-banco-usado-pelo-docker).

O perfil `test` também é utilizado pelos testes automatizados, o que evita alterações no banco de desenvolvimento.

### H2 Console (perfil `test`)

Com o backend em execução, abra:

```text
http://localhost:8080/h2-console
```

Use os dados abaixo:

| Campo        | Docker           | Execução local       |
| ------------ | ---------------- | -------------------- |
| Driver Class | `org.h2.Driver`  | `org.h2.Driver`      |
| JDBC URL     | `jdbc:h2:~/test` | `jdbc:h2:mem:testdb` |
| User Name    | `sa`             | `sa`                 |
| Password     | _(vazio)_        | _(vazio)_            |

- No Docker, o banco é persistido no volume `h2_data`.
- Na execução local, os dados ficam somente em memória e são recriados a cada inicialização.
- O console aceita conexões externas ao container por estar configurado com `spring.h2.console.settings.web-allow-others=true`.

> Mantenha essa opção apenas em ambientes de desenvolvimento ou testes. Não habilite o H2 Console em produção.

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

> No Docker, a URL da API deve continuar sendo `http://localhost:8080`, e não `http://backend:8080`: as requisições são feitas pelo **navegador**, que roda na sua máquina e não enxerga os nomes de serviço da rede do Docker.

## 📖 Documentação da API — Swagger

O projeto utiliza Springdoc OpenAPI para gerar a documentação da API e disponibilizar uma interface interativa para visualizar e testar os endpoints.

Com o backend em execução, acesse:

| Recurso      | URL                                         |
| ------------ | ------------------------------------------- |
| Swagger UI   | http://localhost:8080/swagger-ui/index.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs           |

A documentação permite:

- Visualizar os endpoints disponíveis.
- Consultar métodos HTTP, parâmetros e respostas.
- Visualizar os modelos de requisição e resposta (DTOs).
- Testar requisições diretamente pela interface Swagger.

Os endpoints de documentação são públicos. Os endpoints protegidos continuam exigindo autenticação JWT.

## 📡 Principais endpoints

| Método   | Endpoint                   | Descrição                                                | Autenticação |
| -------- | -------------------------- | -------------------------------------------------------- | ------------ |
| `POST`   | `/auth/login`              | Autentica e retorna JWT                                  | Não          |
| `GET`    | `/products`                | Lista produtos                                           | Não          |
| `GET`    | `/products/{id}`           | Busca um produto                                         | Não          |
| `POST`   | `/products`                | Cadastra produto                                         | Sim — ADMIN  |
| `PUT`    | `/products/{id}`           | Edita produto                                            | Sim — ADMIN  |
| `DELETE` | `/products/{id}`           | Exclui produto                                           | Sim — ADMIN  |
| `GET`    | `/categories`              | Lista categorias                                         | Não          |
| `POST`   | `/categories`              | Cadastra categoria                                       | Sim — ADMIN  |
| `PUT`    | `/categories/{id}`         | Edita categoria                                          | Sim — ADMIN  |
| `DELETE` | `/categories/{id}`         | Exclui categoria                                         | Sim — ADMIN  |
| `GET`    | `/addresses`               | Lista endereços do usuário                               | Sim          |
| `POST`   | `/addresses`               | Cadastra endereço                                        | Sim          |
| `GET`    | `/addresses/cep/{zipCode}` | Consulta endereço pelo ViaCEP                            | Não          |
| `GET`    | `/orders`                  | Lista pedidos do usuário ou de todos, para administrador | Sim          |
| `POST`   | `/orders`                  | Cria pedido e reduz o estoque                            | Sim — CLIENT |
| `GET`    | `/orders/{id}`             | Busca pedido                                             | Sim          |
| `PUT`    | `/orders/{id}`             | Atualiza status conforme as regras de perfil             | Sim          |

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

Credenciais de demonstração (**apenas para desenvolvimento**):

| Perfil   | E-mail             | Senha             |
| -------- | ------------------ | ----------------- |
| `ADMIN`  | `admin@email.com`  | `[senha-de-demo]` |
| `CLIENT` | `marcus@email.com` | `[senha-de-demo]` |

Você também pode criar uma conta nova em `/register`. Contas criadas pela interface têm o perfil `CLIENT`.

> Os usuários do `data.sql` possuem senhas armazenadas como hash BCrypt.
>
> O `data.sql` limpa todas as tabelas (`DELETE FROM`) antes de inserir os dados de exemplo. No perfil `test`, ele é carregado em H2 em arquivo e recriado pelo Hibernate; no perfil `dev` com PostgreSQL, o script não é executado automaticamente.

## 🖥️ Páginas do frontend

### Usuário cliente

- `/` — página inicial e cardápio por categoria.
- `/login` — login.
- `/register` — cadastro.
- `/checkout` — endereços e confirmação do pedido.
- `/pedidos` — pedidos do usuário, separados entre ativos e encerrados.
- `/pedidos/{id}` — confirmação e detalhes de um pedido.

### Administrador

- `/admin/pedidos` — controle e alteração de status dos pedidos.
- `/admin/categorias` — cadastro, edição e exclusão de categorias.
- `/admin/produtos` — cadastro, edição e exclusão de produtos.
- `/` — consulta do cardápio sem carrinho ou botão de adicionar.
- `/checkout` — tela acessível, porém sem confirmar pedido.

Na navbar do administrador aparecem **Categorias**, **Produtos** e **Pedidos**. O link de perfil abre o painel administrativo, e não a página “Meus pedidos”.

## 📦 Status dos pedidos

```text
PENDING → PREPARING → READY → DELIVERED
   └──────────┴─────────┘
          ↓
      CANCELLED
```

| Status      | Descrição                              |
| ----------- | -------------------------------------- |
| `PENDING`   | Pendente                               |
| `PREPARING` | Preparando                             |
| `READY`     | Pronto                                 |
| `DELIVERED` | Entregue (final)                       |
| `CANCELLED` | Cancelado (final, exibido em vermelho) |

A trilha de acompanhamento do cliente possui 4 etapas (`PENDING` a `DELIVERED`). Pedidos `CANCELLED` são exibidos como pedidos encerrados.

No painel administrativo:

- O status avança uma etapa por vez, sem retroceder.
- `DELIVERED` e `CANCELLED` são estados finais e ficam bloqueados.
- Pedidos ainda ativos podem ser cancelados.

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

### Docker

```bash
docker compose up --build
docker compose down
docker compose logs -f backend
docker compose --profile tests run --rm backend-test   # executa os testes do backend
```

## 📦 Build de produção

### Frontend

```powershell
cd frontend
npm run build
```

Os arquivos finais serão gerados em `frontend/dist`.

### Backend

```powershell
cd backend
.\mvnw.cmd clean package
```

O arquivo executável será gerado em `backend/target`.

> O `Dockerfile` do frontend executa o servidor de desenvolvimento do Vite e serve ao uso em desenvolvimento. Para produção, gere o build (`npm run build`) e sirva a pasta `dist` com um servidor web como o Nginx.

## 🧪 Testes

O backend possui testes automatizados com **JUnit 6.0.3** e Spring Boot.

Os testes utilizam o perfil `test`, configurado em `application-test.properties`, com banco H2 em arquivo. Dessa forma, não dependem do PostgreSQL de desenvolvimento nem de variáveis de ambiente.

### Teste de concorrência do estoque

O teste `StockConcurrencyTest` simula dois clientes tentando realizar um pedido simultaneamente para comprar o último produto disponível.

O teste verifica se:

- Apenas um pedido é confirmado.
- O estoque do produto permanece em zero.
- Apenas um pedido é registrado no banco de dados.

A implementação utiliza `CountDownLatch` para sincronizar o início das requisições concorrentes e valida o controle de estoque com bloqueio pessimista no banco de dados.

### Executar os testes

Na pasta `backend`, execute todos os testes:

```powershell
.\mvnw.cmd test
```

Para executar apenas o teste de concorrência:

```powershell
.\mvnw.cmd -Dtest=StockConcurrencyTest test
```

Para executar os testes e validar o build:

```powershell
.\mvnw.cmd clean verify
```

### Executar os testes com Docker

A imagem do backend (`Dockerfile`) contém apenas o JRE e o `.jar`, sem Maven nem código-fonte, então os testes rodam em um container separado. O `docker-compose.yml` possui o serviço `backend-test` para isso:

```yaml
backend-test:
  image: maven:3.9-eclipse-temurin-25
  profiles: ["tests"]
  working_dir: /app
  volumes:
    - ./backend:/app
    - maven_repo:/root/.m2
  command: mvn -B clean verify
```

Execute todos os testes:

```bash
docker compose --profile tests run --rm backend-test
```

Execute apenas o teste de concorrência:

```bash
docker compose --profile tests run --rm backend-test mvn -B -Dtest=StockConcurrencyTest test
```

Observações:

- O serviço só roda quando chamado com `--profile tests`; o `docker compose up` normal **não** o inicia. (O `tests` é um _profile do Compose_, sem relação com o perfil `test` do Spring.)
- Os testes usam o perfil `test` do Spring (H2 em arquivo), então **não precisam do PostgreSQL nem do `.env`**. Por segurança, o serviço não usa `env_file`: o `setUp()` do teste executa `deleteAll()` nas tabelas e jamais deve apontar para o banco do Docker.
- O volume `maven_repo` guarda as dependências do Maven: a primeira execução baixa tudo e demora; as seguintes são bem mais rápidas.
- Se o Maven reclamar de permissão em `backend/target` no Linux, use `--user "$(id -u):$(id -g)"` no comando `docker compose run`.

## ⚙️ Integração contínua — GitHub Actions

O projeto utiliza **GitHub Actions** para executar automaticamente verificações de integração contínua (CI).

O workflow está localizado em:

```text
.github/workflows/ci.yml
```

### Quando o CI é executado?

O workflow é acionado:

- A cada `push` para a branch `main`.
- A cada Pull Request direcionado à branch `main`.

### Backend

O job do backend:

- Configura Java 25.
- Ativa o perfil `test`.
- Utiliza H2 em arquivo.
- Executa os testes automatizados.
- Valida o build com Maven.

Comando executado:

```bash
bash ./mvnw -B clean verify
```

O CI **não utiliza o arquivo `.env`** (ele não é versionado) e não precisa dele: a chave JWT utilizada nos testes é exclusiva do perfil `test`, definida em `application-test.properties`, e o banco é H2 em arquivo. O CI não acessa nenhuma chave ou senha de desenvolvimento ou produção.

### Frontend

O job do frontend:

- Configura Node.js 22.
- Instala as dependências com `npm ci`.
- Executa o ESLint.
- Gera o build de produção.

Comandos executados:

```bash
npm ci
npm run lint
npm run build
```

### GitHub Flow

O desenvolvimento segue o fluxo:

1. Criar uma branch para a funcionalidade ou correção.
2. Implementar e testar as alterações.
3. Enviar a branch para o GitHub.
4. Abrir um Pull Request para `main`.
5. Aguardar a aprovação dos checks do CI.
6. Fazer o merge para `main`.

O CI valida o backend e o frontend antes da integração das alterações na branch principal.

> O workflow atual realiza integração contínua (CI). O deploy automático (CD) poderá ser configurado posteriormente com a plataforma de hospedagem.

## 👨‍💻 Autor

**Marcus Vinícius Vieira Santos**

Projeto pessoal de portfólio para praticar desenvolvimento full-stack com Java, Spring Boot, React e integração de APIs REST.

- LinkedIn: https://www.linkedin.com/in/marcusantosdev
- GitHub: https://github.com/MarcuSantosDev
- E-mail: marcus.xtreme03@gmail.com

## 📄 Licença

Distribuído sob a licença MIT. Consulte o arquivo `LICENSE` para mais detalhes.

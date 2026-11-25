# 🍔 Marcus's Burgers

Aplicação full-stack para uma hamburgueria, desenvolvida com **React + Vite** no frontend e **Java + Spring Boot** no backend.

O projeto inclui catálogo por categorias, carrinho persistido, autenticação JWT, endereços com consulta ViaCEP, checkout, controle de estoque e painel administrativo.

## ✨ Funcionalidades

- Catálogo de produtos agrupado por categoria, com imagens individuais por URL.
- Carrinho com quantidade, remoção, limpeza e persistência no navegador.
- Limite de quantidade no carrinho conforme o estoque disponível.
- Cadastro e login com autenticação JWT.
- Cadastro e seleção de endereços, com busca automática por CEP (API ViaCEP, via backend).
- Controle de estoque com bloqueio pessimista, garantindo que dois pedidos simultâneos não vendam o mesmo último item (coberto por teste de concorrência).
- Atualização automática do catálogo e do estoque após finalizar pedidos.
- Criação e acompanhamento de pedidos.
- Perfil do usuário com nome e e-mail no cabeçalho.
- Painel administrativo para acompanhar e alterar o status dos pedidos.
- Cadastro, edição e exclusão de categorias e de produtos pelo administrador.
- Administrador pode navegar pelo cardápio e acessar o checkout, mas não possui carrinho nem pode confirmar pedidos.
- Navbar personalizada por perfil: clientes veem pedidos; administradores veem categorias, produtos e pedidos.
- Controle de acesso para usuários `CLIENT` e administradores `ADMIN`.
- Validação de dados com Bean Validation.
- Documentação interativa da API com Swagger UI / OpenAPI.
- Execução completa (banco, backend e frontend) com um único comando via Docker Compose.
- Testes automatizados e integração contínua com GitHub Actions.
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

### Infraestrutura e CI

- Docker e Docker Compose
- GitHub Actions
- GitHub Flow

## ⚡ Início rápido

Com Docker e Git instalados, na raiz do projeto:

```powershell
Copy-Item .env.example .env
docker compose up --build
```

> O `.env` é necessário mesmo no perfil padrão, pois o container do PostgreSQL também é iniciado. Os valores do `.env.example` já funcionam sem edição.

| Serviço           | Endereço                                    |
| ----------------- | ------------------------------------------- |
| Loja (frontend)   | http://localhost:5173                       |
| API do backend    | http://localhost:8080                       |
| Swagger / OpenAPI | http://localhost:8080/swagger-ui/index.html |
| Console do H2     | http://localhost:8080/h2-console            |
| PostgreSQL        | `localhost:5433` (usado no perfil `dev`)    |

Por padrão, o Compose inicia o backend com o perfil `test` e o banco **H2**, com os dados de demonstração. Não é necessário configurar PostgreSQL nem a URL do H2.

Comandos úteis:

```powershell
docker compose up -d --build   # executa em segundo plano
docker compose logs -f backend # acompanha os logs do backend
docker compose down            # para os containers, mantendo os volumes
docker compose down -v         # para os containers e apaga os volumes (dados)
```

Para usar o PostgreSQL em vez do H2, veja [Trocar o perfil no Docker](#trocar-o-perfil-no-docker).

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
| Local, perfil `test` (H2 em memória) | Java 25, Node.js 22 ou superior, Git             |
| Local, perfil `dev` (PostgreSQL)     | Java 25, Node.js 22 ou superior, PostgreSQL, Git |

> Os comandos deste README assumem **Windows com PowerShell**. No Linux/macOS, use `./mvnw` no lugar de `.\mvnw.cmd` e `cp` no lugar de `Copy-Item`.

## 🔐 Variáveis de ambiente

Todas as variáveis ficam em **um único arquivo `.env`**, na raiz do projeto. Não é necessário editar nenhum arquivo `application*.properties`: eles apenas referenciam as variáveis (por exemplo, `${DB_USERNAME}`), e o Spring as preenche ao iniciar.

Crie o arquivo a partir do exemplo (`Copy-Item .env.example .env`) e preencha os valores:

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

> O perfil `test` **não precisa de nenhuma variável do Spring**: usa H2 e uma chave JWT própria, definida em `application-test.properties`.

**Como o `.env` é usado:**

- **Docker Compose:** é lido automaticamente e repassado ao backend. Dentro do Docker, o `DB_HOST` é sobrescrito para `db` (nome do serviço do PostgreSQL), então o `localhost` do `.env` não interfere.
- **Execução local:** o `application.properties` importa o arquivo com `spring.config.import=optional:file:.env[.properties]`. Execute o backend a partir da raiz do projeto ou da pasta `backend`. Como o import é `optional`, a ausência do `.env` (no perfil `test` e no CI) não gera erro.

**Gerar a chave JWT:** a chave deve estar em Base64 e ter pelo menos 32 bytes após a decodificação (HS256).

```powershell
$bytes = New-Object byte[] 32
[System.Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
[Convert]::ToBase64String($bytes)
```

No Linux/macOS: `openssl rand -base64 32`. O token expira em 3600 segundos (1 hora), valor definido em `security.jwt.expiration`.

> ⚠️ **Nunca commite o `.env`.** Ele está no `.gitignore`; apenas o `.env.example` (com valores fictícios) vai para o repositório. Use uma chave JWT exclusiva para cada ambiente.

## 💻 Execução local

Para rodar sem Docker, abra dois terminais no VS Code.

### Backend

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

Também é possível usar o Maven instalado: `mvn spring-boot:run`. O backend inicia em http://localhost:8080 com o perfil padrão `test` (H2 em memória, sem instalar banco).

Para usar o PostgreSQL, preencha o `.env` e execute com o perfil `dev`:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev"
```

O banco pode ser um PostgreSQL instalado na máquina ou o container do projeto (`docker compose up -d db`). Neste caso, defina `DB_PORT=5433` no `.env`.

### Frontend

```powershell
cd frontend
npm install
npm run dev
```

O frontend inicia em http://localhost:5173.

## 🧪 Perfis e banco de dados

| Perfil              | Banco      | Persistência                                                  | Dados de demonstração          |
| ------------------- | ---------- | ------------------------------------------------------------- | ------------------------------ |
| `test` (**padrão**) | H2         | Memória (local) ou volume `h2_data` (Docker)                  | Carregados pelo `data.sql`     |
| `dev`               | PostgreSQL | Volume `postgres_data` (Docker) ou instalação local           | Não carregados automaticamente |

O perfil ativo é definido em `application.properties` (`spring.profiles.active=test`) e pode ser sobrescrito:

- **Maven:** `-Dspring-boot.run.profiles=dev`
- **Docker:** `SPRING_PROFILES_ACTIVE`, no `docker-compose.yml` (veja abaixo).

O perfil `test` também é usado pelos testes automatizados, o que evita alterações no banco de desenvolvimento.

### Trocar o perfil no Docker

Edite o serviço `backend` no `docker-compose.yml` e use:

```yaml
environment:
  SPRING_PROFILES_ACTIVE: dev
  DB_HOST: db
```

Remova o campo `SPRING_DATASOURCE_URL: jdbc:h2:~/test`, pois o perfil `dev` usa a configuração PostgreSQL de `application-dev.properties`. Depois, recrie o backend:

```powershell
docker compose up -d --build backend
```

Para voltar ao H2, restaure `SPRING_PROFILES_ACTIVE: test` e a linha `SPRING_DATASOURCE_URL` removida, e execute o mesmo comando.

### Acessar o PostgreSQL (pgAdmin, DBeaver)

O container publica o banco na porta **5433** do seu computador, para não conflitar com um PostgreSQL instalado localmente (que usa a 5432). Dentro do Docker, o backend continua usando `db:5432`.

| Campo    | Valor                                    |
| -------- | ---------------------------------------- |
| Host     | `127.0.0.1`                              |
| Porta    | `5433`                                   |
| Banco    | `hamburgueria` (valor de `DB_NAME`)      |
| Usuário  | valor de `DB_USERNAME` no `.env`         |
| Senha    | valor de `DB_PASSWORD` no `.env`         |

> Use `127.0.0.1` em vez de `localhost`: em alguns Windows o `localhost` resolve para IPv6 (`::1`) e pode cair em outro servidor. As tabelas só aparecem se o backend estiver no perfil `dev`.

### H2 Console (perfil `test`)

Com o backend em execução, abra http://localhost:8080/h2-console e use:

| Campo        | Docker           | Execução local       |
| ------------ | ---------------- | -------------------- |
| Driver Class | `org.h2.Driver`  | `org.h2.Driver`      |
| JDBC URL     | `jdbc:h2:~/test` | `jdbc:h2:mem:testdb` |
| User Name    | `sa`             | `sa`                 |
| Password     | _(vazio)_        | _(vazio)_            |

- No Docker, o arquivo do banco fica no volume `h2_data`. Na execução local, os dados ficam só em memória.
- O console aceita conexões externas ao container porque `spring.h2.console.settings.web-allow-others=true` está configurado.

> Mantenha essa opção apenas em desenvolvimento e testes. Não habilite o H2 Console em produção.

## 🌐 Configuração da API no frontend

Por padrão, o frontend usa `http://localhost:8080`. Para usar outra URL, crie um arquivo `.env` dentro de `frontend` e reinicie o Vite:

```env
VITE_API_URL=http://localhost:8080
```

> No Docker, a URL deve continuar sendo `http://localhost:8080`, e não `http://backend:8080`: as requisições são feitas pelo **navegador**, que roda na sua máquina e não enxerga os nomes de serviço da rede do Docker.

## 📖 Documentação da API — Swagger

O projeto usa Springdoc OpenAPI para gerar a documentação e uma interface interativa que permite visualizar os endpoints, parâmetros, respostas e DTOs, além de testar requisições. Com o backend em execução:

| Recurso      | URL                                         |
| ------------ | ------------------------------------------- |
| Swagger UI   | http://localhost:8080/swagger-ui/index.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs           |

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
- Usuários administrativos e clientes (senhas armazenadas como hash BCrypt).
- Endereços e pedidos de exemplo.

Credenciais de demonstração (**apenas para desenvolvimento**):

| Perfil   | E-mail             | Senha             |
| -------- | ------------------ | ----------------- |
| `ADMIN`  | `admin@email.com`  | `[senha-de-demo]` |
| `CLIENT` | `marcus@email.com` | `[senha-de-demo]` |

Você também pode criar uma conta em `/register`; contas criadas pela interface têm o perfil `CLIENT`.

> O `data.sql` limpa todas as tabelas (`DELETE FROM`) antes de inserir os dados de exemplo, então os dados são recriados a cada inicialização. Por isso, ele é carregado apenas no perfil `test` e não é executado no perfil `dev` com PostgreSQL, onde o banco inicia vazio (sem usuário `ADMIN`).

## 🖥️ Páginas do frontend

### Cliente

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
- `/` — consulta do cardápio, sem carrinho nem botão de adicionar.
- `/checkout` — tela acessível, porém sem confirmar pedido.

Na navbar do administrador aparecem **Categorias**, **Produtos** e **Pedidos**. O link de perfil abre o painel administrativo, e não a página "Meus pedidos".

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

A trilha de acompanhamento do cliente possui 4 etapas (`PENDING` a `DELIVERED`). Pedidos `CANCELLED` são exibidos como encerrados.

No painel administrativo:

- O status avança uma etapa por vez, sem retroceder.
- `DELIVERED` e `CANCELLED` são estados finais e ficam bloqueados.
- Pedidos ainda ativos podem ser cancelados.

## 📦 Build de produção

```powershell
cd frontend
npm run build          # gera frontend/dist
```

```powershell
cd backend
.\mvnw.cmd clean package   # gera o .jar em backend/target
```

> O `Dockerfile` do frontend executa o servidor de desenvolvimento do Vite e serve ao uso em desenvolvimento. Para produção, sirva a pasta `dist` com um servidor web como o Nginx.

Outros scripts do frontend: `npm run lint` (ESLint) e `npm run preview` (pré-visualiza o build).

## 🧪 Testes

O backend possui testes automatizados com **JUnit 6.0.3** e Spring Boot. Eles usam o perfil `test` (`application-test.properties`, H2 em memória), portanto não dependem do PostgreSQL nem de variáveis de ambiente.

### Teste de concorrência do estoque

O `StockConcurrencyTest` simula dois clientes tentando comprar, ao mesmo tempo, o último produto disponível. Ele verifica que:

- apenas um pedido é confirmado;
- o estoque do produto permanece em zero;
- apenas um pedido é registrado no banco.

A implementação usa `CountDownLatch` para sincronizar o início das requisições concorrentes e valida o bloqueio pessimista do banco.

### Executar os testes

Na pasta `backend`:

```powershell
.\mvnw.cmd test                                # todos os testes
.\mvnw.cmd -Dtest=StockConcurrencyTest test    # apenas o teste de concorrência
.\mvnw.cmd clean verify                        # testes + validação do build
```

### Executar os testes com Docker

A imagem do backend contém apenas o JRE e o `.jar`, sem Maven nem código-fonte, então os testes rodam em um container separado. O `docker-compose.yml` possui o serviço `backend-test` para isso:

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

```bash
docker compose --profile tests run --rm backend-test                                       # todos os testes
docker compose --profile tests run --rm backend-test mvn -B -Dtest=StockConcurrencyTest test  # apenas concorrência
```

- O serviço só roda com `--profile tests`; o `docker compose up` normal **não** o inicia. (O `tests` é um _profile do Compose_, sem relação com o perfil `test` do Spring.)
- Por segurança, o serviço não usa `env_file`: o `setUp()` do teste executa `deleteAll()` nas tabelas e jamais deve apontar para o banco do Docker.
- O volume `maven_repo` guarda as dependências: a primeira execução demora, as seguintes são bem mais rápidas.
- No Linux, se houver erro de permissão em `backend/target`, adicione `--user "$(id -u):$(id -g)"` ao `docker compose run`.

## ⚙️ Integração contínua — GitHub Actions

O workflow `.github/workflows/ci.yml` roda a cada `push` e a cada Pull Request para a branch `main`.

**Backend:** configura Java 25, ativa o perfil `test` (H2 em memória) e executa os testes e o build com `bash ./mvnw -B clean verify`.

**Frontend:** configura Node.js 22 e executa `npm ci`, `npm run lint` e `npm run build`.

O CI **não utiliza o `.env`** (ele não é versionado) e não precisa dele: a chave JWT dos testes é exclusiva do perfil `test`, e o banco é o H2. Nenhuma chave ou senha de desenvolvimento ou produção é acessada.

### GitHub Flow

1. Criar uma branch para a funcionalidade ou correção.
2. Implementar e testar as alterações.
3. Enviar a branch para o GitHub.
4. Abrir um Pull Request para `main`.
5. Aguardar a aprovação dos checks do CI.
6. Fazer o merge para `main`.

> O workflow atual realiza integração contínua (CI). O deploy automático (CD) poderá ser configurado posteriormente com a plataforma de hospedagem.

## 👨‍💻 Autor

**Marcus Vinícius Vieira Santos**

Projeto pessoal de portfólio para praticar desenvolvimento full-stack com Java, Spring Boot, React e integração de APIs REST.

- LinkedIn: https://www.linkedin.com/in/marcusantosdev
- GitHub: https://github.com/MarcuSantosDev
- E-mail: marcus.xtreme03@gmail.com

## 📄 Licença

Distribuído sob a licença MIT. Consulte o arquivo `LICENSE` para mais detalhes.

## ⚙️ Backend

The backend is developed using **Java and Spring Boot**, following a layered architecture with REST APIs and PostgreSQL for data persistence. Authentication is stateless, based on **JWT**.

### Technologies and Dependencies

| Dependency | Purpose |
|---|---|
| Spring Web | Development of REST APIs and HTTP endpoints |
| Spring Data JPA | Data persistence and ORM integration |
| PostgreSQL Driver | Connection to the PostgreSQL database |
| Validation | Validation of incoming data |
| Lombok | Reduction of boilerplate code |
| Spring Security | Authentication and authorization |
| OAuth2 Resource Server | JWT encoding and decoding (`JwtEncoder` / `JwtDecoder`) |
| Spring Boot DevTools | Development productivity and automatic restart |
| H2 Database | In-memory database used by the `test` profile |

### Main Technologies

- **Java** — Programming language.
- **Spring Boot** — Backend application framework.
- **Spring Security + JWT** — Stateless authentication and role-based authorization.
- **Spring Data JPA / Hibernate** — Object-relational mapping and persistence.
- **PostgreSQL** — Relational database.
- **Maven** — Dependency and project management.

### Authentication

1. `POST /auth/login` receives e-mail and password and returns a signed JWT.
2. Protected requests send the token in the header `Authorization: Bearer <token>`.
3. A custom `JwtAuthenticationFilter` validates the token and loads the user's role into the security context.

Roles: `CLIENT` and `ADMIN`.

### Configuration

The application reads sensitive data from **environment variables**. Define them before running.

#### Required variables

The application will **not start** without these.

| Variable | Description |
|---|---|
| `JWT_SECRET` | Key used to sign JWT tokens (Base64, at least 32 bytes) |
| `DB_USERNAME` | PostgreSQL user |
| `DB_PASSWORD` | PostgreSQL password |

#### Optional variables

If not set, the default value is used.

| Variable | Description | Default |
|---|---|---|
| `DB_HOST` | PostgreSQL host | `localhost` |
| `DB_PORT` | PostgreSQL port | `5432` |
| `DB_NAME` | Database name | `hamburgueria` |

#### Generating the JWT secret

```bash
# Linux / macOS / Git Bash
openssl rand -base64 32
```

```powershell
# Windows (PowerShell)
$bytes = New-Object byte[] 32
[System.Security.Cryptography.RandomNumberGenerator]::Create().GetBytes($bytes)
[Convert]::ToBase64String($bytes)
```

#### Setting the variables

```bash
# Linux / macOS
export JWT_SECRET=<generated value>
export DB_USERNAME=<user>
export DB_PASSWORD=<password>
```

```powershell
# Windows (PowerShell)
$env:JWT_SECRET="<generated value>"
$env:DB_USERNAME="<user>"
$env:DB_PASSWORD="<password>"
```

In IntelliJ: *Run → Edit Configurations → Environment variables*.

> **Never** commit real secrets or passwords to the repository.

### Profiles

- `dev` (default): PostgreSQL.
- `test`: H2 in-memory database with its own JWT secret. Activate with `@ActiveProfiles("test")` or `--spring.profiles.active=test`.

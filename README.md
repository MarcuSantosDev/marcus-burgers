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

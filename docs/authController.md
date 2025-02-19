# Documentação dos Endpoints do AuthController

## Base URL
```
/auth
```

## Endpoints

### 1. Login

**Endpoint:**
```
POST /auth/login
```

**Descrição:**
Autentica um utilizador e retorna um token JWT.

**Requisição:**
```json
{
  "login": "string",
  "password": "string"
}
```

**Respostas:**
- `200 OK` - Retorna um token JWT.
- `401 UNAUTHORIZED` - Login ou palavra-passe inválidos.

**Exemplo de Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

### 2. Obter Utilizador Autenticado

**Endpoint:**
```
GET /auth/me
```

**Descrição:**
Retorna os dados do utilizador autenticado.

**Cabeçalhos:**
```
Authorization: Bearer <token>
```

**Respostas:**
- `200 OK` - Retorna os dados do utilizador autenticado.
- `401 UNAUTHORIZED` - Token inválido ou expirado.

**Exemplo de Resposta:**
```json
{
  "id": 1,
  "login": "user@example.com",
  "role": "USER"
}
```

---

### 3. Registar Utilizador

**Endpoint:**
```
POST /auth/register
```

**Descrição:**
Regista um novo utilizador na plataforma.

**Requisição:**

```json
{
  "login": "string",
  "password": "string",
  "role": "USER | ADMIN | ETC" 
}
```

**Respostas:**
- `200 OK` - Utilizador registado com sucesso.
- `400 BAD REQUEST` - Nome de utilizador já em uso.

**Exemplo de Resposta em caso de erro:**
```json
{
  "error": "Login/Username already in use"
}

# Documentação da API – Piauí Eventos

## Visão Geral

Esta é a documentação técnica oficial para a API do Piauí Eventos. A API é do tipo RESTful e foi projetada para gerenciar eventos, usuários e suas respectivas localizações de forma eficiente e segura.

## Autenticação

A API utiliza **JWT** com autenticação híbrida:
- Padrão: **HTTP Cookies** (`accessToken`, `refreshToken`, ambos HttpOnly/Secure/SameSite=Strict)
- Opcional: **Header Authorization: Bearer <token>**

Fluxo:
1. **Login**: define cookies e também retorna o `accessToken`:
   - Header: `Authorization: Bearer <token>` (exposto via `Access-Control-Expose-Headers`)
   - Corpo: `{ "accessToken": "..." }`
2. **Requisições**: envie o header Authorization (opcional). Se ausente, os cookies autenticam automaticamente.
3. **Refresh**: idem ao login (atualiza cookies, header e corpo com novo `accessToken`).

Observação: o filtro de segurança aceita primeiro o header Authorization; se não existir, usa o cookie `accessToken`.

---

## Endpoints

A seguir estão detalhados todos os endpoints disponíveis na API, agrupados por funcionalidade.

### 1. Autenticação (`/api/auth`)

#### **POST `/api/auth/login`**
Realiza o login e configura automaticamente os cookies. Também retorna o `accessToken` no header e no corpo.
- **Request Body:** `LoginRequestDTO`
- **Success (200 OK):**
  - Headers: `Set-Cookie` (tokens) e `Authorization: Bearer <accessToken>`
  - Body: `{ "message": "...", "accessToken": "..." }`

#### **POST `/api/auth/refresh`**
Renova os tokens usando o cookie `refreshToken`. Também retorna o novo `accessToken` no header e no corpo.
- **Success (200 OK):**
  - Headers: `Set-Cookie` atualizados e `Authorization: Bearer <accessToken>`
  - Body: `{ "message": "...", "accessToken": "..." }`

#### **POST `/api/auth/logout`**
Invalida os cookies de autenticação.
- **Success (200 OK):** `{ "message": "Logout realizado com sucesso" }`

---

### 2. Gerenciamento de Eventos (`/api/events`)

#### **GET `/api/events`**
Lista todos os eventos cadastrados no sistema.
- **Success (200 OK):** Lista de `EventResponseDTO`.

#### **POST `/api/events`**
Cria um novo evento.
- **Request Body:** `EventRequestDTO`
- **Success (200 OK):** `EventResponseDTO` criado.

#### **GET `/api/events/{id}`**
Retorna um evento por ID.
- **Path:** `id` (integer)
- **Success (200 OK):** `EventResponseDTO`.

#### **PUT `/api/events/{id}`**
Atualiza um evento.
- **Path:** `id` (integer)
- **Request Body:** `EventRequestDTO`
- **Success (200 OK):** `EventResponseDTO` atualizado.

#### **DELETE `/api/events/{id}`**
Remove um evento.
- **Path:** `id` (integer)
- **Success (200 OK):** Confirmação.

---

### 3. Gerenciamento de Usuários (`/api/user`)

#### **POST `/api/user`**
Cria um novo usuário no sistema.
- **Request Body:** `UserRequestDTO`
- **Success (200 OK):** `UserResponseDTO`.

---

### 4. Gerenciamento de Localizações (`/api/location`)

#### **POST `/api/location`**
Cria uma nova localização para eventos.
- **Request Body:** `EventLocationDTO`
- **Success (200 OK):** `EventLocationDTO` criado.

#### **GET `/api/location/{cep}`**
Busca endereço por CEP.
- **Path:** `cep` (string)
- **Success (200 OK):** JSON com dados do endereço.

---

## Modelos de Dados (DTOs)

### `EventRequestDTO`

| Campo         | Tipo             | Descrição                                         |
| :------------ | :--------------- | :------------------------------------------------ |
| `name`        | string           | Nome do evento.                                   |
| `description` | string           | Descrição detalhada do evento.                    |
| `imageUrl`    | string           | URL da imagem de capa do evento.                  |
| `eventDate`   | string (datetime)| Data/hora no formato ISO 8601.                    |
| `eventType`   | string           | Categoria do evento.                              |
| `maxSubs`     | integer          | Número máximo de inscritos.                       |
| `location`    | EventLocationDTO | Dados da localização.                             |

### `EventResponseDTO`

| Campo        | Tipo              | Descrição                                  |
| :----------- | :---------------- | :----------------------------------------- |
| `id`         | integer           | ID do evento.                              |
| `name`       | string            | Nome do evento.                            |
| `description`| string            | Descrição detalhada.                       |
| `imageUrl`   | string            | URL da imagem.                             |
| `eventDate`  | string (datetime) | Data/hora do evento.                       |
| `eventType`  | string            | Categoria.                                 |
| `maxSubs`    | integer           | Máximo de inscritos.                       |
| `locationId` | integer           | ID da localização associada.               |
| `version`    | integer           | Versão do registro.                        |

### `UserRequestDTO`

| Campo         | Tipo    | Descrição                                      |
| :------------ | :------ | :--------------------------------------------- |
| `id`          | integer | ID do usuário (criação: null).                 |
| `name`        | string  | Nome completo.                                 |
| `email`       | string  | E-mail (usado para login).                     |
| `password`    | string  | Senha de acesso.                               |
| `phoneNumber` | string  | Telefone do usuário (somente dígitos, com/sem 55). |
| `roleId`      | integer | ID do perfil/regra de acesso.                  |

### `LoginRequestDTO`

| Campo      | Tipo   | Descrição                                                       |
| :--------- | :----- | :-------------------------------------------------------------- |
| `username` | string | E-mail ou telefone brasileiro (com ou sem +55; com/sem máscara). |
| `password` | string | Senha do usuário.                                               |

---

## Acesso à Documentação Interativa

A API disponibiliza documentação interativa via Swagger UI.

### URLs:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### Como usar (cookies e/ou Bearer):
1. Inicie a aplicação e acesse o Swagger UI
2. Faça login no endpoint `/api/auth/login`
3. Após o login, você pode:
   - Usar os cookies (funciona automaticamente); ou
   - Clicar em **Authorize** e colar o `accessToken` (sem o prefixo "Bearer")
4. Para renovar, use `/api/auth/refresh` (cookies) e repita a autorização se necessário
5. Para sair, use `/api/auth/logout`

Dica: o `accessToken` também é retornado no header `Authorization` e no corpo da resposta.

---

## Executando a Aplicação

### Pré-requisitos:
- Java 17+
- Maven 3.6+
- PostgreSQL (ou acesso ao banco configurado)

### Passos:
1. Configure as variáveis de ambiente (ex.: `DB_PASSWORD`)
2. Execute: `./mvnw spring-boot:run`
3. Acesse: `http://localhost:8081`

---

## Tecnologias Utilizadas

- Spring Boot 3.5.5
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT
- Springdoc OpenAPI
- MapStruct
- Hibernate

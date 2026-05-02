# FoodHub API

## 1. Visão Geral do Projeto

### O que é a API
FoodHub é uma API REST desenvolvida em **Java 21** com **Spring Boot 4.x** para gerenciamento de usuários de um sistema de delivery de alimentos. A aplicação permite o cadastro, consulta, atualização e exclusão de usuários com diferentes perfis (CLIENT, OWNER, ADMIN), além de fornecer autenticação segura via JWT.

### Objetivo da Aplicação
O FoodHub foi projetado para ser o núcleo de gerenciamento de usuários de uma plataforma de delivery, permitindo:
- Cadastro de clientes, proprietários de restaurantes e administradores
- Autenticação segura com tokens JWT
- Gerenciamento completo de dados pessoais e endereços
- Busca avançada de usuários com filtros dinâmicos
- Controle de acesso baseado em perfis de usuário

### Qual Problema Ela Resolve
Em sistemas de delivery, o gerenciamento de múltiplos tipos de usuários (clientes, restaurantes, admins) com dados sensíveis requer:
- **Segurança**: Proteção de senhas com BCrypt e autenticação stateless via JWT
- **Consistência**: Validação rigorosa de dados e prevenção de e-mails duplicados
- **Escalabilidade**: Arquitetura em camadas que permite crescimento modular
- **Manutenibilidade**: Código organizado com separação de responsabilidades

### Contexto do Projeto
Este projeto foi desenvolvido no contexto acadêmico da **FIAP** (Faculdade de Informática e Administração Paulista), demonstrando boas práticas de desenvolvimento backend com Spring Boot, incluindo:
- Arquitetura em camadas (Controller-Service-Repository)
- Persistência com JDBC Client (abordagem moderna sem JPA/Hibernate)
- Segurança com Spring Security e OAuth2 Resource Server
- Documentação automática com OpenAPI/Swagger
- Containerização com Docker

### Arquitetura Utilizada
A aplicação segue uma **arquitetura em camadas tradicional** com princípios SOLID:

```
┌─────────────────┐
│   Controller    │ ← Camada de apresentação (REST API)
├─────────────────┤
│     Service     │ ← Camada de negócio (regras e validações)
├─────────────────┤
│   Repository    │ ← Camada de acesso a dados (JDBC Client)
├─────────────────┤
│    Database     │ ← PostgreSQL (persistência)
└─────────────────┘
```

**Fluxo de requisição:**
```
Request → Controller → Service → Repository → Database
                              ↓
Response ← JSON ← DTOs ←  Mapper
```

---

## 2. Tecnologias Utilizadas

### Java 21
- **Motivo**: Versão LTS com recursos modernos (records, pattern matching, virtual threads)
- **Uso**: Linguagem principal da aplicação
- **Como foi usado**: Toda a base de código, DTOs como records, streams para processamento

### Spring Boot 4.0.5
- **Motivo**: Framework líder para aplicações Java enterprise com configuração automática
- **Uso**: Estrutura base da aplicação, injeção de dependências, auto-configuração
- **Como foi usado**: `@SpringBootApplication`, starters especializados

### Spring Web MVC
- **Motivo**: Implementação de APIs RESTful com controle total sobre requisições/respostas
- **Uso**: Exposição de endpoints HTTP
- **Como foi usado**: `@RestController`, `@RequestMapping`, `ResponseEntity` para controle de status HTTP

### Spring Security 6.x
- **Motivo**: Segurança robusta e integração nativa com OAuth2/JWT
- **Uso**: Proteção de endpoints, autenticação stateless
- **Como foi usado**: `SecurityFilterChain` com regras de autorização, `@EnableWebSecurity`

### Spring Boot JDBC Client
- **Motivo**: Alternativa moderna e performática ao JPA para consultas SQL diretas
- **Uso**: Acesso a dados sem overhead de ORM
- **Como foi usado**: `JdbcClient` para execução de queries parametrizadas e mapeamento manual

### JWT (JSON Web Token) - JJWT 0.12.7
- **Motivo**: Tokens assinados para autenticação stateless entre cliente e servidor
- **Uso**: Geração e validação de tokens de acesso
- **Como foi usado**: `JwtService` gera tokens com claims (email, role); `SecurityConfig` valida via `NimbusJwtDecoder`

### BCrypt (Spring Security Crypto)
- **Motivo**: Algoritmo de hash adaptativo seguro para senhas
- **Uso**: Criptografia de senhas antes da persistência
- **Como foi usado**: `BCryptPasswordEncoder` bean em `PasswordConfig`, usado em `UserService` e `AuthService`

### PostgreSQL 15
- **Motivo**: Banco relacional robusto, open-source e amplamente utilizado
- **Uso**: Persistência de dados de usuários e endereços
- **Como foi usado**: Driver JDBC, schema definido em `data.sql`, containers Docker

### Bean Validation (Jakarta Validation)
- **Motivo**: Validação declarativa de dados de entrada
- **Uso**: Validação automática de DTOs nos controllers
- **Como foi usado**: Anotações `@NotBlank`, `@Email`, `@Size`, `@Pattern`, `@Valid`

### OpenAPI 3 / Swagger (SpringDoc 3.0.2)
- **Motivo**: Documentação interativa e testável da API
- **Uso**: Documentação automática de endpoints, schemas e segurança
- **Como foi usado**: `@Operation`, `@ApiResponse`, `@Schema`, `@SecurityRequirement`

### Lombok
- **Motivo**: Redução de boilerplate (getters, setters, constructors)
- **Uso**: Entidades e DTOs mais limpos
- **Como foi usado**: Anotações `@Getter`, `@Setter`, `@AllArgsConstructor`, `@ToString`

### Docker & Docker Compose
- **Motivo**: Containerização para ambientes consistentes
- **Uso**: Orquestração de aplicação e banco de dados
- **Como foi usado**: `Dockerfile` multi-stage build, `docker-compose.yml` com PostgreSQL e app

### Maven 3.9
- **Motivo**: Gerenciamento de dependências e build automatizado
- **Uso**: Compilação, testes e empacotamento
- **Como foi usado**: `pom.xml` com Spring Boot Parent, plugins de compilação

### JUnit 5
- **Motivo**: Framework padrão para testes unitários em Java
- **Uso**: Validação de comportamento dos controllers
- **Como foi usado**: `@Test`, `@BeforeEach`, stubs manuais para isolamento

---

## 3. Arquitetura do Projeto

### Estrutura de Pastas

```
src/main/java/br/com/fiap/foodhub/
├── config/               # Configurações do Spring
│   ├── OpenApiConfig.java      # Configuração do Swagger
│   ├── PasswordConfig.java     # Bean de criptografia
│   └── SecurityConfig.java     # Configuração de segurança JWT
├── controller/           # Camada de apresentação REST
│   ├── AuthController.java     # Endpoint de autenticação
│   └── UserController.java     # CRUD de usuários
├── domain/               # Entidades de domínio (anêmicas)
│   ├── Address.java
│   └── User.java
├── dtos/                 # Objetos de transferência de dados
│   ├── error/            # DTOs de erro
│   ├── request/          # DTOs de entrada
│   └── response/         # DTOs de saída
├── enums/                # Enumerações
│   └── UserType.java
├── exceptions/           # Exceções customizadas
│   ├── EmailAlreadyExistsException.java
│   └── ResourceNotFoundException.java
├── repository/           # Camada de acesso a dados
│   ├── AddressRepository.java      # Interface
│   ├── AddressRepositoryImp.java   # Implementação JDBC
│   ├── UserRepository.java         # Interface
│   └── UserRepositoryImp.java      # Implementação JDBC
├── security/             # Serviços de segurança
│   └── JwtService.java
├── service/              # Camada de negócio
│   ├── AuthService.java
│   └── UserService.java
└── util/                 # Utilitários
    └── PasswordGenerator.java
```

### Responsabilidade de Cada Camada

| Camada | Responsabilidade | Classes Principais |
|--------|------------------|-------------------|
| **Controller** | Receber requisições HTTP, validar entrada, chamar services, retornar respostas | `AuthController`, `UserController` |
| **Service** | Implementar regras de negócio, orquestrar operações, garantir transações | `AuthService`, `UserService` |
| **Repository** | Executar queries SQL, mapear resultados, abstrair o banco | `UserRepositoryImp`, `AddressRepositoryImp` |
| **Domain** | Representar entidades do negócio (dados apenas) | `User`, `Address` |
| **DTOs** | Transferir dados entre camadas, definir contratos de API | `UserRequest`, `UserResponse`, etc. |
| **Config** | Configurar beans e aspectos transversais | `SecurityConfig`, `OpenApiConfig` |
| **Security** | Gerenciar tokens JWT | `JwtService` |

### Fluxo de Requisição

```
┌─────────────┐
│   Cliente   │
└──────┬──────┘
       │ HTTP Request
       ▼
┌─────────────────────────┐
│  @RestController          │
│  - Validação @Valid       │
│  - Mapeamento de endpoints │
└──────┬────────────────────┘
       │ Chama
       ▼
┌─────────────────────────┐
│  @Service                 │
│  - Regras de negócio      │
│  - @Transactional         │
│  - Validações complexas   │
└──────┬────────────────────┘
       │ Chama
       ▼
┌─────────────────────────┐
│  @Repository              │
│  - JdbcClient             │
│  - Queries SQL            │
│  - RowMappers             │
└──────┬────────────────────┘
       │ Executa
       ▼
┌─────────────────────────┐
│  PostgreSQL               │
│  - Tabelas: users,        │
│    addresses              │
└─────────────────────────┘
```

---

## 4. Modelagem de Domínio

### User (Usuário)

Representa um usuário do sistema com perfil definido.

```java
public class User {
    private Long id;                    // Identificador único
    private String fullname;            // Nome completo
    private String email;               // E-mail único
    private String passwordHash;        // Hash BCrypt da senha
    private UserType userType;          // Perfil: CLIENT, OWNER, ADMIN
    private LocalDateTime createdAt;    // Data de criação
    private LocalDateTime updatedAt;    // Data de última atualização
    private Address address;            // Endereço vinculado
}
```

### Address (Endereço)

Endereço vinculado a um usuário (relação 1:1).

```java
public class Address {
    private Long id;
    private String street;      // Rua
    private int number;         // Número
    private String city;        // Cidade
    private String zipCode;     // CEP (formato: 00000-000)
    private Long userId;        // FK para usuário
}
```

### Relacionamento entre Entidades

```
┌──────────────┐         1:1         ┌──────────────┐
│    User      │─────────────────────│   Address    │
├──────────────┤                     ├──────────────┤
│ id (PK)      │                     │ id (PK)      │
│ fullname     │                     │ street       │
│ email (UQ)   │                     │ number       │
│ passwordHash │                     │ city         │
│ userType     │                     │ zipCode      │
│ createdAt    │                     │ user_id (FK) │
│ updatedAt    │                     └──────────────┘
└──────────────┘
```

- **Tipo**: Um-para-Um (1:1)
- **Implementação**: FK `user_id` na tabela `addresses` com `UNIQUE` constraint
- **Cascade**: `ON DELETE CASCADE` - ao excluir usuário, endereço é excluído automaticamente

### UserType Enum

```java
public enum UserType {
    CLIENT,    // Cliente do delivery
    OWNER,     // Proprietário de restaurante
    ADMIN      // Administrador do sistema
}
```

### Regras de Negócio Implementadas

| Regra | Implementação | Local |
|-------|--------------|-------|
| E-mail único | Validação case-insensitive no banco | `UserRepository.existsByEmail()` |
| Senha criptografada | BCrypt com salt automático | `UserService.save()`, `BCryptPasswordEncoder` |
| Validação de senha atual | Comparação de hash no update | `UserService.updatePassword()` |
| Endereço obrigatório | `@NotNull` + `@Valid` no `UserRequest` | `UserRequest.address` |
| Soft delete não implementado | Delete físico com cascade | `UserService.deleteById()` |
| Timestamps automáticos | `LocalDateTime.now()` nos saves/updates | Repository implementations |

### Decisões de Modelagem

1. **JDBC Client em vez de JPA**: Maior controle sobre queries SQL, melhor performance para operações simples, sem overhead de cache/flush do Hibernate

2. **DTOs como Records**: Imutabilidade, menos código boilerplate, integração natural com JSON

3. **Separação User/Address**: Normalização do banco, possibilidade futura de múltiplos endereços

4. **UserType como Enum**: Tipagem forte, valores pré-definidos, validação automática

---

## 5. Segurança

### Spring Security Configuration

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf.disable())                    // Stateless não precisa de CSRF
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/login").permitAll()  // Público
            .requestMatchers("/users/**").authenticated()  // Protegido
            .anyRequest().permitAll()
        )
        .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()))
        .sessionManagement(session -> 
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .build();
}
```

### Autenticação JWT

**Geração do Token** (`JwtService.generateToken()`):
```java
public String generateToken(UserCredentials user) {
    return Jwts.builder()
        .subject(user.email())                    // Identificador
        .claim("role", user.userType().name())    // Perfil do usuário
        .issuedAt(new Date())                     // Emissão
        .expiration(new Date(System.currentTimeMillis() + expiration))  // 1 hora
        .signWith(getSignKey())                   // Assinatura HMAC-SHA256
        .compact();
}
```

**Validação do Token** (`SecurityConfig.jwtDecoder()`):
```java
@Bean
public JwtDecoder jwtDecoder(SecretKey secretKey) {
    return NimbusJwtDecoder
        .withSecretKey(secretKey)
        .macAlgorithm(MacAlgorithm.HS256)
        .build();
}
```

### Endpoints Públicos vs Protegidos

| Endpoint | Método | Acesso | Descrição |
|----------|--------|--------|-----------|
| `/auth/login` | POST | Público | Autenticação e geração de token |
| `/users` | GET | Protegido (JWT) | Listar usuários paginados |
| `/users/{id}` | GET | Protegido (JWT) | Buscar usuário por ID |
| `/users/search` | GET | Protegido (JWT) | Pesquisa com filtros |
| `/users` | POST | Protegido (JWT) | Criar usuário |
| `/users/{id}` | PUT | Protegido (JWT) | Atualizar usuário |
| `/users/{id}/password` | PATCH | Protegido (JWT) | Alterar senha |
| `/users/{id}` | DELETE | Protegido (JWT) | Excluir usuário |

### Uso do Token (Authorization Bearer)

**Header nas requisições:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**Fluxo de Login:**
```
┌─────────┐                                    ┌─────────────┐
│ Cliente │── POST /auth/login ───────────────▶│ AuthController│
└─────────┘   {email, password}                └──────┬──────┘
                                                      │
                                                      ▼
                                              ┌───────────────┐
                                              │  AuthService   │
                                              │  - Valida credenciais
                                              │  - Gera JWT    │
                                              └──────┬────────┘
                                                     │
┌─────────┐◀── 200 OK {token: "eyJhbG..."} ──────────┘
│ Cliente │
└─────────┘
```

### BCryptPasswordEncoder

**Configuração:**
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();  // Custo padrão: 10 rounds
}
```

**Uso:**
- **Criação**: `passwordEncoder.encode(plainPassword)` → hash armazenado
- **Validação**: `passwordEncoder.matches(plainPassword, hash)` → boolean

---

## 6. Endpoints

### POST /auth/login
Autentica usuário e retorna token JWT.

**Request:**
```json
{
  "email": "admin@admin.com",
  "password": "admin@123"
}
```

**Response 200:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBhZG1pbi5jb20iLCJyb2xlIjoiQURNSU4iLCJpYXQiOjE3MDQwMDAwMDAsImV4cCI6MTcwNDAwMzYwMH0..."
}
```

**Erros:**
- `401 Unauthorized`: Credenciais inválidas

---

### GET /users
Lista usuários com paginação.

**Query Params:**
| Param | Tipo | Padrão | Descrição |
|-------|------|--------|-----------|
| page | int | 0 | Página (0-based) |
| size | int | 10 | Itens por página |

**Response 200:**
```json
[
  {
    "id": 1,
    "fullname": "John Doe",
    "email": "john@email.com",
    "userType": "CLIENT",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00",
    "address": {
      "street": "Rua das Flores",
      "number": 123,
      "city": "São Paulo",
      "zipCode": "01234-567"
    }
  }
]
```

**Headers obrigatórios:**
```
Authorization: Bearer <token>
```

---

### GET /users/{id}
Busca usuário por ID.

**Path Params:**
- `id` (Long): ID do usuário

**Response 200:** Objeto `UserResponse`

**Erros:**
- `401`: Não autenticado
- `404`: Usuário não encontrado

---

### GET /users/search
Pesquisa usuários com filtros dinâmicos.

**Query Params:**
| Param | Tipo | Obrigatório | Descrição |
|-------|------|-------------|-----------|
| fullname | String | Não | Busca parcial (LIKE %%) |
| email | String | Não | Busca exata |
| userType | Enum | Não | CLIENT, OWNER, ADMIN |

**Restrição**: Pelo menos um filtro deve ser informado.

**Response 200:** Lista de `UserResponse`

**Erros:**
- `400`: Filtros de busca vazios

---

### POST /users
Cadastra novo usuário.

**Request:**
```json
{
  "fullname": "Geová José",
  "email": "geova@email.com",
  "password": "SenhaSegura123",
  "userType": "CLIENT",
  "address": {
    "street": "Rua das Flores",
    "number": 123,
    "city": "São Paulo",
    "zipCode": "01234-567"
  }
}
```

**Response 201**: Created (sem body)

**Erros:**
- `400`: Dados inválidos (validação)
- `401`: Não autenticado
- `409`: E-mail já cadastrado

---

### PUT /users/{id}
Atualiza dados do usuário.

**Path Params:**
- `id` (Long): ID do usuário

**Request:**
```json
{
  "fullname": "Nome Atualizado",
  "email": "novo@email.com",
  "address": {
    "street": "Rua Nova",
    "number": 456,
    "city": "Rio de Janeiro",
    "zipCode": "98765-432"
  }
}
```

**Response 204**: No Content

**Erros:**
- `400`: Dados inválidos
- `401`: Não autenticado
- `404`: Usuário não encontrado
- `409`: E-mail já cadastrado (por outro usuário)

---

### PATCH /users/{id}/password
Altera senha do usuário (requer senha atual).

**Path Params:**
- `id` (Long): ID do usuário

**Request:**
```json
{
  "currentPassword": "SenhaAtual123",
  "newPassword": "NovaSenha456"
}
```

**Response 204**: No Content

**Erros:**
- `400`: Nova senha com menos de 8 caracteres
- `401`: Não autenticado
- `404`: Usuário não encontrado
- Validação: Senha atual incorreta

---

### DELETE /users/{id}
Remove usuário e seu endereço.

**Path Params:**
- `id` (Long): ID do usuário

**Response 204**: No Content

**Erros:**
- `401`: Não autenticado
- `404`: Usuário não encontrado

---

## 7. Validações

### Validações de Entrada (@Valid)

As validações são aplicadas automaticamente pelo Spring usando Jakarta Bean Validation:

| Anotação | Campo | Descrição |
|----------|-------|-----------|
| `@NotBlank` | fullname, email, password | Campo não pode ser null, vazio ou whitespace |
| `@Email` | email | Formato válido de e-mail (RFC 2822) |
| `@Size(min=8)` | password, newPassword | Mínimo 8 caracteres |
| `@Pattern(regexp="^\\d{5}-?\\d{3}$")` | zipCode | CEP no formato 00000-000 ou 00000000 |
| `@NotNull` | number, userType, address | Campo obrigatório |
| `@Positive` | number | Valor maior que zero |
| `@Valid` | address | Validações aninhadas do AddressRequest |

### Validação de E-mail Duplicado

Implementação customizada no `UserService`:

```java
@Transactional
public void save(UserRequest userRequest) {
    if (userRepository.existsByEmail(userRequest.email())) {
        throw new EmailAlreadyExistsException(userRequest.email());
    }
    // ... prossegue com salvamento
}
```

A query no repositório usa comparação case-insensitive:
```sql
SELECT COUNT(1) FROM users WHERE LOWER(email) = LOWER(:email)
```

### Validações Customizadas

| Validação | Onde | Implementação |
|-----------|------|---------------|
| Filtros de busca não vazios | `UserService.search()` | RuntimeException se todos null/blank |
| Senha atual correta | `UserService.updatePassword()` | Comparação BCrypt |
| ID positivo | `@Positive @PathVariable` | Spring Validation |

### Tratamento de Exceções

**Nota**: O projeto atualmente não possui `@ControllerAdvice`. As exceções são propagadas e retornadas com stack trace em ambiente de desenvolvimento.

Exceções lançadas:
- `EmailAlreadyExistsException` → Retorna mensagem com e-mail duplicado
- `ResourceNotFoundException` → Retorna "Usuário não encontrado"
- `RuntimeException` → Mensagens variadas ("Invalid credentials", "Senha atual incorreta")

---

## 8. Banco de Dados

### PostgreSQL 15

Configuração em `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/foodhub
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver
```

### Schema

**Tabela `users`:**
```sql
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    fullname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    user_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

**Tabela `addresses`:**
```sql
CREATE TABLE IF NOT EXISTS addresses (
    id SERIAL PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    number INTEGER NOT NULL,
    city VARCHAR(100) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    user_id INTEGER NOT NULL UNIQUE,
    CONSTRAINT fk_user_address
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);
```

### Constraints

| Constraint | Tabela | Descrição |
|------------|--------|-----------|
| PRIMARY KEY | users(id), addresses(id) | Identificador único |
| UNIQUE | users(email) | E-mail não repetido |
| UNIQUE | addresses(user_id) | Um endereço por usuário |
| FOREIGN KEY | addresses(user_id) → users(id) | Integridade referencial |
| NOT NULL | Vários campos | Campos obrigatórios |

### Relacionamento e Cascade

- **Tipo**: Um-para-Um (1:1)
- **FK**: `addresses.user_id` referencia `users.id`
- **Cascade**: `ON DELETE CASCADE` - exclusão do usuário remove automaticamente o endereço

### Inserts Iniciais (data.sql)

Um usuário admin é criado automaticamente na inicialização:

```sql
-- Usuário admin (senha: admin@123, hash BCrypt)
INSERT INTO users (fullname, email, password_hash, user_type, created_at, updated_at)
VALUES (
    'Administrador',
    'admin@admin.com',
    '$2a$10$Lf.G9iCM9hE/bQt8R.fHCu5PI0hzDDRJxOrHLKKAoYyUXAM8JsmPC',
    'ADMIN',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Endereço do admin
INSERT INTO addresses (street, number, city, zip_code, user_id)
VALUES ('Rua 1', 1, 'Cidade 1', '12345678', (SELECT id FROM users WHERE email = 'admin@admin.com'));
```

### Estratégia de Inicialização

```properties
spring.sql.init.mode=always
spring.sql.init.data-locations=classpath:data.sql
```

- `always`: Executa `data.sql` a cada inicialização
- As instruções SQL usam `WHERE NOT EXISTS` para evitar duplicatas
- Ideal para desenvolvimento; em produção, usar Flyway/Liquibase

---

## 9. Docker

### Docker Compose (docker-compose.yml)

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15
    container_name: postgres-db
    restart: unless-stopped
    environment:
      POSTGRES_DB: ${DB_NAME}
      POSTGRES_USER: ${DB_USER}
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - app-network

  app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: spring-app
    restart: unless-stopped
    depends_on:
      - postgres
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: docker
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/${DB_NAME}
      SPRING_DATASOURCE_USERNAME: ${DB_USER}
      SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}
    networks:
      - app-network

volumes:
  postgres_data:

networks:
  app-network:
```

### Containers Utilizados

| Serviço | Imagem | Porta | Descrição |
|---------|--------|-------|-----------|
| postgres | postgres:15 | 5432 | Banco de dados PostgreSQL |
| app | Dockerfile local | 8080 | Aplicação Spring Boot |

### Variáveis de Ambiente (.env)

```
DB_NAME=foodhub
DB_USER=postgres
DB_PASSWORD=postgres
```

### Comandos Docker

**Subir aplicação (background):**
```bash
docker compose up -d
```
- Inicia ambos os containers em modo detached
- Cria rede `app-network` para comunicação
- Volume `postgres_data` persiste dados do banco

**Ver logs:**
```bash
docker compose logs -f          # Todos os serviços
docker compose logs -f app      # Apenas a aplicação
docker compose logs -f postgres # Apenas o banco
```

**Derrubar aplicação:**
```bash
docker compose down
```
- Para e remove containers
- Mantém volumes (dados persistem)

**Derrubar e remover volumes:**
```bash
docker compose down -v
```
- ⚠️ **Cuidado**: Remove todos os dados do banco

**Rebuildar após mudanças:**
```bash
docker compose up --build
```
- Reconstrói a imagem da aplicação
- Útil após modificações no código

**Restart de serviço:**
```bash
docker compose restart app
```

**Status dos containers:**
```bash
docker compose ps
```

### Dockerfile (Multi-Stage Build)

```dockerfile
# Etapa 1: Build
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Vantagens do multi-stage:**
- Imagem final mais leve (~200MB vs ~1GB)
- Sem código fonte, Maven ou dependências de build
- Apenas JRE e o JAR da aplicação

---

## 10. Testes

### Estratégia de Testes

O projeto utiliza **testes unitários** com abordagem de stubs manuais para isolar os controllers:

- **Framework**: JUnit 5 (Jupiter)
- **Abordagem**: Testes isolados sem contexto Spring
- **Stubs**: Implementações manuais dos Services

### Controllers Testados

| Controller | Cenários de Teste |
|------------|-------------------|
| `AuthControllerTest` | Login sucesso, e-mail inválido, senha inválida |
| `UserControllerTest` | CRUD completo, busca com filtros, alteração de senha |

### Cenários de Sucesso

- `shouldLoginSuccessfully`: Autenticação válida retorna token
- `shouldReturnUsersWhenFindAll`: Listagem paginada
- `shouldReturnUserWhenFindById`: Busca por ID existente
- `shouldCreateUserSuccessfully`: Cadastro com dados válidos
- `shouldUpdateUserSuccessfully`: Atualização de dados
- `shouldUpdatePasswordSuccessfully`: Alteração de senha com validação
- `shouldDeleteUserSuccessfully`: Exclusão de usuário
- `shouldReturnUsersWhenSearchWithFilters`: Busca com parâmetros

### Cenários de Erro

| Teste | Exceção Esperada | Mensagem |
|-------|-----------------|----------|
| `shouldThrowExceptionWhenInvalidCredentials` | RuntimeException | "Invalid credentials" |
| `shouldThrowExceptionWhenUserNotFound` | ResourceNotFoundException | "Usuário não encontrado" |
| `shouldThrowExceptionWhenCreateUserWithDuplicateEmail` | EmailAlreadyExistsException | Contém e-mail duplicado |
| `shouldThrowExceptionWhenUpdateNonExistentUser` | ResourceNotFoundException | "Usuário não encontrado" |
| `shouldThrowExceptionWhenDeleteNonExistentUser` | ResourceNotFoundException | "Usuário não encontrado" |
| `shouldThrowExceptionWhenSearchWithEmptyFilters` | RuntimeException | "Filtros de busca vazios" |
| `shouldThrowExceptionWhenUpdatePasswordWithInvalidCurrentPassword` | RuntimeException | "Senha atual incorreta" |

### Como Rodar os Testes

```bash
./mvnw test
```

Ou com Maven instalado:
```bash
mvn test
```

### Exemplo de Teste

```java
@Test
void shouldCreateUserSuccessfully() {
    AddressRequest addressRequest = new AddressRequest("Rua Teste", 123, "São Paulo", "01234-567");
    UserRequest request = new UserRequest("New User", "newuser@email.com", "password123", UserType.CLIENT, addressRequest);

    ResponseEntity<Void> response = userController.save(request);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
}
```

### Cobertura de Regras de Negócio

- ✓ Validação de credenciais de login
- ✓ Verificação de e-mail duplicado
- ✓ Verificação de existência de usuário
- ✓ Validação de filtros de busca
- ✓ Verificação de senha atual na alteração

---

## 11. Como Rodar Localmente

### Pré-requisitos

| Requisito | Versão | Verificação |
|-----------|--------|-------------|
| Java | 21+ | `java -version` |
| Maven | 3.9+ | `mvn -version` |
| Docker | 20+ | `docker --version` |
| Docker Compose | 2+ | `docker compose version` |
| PostgreSQL | 15 (opcional local) | `psql --version` |

### Variáveis de Ambiente

Crie o arquivo `.env` na raiz:
```
DB_NAME=foodhub
DB_USER=postgres
DB_PASSWORD=postgres
```

### Opção 1: Com Docker (Recomendado)

**Passo 1**: Subir infraestrutura
```bash
docker compose up -d
```

**Passo 2**: Verificar se está rodando
```bash
docker compose ps
```

**Passo 3**: Acessar aplicação
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html

### Opção 2: Sem Docker (Desenvolvimento)

**Passo 1**: Instalar PostgreSQL local ou usar existente

**Passo 2**: Criar banco de dados
```sql
CREATE DATABASE foodhub;
```

**Passo 3**: Configurar `application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/foodhub
spring.datasource.username=postgres
spring.datasource.password=postgres
```

**Passo 4**: Build
```bash
./mvnw clean install -DskipTests
```

**Passo 5**: Run
```bash
./mvnw spring-boot:run
```

Ou executar o JAR:
```bash
java -jar target/foodhub-0.0.1-SNAPSHOT.jar
```

### Acessando a API

| Recurso | URL |
|---------|-----|
| API Base | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |
| Health Check | http://localhost:8080/actuator/health (se habilitado) |

### Teste Rápido

**1. Login (obter token):**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@admin.com","password":"admin@123"}'
```

**2. Listar usuários (com token):**
```bash
curl -X GET http://localhost:8080/users \
  -H "Authorization: Bearer <token-aqui>"
```

---

## 12. Licença

Este projeto foi desenvolvido para fins acadêmicos na FIAP.

---

**Autor**: [JuniorGDev](https://github.com/JuniorGDev)  
**Versão**: 0.0.1-SNAPSHOT  
**Última Atualização**: Maio 2026

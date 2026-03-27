# BDGest-api

API REST de gestion de bandes dessinées et utilisateurs (Spring Boot + PostgreSQL).

---

## 🚀 Lancer le projet

### 1. Démarrer PostgreSQL (Docker)

```bash
docker compose up -d
```

### 2. Lancer l'API

Depuis IntelliJ ou :

```bash
./mvnw spring-boot:run
```

---

## 📚 Endpoints BD

### 🔍 Liste des BD

```http
GET /list-bd
```

---

### 🎲 BD aléatoires

```http
GET /random-bd/{nb}
```

Exemple :

```http
GET /random-bd/10
```

---

### 🔎 Recherche

```http
GET /search?...
```

Exemples :

```http
/search?serie=Naruto
/search?auteur=Isayama
/search?titre=Tome 1
```

---

## 👤 Gestion des utilisateurs

### 📝 Inscription

```http
POST /users/register
```

#### Body :

```json
{
  "mail": "test@gmail.com",
  "mdp": "1234"
}
```

#### Contraintes :

* email valide obligatoire
* mot de passe ≥ 4 caractères

#### Résultat :

* ✅ Utilisateur créé
* ❌ Email déjà utilisé
* ❌ Validation échouée

---

### 🔐 Connexion

```http
POST /users/login
```

#### Body :

```json
{
  "mail": "test@gmail.com",
  "mdp": "1234"
}
```

#### Résultat :

* ✅ Login réussi
* ❌ Identifiants invalides

---

## 🔒 Sécurité

* Mots de passe hashés avec **BCrypt**
* Validation des entrées (email, mot de passe)
* Spring Security configuré (mode ouvert pour dev)

---

## 🛠️ Stack technique

* Java / Spring Boot
* Spring Data JPA
* PostgreSQL
* Docker

---

## 📌 Notes

* La base de données doit être initialisée avec le dump fourni
* `ddl-auto` est désactivé pour éviter toute modification automatique du schéma
* API en mode développement (sécurité permissive)

---

## 🧪 Test rapide (PowerShell)

```powershell
$body = @{
  mail = "test@gmail.com"
  mdp  = "1234"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/users/register" `
  -Method POST `
  -ContentType "application/json" `
  -Body $body
```

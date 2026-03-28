# BDGest-api

**API REST de gestion de bandes dessinées et utilisateurs**  
*(Spring Boot + PostgreSQL)*

---

## 🚀 Lancer le projet

### 1. Démarrer PostgreSQL (Docker)

```bash
docker compose up -d
```

### 2. Lancer l'API

Depuis IntelliJ ou via la ligne de commande :

```bash
./mvnw spring-boot:run
```

---

## 📚 Endpoints BD


| Méthode | Chemin            | Description                                |
| ------- | ----------------- | ------------------------------------------ |
| GET     | `/list-bd`        | Liste toutes les BD                        |
| GET     | `/random-bd/{nb}` | Retourne `{nb}` BD aléatoires              |
| GET     | `/search`         | Recherche de BD par série, auteur ou titre |
| GET     | `/list-series`    | Liste toutes les séries                    |
| GET     | `/list-auteurs`   | Liste tous les auteurs                     |


### Paramètres de recherche (`/search`)

- `serie` : Nom de la série
- `auteur` : Nom de l'auteur
- `titre` : Titre de la BD

### Exemples de requêtes

```http
GET /random-bd/10
GET /search?serie=Naruto
GET /search?auteur=Isayama
GET /search?titre=Tome 1
GET /list-series
GET /list-auteurs
```

### Exemple de réponse (`/list-series`)

```json
[
  { "id": 1, "nom": "Naruto" },
  { "id": 2, "nom": "Attack on Titan" }
]
```

### Exemple de réponse (`/list-auteurs`)

```json
[
  { "id": 1, "nom": "Masashi Kishimoto" },
  { "id": 2, "nom": "Hajime Isayama" }
]
```

---

## 👤 Gestion des utilisateurs

### 📝 Inscription

**Endpoint** : `POST /users/register`  
**Body** :

```json
{
  "mail": "test@gmail.com",
  "mdp": "1234"
}
```

**Contraintes** :

- Email valide obligatoire
- Mot de passe ≥ 4 caractères

**Résultats possibles** :

- ✅ Utilisateur créé
- ❌ Email déjà utilisé
- ❌ Validation échouée

---

### 🔐 Connexion

**Endpoint** : `POST /users/login`  
**Body** :

```json
{
  "mail": "test@gmail.com",
  "mdp": "1234"
}
```

**Résultats possibles** :

- ✅ Login réussi → renvoie `userId` et `token JWT`
- ❌ Identifiants invalides

---

### 👤 Info utilisateur courant

**Endpoint** : `GET /users/me`  
**Headers** :

```http
Authorization: Bearer <JWT_TOKEN>
```

**Résultats possibles** :

- ✅ Renvoie les infos de l'utilisateur courant
- ❌ Token invalide ou absent → erreur

---

### Exemple PowerShell pour tester `/me` après login

```powershell
# Login pour récupérer le token
$loginBody = @{
  mail = "test@gmail.com"
  mdp  = "1234"
} | ConvertTo-Json

$loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/users/login" `
                                  -Method POST `
                                  -ContentType "application/json" `
                                  -Body $loginBody

if ($loginResponse.success -eq $true) {
    $token = $loginResponse.token

    # Appel de /me avec le token JWT
    $meResponse = Invoke-RestMethod -Uri "http://localhost:8080/users/me" `
                                    -Method GET `
                                    -Headers @{ Authorization = "Bearer $token" }

    $meResponse | ConvertTo-Json -Depth 3
} else {
    Write-Host "Login échoué :" $loginResponse.message
}
```

---

## 🔒 Sécurité

- Mots de passe hashés avec **BCrypt**
- **JWT** pour authentification et accès aux endpoints sécurisés
- Endpoint `/me` pour récupérer les infos de l'utilisateur courant
- Validation des entrées (email, mot de passe)
- Spring Security configuré (mode ouvert pour dev)

---

## 🛠️ Stack technique

- Java / Spring Boot
- Spring Data JPA
- PostgreSQL
- Docker
- Spring Security + JWT

---

## 📌 Notes

- La base de données doit être initialisée avec le dump fourni
- `ddl-auto` est désactivé pour éviter toute modification automatique du schéma
- API en mode développement (sécurité permissive)

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
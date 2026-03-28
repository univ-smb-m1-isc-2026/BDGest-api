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


| Méthode | Chemin            | Description                                      |
| ------- | ----------------- | ------------------------------------------------ |
| GET     | `/random-bd/{nb}` | Retourne `{nb}` BD aléatoires                    |
| GET     | `/search`         | Recherche de BD par série, auteur, titre ou ISBN |
| GET     | `/list-series`    | Liste toutes les séries                          |
| GET     | `/list-auteurs`   | Liste tous les auteurs                           |


### Paramètres de recherche (`/search`)


| Paramètre | Description                               |
| --------- | ----------------------------------------- |
| `serie`   | Nom de la série                           |
| `auteur`  | Nom de l'auteur                           |
| `titre`   | Titre de la BD                            |
| `isbn`    | ISBN exact                                |
| `limit`   | Nombre maximum de résultats (défaut : 10) |


### Exemples de requêtes

```http
GET /random-bd/10
GET /search?serie=Naruto
GET /search?serie=Naruto&limit=5
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

## 🔒 Sécurité

- Mots de passe hashés avec **BCrypt**
- **JWT** pour authentification et accès aux endpoints sécurisés
- Endpoint `/users/me` pour récupérer les infos de l'utilisateur courant
- Validation des entrées (email, mot de passe)
- Spring Security configuré (mode ouvert pour dev)

---

## 🛠️ Stack technique

- Java / Spring Boot
- Spring Data JPA
- PostgreSQL
- Docker
- Spring Security + JWT
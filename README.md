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
| `limit`   | Nombre maximum de résultats (défaut : 12) |
| `offset`  | Décale les  résultats (défaut : 0)        |


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

### ❌ Suppression du compte

**Endpoint** : `DELETE /users/me`  
**Headers** :

```http
Authorization: Bearer <JWT_TOKEN>
```

**Description** :  
Supprime le compte de l'utilisateur courant. Tous les liens avec la collection, les séries suivies, les auteurs suivis et les prêts associés seront également supprimés.

**Résultats possibles** :

- ✅ Compte supprimé avec succès
- ❌ Token invalide ou absent → erreur

**Exemple de requête (PowerShell)** :

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/users/me" `
  -Method DELETE `
  -Headers @{ Authorization = "Bearer $token" }
```

**Exemple de réponse** :

```json
{
  "success": true,
  "message": "Compte supprimé"
}
```

---

## 📚 Collection et suivi


| Action                         | Méthode | Chemin                      | Description                                    |
| ------------------------------ | ------- | --------------------------- | ---------------------------------------------- |
| Ajouter une BD à la collection | POST    | `/users/collection/{bdId}`  | Ajoute une BD à la collection de l'utilisateur |
| Lister la collection           | GET     | `/users/collection`         | Retourne toutes les BD de la collection        |
| Supprimer une BD               | DELETE  | `/users/collection/{bdId}`  | Supprime une BD de la collection               |
| Ajouter un auteur suivi        | POST    | `/users/auteurs/{auteurId}` | Suivre un auteur                               |
| Lister les auteurs suivis      | GET     | `/users/auteurs`            | Liste tous les auteurs suivis                  |
| Supprimer un auteur suivi      | DELETE  | `/users/auteurs/{auteurId}` | Supprime un auteur des suivis                  |
| Ajouter une série suivie       | POST    | `/users/series/{serieId}`   | Suivre une série                               |
| Lister les séries suivies      | GET     | `/users/series`             | Liste toutes les séries suivies                |
| Supprimer une série suivie     | DELETE  | `/users/series/{serieId}`   | Supprime une série des suivies                 |


### Exemples de requêtes (PowerShell)

```powershell
# Ajouter BD à la collection
Invoke-RestMethod -Uri "http://localhost:8080/users/collection/10" -Method POST -Headers @{ Authorization = "Bearer $token" }

# Lister la collection
Invoke-RestMethod -Uri "http://localhost:8080/users/collection" -Method GET -Headers @{ Authorization = "Bearer $token" }

# Supprimer BD de la collection
Invoke-RestMethod -Uri "http://localhost:8080/users/collection/10" -Method DELETE -Headers @{ Authorization = "Bearer $token" }

# Ajouter auteur suivi
Invoke-RestMethod -Uri "http://localhost:8080/users/auteurs/3" -Method POST -Headers @{ Authorization = "Bearer $token" }

# Lister auteurs suivis
Invoke-RestMethod -Uri "http://localhost:8080/users/auteurs" -Method GET -Headers @{ Authorization = "Bearer $token" }

# Supprimer auteur suivi
Invoke-RestMethod -Uri "http://localhost:8080/users/auteurs/3" -Method DELETE -Headers @{ Authorization = "Bearer $token" }

# Ajouter série suivie
Invoke-RestMethod -Uri "http://localhost:8080/users/series/5" -Method POST -Headers @{ Authorization = "Bearer $token" }

# Lister séries suivies
Invoke-RestMethod -Uri "http://localhost:8080/users/series" -Method GET -Headers @{ Authorization = "Bearer $token" }

# Supprimer série suivie
Invoke-RestMethod -Uri "http://localhost:8080/users/series/5" -Method DELETE -Headers @{ Authorization = "Bearer $token" }
```

---

---

## 🤝 Gestion des prêts

Permet de gérer les bandes dessinées prêtées à d'autres personnes.

---

### 📚 Lister les prêts

**Endpoint** : `GET /users/prets`

**Headers** :

```http
Authorization: Bearer <JWT_TOKEN>
```

**Résultat** :

- ✅ Liste des prêts de l'utilisateur
- ❌ Erreur si token invalide

---

### ➕ Ajouter un prêt

**Endpoint** : `POST /users/prets/{bdId}`

**Paramètre** :

- `emprunteur` : nom de la personne à qui la BD est prêtée

**Exemple** :

```http
POST /users/prets/10?emprunteur=Paul
```

**Résultat** :

- ✅ BD prêtée
- ❌ BD introuvable

---

### 🔄 Marquer une BD comme rendue

**Endpoint** : `PUT /users/prets/{pretId}/retour`

**Résultat** :

- ✅ BD rendue (date de retour enregistrée)
- ❌ Prêt introuvable ou non autorisé

---

### ❌ Supprimer un prêt

**Endpoint** : `DELETE /users/prets/{pretId}`

**Résultat** :

- ✅ Prêt supprimé
- ❌ Prêt introuvable ou non autorisé

---

### 🧪 Exemples PowerShell

```powershell
# Ajouter un prêt
Invoke-RestMethod -Uri "http://localhost:8080/users/prets/10?emprunteur=Paul" `
  -Method POST `
  -Headers @{ Authorization = "Bearer $token" }

# Voir les prêts
Invoke-RestMethod -Uri "http://localhost:8080/users/prets" `
  -Method GET `
  -Headers @{ Authorization = "Bearer $token" }

# Marquer comme rendu
Invoke-RestMethod -Uri "http://localhost:8080/users/prets/1/retour" `
  -Method PUT `
  -Headers @{ Authorization = "Bearer $token" }

# Supprimer un prêt
Invoke-RestMethod -Uri "http://localhost:8080/users/prets/1" `
  -Method DELETE `
  -Headers @{ Authorization = "Bearer $token" }
```

---

### 🧠 Notes

- Une même BD peut être prêtée plusieurs fois (historique conservé)
- Un utilisateur peut avoir plusieurs prêts en cours
- La date de retour est `null` tant que la BD n'est pas rendue

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

# **Projet JEE - 4X Game**
## **Auteurs**
- Robin Simonneau
- Loann Potier
- Simon Landry

---
## **Description**
Ce projet consiste à développer un jeu de stratégie multijoueur au tour par tour respectant les principes des "4X" : **eXploration, eXpansion, eXploitation, et eXtermination**. L'objectif est de fournir une application web robuste et interactive en utilisant les technologies Jakarta EE et en respectant l'architecture MVC.

## **Fonctionnalités principales**
- **Inscription et connexion** : Gestion des utilisateurs avec des pseudos uniques.
- **Multijoueur** : Synchronisation des actions entre plusieurs joueurs.
- **Gameplay** :
  - Gestion des soldats, villes, et ressources.
  - Déplacement, combat, et recrutement dans un environnement interactif.
- **Gestion du score** : Calcul et affichage des scores basés sur les actions des joueurs.
- **Interface intuitive** : Pages JSP dynamiques pour la gestion du jeu.

---

## **Prérequis**
- **Java Development Kit (JDK)** : Version 21
- **Apache Tomcat** : Version 11
- **Base de données** : MySQL
- **Maven** : Outil de gestion de build

---

## **Structure du projet**
```plaintext
Projet4DX
├── src
│   ├── main
│   │   ├── java
│   │   │   └── org.example.projet4dx
│   │   │       ├── controller
│   │   │       │   ├── websocket
│   │   │       │   │   └── GameController
│   │   │       │   ├── HelloServlet
│   │   │       │   ├── LoginController
│   │   │       │   └── ProfilePageController
│   │   │       ├── model
│   │   │       ├── service
│   │   │       └── util
│   │   ├── resources
│   │   └── webapp
│   │       ├── assets
│   │       │   ├── css
│   │       │   ├── images
│   │       │   └── js
│   │       ├── views
│   │       ├── WEB-INF
│   │       └── index.jsp
├── test
├── target
├── Dockerfile
├── pom.xml
├── Projet4DX.iml
└── README.md
```

---

## **Installation**
1. **Cloner le projet :**
   ```bash
   git clone https://github.com/Rob1Sim/Projet4DX.git
   cd Projet4DX
  

2. **Configurer la base de données :**
   - Installer MySQL ou builder l'image docker du repo.
     - ```bash 
       docker build -t mysql-4dx . 
       docker run -d --name mysql-4dx-container -p 3306:3306 mysql-4dx
       ```
   - Le user par défaut est 'user' le mdp 'password' le nom de la base 'projet_4dx'
   - Les scripts SQL nécessaires sont disponibles dans le dossier `src/main/resources/sql`.



3. **Compiler et déployer :**
   - Avec Maven :
     ```bash
     mvn clean install
     ```

4. **Déployer sur Tomcat :**
   - Copier le fichier `.war` généré dans le dossier `webapps` de Tomcat.
   - Démarrer Tomcat et accéder à l'application via [http://localhost:8080/](http://localhost:8080/) (peut varier celon votre configuration tomcat).

---

## **Technologies utilisées**
- **Langage** : Java (JDK 21)
- **Framework** : Jakarta EE (Servlets et JSP)
- **Serveur d'application** : Apache Tomcat 11
- **Base de données** : MySQL
- **Client-side** : HTML, CSS, JavaScript

---
## **Documentation**
La documentation web est peut être générer par la commande :
```bash
mvn javadoc:javadoc
```
Et est accessible dans le dossier target/site/apidocs/.






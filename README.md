
# **Projet JEE - 4X Game**
## **Auteurs**
- Robin Simonneau
- [Autres contributeurs]

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
├── src/main/java
│   ├── org.example.model        # Entités et logique métier
│   ├── org.examplecontroller   # Servlets et contrôleurs
│   ├── org.exampleutil         # Classes utilitaires
├── src/main/webapp
│   ├── WEB-INF
│   │   └── web.xml             # Configuration du déploiement
│   ├── views                   # Fichiers JSP
│   │   ├── login.jsp
│   │   ├── game.jsp
│   │   ├── score.jsp
│   │   └── combat.jsp
│   ├── assets                  # Ressources statiques
│       ├── css                 # Styles CSS
│       ├── js                  # Scripts JavaScript
│       └── images              # Images nécessaires
├── src/main/resources
│   └── sql   
├── src/test/java               # Tests unitaires
├── pom.xml                     # Configuration Maven
└── README.md                   # Documentation
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


## **Technologies utilisées**
- Via le websocket synchroniser les soldats à chaque tour
- Sélectionner et mouvoir tous les soldats dans le front
- Envoyer les changements des soldats via le websocket et appelé le backend en conséquence
- Configurer les actions des soldats (+ limité le déplacement à une case de chaque soldats)
- Configurer la fin de tour (et donc bloquer les joueurs à qui ce n'est pas le tour coté front)
- Corriger les bugs de toutes les parties précédentes
- Corriger les bugs du système de jeu qui n'a pas été testé
- Polish en ajoutant du responsive





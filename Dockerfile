# Utiliser l'image officielle MySQL
FROM mysql:9.1.0

# Définir les variables d'environnement pour la configuration MySQL
ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_DATABASE=projet_4dx
ENV MYSQL_USER=user
ENV MYSQL_PASSWORD=password

# Exposer le port 3306 pour MySQL
EXPOSE 3306


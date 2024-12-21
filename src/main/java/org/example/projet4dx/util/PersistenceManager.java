package org.example.projet4dx.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Manages the persistence of entities using JPA.
 * <p>
 * This class provides methods to obtain an {@link EntityManager} for interacting with the database
 * and to close the {@link EntityManager} when finished.
 */
public class PersistenceManager {
    private static final String PERSISTENCE_UNIT_NAME = "project"; // Correspond au nom dans persistence.xml
    private static EntityManagerFactory emf;

    static {
        try {
            System.out.println("Initializing PersistenceManager");
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("Échec de l'initialisation de l'EntityManagerFactory : " + e.getMessage());
        }
    }

    public static EntityManager getEntityManager() {
        if (emf == null) {
            throw new IllegalStateException("L'EntityManagerFactory n'a pas été initialisée.");
        }
        System.out.println("Création du EntityManager");
        return emf.createEntityManager();
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}

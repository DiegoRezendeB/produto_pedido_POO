package br.unisales.jpateste.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ConnectionFactory {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");

    // Método para obter o EntityManager
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // Método para fechar a fábrica de EntityManager quando não for mais necessário
    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}

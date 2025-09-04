package com.algaworks.ecommerce;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class EntityManagerTest {

    protected EntityManagerFactory entityManagerFactory;

    protected EntityManager entityManager;

    @BeforeEach
    public void setup(){
        entityManagerFactory = Persistence.createEntityManagerFactory("Ecommerce-PU");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    public void after(){
        entityManagerFactory.close();
        entityManager.close();
    }

}

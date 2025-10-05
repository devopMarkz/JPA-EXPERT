package com.algaworks.ecommerce.concorrencia;

import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LockOtimistaTest {

    protected static EntityManagerFactory entityManagerFactory;

    @BeforeAll
    public static void setUpBeforeClass () {
        entityManagerFactory = Persistence
                .createEntityManagerFactory("Ecommerce-PU");
    }

    @AfterAll
    public static void tearDownAfterClass () {
        entityManagerFactory.close();
    }

    public static void log(Object obj, Object... args) {
        System.out.println(
                String.format("[LOG " + System.currentTimeMillis() + "] " + obj, args)
        );
    }

    private static void esperar(int segundos){
        try {
            Thread.sleep(segundos * 1000);
        } catch (InterruptedException e){
            System.out.println(e.getCause().getMessage());
        }
    }

    @Test
    public void usarLockOtimista() {
        Runnable runnable1 = () -> {
            EntityManager em = entityManagerFactory.createEntityManager();
            try {
                em.getTransaction().begin();

                log("Runnable 1 vai carregar o produto 1.");
                Produto p = em.find(Produto.class, 1, LockModeType.PESSIMISTIC_WRITE);

                log("Runnable 1 vai esperar por 3 segundos.");
                esperar(3);

                log("Runnable 1 vai alterar o produto.");
                p.setDescricao("Descrição detalhada");

                log("Runnable 1 vai confirmar a transação.");
                em.getTransaction().commit();
            } catch (Exception e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                throw e;
            } finally {
                em.close();
            }
        };

        Runnable runnable2 = () -> {
            EntityManager em = entityManagerFactory.createEntityManager();
            try {
                em.getTransaction().begin();

                log("Runnable 2 vai carregar o produto 1.");
                Produto p = em.find(Produto.class, 1, LockModeType.PESSIMISTIC_WRITE);

                log("Runnable 2 vai esperar por 5 segundos.");
                esperar(5); // ✅ garante que chega depois

                log("Runnable 2 vai alterar o produto.");
                p.setDescricao("Descrição massa!");

                log("Runnable 2 vai confirmar a transação.");
                em.getTransaction().commit();
            } catch (jakarta.persistence.OptimisticLockException ole) {
                log("Runnable 2 falhou com OptimisticLockException (como esperado).");
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
            } catch (Exception e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                throw e;
            } finally {
                em.close();
            }
        };

        Thread t1 = new Thread(runnable1);
        Thread t2 = new Thread(runnable2);
        t1.start(); t2.start();
        try { t1.join(); t2.join(); } catch (InterruptedException e) { throw new RuntimeException(e); }

        // Leitura final — escolha UMA das opções:
        // Opção A: com lock (precisa de transação)
        EntityManager em3 = entityManagerFactory.createEntityManager();
        em3.getTransaction().begin();
        Produto produto = em3.find(Produto.class, 1, LockModeType.PESSIMISTIC_WRITE);
        em3.getTransaction().commit();
        em3.close();

        // Opção B: sem lock (sem transação) — substitua o bloco acima por:
        // EntityManager em3 = entityManagerFactory.createEntityManager();
        // Produto produto = em3.find(Produto.class, 1);
        // em3.close();

//        Assertions.assertEquals("Descrição detalhada", produto.getDescricao());
        log("Encerrado método de teste.");
    }

}

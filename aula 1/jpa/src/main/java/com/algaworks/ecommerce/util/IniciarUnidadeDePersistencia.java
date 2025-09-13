package com.algaworks.ecommerce.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class IniciarUnidadeDePersistencia {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Ecommerce-PU");
        EntityManager em = emf.createEntityManager();

        // Usando Tuple para múltiplas colunas
        TypedQuery<Object[]> query = em.createQuery("SELECT nome, descricao FROM Produto", Object[].class);

        List<Object[]> list = query.getResultList();

        list.forEach(System.out::println);

        // Fechar EntityManager
        em.close();
        emf.close();
    }
}
package com.algaworks.ecommerce.util;

import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.math.BigDecimal;

public class IniciarUnidadeDePersistencia {

    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Ecommerce-PU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Produto produto = new Produto(1, "Geladeira", "Geladeira 2 portas", BigDecimal.valueOf(1320.00));

        em.persist(produto);

        Produto encontrado = em.find(Produto.class, 1);

        System.out.println(encontrado.getNome());

        em.getTransaction().commit();

        emf.close();
        em.close();
    }

}

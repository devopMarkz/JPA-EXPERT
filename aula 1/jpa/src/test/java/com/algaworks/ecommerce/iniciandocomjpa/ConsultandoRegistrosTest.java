package com.algaworks.ecommerce.iniciandocomjpa;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class ConsultandoRegistrosTest extends EntityManagerTest {

    @Test
    void testeDePersistencia(){
        EntityTransaction transaction = entityManager.getTransaction();
        Produto produto = new Produto(1, "Geladeira", "Geladeira 2 portas", BigDecimal.valueOf(1320.00));

        transaction.begin();
        entityManager.persist(produto);
        transaction.commit();
        Produto encontrado = entityManager.find(Produto.class, 1);

        Assertions.assertEquals("Geladeira", encontrado.getNome());
        Assertions.assertNotNull(encontrado);
    }

    @Test
    void atualizarAReferencia(){
        Produto produto = new Produto(1, "Geladeira", "Geladeira 2 portas", BigDecimal.valueOf(1320.00));
        entityManager.getTransaction().begin();

        entityManager.persist(produto);

        Produto encontrado = entityManager.getReference(Produto.class, 1);

        encontrado.setNome("Microfone Samson");
        entityManager.merge(encontrado);

        entityManager.getTransaction().commit();
        Assertions.assertEquals("Microfone Samson", encontrado.getNome());
    }

    @Test
    void testarMergeParaInsert(){
        entityManager.getTransaction().begin();
        Produto produto = new Produto(1, "Geladeira", "Geladeira 2 portas", BigDecimal.valueOf(1320.00));

        Produto copiado = entityManager.merge(produto);
        copiado.setNome("Copiado");

        entityManager.remove(copiado);

        Assertions.assertNotNull(copiado);
        entityManager.getTransaction().commit();
    }

    @Test
    void testarDetach(){
        entityManager.getTransaction().begin();
        Produto produto = new Produto(1, "Geladeira", "Geladeira 2 portas", BigDecimal.valueOf(1320.00));

        entityManager.persist(produto);

        entityManager.getTransaction().commit();
        
        Assertions.assertNotNull(produto);
    }

}

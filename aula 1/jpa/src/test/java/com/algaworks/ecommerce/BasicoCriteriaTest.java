package com.algaworks.ecommerce;

import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BasicoCriteriaTest extends EntityManagerTest{

    @Test
    void buscarPorIdentificador(){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> cq = cb.createQuery(Produto.class);
        Root<Produto> root = cq.from(Produto.class);

        Predicate p1 = cb.equal(root.get("nome"), "Geladeira");
        Predicate p2 = cb.equal(root.get("id"), 1);

        cq.select(root).where(p1);

        TypedQuery<Produto> query = entityManager.createQuery(cq);
        List<Produto> objs = query.getResultList();

        for (Produto obj : objs){
            System.out.println(obj.getNome());
        }
    }

    @Test
    void buscarTuplaPorIdentificador(){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Produto> root = cq.from(Produto.class);

        TypedQuery<Tuple> tQuery = entityManager.createQuery(cq);
        List<Tuple> list = tQuery.getResultList();

        for (Tuple t : list){
            List<TupleElement<?>> tpl = t.getElements();
            Tuple t1 = t;
            Produto p = (Produto) t1.get(0);
            System.out.println(p.getNome());
            System.out.println(p.getDataCriacao());
            System.out.println(p.getDescricao());
        }
    }

    @Test
    void buscarObjectPorIdentificador(){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Produto> root = cq.from(Produto.class);

        cq.multiselect(root.get("nome").alias("nome"), root.get("descricao").alias("descricao"));

        Query query = entityManager.createQuery(cq);
        List<Object[]> list = query.getResultList();

        for (Object[] obj : list){
            System.out.println(obj[0]);
            System.out.println(obj[1]);
        }
    }

    @Test
    void buscarTuple2PorIdentificador(){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Produto> root = cq.from(Produto.class);

        cq.multiselect(root.get("nome").alias("nome"), root.get("descricao").alias("descricao"));

        TypedQuery<Tuple> query = entityManager.createQuery(cq);
        List<Tuple> list = query.getResultList();

        for (Tuple t : list){
            System.out.println("Nome: " + t.get("nome") + " / Descrição: " + t.get("descricao"));
        }
    }

    @Test
    void relembrando(){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> cq = cb.createQuery(Produto.class);
        Root<Produto> root = cq.from(Produto.class);

        root.join("categorias", JoinType.LEFT);

        Predicate nomeIgual = cb.equal(root.get("nome"), "Geladeira");

        cq.select(root).where(nomeIgual);

        TypedQuery<Produto> query = entityManager.createQuery(cq);

        Produto produto = query.getSingleResult();

        System.out.println(produto.getNome());
    }

    @Test
    void testarCache(){
        Produto produto = new Produto(LocalDateTime.now(), LocalDateTime.now(), "Prod", "Descr", BigDecimal.valueOf(200.0));
        Cache cache = entityManagerFactory.getCache();
    }

}

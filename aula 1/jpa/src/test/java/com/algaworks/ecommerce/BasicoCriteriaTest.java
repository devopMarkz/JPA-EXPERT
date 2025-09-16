package com.algaworks.ecommerce;

import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;

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

        cq.multiselect(root.get("nome").alias("nome"), root.get("descricao").alias("descricao"), root.get("id").alias("id"));

        TypedQuery<Tuple> tQuery = entityManager.createQuery(cq);
        List<Tuple> list = tQuery.getResultList();

        for (Tuple t : list){
            System.out.println(t.get("nome"));
            System.out.println(t.get("descricao"));
            System.out.println(t.get("id"));
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

}

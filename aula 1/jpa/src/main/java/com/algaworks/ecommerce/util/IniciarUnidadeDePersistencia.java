package com.algaworks.ecommerce.util;

import com.algaworks.ecommerce.model.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

public class IniciarUnidadeDePersistencia {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Ecommerce-PU");
        EntityManager em = emf.createEntityManager();

        // Iniciando a transação
        em.getTransaction().begin();

        // Criar um Cliente (suposição de entidade Cliente já definida)
        Cliente cliente = new Cliente();
        cliente.setNome("João da Silva");
        em.persist(cliente);

        // Criar categorias
        Categoria categoria1 = new Categoria("Eletrônicos");
        Categoria categoria2 = new Categoria("Informática");

        // Persistir categorias
        em.persist(categoria1);
        em.persist(categoria2);

        // Criar um produto
        Produto produto = new Produto();
        produto.setNome("Notebook");
        produto.setDescricao("Notebook com 16GB de RAM");
        produto.setPreco(new BigDecimal("2500.00"));
        produto.setCategorias(List.of(categoria1, categoria2)); // Associando as categorias ao produto

        // Persistir o produto
        em.persist(produto);

        // Criar estoque para o produto
        Estoque estoque = new Estoque();
        estoque.setProduto(produto);
        estoque.setQuantidade(100);

        // Persistir o estoque
        em.persist(estoque);

        // Criar um pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);  // Associando o cliente
        pedido.setStatus(StatusPedido.AGUARDANDO);  // Status do pedido

        EnderecoEntregaPedido enderecoEntregaPedido = new EnderecoEntregaPedido();
        enderecoEntregaPedido.setCep("65042235");
        enderecoEntregaPedido.setCidade("São Luís");
        enderecoEntregaPedido.setBairro("Coroado");
        enderecoEntregaPedido.setEstado("Maranhão");
        enderecoEntregaPedido.setLogradouro("Rua do Cobre");
        enderecoEntregaPedido.setComplemento("Quadra 37");

        pedido.setEnderecoEntrega(enderecoEntregaPedido);  // Definindo o endereço

        // Persistir o pedido (antes de itens)
        em.persist(pedido);

        // Criar itens de pedido
        ItemPedido itemPedido1 = new ItemPedido();
        itemPedido1.setProduto(produto);  // Associando o produto
        itemPedido1.setPedido(pedido);  // Associando o pedido
        itemPedido1.setPrecoProduto(produto.getPreco());  // Preço do produto
        itemPedido1.setQuantidade(2);  // Quantidade

        // Definindo a chave composta para o ItemPedido
        ItemPedidoId itemPedidoId = new ItemPedidoId(pedido.getId(), produto.getId()); // Definindo a chave composta
        itemPedido1.setId(itemPedidoId);  // Setando a chave composta no item de pedido

        // Persistir item de pedido
        em.persist(itemPedido1);

        // Realizar commit da transação
        em.getTransaction().commit();

        // Fechar EntityManager
        em.close();
        emf.close();
    }
}

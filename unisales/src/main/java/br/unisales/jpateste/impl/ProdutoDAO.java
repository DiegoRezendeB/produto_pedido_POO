package br.unisales.jpateste.impl;

import br.unisales.jpateste.models.Produto;
import br.unisales.jpateste.persistence.ConnectionFactory;
import br.unisales.jpateste.spec.IDAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import java.util.List;

public class ProdutoDAO implements IDAO<Produto> {

    @Override
    public Produto get(Object id) {
        // Obtém o EntityManager da ConnectionFactory
        EntityManager em = ConnectionFactory.getEntityManager();
        
        Produto produto = em.find(Produto.class, id); // Busca o Produto pelo ID
        
        if (produto == null) {
            System.out.println("Produto não encontrado.");
        }
        
        em.close(); // Fecha o EntityManager após o uso
        return produto;
    }

    @Override
    public void persist(Produto produto) {
        // Obtém o EntityManager
        EntityManager em = ConnectionFactory.getEntityManager();
        
        // Inicia a transação
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin(); // Inicia a transação
            em.persist(produto); // Persiste o produto no banco de dados
            transaction.commit(); // Comita a transação
            System.out.println("Produto inserido com sucesso!");
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback(); // Faz rollback em caso de erro
            }
            e.printStackTrace(); // Exibe o erro
        } finally {
            em.close(); // Fecha o EntityManager
        }
    }

    @Override
    public void delete(Object id) {
        // Obtém o EntityManager
        EntityManager em = ConnectionFactory.getEntityManager();
        
        // Inicia a transação
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin(); // Inicia a transação
            Produto produto = em.find(Produto.class, id); // Encontra o produto pelo ID
            
            if (produto != null) {
                em.remove(produto); // Remove o produto
                transaction.commit(); // Comita a transação
                System.out.println("Produto excluído com sucesso!");
            } else {
                System.out.println("Produto não encontrado para remoção.");
            }
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback(); // Faz rollback em caso de erro
            }
            e.printStackTrace(); // Exibe o erro
        } finally {
            em.close(); // Fecha o EntityManager
        }
    }

    @Override
    public void update(Produto produto) {
        // Obtém o EntityManager
        EntityManager em = ConnectionFactory.getEntityManager();
        
        // Inicia a transação
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin(); // Inicia a transação
            em.merge(produto); // Atualiza o produto no banco de dados
            transaction.commit(); // Comita a transação
            System.out.println("Produto atualizado com sucesso!");
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback(); // Faz rollback em caso de erro
            }
            e.printStackTrace(); // Exibe o erro
        } finally {
            em.close(); // Fecha o EntityManager
        }
    }

    @Override
    public List<Produto> getAll() {
        // Obtém o EntityManager
        EntityManager em = ConnectionFactory.getEntityManager();
        
        // Cria uma query para buscar todos os produtos
        TypedQuery<Produto> query = em.createQuery("SELECT p FROM Produto p", Produto.class);
        List<Produto> produtos = query.getResultList();
        
        em.close(); // Fecha o EntityManager após o uso
        return produtos;
    }
}

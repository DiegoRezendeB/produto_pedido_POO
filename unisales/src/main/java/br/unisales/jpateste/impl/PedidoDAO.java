package br.unisales.jpateste.impl;

import br.unisales.jpateste.models.Pedido;
import br.unisales.jpateste.persistence.ConnectionFactory;
import br.unisales.jpateste.spec.IDAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class PedidoDAO implements IDAO<Pedido> {

    @Override
    public void persist(Pedido pedido) {
        EntityManager em = ConnectionFactory.getEntityManager(); // Obtém o EntityManager da ConnectionFactory
        EntityTransaction transaction = em.getTransaction(); // Inicia a transação
        try {
            transaction.begin(); // Inicia a transação
            em.persist(pedido); // Persiste o pedido no banco de dados
            transaction.commit(); // Comita a transação
            System.out.println("Pedido inserido com sucesso!");
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback(); // Se ocorrer erro, faz rollback
            }
            e.printStackTrace(); // Exibe o erro
        } finally {
            em.close(); // Fecha o EntityManager
        }
    }

    @Override
    public void delete(Object id) {
        EntityManager em = ConnectionFactory.getEntityManager(); // Obtém o EntityManager
        EntityTransaction transaction = em.getTransaction(); // Inicia a transação
        try {
            transaction.begin(); // Inicia a transação
            Pedido pedido = em.find(Pedido.class, id); // Encontra o pedido pelo ID
            if (pedido != null) {
                em.remove(pedido); // Remove o pedido
                transaction.commit(); // Comita a transação
                System.out.println("Pedido excluído com sucesso!");
            } else {
                System.out.println("Pedido não encontrado para remoção.");
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
    public void update(Pedido pedido) {
        EntityManager em = ConnectionFactory.getEntityManager(); // Obtém o EntityManager
        EntityTransaction transaction = em.getTransaction(); // Inicia a transação
        try {
            transaction.begin(); // Inicia a transação
            em.merge(pedido); // Atualiza o pedido no banco de dados
            transaction.commit(); // Comita a transação
            System.out.println("Pedido atualizado com sucesso!");
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
    public Pedido get(Object id) {
        EntityManager em = ConnectionFactory.getEntityManager(); // Obtém o EntityManager
        Pedido pedido = em.find(Pedido.class, id); // Encontra o pedido pelo ID
        em.close(); // Fecha o EntityManager
        return pedido;
    }

    @Override
    public List<Pedido> getAll() {
        EntityManager em = ConnectionFactory.getEntityManager(); // Obtém o EntityManager
        List<Pedido> pedidos = em.createQuery("SELECT p FROM Pedido p", Pedido.class).getResultList(); // Busca todos os pedidos
        em.close(); // Fecha o EntityManager
        return pedidos;
    }
}

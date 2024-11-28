package br.unisales.jpateste;

import br.unisales.jpateste.impl.PedidoDAO;
import br.unisales.jpateste.impl.ProdutoDAO;
import br.unisales.jpateste.models.Pedido;
import br.unisales.jpateste.models.Produto;
import br.unisales.jpateste.persistence.ConnectionFactory;
import br.unisales.jpateste.spec.IDAO;

import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;

public class Application {

    private static final IDAO<Produto> produtoDAO = new ProdutoDAO();
    private static final IDAO<Pedido> pedidoDAO = new PedidoDAO();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Aplicação está funcionando!");
        menu(); // Chamando o menu principal
    }

    

    // **Operações para Produto**
    public static void insereProduto() {
        System.out.print("Digite o nome do produto: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o preço do produto: ");
        double preco = Double.parseDouble(scanner.nextLine());
        Produto produto = new Produto(null, nome, preco);
        produtoDAO.persist(produto); // Usando a instância de produtoDAO
        System.out.println("Produto inserido com sucesso!");
    }

    public static void listaProdutos() {
        List<Produto> produtos = produtoDAO.getAll();
        System.out.println("Produtos:");
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto encontrado.");
        } else {
            for (Produto p : produtos) {
                System.out.printf("ID: %-4d Nome: %-20s Preço: R$ %-10.2f\n", p.getId(), p.getNome(), p.getPreco());
            }
        }
    } // Faltava fechar o método listaProdutos()
    
    public static void selecionaProduto() {
        System.out.print("Digite o ID do produto: ");
        Long id = Long.parseLong(scanner.nextLine());
        Produto produto = produtoDAO.get(id); // Corrigido para usar a instância de produtoDAO
        if (produto != null) {
            System.out.println("Produto selecionado:\n" + produto.getId() + " - " + produto.getNome() + " - R$ " + produto.getPreco());
        } else {
            System.out.println("Produto não encontrado.");
        }
    }
    

    public static void atualizaProduto() {
        System.out.print("Digite o ID do produto: ");
        Long id = Long.parseLong(scanner.nextLine());
        Produto produto = produtoDAO.get(id); // Corrigido para usar a instância de produtoDAO
        if (produto != null) {
            System.out.print("Digite o novo nome do produto: ");
            String nome = scanner.nextLine();
            System.out.print("Digite o novo preço do produto: ");
            double preco = Double.parseDouble(scanner.nextLine());
            produto.setNome(nome);
            produto.setPreco(preco);
            produtoDAO.update(produto); // Usando a instância de produtoDAO
            System.out.println("Produto atualizado com sucesso!");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    public static void excluiProduto() {
        System.out.print("Digite o ID do produto: ");
        Long id = Long.parseLong(scanner.nextLine());
        Produto produto = produtoDAO.get(id); // Corrigido para usar a instância de produtoDAO
        if (produto != null) {
            produtoDAO.delete(id); // Usando a instância de produtoDAO
            System.out.println("Produto excluído com sucesso!");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    // **Operações para Pedido**
    public static void inserePedido() {
        // Inserir um pedido
        System.out.print("Digite a descrição do pedido: ");
        String descricao = scanner.nextLine();
        System.out.print("Digite a quantidade do pedido: ");
        int quantidade = Integer.parseInt(scanner.nextLine());
        Pedido pedido = new Pedido(null, descricao, quantidade);
    
        // Selecionando produtos para o pedido
        boolean adicionarProdutos = true;
        while (adicionarProdutos) {
            System.out.print("Digite o ID do produto para adicionar ao pedido (ou 0 para terminar): ");
            Long produtoId = Long.parseLong(scanner.nextLine());
    
            if (produtoId == 0) {
                adicionarProdutos = false;
            } else {
                Produto produto = produtoDAO.get(produtoId); // Busca o produto pelo ID
                if (produto != null) {
                    pedido.addProduto(produto); // Adiciona o produto ao pedido
                    System.out.println("Produto adicionado ao pedido.");
                } else {
                    System.out.println("Produto não encontrado.");
                }
            }
        }
    
        // Persistir o pedido com os produtos associados
        EntityManager em = ConnectionFactory.getEntityManager(); // Obtém o EntityManager
        em.getTransaction().begin(); // Inicia a transação
    
        try {
            em.persist(pedido); // Salva o pedido
            // A tabela intermediária de relacionamento será atualizada automaticamente pelo JPA
            em.getTransaction().commit(); // Finaliza a transação com commit
            System.out.println("Pedido inserido com sucesso!");
        } catch (Exception e) {
            em.getTransaction().rollback(); // Em caso de erro, faz o rollback da transação
            System.out.println("Erro ao inserir pedido: " + e.getMessage());
        } finally {
            em.close(); // Fecha o EntityManager
        }
    }
    

    

    public static void listaPedidos() {
        List<Pedido> pedidos = pedidoDAO.getAll();
        System.out.println("Pedidos:");
        for (Pedido p : pedidos) {
            System.out.println(p.getId() + " - " + p.getDescricao() + " - Quantidade: " + p.getQuantidade());
        }
    }

    public static void selecionaPedido() {
        System.out.print("Digite o ID do pedido: ");
        Long id = Long.parseLong(scanner.nextLine());
        Pedido pedido = pedidoDAO.get(id); // Corrigido para usar a instância de pedidoDAO
        if (pedido != null) {
            System.out.println("Pedido selecionado:\n" + pedido.getId() + " - " + pedido.getDescricao() + " - Quantidade: " + pedido.getQuantidade());
        } else {
            System.out.println("Pedido não encontrado.");
        }
    }
    
    public static void atualizaPedido() {
        System.out.print("Digite o ID do pedido: ");
        Long id = Long.parseLong(scanner.nextLine());
        Pedido pedido = pedidoDAO.get(id); // Corrigido para usar a instância de pedidoDAO
        if (pedido != null) {
            System.out.print("Digite a nova descrição do pedido: ");
            String descricao = scanner.nextLine();
            System.out.print("Digite a nova quantidade do pedido: ");
            int quantidade = Integer.parseInt(scanner.nextLine());
            pedido.setDescricao(descricao);
            pedido.setQuantidade(quantidade);
            pedidoDAO.update(pedido); // Usando a instância de pedidoDAO
            System.out.println("Pedido atualizado com sucesso!");
        } else {
            System.out.println("Pedido não encontrado.");
        }
    }

    public static void excluiPedido() {
        System.out.print("Digite o ID do pedido: ");
        Long id = Long.parseLong(scanner.nextLine());
        Pedido pedido = pedidoDAO.get(id); // Corrigido para usar a instância de pedidoDAO
        if (pedido != null) {
            pedidoDAO.delete(id); // Usando a instância de pedidoDAO
            System.out.println("Pedido excluído com sucesso!");
        } else {
            System.out.println("Pedido não encontrado.");
        }
    }

    // **Menu Principal**
    public static void menu() {
        int op;
        do {
            System.out.println("Escolha uma opção:");
            System.out.println("0 - Sair");
            System.out.println("1 - Gerenciar Produtos");
            System.out.println("2 - Gerenciar Pedidos");
            op = Integer.parseInt(scanner.nextLine());
            switch (op) {
                case 1 -> menuProduto();
                case 2 -> menuPedido();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida!");
            }
        } while (op != 0);
    }

    // **Menus específicos**
    private static void menuProduto() {
        int op;
        do {
            System.out.println("Gerenciar Produtos:");
            System.out.println("0 - Voltar");
            System.out.println("1 - Inserir");
            System.out.println("2 - Listar");
            System.out.println("3 - Selecionar");
            System.out.println("4 - Atualizar");
            System.out.println("5 - Excluir");
            op = Integer.parseInt(scanner.nextLine());
            switch (op) {
                case 1 -> insereProduto();
                case 2 -> listaProdutos();
                case 3 -> selecionaProduto();
                case 4 -> atualizaProduto();
                case 5 -> excluiProduto();
                case 0 -> System.out.println("Voltando ao menu principal...");
                default -> System.out.println("Opção inválida!");
            }
        } while (op != 0);
    }

    private static void menuPedido() {
        int op;
        do {
            System.out.println("Gerenciar Pedidos:");
            System.out.println("0 - Voltar");
            System.out.println("1 - Inserir");
            System.out.println("2 - Listar");
            System.out.println("3 - Selecionar");
            System.out.println("4 - Atualizar");
            System.out.println("5 - Excluir");
            op = Integer.parseInt(scanner.nextLine());
            switch (op) {
                case 1 -> inserePedido();
                case 2 -> listaPedidos();
                case 3 -> selecionaPedido();
                case 4 -> atualizaPedido();
                case 5 -> excluiPedido();
                case 0 -> System.out.println("Voltando ao menu principal...");
                default -> System.out.println("Opção inválida!");
            }
        } while (op != 0);
    }
}

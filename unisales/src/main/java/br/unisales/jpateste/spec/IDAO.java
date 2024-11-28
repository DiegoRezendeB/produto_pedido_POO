package br.unisales.jpateste.spec;

import java.util.List;

public interface IDAO<T> {
    void persist(T t);        // Inserir
    void delete(Object id);   // Excluir por ID
    void update(T t);         // Atualizar
    T get(Object id);         // Buscar por ID
    List<T> getAll();         // Buscar todos
}

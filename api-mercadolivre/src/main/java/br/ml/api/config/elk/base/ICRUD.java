package br.ml.api.config.elk.base;


public interface ICRUD<T> {
	void adicionar(T objeto);

	void remover(T objeto);

	boolean alterar(T objeto);

	T buscar(int id);

	void exibirTodos();
}
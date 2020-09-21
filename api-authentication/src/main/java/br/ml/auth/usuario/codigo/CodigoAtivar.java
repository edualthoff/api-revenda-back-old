package br.ml.auth.usuario.codigo;


public interface CodigoAtivar<T> {

	public String gerarCodigo();
	
	public T reveterCodigo(String codigo);
}

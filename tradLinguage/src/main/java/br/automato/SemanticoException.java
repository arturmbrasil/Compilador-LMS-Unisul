package br.automato;

public class SemanticoException extends Exception{
	String erro;
	
	public SemanticoException(String erro) {
		this.erro = erro;
	}
	
	public String getMessage(){
		return erro;
	}
}

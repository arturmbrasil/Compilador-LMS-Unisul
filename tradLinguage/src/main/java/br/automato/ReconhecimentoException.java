package br.automato;

public class ReconhecimentoException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private int linha, coluna;
	private String cadeia;
	
	public ReconhecimentoException(int linha, int coluna, String cadeia) {
		this.linha = linha;
		this.coluna = coluna;
		this.cadeia = cadeia;
	}
	
	@Override
	public String toString() {
		return "Erro na linha " + linha + " coluna " + coluna + ". Cadeia " + cadeia + " nãoo reconhecida.";
	}

}

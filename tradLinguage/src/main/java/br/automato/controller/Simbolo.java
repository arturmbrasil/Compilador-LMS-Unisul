package br.automato.controller;

public class Simbolo {
	String nome, categoria;
	int nivel, geralA, geralB;
	
	public Simbolo(String nome, String categoria, int geralA, int geralB, int nivel) {
		super();
		this.nome = nome;
		this.categoria = categoria;
		this.geralA = geralA;
		this.geralB = geralB;
		this.nivel = nivel;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public int getGeralA() {
		return geralA;
	}

	public void setGeralA(int geralA) {
		this.geralA = geralA;
	}

	public int getGeralB() {
		return geralB;
	}

	public void setGeralB(int geralB) {
		this.geralB = geralB;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	@Override
	public String toString() {
		return nome + " | " + categoria + " | " + nivel + " | " +  geralA + " | " + geralB;
	}
	
	
	
	
}

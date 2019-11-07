package br.automato.controller;

public class Instrucao {
	String nome;
	int op1, op2;
	
	public Instrucao(String nome, int op1, int op2) {
		super();
		this.nome = nome;
		this.op1 = op1;
		this.op2 = op2;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getOp1() {
		return op1;
	}
	public void setOp1(int op1) {
		this.op1 = op1;
	}
	public int getOp2() {
		return op2;
	}
	public void setOp2(int op2) {
		this.op2 = op2;
	}
	
	
}

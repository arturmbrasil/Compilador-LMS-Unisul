package br.automato.controller;

public class Pilha {
	private Integer[] pilha;
	private int index;

	public Pilha() {
		this.pilha = new Integer[10000];
		this.index = 0;
	}

	public boolean vazia() {
		if (this.index == 0) {
			return true;
		}
		return false;
	}

	public boolean cheia() {
		if (this.index == 0) {
			return false;
		}
		return true;
	}

	public void insere(int e) {
		this.pilha[this.index] = Integer.valueOf(e);
		this.index += 1;
	}

	public boolean remove() {
		if (this.index == 0) {
			return false;
		}
		this.index -= 1;
		this.pilha[this.index] = null;
		return true;
	}

	public int topo()	{
		return this.pilha[(this.index - 1)].intValue();
	}

	public String listaPilha() {
		String tot = "";

		for (int i = 0; i < this.index; i++) {
			tot = tot + this.pilha[i] + " ";
		}

		return tot;
	}
}

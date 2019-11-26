package br.automato.controller;

import java.util.ArrayList;

public class TabelaSimbolos {
	private ArrayList<Simbolo> tabela = new ArrayList<Simbolo>();

	//Inserir na tabela
	public boolean insere (String nome, String categoria, int nivel, int geralA, int geralB) {
		
		int posicao = busca(nome); //Verifica se o simbolo ja existe

		if(posicao == -1 || tabela.get(posicao).getNivel() != nivel) { //Se não existe
			Simbolo s = new Simbolo(nome, categoria, geralA, geralB, nivel);
			tabela.add(s);
		}
		else {
			return false; //Quando o simbolo já esta na tabela
		}
		
		return true;
	}
	
	
	//Busca Simbolo
	public int busca (String nome) {
		for(int i = 0 ; i <= tabela.size() - 1 ; i++){
			if(tabela.get(i).getNome().equals(nome))
				return i;
		}
		return -1;
	}
	
    public Simbolo buscaParametro(String nome) {
        for(int i = 0 ; i <= tabela.size() - 1 ; i++){
			if(tabela.get(i).getNome().equals(nome))
				return tabela.get(i);
		}
        return null;
    }
    
    public Simbolo buscaSimbolo(String nome) {
        for(int i = 0 ; i <= tabela.size() - 1 ; i++){
			if(tabela.get(i).getNome().equals(nome))
				return tabela.get(i);
		}
        return null;
    }
	
	//Alterar Simbolo
	public boolean altera (int posicao, String nome, String categoria, int nivel, int geralA, int geralB) {
		if(posicao >= tabela.size() || posicao < 0) {
			return false; //Quando não existe simbolo na posição que esta sendo alterada
		}
		
		tabela.get(posicao).setNome(nome);
		tabela.get(posicao).setCategoria(categoria);
		tabela.get(posicao).setNivel(nivel);
		tabela.get(posicao).setGeralA(geralA);
		tabela.get(posicao).setGeralB(geralB);
		return true; //Alterado com sucesso
	}
	
	//Excluir Simbolo Especifico
	public boolean exclui (int posicao) {
		if(posicao >= tabela.size() || posicao < 0) {
			return false; //Quando não existe simbolo na posição que esta sendo excluida
		}
		
		tabela.remove(posicao);
		
		return true; //Excluido com sucesso
	}
	
	//Excluir todos os Simbolos de um nível
	public void excluiNivel (int nivel) {
		for(int i = 0 ; i <= tabela.size() - 1 ; i++){
			if(tabela.get(i).getNivel() == nivel)
				tabela.remove(i);
		}
	}

	public ArrayList<Simbolo> getTabela() {
		return tabela;
	}

	@Override
	public String toString() {
		String ret = "TABELA DE SÍMBOLOS\n"
				+ "Nome | Categoria | Nível | geralA | geralB  \n";
		
		for(int i = 0 ; i <= tabela.size() - 1 ; i++){
			ret += tabela.get(i).toString() + "\n";
		}
		
		return ret;
	}
	
	
	
}

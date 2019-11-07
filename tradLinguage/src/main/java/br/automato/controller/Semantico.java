package br.automato.controller;

import br.automato.domain.Token;

public class Semantico {
	
	private TabelaSimbolos tabelaSimbolos;
	private Pilha ifs;
	private Pilha whiles;
	private Pilha repeats;
	private Pilha procedures;
	private Pilha cases;
	private Pilha fors;
	private Pilha parametros;

	private int nivelAtual;
	private int nv;
	private int deslocamento;
	private int lc;
	private int lit;
	private int acaoAcumulada = 3;
	
	private AreaInstrucoes ai;
	private AreaLiterais al;
	private Hipotetica hipotetica;
	
	private String penultimo;
	private String antepenultimo;
	private String nomeIdentificador = "";
	private String tipo_identificador = "";
	
	private Instrucoes instrucoes;

	
	public void executaAcao(int codigo, Token tokenAtual, Token tokenAnterior) {
		codigo = codigo - 77;
		
		switch (codigo) {
		case 100:	// Reconhecendo o nome do programa
			hipotetica = new Hipotetica();
			ifs = new Pilha();
			whiles = new Pilha();
			repeats = new Pilha();
			procedures = new Pilha();
			parametros = new Pilha();
			cases = new Pilha();
			fors = new Pilha();
			tabelaSimbolos = new TabelaSimbolos();
			hipotetica.InicializaAI(ai);
			hipotetica.InicializaAL(al);
			
			nivelAtual = 0;
			lc = 1;
			lit = 1;
			
			tabelaSimbolos = new TabelaSimbolos();
			nv = 0;
			deslocamento = 3;
			break;
			
		case 101:	// Final de programa
			instrucoes.insereInstrucao(26, 0, 0);
			hipotetica.IncluirAI(ai, 26, 0, 0);
			break;
			
		case 102:	//Gera amem
			instrucoes.insereInstrucao(24, 0, acaoAcumulada);
			hipotetica.IncluirAI(ai, 26, 0, acaoAcumulada);
			acaoAcumulada = 3;
			break;		
		
		case 103:	// tipo ident = rotulo
			tipo_identificador = "rotulo";
			break;
		
		case 104: //#104: Encontrado o nome de rótulo, de variável, ou de parâmetro de procedure em declaração

			break;
			
		case 105: //#105: Reconhecido nome de constante em declaração
			
			break;
			
		case 106: //#106: Reconhecido valor de constante em declaração
			
			break;
		case 107://#107: Antes de lista de identificadores em declaração de variáveis
			tipo_identificador = "variavel";
			nv = 0;
			break;
		case 108:	//#108: Após nome de procedure, em declaração
			
			break;
		
			//...
		}
		
	}

}

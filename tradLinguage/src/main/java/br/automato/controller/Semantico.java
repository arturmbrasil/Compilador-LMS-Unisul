package br.automato.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import br.automato.SemanticoException;
import br.automato.domain.Recon;
import br.automato.domain.Token;

public class Semantico {
    private static List<String[]> codigoIntermediario = new ArrayList<>();

	private TabelaSimbolos tabelaSimbolos;
	private Stack<Integer> ifs;
	private Stack<Integer> whiles;
	private Stack<Integer> repeats;
	private Stack<Integer> pilhaProcedures;
	private Stack<Integer> cases;
	private Stack<Integer> fors;
	private  List<Simbolo> parametros = new ArrayList<>();

	private Stack<Simbolo> procedures;
	
	private int nivelAtual;
	private int nv; //Numero de variaveis
	private int np = 0; //Numero de parametros
	private int deslocamento;
	private int lc; //Aponta para a prox instrucao a ser gerada
	private int lit; //prox aux area de literais
	private int acaoAcumulada = 3;
	private int pt_livre;
	private int escopo[] = new int[9999];
	
	public int nivelVar, deslocVar;
	private int linhaAtual = 1;

	private AreaInstrucoes ai;
	private AreaLiterais al;
	private Hipotetica hipotetica;
	
	private String penultimo;
	private String antepenultimo;
	private String nome_identificador = "";
	private String tipo_identificador = "";

	private String contexto;
	
    private static Simbolo simboloAtual;
	
	private Instrucoes instrucoes;
	
	boolean teveParametro;
	
	public void executaAcao(int codigo, Recon tokenAtual, Recon tokenAnterior) throws SemanticoException{
		codigo = codigo - 77;
		
		switch (codigo) {
		case 100:	// Reconhecendo o nome do programa
			hipotetica = new Hipotetica();
			ifs = new Stack<>();
			whiles = new Stack<>();
			repeats = new Stack<>();
			pilhaProcedures = new Stack<>();
	        procedures = new Stack<>();

			cases = new Stack<>();
			fors = new Stack<>();
			tabelaSimbolos = new TabelaSimbolos();
			hipotetica.InicializaAI(ai);
			hipotetica.InicializaAL(al);
			
			nivelAtual = 0;
			pt_livre = 0;
			escopo[0] = 1;
			nv = 0;
			deslocamento = 3;
			lc = 1;
			lit = 1;
			
			linhaAtual = 1;
			
			tabelaSimbolos = new TabelaSimbolos();
			nv = 0;
			deslocamento = 3;
			break;
			
		case 101:	// Final de programa - PARA
	        codigoIntermediario.add(new String[] {"PARA", "-", "-"});
			
	        instrucoes.insereInstrucao(26, 0, 0);
			
			hipotetica.IncluirAI(ai, 26, 0, 0);
			break;
			
		case 102:	//Gera AMEM
			deslocamento = 3;
			int op2 = deslocamento + nv;
			codigoIntermediario.add(new String[] {"AMEM", "-", String.valueOf(op2)});

			instrucoes.insereInstrucao(24, 0, op2);
			hipotetica.IncluirAI(ai, 24, 0, op2);

			if (simboloAtual != null && simboloAtual.getCategoria().equals("PROCEDURE")) {
	            procedures.push(simboloAtual);
	        }
			break;		
		
		case 104: //#104: Encontrado o nome de rótulo, de variável, ou de parâmetro de procedure em declaração
			if (tipo_identificador.equals("ROTULO")) {
	            tabelaSimbolos.insere(tokenAtual.getToken(), "ROTULO", nivelAtual, 0, 0);
	        }

	        else if (tipo_identificador.equals("VARIAVEL")) {
	            tabelaSimbolos.insere(tokenAtual.getToken(), "VARIAVEL", nivelAtual, deslocamento, 0);
	            nv++;
	            deslocamento++;
	        }

	        else if (tipo_identificador.equals("PARAMETRO")) {
	            tabelaSimbolos.insere(tokenAtual.getToken(), "PARAMETRO", nivelAtual, 0, 0);
	            parametros.add(tabelaSimbolos.buscaSimbolo(tokenAtual.getToken()));
	            np++;
	        }
			break;
			
		case 105: //#105: Reconhecido nome de constante em declaração
			tabelaSimbolos.insere(tokenAtual.getToken(), "CONSTANTE", nivelAtual, 0, 0);
			simboloAtual = tabelaSimbolos.buscaSimbolo(tokenAtual.getToken());
			break;
			
		case 106: //#106: Reconhecido valor de constante em declaração
			simboloAtual.setGeralA(Integer.parseInt(tokenAtual.getToken()));
			break;
			
		case 107://#107: Antes de lista de identificadores em declaração de variáveis
			tipo_identificador = "VARIAVEL";
			break;
			
		case 108:	//#108: Após nome de procedure, em declaração			
			tabelaSimbolos.insere(tokenAtual.getToken(), "PROCEDURE", nivelAtual, 0, 0);
			simboloAtual = tabelaSimbolos.buscaSimbolo(tokenAtual.getToken());
			teveParametro = false;
			np = 0;
			nivelAtual++;
			nv = 0;
			deslocamento = 3;
			break;
		
		case 109: //#109: Após declaração de procedure
	        if (teveParametro) {
	        		simboloAtual.setGeralB(np);
	        }
	        for (Simbolo parametro: parametros) {
	            parametro.setGeralA(-np);
	            np--;
	        }
	        parametros.clear();
	        pilhaProcedures.push(codigoIntermediario.size());
	        codigoIntermediario.add(new String[] {"DSVS", "-", "-"});
	        simboloAtual.setGeralA(codigoIntermediario.size()+1);        
			break;
		
		case 110: //#110: Fim de procedure
			codigoIntermediario.add(new String[]{"RETU", "-", procedures.pop().getGeralB()+""});
			codigoIntermediario.get(pilhaProcedures.pop())[2] = String.valueOf(codigoIntermediario.size() + 1);
	        tabelaSimbolos.excluiNivel(nivelAtual);
	        nivelAtual--;
			break;
			
		case 111: //#111: Antes de parâmetros formais de procedures
			tipo_identificador = "PARAMETRO";
			teveParametro = true;
			break;
			
		case 112: //#112: Identificador de instrução rotulada ou comando de atribuição
			nome_identificador = tokenAtual.getToken();
			break;
			
		case 114: //#114: Atribuição parte esquerda	        
			simboloAtual = tabelaSimbolos.buscaSimbolo(tokenAtual.getToken());
	        if (!simboloAtual.getCategoria().equals("VARIAVEL"))
	            throw new SemanticoException(tokenAtual.getToken() + ": não é uma variável");
			break;
			
		case 115: //#115 : Após expressão em atribuição
			codigoIntermediario.add(new String[] { "ARMZ",  String.valueOf(nivelAtual - simboloAtual.getNivel()), simboloAtual.getGeralA() + "" });
			hipotetica.IncluirAI(ai, 4, nivelAtual - simboloAtual.getNivel(), simboloAtual.getGeralA());
			break;
		
		case 116: //#116 : Chamada de procedure
			simboloAtual = tabelaSimbolos.buscaSimbolo(tokenAtual.getToken());
	        if (!simboloAtual.getCategoria().equals("PROCEDURE"))
	            throw new SemanticoException(tokenAtual.getToken() + ": não é uma procedure!");
	   
	        np = 0;
			break;
		
		case 117: //#117: Após comando call
			 if (simboloAtual.getGeralB() != np)
		            throw new SemanticoException("Erro semantico no número de parametros");
			 np = 0;
		     codigoIntermediario.add(new String[]{"CALL", String.valueOf(nivelAtual - simboloAtual.getNivel()), simboloAtual.getGeralA() + ""});
   
		     hipotetica.IncluirAI(ai, 25, nivelAtual - simboloAtual.getNivel(), simboloAtual.getGeralA());
			break;
			
		case 118: //#118: Após expressão, em comando call
			np++;
			break;
			
		case 120: //#120: Após expressão num comando IF
			codigoIntermediario.add(new String[] {"DVSF", "-", "" });
	        ifs.push(codigoIntermediario.size() - 1);

	        //hipotetica.IncluirAI(ai, 20, -1, -1); // ??????
			break;

		case 121: //#121: Após instrução IF	
			codigoIntermediario.get(ifs.pop())[2] = String.valueOf(codigoIntermediario.size() + 1);

	        //hipotetica.AlterarAI(ai, ??, 0, linhaAtual); ????????
			break;
		
		case 122: //#122: Após domínio do THEN, antes do ELSE
			codigoIntermediario.add(new String[]{"DSVS", "-", ""});
			codigoIntermediario.get(ifs.pop())[2] = String.valueOf(codigoIntermediario.size() + 1);
	        ifs.push(codigoIntermediario.size() - 1); 
	        
	        //hipotetica.AlterarAI(ai, ??, 0, linhaAtual); ????????

			break;
			
		case 123: //#123: Comando WHILE antes da expressão
	        whiles.push(codigoIntermediario.size() + 1);
			break;
			
		case 124: //#124: Comando WHILE depois da expressão
			codigoIntermediario.add(new String[]{"DSVF", "-", ""});
	        whiles.push(codigoIntermediario.size() - 1);

	        hipotetica.IncluirAI(ai, 20, 0, 0);
			break;

		case 125: //#125: Após comando WHILE
			codigoIntermediario.get(whiles.pop())[2] = String.valueOf(codigoIntermediario.size() + 2);
			codigoIntermediario.add(new String[]{"DSVS", "-", String.valueOf(whiles.pop())});
			break;
			
		case 126: //#126: Comando REPEAT – início
			repeats.add(codigoIntermediario.size());
			break;
			
		case 127: //#127: Comando REPEAT – fim
			codigoIntermediario.add(new String[]{"DSVF", "-", String.valueOf(repeats.pop() + 1)});
			break;
			
		case 128: //#128: Comando READLN início
			contexto = "readln";
			break;
		
		case 129:
			
			break;
			
		case 130:
			
			break;
		
		case 131:
			
			break;
			
		case 132:
			
			break;
		
		case 133:
			
			break;
			
			
			
			
			
		}
	}
}

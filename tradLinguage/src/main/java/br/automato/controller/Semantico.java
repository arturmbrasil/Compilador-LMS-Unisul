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
	private Stack<Simbolo> controle;
	
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

	private AreaInstrucoes AI;
	private AreaLiterais AL;
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
	        controle = new Stack<>();

			cases = new Stack<>();
			fors = new Stack<>();
			tabelaSimbolos = new TabelaSimbolos();
			AI = new AreaInstrucoes();
			AL = new AreaLiterais();
			Hipotetica.InicializaAI(AI);
			Hipotetica.InicializaAL(AL);
			
			nivelAtual = 0;
			pt_livre = 0;
			escopo[0] = 1;
			nv = 0;
			deslocamento = 3;
			AI.LC = 1;
			AL.LIT = 1;
			
			linhaAtual = 1;
			
			tabelaSimbolos = new TabelaSimbolos();
			nv = 0;
			deslocamento = 3;
			break;
			
		case 101:	// Final de programa - PARA
	        codigoIntermediario.add(new String[] {"PARA", "-1", "-1"});
			
	        executaCodigo();
			break;
			
		case 102:	//Gera AMEM
			deslocamento = 3;
			int op2 = deslocamento + nv;
			codigoIntermediario.add(new String[] {"AMEM", "-1", String.valueOf(op2)});

			if (simboloAtual != null && simboloAtual.getCategoria().equals("PROCEDURE")) {
	            procedures.push(simboloAtual);
	        }
			break;		
		
		case 103: //#103: Após palavra LABEL em declaração de rótulo
			tipo_identificador = "ROTULO";
			break;
			
		case 104: //#104: Encontrado o nome de rótulo, de variável, ou de parâmetro de procedure em declaração
			if (tipo_identificador.equals("ROTULO")) {
	            tabelaSimbolos.insere(tokenAnterior.getToken(), "ROTULO", nivelAtual, 0, 0);
	        }

	        else if (tipo_identificador.equals("VARIAVEL")) {
	            tabelaSimbolos.insere(tokenAnterior.getToken(), "VARIAVEL", nivelAtual, deslocamento, 0);
	            nv++;
	            deslocamento++;
	        }

	        else if (tipo_identificador.equals("PARAMETRO")) {
	            tabelaSimbolos.insere(tokenAnterior.getToken(), "PARAMETRO", nivelAtual, 0, 0);
	            parametros.add(tabelaSimbolos.buscaSimbolo(tokenAnterior.getToken()));
	            np++;
	        }
			break;
			
		case 105: //#105: Reconhecido nome de constante em declaração
			tabelaSimbolos.insere(tokenAnterior.getToken(), "CONSTANTE", nivelAtual, 0, 0);
			simboloAtual = tabelaSimbolos.buscaSimbolo(tokenAnterior.getToken());
			break;
			
		case 106: //#106: Reconhecido valor de constante em declaração
			simboloAtual.setGeralA(Integer.parseInt(tokenAnterior.getToken()));
			break;
			
		case 107://#107: Antes de lista de identificadores em declaração de variáveis
			tipo_identificador = "VARIAVEL";
			break;
			
		case 108:	//#108: Após nome de procedure, em declaração			
			tabelaSimbolos.insere(tokenAnterior.getToken(), "PROCEDURE", nivelAtual, 0, 0);
			simboloAtual = tabelaSimbolos.buscaSimbolo(tokenAnterior.getToken());
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
	        codigoIntermediario.add(new String[] {"DSVS", "-1", ""});
	        simboloAtual.setGeralA(codigoIntermediario.size()+1);        
			break;
		
		case 110: //#110: Fim de procedure
			codigoIntermediario.add(new String[]{"RETU", "-1", procedures.pop().getGeralB()+""});
			codigoIntermediario.get(pilhaProcedures.pop())[2] = String.valueOf(codigoIntermediario.size() + 1);
	        tabelaSimbolos.excluiNivel(nivelAtual);
	        nivelAtual--;
			break;
			
		case 111: //#111: Antes de parâmetros formais de procedures
			tipo_identificador = "PARAMETRO";
			teveParametro = true;
			break;
			
		case 112: //#112: Identificador de instrução rotulada ou comando de atribuição
			nome_identificador = tokenAnterior.getToken();
			break;
			
		case 114: //#114: Atribuição parte esquerda	        
			simboloAtual = tabelaSimbolos.buscaSimbolo(tokenAnterior.getToken());
	        if (!simboloAtual.getCategoria().equals("VARIAVEL"))
	            throw new SemanticoException(tokenAnterior.getToken() + ": não é uma variável");
			break;
			
		case 115: //#115 : Após expressão em atribuição
			codigoIntermediario.add(new String[] { "ARMZ",  String.valueOf(nivelAtual - simboloAtual.getNivel()), simboloAtual.getGeralA() + "" });
			break;
		
		case 116: //#116 : Chamada de procedure
			simboloAtual = tabelaSimbolos.buscaSimbolo(tokenAnterior.getToken());
	        if (!simboloAtual.getCategoria().equals("PROCEDURE"))
	            throw new SemanticoException(tokenAnterior.getToken() + ": não é uma procedure!");
	   
	        np = 0;
			break;
		
		case 117: //#117: Após comando call
			 if (simboloAtual.getGeralB() != np)
		            throw new SemanticoException("Erro semantico no número de parametros");
			 np = 0;
		     codigoIntermediario.add(new String[]{"CALL", String.valueOf(nivelAtual - simboloAtual.getNivel()), simboloAtual.getGeralA() + ""});
   
			break;
			
		case 118: //#118: Após expressão, em comando call
			np++;
			break;
			
		case 120: //#120: Após expressão num comando IF
			codigoIntermediario.add(new String[] {"DSVF", "-1", "" });
	        ifs.push(codigoIntermediario.size() - 1);
			break;

		case 121: //#121: Após instrução IF	
			codigoIntermediario.get(ifs.pop())[2] = String.valueOf(codigoIntermediario.size() + 1);
			break;
		
		case 122: //#122: Após domínio do THEN, antes do ELSE
			codigoIntermediario.add(new String[]{"DSVS", "-1", ""});
			codigoIntermediario.get(ifs.pop())[2] = String.valueOf(codigoIntermediario.size() + 1);
	        ifs.push(codigoIntermediario.size() - 1); 
			break;
			
		case 123: //#123: Comando WHILE antes da expressão
	        whiles.push(codigoIntermediario.size() + 1);
			break;
			
		case 124: //#124: Comando WHILE depois da expressão
			codigoIntermediario.add(new String[]{"DSVF", "-1", ""});
	        whiles.push(codigoIntermediario.size() - 1);

			break;

		case 125: //#125: Após comando WHILE
			codigoIntermediario.get(whiles.pop())[2] = String.valueOf(codigoIntermediario.size() + 2);
			codigoIntermediario.add(new String[]{"DSVS", "-1", String.valueOf(whiles.pop())});
			break;
			
		case 126: //#126: Comando REPEAT – início
			repeats.add(codigoIntermediario.size());
			break;
			
		case 127: //#127: Comando REPEAT – fim
			codigoIntermediario.add(new String[]{"DSVF", "-1", String.valueOf(repeats.pop() + 1)});
			break;
			
		case 128: //#128: Comando READLN início
			contexto = "readln";
			break;
		
		case 129: //#129: Identificador de variável
			Simbolo identificador;
			switch (contexto) {
			case "readln":
				identificador = tabelaSimbolos.buscaSimbolo(tokenAnterior.getToken());
				if(identificador != null) { 
					if (identificador.getCategoria().equals("VARIAVEL")) {
						codigoIntermediario.add(new String[]{"LEIT", "-1", "-1"});
						codigoIntermediario.add(new String[]{"ARMZ", String.valueOf(nivelAtual - identificador.getNivel()), identificador.getGeralA() + ""});
					}					
				}
				else { //Se nao esta na tabela de simbolos = erro
		            throw new SemanticoException(tokenAnterior.getToken() + " não foi encontrado na tabela de simbolos");
				}
				break;
			case "expressao":
				identificador = tabelaSimbolos.buscaSimbolo(tokenAnterior.getToken());
				if(identificador != null) {
		            if (identificador.getCategoria().equals("PROCEDURE") || identificador.getCategoria().equals("ROTULO"))
		            		throw new SemanticoException(tokenAnterior.getToken() + " não deveria ser procedure/rotulo");
		            else if (identificador.getCategoria().equals("CONSTANTE"))
		                codigoIntermediario.add(new String[]{"CRCT", "-1", identificador.getGeralA() + ""});
		            else
		                codigoIntermediario.add(new String[]{"CRVL", String.valueOf(nivelAtual - identificador.getNivel()), identificador.getGeralA() + ""});
				}
				else { //Se nao esta na tabela de simbolos = erro
		            throw new SemanticoException(tokenAnterior.getToken() + " não foi encontrado na tabela de simbolos");
				}
				break;
			default:
	            throw new SemanticoException("Contexto indefinido");
			}
			break;
			
		case 130: //#130: WRITELN - após literal na instrução WRITELN
			codigoIntermediario.add(new String[]{"IMPRL", "-1", AL.LIT+""});
			Hipotetica.IncluirAL(AL, tokenAnterior.getToken()+"");
			break;
		
		case 131: //#131: WRITELN após expressão
	        codigoIntermediario.add(new String[]{"IMPR", "-1", "-1"});
			break;
			
		case 132: //#132 : Após palavra reservada CASE
			
			break;
		
		case 133: //#133: Após comando CASE
			
			break;
			
		case 134: //#134: Ramo do CASE após inteiro, último da lista
			
			break;
			
		case 135: //#135: Após comando em CASE
			
			break;
			
		case 136: //#136: Ramo do CASE: após inteiro
			
			break;
			
		case 137: //#137: Após variável controle comando FOR
	        simboloAtual = tabelaSimbolos.buscaSimbolo(tokenAnterior.getToken());
	        if(simboloAtual == null || !simboloAtual.getCategoria().equals("VARIAVEL")) { 
	            throw new SemanticoException(tokenAnterior.getToken() + " não foi encontrado ou não é nome de variável");
			}
			break;

		case 138: //#138: Após expressão valor inicial
	        codigoIntermediario.add(new String[]{"ARMZ", String.valueOf(nivelAtual - simboloAtual.getNivel()), simboloAtual.getGeralA()+""});
			break;

		case 139: //#139: Após expressão – valor final
	        fors.add(codigoIntermediario.size());
	        codigoIntermediario.add(new String[]{"COPI", "-1", "-1"});
	        codigoIntermediario.add(new String[]{"CRVL", String.valueOf(nivelAtual - simboloAtual.getNivel()), simboloAtual.getGeralA()+""});
	        codigoIntermediario.add(new String[]{"CMAI", "-1", "-1"});
	        codigoIntermediario.add(new String[]{"DSVF", "-1", ""});
	        fors.add(codigoIntermediario.size()-1); //indice do dsvf
	        controle.add(simboloAtual);
			break;

		case 140: //#140: Após comando em FOR
			Simbolo controleAtual = controle.pop();
	        codigoIntermediario.add(new String[]{"CRVL", String.valueOf(nivelAtual - controleAtual.getNivel()), controleAtual.getGeralA()+""});
	        codigoIntermediario.add(new String[]{"CRCT", "-1", "1"});
	        codigoIntermediario.add(new String[]{"SOMA", "-1", "-1"});
	        codigoIntermediario.add(new String[]{"ARMZ", String.valueOf(nivelAtual - controleAtual.getNivel()), controleAtual.getGeralA()+""});
	        codigoIntermediario.get(fors.pop() ) [2] = String.valueOf(codigoIntermediario.size() + 2);
	        codigoIntermediario.add(new String[]{"DSVS", "-1", String.valueOf(fors.pop() + 1)});
	        codigoIntermediario.add(new String[]{"AMEM", "-1", "-1"});
			break;
			
		case 141: //CMIG
	        codigoIntermediario.add(new String[]{"CMIG", "-1", "-1"});
			break;

		case 142: //CMME
	        codigoIntermediario.add(new String[]{"CMME", "-1", "-1"});
			break;

		case 143: //CMMA
	        codigoIntermediario.add(new String[]{"CMMA", "-1", "-1"});
			break;

		case 144: //CMAI
	        codigoIntermediario.add(new String[]{"CMAI", "-1", "-1"});
			break;

		case 145: //CMEI
			codigoIntermediario.add(new String[]{"CMEI", "-1", "-1"});
			break;

		case 146: //CMDF
			codigoIntermediario.add(new String[]{"CMDF", "-1", "-1"});
			break;
			
		case 147: //INVR
			codigoIntermediario.add(new String[]{"INVR", "-1", "-1"});
			break;

		case 148: //SOMA
	        codigoIntermediario.add(new String[]{"SOMA", "-1", "-1"});
			break;
			
		case 149: //SUBT
	        codigoIntermediario.add(new String[]{"SUBT", "-1", "-1"});
			break;
			
		case 150: //DISJ
	        codigoIntermediario.add(new String[]{"DISJ", "-1", "-1"});
			break;
			
		case 151: //MULT
	        codigoIntermediario.add(new String[]{"MULT", "-1", "-1"});
			break;
			
		case 152: //DIVI
	        codigoIntermediario.add(new String[]{"DIVI", "-1", "-1"});
			break;
			
		case 153: //CONJ
	        codigoIntermediario.add(new String[]{"CONJ", "-1", "-1"});
			break;
			
		case 154: //CRCT
	        codigoIntermediario.add(new String[]{"CRCT", "-1", "-1"});
			break;
			
		case 155: //NEGA
	        codigoIntermediario.add(new String[]{"NEGA", "-1", "-1"});
			break;
			
		case 156:
			contexto = "expressao";
			break;
			
		default:
			break;
			
			
		}
	}

	public void executaCodigo() throws SemanticoException {
		int codigoInstrucao, op1, op2;
		instrucoes = new Instrucoes();
		 for (int i = 0; i < codigoIntermediario.size(); i++) { //para cada instrucao
			 //pega valor instrucao
			 
			 codigoInstrucao = instrucoes.buscaInstrucao(codigoIntermediario.get(i)[0]);
			 if(codigoInstrucao == -1) {
		            throw new SemanticoException("Instrucao ' "+ codigoIntermediario.get(i)[0]  +" ' inexistente");
			 }
			 op1 = Integer.parseInt(codigoIntermediario.get(i)[1]);
			 op2 = Integer.parseInt(codigoIntermediario.get(i)[2]);
			 
			 hipotetica.IncluirAI(AI, codigoInstrucao, op1, op2);
		 }
		 
		 Hipotetica.Interpreta(AI, AL);
	}
}

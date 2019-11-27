package br.automato.controller;

import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Instrucoes {

	private String[] instrucoes;
	private int pt;
	private String codigo;
	ArrayList<Instrucao> instrucao = new ArrayList<Instrucao>();
	
	public Instrucoes()
	{
		instrucoes = new String[30];
		instrucoes[1] = "RETU";	//Retorno de procedure
		instrucoes[2] = "CRVL";	//Carrega valor na pilha
		instrucoes[3] = "CRCT";	//Carrega constante na pilha
		instrucoes[4] = "ARMZ";	//Armazena conteudo da pilha no endereco
		instrucoes[5] = "SOMA";	//Soma
		instrucoes[6] = "SUBT";	//Subtracao
		instrucoes[7] = "MULT";	//Multiplicacao
		instrucoes[8] = "DIVI";	//Divisao
		instrucoes[9] = "INVR";	//Inverte
		instrucoes[10] = "NEGA";	//Negacao
		instrucoes[11] = "CONJ";	//AND
		instrucoes[12] = "DISJ";	//OR
		instrucoes[13] = "CMME";	//Compara se é menor
		instrucoes[14] = "CMMA";	//Compara se é maior
		instrucoes[15] = "CMIG";	//Compara se é igual
		instrucoes[16] = "CMDF";	//Compara se é diferente
		instrucoes[17] = "CMEI";	//Compara menor igual
		instrucoes[18] = "CMAI";	//Compara maior igual
		instrucoes[19] = "DSVS";	//Desvia sempre
		instrucoes[20] = "DSVF";	//Desvia se falso
		instrucoes[21] = "LEIT";	//Leitura
		instrucoes[22] = "IMPR";	//Imprime
		instrucoes[23] = "IMPRL";	//Imprime literal
		instrucoes[24] = "AMEM";	//AMEM
		instrucoes[25] = "CALL";	//Chama procedure
		instrucoes[26] = "PARA";	//Fim programa
		instrucoes[27] = "NADA";	//Nada
		instrucoes[28] = "COPI";	//Copia topo pilha
		instrucoes[29] = "DSVT";	//Desvia se true

		pt = 0;

		codigo = "Instrucao - OP1 - OP2";

	}

	//insercao
	public void insereInstrucao(int indice, int op1, int op2)
	{
		String nome = instrucoes[indice];
		Instrucao i = new Instrucao(nome, op1, op2);
		instrucao.add(i);
	}

	//alteracao
	public void alteraInstrucao(int indice, int op1, int op2) {
		instrucao.get(indice).setOp1(op1);
		instrucao.get(indice).setOp2(op2);
	}

	//limpa a tabela
	public void limpar() {
		instrucao.clear();
	}
	
	//busca codigo da instrucao
	public int buscaInstrucao(String instru) {
		for(int i = 1; i<instrucoes.length; i++) {
			if (instrucoes[i].equals(instru)) {
				return i;
			}
		}
		return -1;
	}
}

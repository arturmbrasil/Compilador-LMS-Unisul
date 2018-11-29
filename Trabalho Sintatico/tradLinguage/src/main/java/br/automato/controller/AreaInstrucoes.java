package br.automato.controller;

/**
* Classe utilizada pela classe "Hipotetica" para armazenar a área de
* instruções.
* Esta classe, bem como as classes "Tipos", "AreaLiterais"
* e "Hipotetica" foi criada por Maicon, Reinaldo e Fabio e adaptada
* para este aplicativo.
*/
public class AreaInstrucoes {
	public Tipos AI[]= new Tipos[1000];
	public int LC;
	
  /**
 * Construtor sem parâmetros.
 * Todos os atributos são inicializados com valores padrões.
 */
	AreaInstrucoes(){
		for(int i=0; i<1000; i++){
			AI[i]=new Tipos();
		}
	}
}

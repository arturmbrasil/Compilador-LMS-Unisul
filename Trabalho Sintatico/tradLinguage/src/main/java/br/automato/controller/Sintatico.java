package br.automato.controller;

import java.io.IOException;
import java.util.ArrayList;
import br.automato.domain.Recon;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Sintatico implements MatrizParse {

	static ArrayList<Integer> pilha = new ArrayList<Integer>();

	public void analisar(ObservableList<Recon> tokens) throws IOException {
		
		//Limpa pilha e coloca o simbolo inicial e o simbolo de final de arquivo
		pilha.clear();
		pilha.add(Integer.valueOf(1)); //EOF
		pilha.add(Integer.valueOf(SIMBOLO_INICIAL));
		Integer topoPilha = (Integer)pilha.get(pilha.size() - 1);
		Recon tokenAtual;
		int i = 0; //index token atual
		while (!pilha.isEmpty() || topoPilha != 1) { 
			topoPilha = (Integer)pilha.get(pilha.size() - 1);
			if(i<tokens.size()) {
				tokenAtual = tokens.get(i);				
			}
			else {
				tokenAtual = tokens.get(tokens.size()-1);
			}
			
			if(topoPilhaIsTerminal(topoPilha) || topoPilha == 1 ) {
				if(topoPilha == tokenAtual.getCod()) { //Topo da pilha √© o token atual = reconhecido
					//Remove token do topo da pilha e vai para o prox token
					i++;
					pilha.remove(pilha.size()-1);
					topoPilha = (Integer)pilha.get(pilha.size() - 1);

				}
				else if(topoPilha == 1) {
					Alert alertWindow = new Alert(AlertType.INFORMATION);
					alertWindow.setTitle("SUCESSO");
					alertWindow.setContentText("An·lise sintatica concluida com sucesso!");
					alertWindow.show();		  
					return;
				}
				else {
					//ERRO
					Alert alertWindow = new Alert(AlertType.ERROR);
					alertWindow.setTitle("ERRO SINT¡TICO");
					alertWindow.setContentText("Linha: "+tokenAtual.getLinha()+ 
							" - Token encontrado: "+tokenAtual.getToken()+
							" - CÛdigo do token: " + tokenAtual.getCod() + 
							" - Topo da pilha: " + topoPilha + ERROS_PARSE[topoPilha]);
					alertWindow.show();
					System.out.println("Linha: "+tokenAtual.getLinha()+"\n"
							+ "Token encontrado: "+tokenAtual.getToken()+"\n"
							+ "CÛdigo do token: " + tokenAtual.getCod() + "\n"
							+ "Topo da pilha: " + topoPilha + "\n"
							+ ERROS_PARSE[topoPilha]);

					return;
				}
			}
			else if(!topoPilhaIsTerminal(topoPilha)){
				//Remove o topo da pilha
				pilha.remove(pilha.size()-1);

				//Empilha regras de producao
		        int producao = TABELA_PARSE[topoPilha-46][tokenAtual.getCod()-1];
		        
		        if(producao>=0) {
		        		int[] regra = PRODUCOES[producao];
		        		if(regra[0] != 0) {
			        		for(int k = regra.length-1; k >= 0; k--) {
			        			pilha.add(regra[k]);
			        		}
		        		}
		        }
		        else {
		        		//ERRO
		        		Alert alertWindow = new Alert(AlertType.ERROR);
					alertWindow.setTitle("ERRO SINTATICO");
					alertWindow.setContentText("Linha: "+tokenAtual.getLinha()+ 
								" - Token encontrado: "+tokenAtual.getToken()+
								" - CÛdigo do token: " + tokenAtual.getCod() + 
								" - Topo da pilha: " + topoPilha + ERROS_PARSE[topoPilha]);
					alertWindow.show();
					
					System.out.println("Linha: "+tokenAtual.getLinha()+"\n"
							+ "Token encontrado: "+tokenAtual.getToken()+"\n"
							+ "CÛdidigo do token: " + tokenAtual.getCod() + "\n"
							+ "Topo da pilha: " + topoPilha + "\n"
							+ ERROS_PARSE[topoPilha]);
					return;
				}
		        	
			}
		}
		
		if(pilha.isEmpty() || topoPilha != 1) {
			Alert alertWindow = new Alert(AlertType.INFORMATION);
			alertWindow.setTitle("SUCESSO");
			alertWindow.setContentText("An√°lise sint√°tica concluida com sucesso!");
			alertWindow.show();		  
			return;
		}
		
	}

	
	public boolean topoPilhaIsTerminal (Integer topoPilha) {
		if(topoPilha<46) {
			return true; // √© terminal
		}
		return false; // n√£o √© terminal
	}
	
}

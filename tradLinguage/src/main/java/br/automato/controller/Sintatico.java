package br.automato.controller;

import java.io.IOException;
import java.util.ArrayList;

import br.automato.SemanticoException;
import br.automato.domain.Recon;
import br.automato.domain.Token;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Sintatico implements MatrizParse {

	static ArrayList<Integer> pilha = new ArrayList<Integer>();
	Semantico semantico;

	public boolean isAcaoSemantica(Integer topoPilha) {
		if(topoPilha>=PRIMEIRA_ACAO_SEMANTICA) {
			return true;
		}
		return false;
	}
	public boolean isNaoTerminal(Integer topoPilha) {
		if(topoPilha>=PRIMEIRO_NAO_TERMINAL && topoPilha<PRIMEIRA_ACAO_SEMANTICA) {
			return true;
		}
		return false;
	}
	public void analisar(ObservableList<Recon> tokens, boolean executarSemantico) throws IOException {
		semantico = new Semantico(); //Inicializa o analisador semantico

		//Limpa pilha, coloca o simbolo inicial e o simbolo de final de arquivo
		pilha.clear();
		pilha.add(Integer.valueOf(1)); //EOF
		pilha.add(Integer.valueOf(SIMBOLO_INICIAL));
		Integer topoPilha = (Integer)pilha.get(pilha.size() - 1);
		Recon tokenAtual, tokenAnterior = null;
		
		int i = 0; //index do token que está sendo analisado
		while (!pilha.isEmpty() || topoPilha != 1) { 
			topoPilha = (Integer)pilha.get(pilha.size() - 1);
			if(i<tokens.size()) {
				tokenAtual = tokens.get(i);				
			}
			else {
				tokenAtual = tokens.get(tokens.size()-1);
			}
			if(topoPilhaIsTerminal(topoPilha) || topoPilha == 1 ) { //Topo da pilha é um simbolo terminal
				if(topoPilha == tokenAtual.getCod()) { //Topo da pilha é o token atual = reconhecido
					//Remove token do topo da pilha e vai para o prox token
					i++;
					tokenAnterior = tokenAtual;
					pilha.remove(pilha.size()-1);
					topoPilha = (Integer)pilha.get(pilha.size() - 1);
				}
				else if(topoPilha == 1) {
					if(i<tokens.size()) { //Se ainda tiver tokens para analisar
						Alert alertWindow = new Alert(AlertType.ERROR);
						alertWindow.setTitle("ERRO SINTÁTICO");
						alertWindow.setContentText("Linha: "+tokenAtual.getLinha()+ 
								" - Token encontrado: "+tokenAtual.getToken()+
								" - Código do token: " + tokenAtual.getCod() + 
								" - Topo da pilha: " + topoPilha + " - Foi encontrado o símbolo de final de arquivo mas ainda há tokens a serem analisados.");
						alertWindow.show();
					}
					else { //Topo da pilha = final de arquivo; e não tem mais nenhum token para analisar
						Alert alertWindow = new Alert(AlertType.INFORMATION);
						alertWindow.setTitle("SUCESSO");
						alertWindow.setContentText("Análise sintatica concluida com sucesso!");
						alertWindow.show();		  						
					}
					return;
				}
				else {
					//ERRO
					Alert alertWindow = new Alert(AlertType.ERROR);
					alertWindow.setTitle("ERRO SINTÁTICO");
					alertWindow.setContentText("Linha: "+tokenAtual.getLinha()+ 
							" - Token encontrado: "+tokenAtual.getToken()+
							" - Código do token: " + tokenAtual.getCod() + 
							" - Topo da pilha: " + topoPilha + " - " + ERROS_PARSE[topoPilha]);
					alertWindow.show();
					System.out.println("Linha: "+tokenAtual.getLinha()+"\n"
							+ "Token encontrado: "+tokenAtual.getToken()+"\n"
							+ "Código do token: " + tokenAtual.getCod() + "\n"
							+ "Topo da pilha: " + topoPilha + "\n"
							+ ERROS_PARSE[topoPilha]);

					return;
				}
			}
			else if(isNaoTerminal(topoPilha)){ //Topo da pilha é um não terminal
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
							" - Código do token: " + tokenAtual.getCod() + 
							" - Topo da pilha: " + topoPilha  + " - " + ERROS_PARSE[topoPilha]);
				alertWindow.show();
				
				System.out.println("Linha: "+tokenAtual.getLinha()+"\n"
						+ "Token encontrado: "+tokenAtual.getToken()+"\n"
						+ "Código do token: " + tokenAtual.getCod() + "\n"
						+ "Topo da pilha: " + topoPilha + "\n"
						+ ERROS_PARSE[topoPilha]);
				return;
			}
		}
			else if(isAcaoSemantica(topoPilha)) { //SE FOR ACAO SEMANTICA
				//Executa acao semantica
				if (executarSemantico) {
					try {
						semantico.executaAcao(topoPilha, tokenAtual, tokenAnterior);
					} catch (SemanticoException e) {
		       			Alert alert = new Alert(AlertType.ERROR);
		       			alert.setTitle("Erro durante a execução");
		       			alert.setContentText(e.getMessage());
		       			alert.showAndWait();
						e.printStackTrace();
					}
				}
				//Remove da pilha
				pilha.remove(pilha.size()-1);					
			}
			
			if(i >= tokens.size() && tokenAtual.getCod() != 16) { //Quando analisa todos os tokens mas não encontra simbolo de final de arquivo
				Alert alertWindow = new Alert(AlertType.ERROR);
				alertWindow.setTitle("ERRO SINTÁTICO");
				alertWindow.setContentText("Linha: "+tokenAtual.getLinha()+ 
						" - Token encontrado: "+tokenAtual.getToken()+
						" - Código do token: " + tokenAtual.getCod() + 
						" - Topo da pilha: " + topoPilha + " - Não foi encontrado símbolo de final de arquivo.");
				alertWindow.show();
				return;
			}
		}
	}

	public boolean topoPilhaIsTerminal (Integer topoPilha) {
		if(topoPilha<46) {
			return true; // é terminal
		}
		return false; // não é terminal
	}
	
}

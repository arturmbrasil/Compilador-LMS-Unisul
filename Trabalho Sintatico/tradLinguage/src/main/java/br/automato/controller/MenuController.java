package br.automato.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import br.automato.ReconhecimentoException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

public class MenuController {
	@FXML
	TextArea txtPrograma;
	
	@FXML
	public void buscaArquivo() {
		txtPrograma.setText(null);
		
		FileChooser chooser = new FileChooser();
		File f = chooser.showOpenDialog(null);
		if (f == null) {
			return;
		}
		try { 
			FileReader reader = new FileReader(f); 
			BufferedReader input = new BufferedReader(reader); 
			String linha; 
			while ((linha = input.readLine()) != null) { 
				txtPrograma.appendText(linha + "\n");
			} 
			input.close(); 
			} catch (IOException ioe) { 
			System.out.println(ioe); 
			}
	}
}

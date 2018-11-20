package br.automato.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

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
	
	@FXML
	public void salvar() {
        new File("codigo.txt");

		String texto = "";
        String[] txt=null;
        PrintWriter pw=null;
        try {
            pw = new PrintWriter( new File( "codigo.txt" ) );
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        texto=txtPrograma.getText();
        txt = texto.split("\n");
        for (int i = 0; i < txt.length; i++) {
            pw.println(txt[i]);
        }
        pw.close();
	}
	
	@FXML
	public void btnLexico() {
		File f = new File("codigo.txt");
		
		//Chamar o principal.fxml
		PrincipalController.loadFile(f);
		
	}
}

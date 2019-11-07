package br.automato.domain;

import javafx.beans.property.*;

public class Recon {
	
	private final SimpleIntegerProperty cod;
	private final SimpleStringProperty token;
	private final SimpleStringProperty desc;
	private int linha;
	
	public Recon(int cod, String token, String desc, int linha) {
		this.cod = new SimpleIntegerProperty(cod);
		this.token = new SimpleStringProperty(token);
		this.desc = new SimpleStringProperty(desc);
		this.linha = linha;
	}
	
	

	public int getLinha() {
		return linha;
	}
	public void setLinha(int linha) {
		this.linha = linha;
	}

	
	public Integer getCod() {
		return cod.get();
	}

	public String getToken() {
		return token.get();
	}

	public String getDesc() {
		return desc.get();
	}
	
	public void setCod(Integer cod) {
		this.cod.set(cod);
	}

	public void setToken(String token) {
		this.token.set(token);
	}

	public void setDesc(String desc) {
		this.desc.set(desc);
	}
	

}

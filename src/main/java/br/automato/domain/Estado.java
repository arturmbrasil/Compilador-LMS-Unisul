package br.automato.domain;

import java.util.*;

public class Estado {
	
	public boolean isFinal;
	public Map<String, Estado> transicoes;
	public Token token;
	
	public static final String ASPA = "\"";
	public static final String REGEX_TOKENIZER = "\\s{1,}(?=(\"[^\"]*\"|[^\"])*$)|((\\(\\*).*(\\*\\)))"; 
	public static final String REGEX_NOT_IDENTIFIER = "[^0-9A-z]";
	public static final int MAX_INT = 32767;
	
	public Estado(boolean isFinal, Map<String, Estado> transicoes, Token token) {
		this.isFinal = isFinal;
		this.transicoes = transicoes;
		this.token = token;
	}

}

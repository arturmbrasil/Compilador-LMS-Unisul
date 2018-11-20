package br.automato.controller;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.math.*;

import com.google.common.base.*;
import com.google.common.io.*;

import br.automato.*;
import br.automato.domain.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.control.cell.*;
import javafx.stage.*;

public class PrincipalController implements Initializable {
	
	static Map<String, Estado> mapEstados;
	// Inicializador da classe para popular os estados e transições
	static {
		mapEstados = new HashMap<>();
		mapEstados.put("T", new Estado(false, new HashMap<String, Estado>(){
			{
				put("O", new Estado(true, null, Token.TO));
				put("H", new Estado(false, new HashMap<String, Estado>(){
					{
						put("E", new Estado(false, new HashMap<String, Estado>(){
								{
									put("N", new Estado(true, null, Token.THEN));
								}
						}, null));
					}
				}, null));
			}
		}, null ));
		mapEstados.put("V", new Estado(false, new HashMap<String, Estado>(){
			{
				put("A", new Estado(false, new HashMap<String, Estado>(){
					{
						put("R", new Estado(true, null, Token.VAR));
					}
				}, null));
			}
		}, null));
		mapEstados.put("W", new Estado(false, new HashMap<String, Estado>(){
			{
				put("H", new Estado(false, new HashMap<String, Estado>(){
					{
						put("I", new Estado(false, new HashMap<String, Estado>(){
							{
								put("L", new Estado(false, new HashMap<String, Estado>(){
									{
										put("E", new Estado(true, null, Token.WHILE));
									}
								}, null));
							}
						}, null));
					}
				}, null));
				put("R", new Estado(false, new HashMap<String, Estado>(){
					{
						put("I", new Estado(false, new HashMap<String, Estado>(){
							{
								put("T", new Estado(false, new HashMap<String, Estado>(){
									{
										put("E", new Estado(false, new HashMap<String, Estado>(){
											{
												put("L", new Estado(false, new HashMap<String, Estado>(){
													{
														put("N", new Estado(true, null, Token.WRITELN));
													}
												}, null));
											}
										}, null));
									}
								}, null));
							}
						}, null));
					}
				}, null));
			}
		}, null));
		mapEstados.put("A", new Estado(false, new HashMap<String, Estado>(){
			{
				put("N", new Estado(false, new HashMap<String, Estado>(){
					{
						put("D", new Estado(true, null, Token.AND));
					}
				}, null));
			}
		}, null));
		mapEstados.put("B", new Estado(false, new HashMap<String, Estado>(){
			{
				put("E", new Estado(false, new HashMap<String, Estado>(){
					{
						put("G", new Estado(false, new HashMap<String, Estado>(){
							{
								put("I", new Estado(false, new HashMap<String, Estado>(){
									{
										put("N", new Estado(true, null, Token.BEGIN));
									}
								}, null));
							}
						}, null));
					}
				}, null));
			}
		}, null));
		mapEstados.put("C", new Estado(false, new HashMap<String, Estado>(){
			{
				put("A", new Estado(false, new HashMap<String, Estado>(){
					{
						put("L", new Estado(false, new HashMap<String, Estado>(){
							{
								put("L", new Estado(true, null, Token.CALL));
							}
						}, null));
						put("S", new Estado(false, new HashMap<String, Estado>(){
							{
								put("E", new Estado(true, null, Token.CASE));
							}
						}, null));
					}
				}, null));
				put("O", new Estado(false, new HashMap<String, Estado>(){
					{
						put("N", new Estado(false, new HashMap<String, Estado>(){
							{
								put("S", new Estado(false, new HashMap<String, Estado>(){
									{
										put("T", new Estado(true, null, Token.CONST));
									}
								}, null));
							}
						}, null));
					}
				}, null));
			}
		}, null));
		mapEstados.put("D", new Estado(false, new HashMap<String, Estado>(){
			{
				put("O", new Estado(true, null, Token.DO));
			}
		}, null));
		mapEstados.put("E", new Estado(false, new HashMap<String, Estado>(){
			{
			put("L", new Estado(false, new HashMap<String, Estado>(){
				{
					put("S", new Estado(false, new HashMap<String, Estado>(){
						{
							put("E", new Estado(true, null, Token.ELSE));
						}
					}, null));
				}
			}, null));
			put("N", new Estado(false, new HashMap<String, Estado>(){
				{
					put("D", new Estado(true, null, Token.END));
				}
			}, null));
			}
		}, null));
		mapEstados.put("F", new Estado(false, new HashMap<String, Estado>(){
			{
				put("O", new Estado(false, new HashMap<String, Estado>(){
					{
						put("R", new Estado(true, null, Token.FOR));
					}
				}, null));
			}
		}, null));
		mapEstados.put("I", new Estado(false, new HashMap<String, Estado>(){
			{
				put("F", new Estado(true, null, Token.IF));
				put("N", new Estado(false, new HashMap<String, Estado>(){
					{
						put("T", new Estado(false, new HashMap<String, Estado>(){
							{
								put("E", new Estado(false, new HashMap<String, Estado>(){
									{
										put("G", new Estado(false, new HashMap<String, Estado>(){
											{
												put("E", new Estado(false, new HashMap<String, Estado>(){
													{
														put("R", new Estado(true, null, Token.INTEGER));
													}
												}, null));
											}
										}, null));
									}
								}, null));
							}
						}, null));
					}
				}, null));
			}
		}, null));
		mapEstados.put("N", new Estado(false, new HashMap<String, Estado>(){
			{
				put("O", new Estado(false, new HashMap<String, Estado>(){
					{
						put("T", new Estado(true, null, Token.NOT));
					}
				}, null));
			}
		}, null));
		mapEstados.put("O", new Estado(false, new HashMap<String, Estado>(){
			{
				put("R", new Estado(true, null, Token.OR));
			}
		}, null));
		mapEstados.put("P", new Estado(false, new HashMap<String, Estado>() {
			{
				put("R", new Estado(false, new HashMap<String, Estado>(){
					{
						put("O", new Estado(false, new HashMap<String, Estado>(){
							{
								put("G", new Estado(false, new HashMap<String, Estado>(){
									{
										put("R", new Estado(false, new HashMap<String, Estado>(){
											{
												put("A", new Estado(false, new HashMap<String, Estado>(){
													{
														put("M", new Estado(true, null, Token.PROGRAM));
													}
												}, null));
											}
										}, null));
									}
								}, null));
								put("C", new Estado(false, new HashMap<String, Estado>(){
									{
										put("E", new Estado(false, new HashMap<String, Estado>(){
											{
												put("D", new Estado(false, new HashMap<String, Estado>(){
													{
														put("U", new Estado(false, new HashMap<String, Estado>(){
															{
																put("R", new Estado(false, new HashMap<String, Estado>(){
																	{
																		put("E", new Estado(true, null, Token.PROCEDURE));
																	}
																}, null));
															}
														}, null));
													}
												}, null));
											}
										}, null));
									}
								}, null)); 
							}
						}, null));
					}
				}, null));
			}
		}, null));
		mapEstados.put("R", new Estado(false, new HashMap<String, Estado>(){
			{
				put("E", new Estado(false, new HashMap<String, Estado>(){
					{
						put("A", new Estado(false, new HashMap<String, Estado>(){
							{
								put("D", new Estado(false, new HashMap<String, Estado>(){
									{
										put("L", new Estado(false, new HashMap<String, Estado>(){
											{
												put("N", new Estado(true, null, Token.READLN));
											}
										}, null));
									}
								}, null));
							}
						}, null));
						put("P", new Estado(false, new HashMap<String, Estado>(){
							{
								put("E", new Estado(false, new HashMap<String, Estado>(){
									{
										put("A", new Estado(false, new HashMap<String, Estado>(){
											{
												put("T", new Estado(true, null, Token.REPEAT));
											}
										}, null));
									}
								}, null));
							}
						}, null));
					}
				}, null));
			}
		}, null));
		mapEstados.put("U", new Estado(false, new HashMap<String, Estado>(){
			{
				put("N", new Estado(false, new HashMap<String, Estado>(){
					{
						put("T", new Estado(false, new HashMap<String, Estado>(){
							{
								put("I", new Estado(false, new HashMap<String, Estado>(){
									{
										put("L", new Estado(true, null, Token.UNTIL));
									}
								}, null));
							}
						}, null));
					}
				}, null));
			}
		}, null));
		mapEstados.put("*", new Estado(true, null, Token.SINAL_MULT));
		mapEstados.put("/", new Estado(true, null, Token.SINAL_DIV));
		mapEstados.put("+", new Estado(true, null, Token.SINAL_MAIS));
		mapEstados.put("-", new Estado(true, null, Token.SINAL_MENOS));
		mapEstados.put("=", new Estado(true, null, Token.OP_EQ));
		mapEstados.put(">", new Estado(true, new HashMap<String, Estado>(){
			{
				put("=", new Estado(true, null, Token.OP_MAIOREQ));
			}
		}, Token.OP_MAIOR));
		mapEstados.put("<", new Estado(true, new HashMap<String, Estado>(){
			{
				put("=", new Estado(true, null, Token.OP_MENOREQ));
				put(">", new Estado(true, null, Token.OP_DIF));
			}
		}, Token.OP_MENOR));
		mapEstados.put(";", new Estado(true, null, Token.PONTO_VIRGULA));
		mapEstados.put(",", new Estado(true, null, Token.VIRGULA));
		mapEstados.put(".", new Estado(true, null, Token.PONTO));
		mapEstados.put(":", new Estado(true, new HashMap<String, Estado>(){
			{
				put("=", new Estado(true, null, Token.ATRIBUICAO));
			}
		}, Token.DOIS_PONTOS));
		mapEstados.put("(", new Estado(true, null, Token.LPAREN));
		mapEstados.put(")", new Estado(true, null, Token.RPAREN));
	}
	
	private static StringBuffer buffer = new StringBuffer(25);
	private static StringBuffer buffer2 = new StringBuffer(25);
	private static int linha;
	
	@FXML
	TableView<Recon> tableView;
	@FXML
	TableColumn<Recon, Integer> colCod;
	@FXML
	TableColumn<Recon, String> colToken;
	@FXML
	TableColumn<Recon, String> colDesc;
	
	final static ObservableList<Recon> data = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		colCod.setCellValueFactory(new PropertyValueFactory<Recon, Integer>("cod"));
		colToken.setCellValueFactory(new PropertyValueFactory<Recon, String>("token"));
		colDesc.setCellValueFactory(new PropertyValueFactory<Recon, String>("desc"));
		tableView.setItems(data);
	}
	
	public static void analisar(File input) throws IOException, ReconhecimentoException {
		List<String> lines = Files.readLines(input, Charsets.UTF_8);
		Scanner scanner = null;
		String cadeia;
		List<Token> tokens = new ArrayList<>(50); //Tokens reconhecidos
		try {
			for (String line : lines) {
				linha++;
				scanner = new Scanner(line);
				scanner.useDelimiter(Pattern.compile(Estado.REGEX_TOKENIZER));
				while (scanner.hasNext()) {
					cadeia = scanner.next();
					analisaCadeia(cadeia, null, tokens, true);
				}
				scanner.close();
			}
		} finally {
			if (scanner != null)
			scanner.close();
		}
	}

	private static String analisaCadeia(String cadeia, Estado estado, List<Token> tokens, boolean resetBuffer) throws ReconhecimentoException {
		String caractere;
		char[] charArray = cadeia.toCharArray();
		if (resetBuffer) {
			buffer.setLength(0);
		}
		buffer2.setLength(0);
		for (int i = 0; i < cadeia.length(); i++) {
			caractere = String.valueOf(charArray[i]);
			if (estado != null) {
				Estado e = estado.transicoes.get(caractere.toUpperCase());
				if (!resetBuffer && e == null) {
					data.add(new Recon(estado.token.cod, buffer.toString().toUpperCase(), estado.token.desc,linha));
					tokens.add(estado.token);
					buffer.setLength(0);
					e = mapEstados.get(caractere.toUpperCase());
				}
				estado = e;
			} else {
				estado = mapEstados.get(caractere.toUpperCase());
			}
			if (estado == null) {
				boolean reconhecido = false;
				if (!(reconhecido = analisaNumerico(cadeia, tokens, buffer2))) {
					if (!(reconhecido = analisaLiteral(cadeia, tokens, buffer2))) {
						reconhecido = analisaIdentificador(cadeia, tokens, buffer2);
					}
				}
				if (buffer2.length() > 0) {
					cadeia = buffer2.toString();
					break;
				} else if (!reconhecido) {
					throw new ReconhecimentoException(linha, i, cadeia);
				} else {
					return null;
				}
			} else {
				buffer.append(caractere);
			}
			if (estado.isFinal) {
				if (estado.transicoes != null && i < cadeia.length() - 1) {
					analisaCadeia(StringUtils.substring(cadeia, i+1), estado, tokens, false);
					return null;
				}
				data.add(new Recon(estado.token.cod, buffer.toString().toUpperCase(), estado.token.desc, linha));
				tokens.add(estado.token);
				cadeia = StringUtils.substring(cadeia, i+1);
				break;
			}
		}
		if (estado != null && !estado.isFinal) {
			if (!analisaNumerico(cadeia, tokens, buffer2)) {
				if (!analisaLiteral(cadeia, tokens, buffer2)) {
					analisaIdentificador(cadeia, tokens, buffer2);
				}
			}
			cadeia = buffer2.toString();
		}
		if (StringUtils.isNotBlank(cadeia)) {
			analisaCadeia(cadeia, null, tokens, true);
		}
		return null;
	}
	
	private static boolean analisaNumerico(String cadeia, List<Token> tokens, StringBuffer cadeiaTemp) throws ReconhecimentoException {
		char[] charArray = cadeia.toCharArray();
		String caractere = null;
		buffer.setLength(0);
		Estado estado = null;
		for (int i = 0; i < cadeia.length(); i++) {
			caractere = String.valueOf(charArray[i]);
			if (i == 0 && (estado = mapEstados.get(caractere.toUpperCase())) != null) {
				if (estado.token == Token.SINAL_MENOS) {
					buffer.append("-");
					continue;
				}
			}
			if (StringUtils.isNumeric(caractere.toUpperCase())) {
				buffer.append(caractere);
			} else if (buffer.length() > 0) {
				estado = mapEstados.get(caractere);
				if (estado != null) {
					if (NumberUtils.toInt(buffer.toString()) > 32767) {
						throw new ReconhecimentoException(linha, i, cadeia);
					}
					data.add(new Recon(Token.INTEIRO.cod, buffer.toString().toUpperCase(), Token.INTEIRO.desc, linha));
					tokens.add(Token.INTEIRO);
					cadeiaTemp.setLength(0);
					cadeiaTemp.append(StringUtils.substring(cadeia, i));
					return true;
				} else {
					throw new ReconhecimentoException(linha, i, cadeia);
				}
			} else return false;
		}
		if (buffer.length() > 0) {
			if (NumberUtils.toInt(buffer.toString()) > 32767) {
				throw new ReconhecimentoException(linha, 0, cadeia);
			}
			data.add(new Recon(Token.INTEIRO.cod, buffer.toString().toUpperCase(), Token.INTEIRO.desc, linha));
			tokens.add(Token.INTEIRO);
			return true;
		}
		return false;
	}
	
	private static boolean analisaLiteral(String cadeia, List<Token> tokens, StringBuffer cadeiaTemp) throws ReconhecimentoException {
		char[] charArray = cadeia.toCharArray();
		String caractere = null;
		buffer.setLength(0);
		int indexAspa2 = -1;
		for (int i = 0; i < cadeia.length(); i++) {
			caractere = String.valueOf(charArray[i]);
			if (Estado.ASPA.equals(caractere)) {
				if ((indexAspa2 = StringUtils.substring(cadeia, i+1).indexOf("\"")) - i > 255 || indexAspa2 == -1) {
					throw new ReconhecimentoException(linha, i, cadeia);
				} else {
					buffer.append(StringUtils.substring(cadeia, i, indexAspa2+2));
					data.add(new Recon(Token.LITERAL.cod, buffer.toString().toUpperCase(), Token.LITERAL.desc, linha));
					tokens.add(Token.LITERAL);
					cadeiaTemp.setLength(0);
					cadeiaTemp.append(StringUtils.substring(cadeia, indexAspa2+2));
					return true;
				}
			} else return false;
		}
		return false;
	}

	private static boolean analisaIdentificador(String cadeia, List<Token> tokens, StringBuffer cadeiaTemp) throws ReconhecimentoException {
		char[] charArray = cadeia.toCharArray();
		String caractere = null;
		buffer.setLength(0);
		for (int i = 0; i < cadeia.length(); i++) {
			caractere = String.valueOf(charArray[i]);
			if (i == 0) {
				if (StringUtils.isAlpha(caractere))
				buffer.append(caractere);
				else {
					throw new ReconhecimentoException(linha, i, cadeia);
				}
			} else if (StringUtils.isAlphanumeric(caractere)) {
				buffer.append(caractere);
			} else if (mapEstados.get(caractere.toUpperCase()) == null) {
				throw new ReconhecimentoException(linha, i, cadeia);
			} else if (buffer.length() <= 30) {
				data.add(new Recon(Token.ID.cod, buffer.toString().toUpperCase(), Token.ID.desc, linha));
				tokens.add(Token.ID);
				cadeiaTemp.setLength(0);
				cadeiaTemp.append(StringUtils.substring(cadeia, i));
				return true;
			} else { 
				throw new ReconhecimentoException(linha, i, cadeia);
			}
		}
		if (buffer.length() > 0) {
			data.add(new Recon(Token.ID.cod, buffer.toString().toUpperCase(), Token.ID.desc,linha));
			tokens.add(Token.ID);
			return true;
		}
		return false;
	}
	
	static boolean lexicoSucesso = true;
	
	@FXML
	public static void loadFile(File f) {
		data.clear();
		linha = 0;
		if (buffer != null) {
			buffer.setLength(0);
		}
		if (buffer2 != null) {
			buffer2.setLength(0);
		}
		FileChooser chooser = new FileChooser();
		//File f = chooser.showOpenDialog(null);
		if (f == null) {
			return;
		}
		try {
			//ANALISE LEXICA
			analisar(f);
			
			if(lexicoSucesso) {
				//ANALISE SINTATICA
				System.out.println("sucesso");
				System.out.println(data);
				//Sintatico sintatico = new Sintatico();
				//sintatico.analisar(data);

			}
			
		} catch (ReconhecimentoException e) {
			data.clear();
			Alert alertWindow = new Alert(AlertType.ERROR);
			alertWindow.setTitle("ERRO");
			alertWindow.setContentText(e.toString());
//			alertWindow.setHeaderText("");
			alertWindow.show();
			lexicoSucesso = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

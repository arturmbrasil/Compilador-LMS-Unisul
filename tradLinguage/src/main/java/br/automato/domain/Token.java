package br.automato.domain;

public enum Token {
	
	CADEIA_VAZIA(0, "cadeia vazia"),
	EOF(1, "Simbolo Terminal Fim de Arquivo"),
	SINAL_MAIS(2, "Operador Soma"),
	SINAL_MENOS(3, "Operador Subtração"),
	SINAL_MULT(4, "Operador de Multiplicação"),
	SINAL_DIV(5, "Operador de Divisão"),
	OP_EQ(6, "Operador Relacional de Igualdade"),
	OP_MAIOR(7, "Operador Relacional Maior"),
	OP_MAIOREQ(8, "Operador Relacional Maior Que"),
	OP_MENOR(9, "Operador Relacional Menor"),
	OP_MENOREQ(10, "Operador Relacional Menor que"),
	OP_DIF(11, "Operador Relacional Diferença"),
	ATRIBUICAO(12, "Simbolor Especiais Atribuição" ),
	DOIS_PONTOS(13, "Simbolo Especial Dois Pontos"),
	PONTO_VIRGULA(14, "Simbolo Especial Ponto e Virgula"),
	VIRGULA(15, "Simbolo Especial Virgula"),
	PONTO(16, "Simbolo Especial Ponto"),
	LPAREN(17, "Simbolo Especial Abre Parenteses"),
	RPAREN(18, "Simbolo Especial Fecha Parenteses"),
	ID(19, "Identificador"),
	INTEIRO(20, "Numeros Inteiros"),
	LITERAL(21, "String"),
	PROGRAM(22, "Palavra Reservada PROGRAM"),
	CONST(23, "Palavra Reservada CONST"),
	VAR(24, "Palavra Reservada VAR"),
	PROCEDURE(25, "Palavra Reservada PROCEDURE"),
	BEGIN(26, "Palavra Reservada BEGIN"),
	END(27, "Palavra Reservada END"),
	INTEGER(28, "Palavra Reservada INTEGER"),
	OF(29, "Palavra Reservada OF"),
	CALL(30, "Palavra Reservada CALL"),
	IF(31, "Palavra Reservada IF"),
	THEN(32, "Palavra Reservada THEN"),
	ELSE(33, "Palavra Reservada ELSE"),
	WHILE(34, "Palavra Reservada WHILE"),
	DO(35, "Palavra Reservada DO"),
	REPEAT(36, "Palavra Reservada REPEAT"),
	UNTIL(37, "Palavra Reservada UNTIL"),
	READLN(38, "Palabra Reservada READLN"),
	WRITELN(39, "Palavra Reservada WRITELN"),
	OR(40, "Palabra Reservada OR"),
	AND(41, "Palavra Reservada AND"),
	NOT(42, "Palavra Reservada NOT"),
	FOR(43, "Palavra Reservada FOR"),
	TO(44, "Palavra Reservada TO"),
	CASE(45, "Palavra Reservada CASE");
	
	public int cod;
	public String desc;
	
	private Token(int cod, String desc) {
		this.cod = cod;
		this.desc = desc;
	}
}

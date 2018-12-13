package org.duo.webcsv;


public class No {
	private String palavra;
	private String resposta;

	public No(String palavra, String resposta) {
		this.palavra = palavra;
		this.resposta = resposta;
	}
	public String getPalavra() {
		return palavra;
	}
	public void setPalavra(String palavra) {
		this.palavra = palavra;
	}
	public String getResposta() {
		return resposta;
	}
	public void setResposta(String resposta) {
		this.resposta = resposta;
	}
	@Override
	public String toString() {
		return "[" + this.palavra + " - " + this.resposta + "]";
	}
}

package br.com.moodie.model;
/**
 * Entidade que representa o usuário do sistema e suas configurações de conta.
 */
public class Usuario {
	private final String nome;
	private final int idade;
	private final PerfilCinefilo perfil;
	private boolean notificacoesAtivas = true;
	
	public Usuario(String nome, int idade) {
		this.nome = nome;
		this.idade = idade;
		this.perfil = new PerfilCinefilo();
	}
	
	public String getNome() {return nome; }
	public int getIdade() {return idade; }
	public PerfilCinefilo getPerfil() {return perfil; }
	public boolean isNotificacoesAtivas() {return notificacoesAtivas; }
	public void setNotificacoesAtivas(boolean notificacoesAtivas) {this.notificacoesAtivas = notificacoesAtivas;}
}
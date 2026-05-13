package br.com.moodie.service;

import br.com.moodie.model.Usuario;
/**
 * Serviço de comunicação externa para alertas e notificações ao usuário.
 */
public interface NotificadorPush {
	/**
	 * Dispara um alerta para o dispositivo do usuário informando sobre novas sugestões.
	 * @param usuario
	 */
	void enviar(Usuario usuario);
}

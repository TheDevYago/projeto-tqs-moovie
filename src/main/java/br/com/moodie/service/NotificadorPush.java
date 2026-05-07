package br.com.moodie.service;

import br.com.moodie.model.Usuario;

public interface NotificadorPush {
	void enviar(Usuario usuario);
}

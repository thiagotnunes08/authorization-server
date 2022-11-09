package br.com.deveficiente.desafiomercadolivreauth.config;

import br.com.deveficiente.desafiomercadolivreauth.domain.Usuario;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;


public class AuthUser extends User {

   // private static final long serialVersionUID = 1L;

    private String fullName;

    public AuthUser(Usuario usuario) {
        super(usuario.getEmail(), usuario.getSenha(), Collections.emptyList());

        this.fullName = usuario.getNome();
    }

    public String getFullName() {
        return fullName;
    }
}
package br.com.deveficiente.desafiomercadolivreauth.config;

import br.com.deveficiente.desafiomercadolivreauth.domain.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;


public class AuthUser extends User {

   // private static final long serialVersionUID = 1L;

    private String fullName;
    private Long id;

    public AuthUser(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
        super(usuario.getEmail(), usuario.getSenha(),authorities);

        this.fullName = usuario.getNome();
        this.id = usuario.getId();
    }

    public String getFullName() {
        return fullName;
    }

    public Long getId() {
        return id;
    }
}
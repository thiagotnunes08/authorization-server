package br.com.deveficiente.desafiomercadolivreauth.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToMany
    private Set<Permissao> permissoes = new HashSet<>();

    @Deprecated
    public Grupo() {
    }

    public Set<Permissao> getPermissoes() {
        return permissoes;
    }
}

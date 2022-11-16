package br.com.deveficiente.desafiomercadolivreauth.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Permissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;

    @Deprecated
    public Permissao() {
    }

    public String getNome() {
        return nome;
    }
}

package br.com.deveficiente.desafiomercadolivreauth.domain;

import javax.persistence.*;

//@Data
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Usuario {

   // @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    @Deprecated
    public Usuario() {
    }

}
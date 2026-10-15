package com.example.oficinaimpossivel.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.example.oficinaimpossivel.R;

import java.io.Serializable;

/**
 * Modelo de uma invenção exibida na Feira.
 * É Serializable apenas para sobreviver a mudanças de configuração (ex.: rotação).
 */
public class Invencao implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String nome;
    private final String inventor;
    private final String categoria;
    private final String descricao;
    @DrawableRes
    private final int imagemResId;

    public Invencao(@NonNull String nome,
                    @NonNull String inventor,
                    @NonNull String categoria,
                    @NonNull String descricao,
                    @DrawableRes int imagemResId) {
        this.nome = nome;
        this.inventor = inventor;
        this.categoria = categoria;
        this.descricao = descricao;
        this.imagemResId = imagemResId;
    }

    /** Invenções cadastradas pelo usuário sempre recebem a imagem padrão. */
    public static Invencao criarDoUsuario(@NonNull String nome,
                                          @NonNull String inventor,
                                          @NonNull String categoria,
                                          @NonNull String descricao) {
        return new Invencao(nome, inventor, categoria, descricao, R.drawable.img_invencao_padrao);
    }

    @NonNull
    public String getNome() {
        return nome;
    }

    @NonNull
    public String getInventor() {
        return inventor;
    }

    @NonNull
    public String getCategoria() {
        return categoria;
    }

    @NonNull
    public String getDescricao() {
        return descricao;
    }

    @DrawableRes
    public int getImagemResId() {
        return imagemResId;
    }
}

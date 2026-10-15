package com.example.oficinaimpossivel.util;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.Nullable;

/** Regras de validação compartilhadas pelas telas. */
public final class Validacao {

    public static final int MINIMO_CARACTERES_NOME = 3;
    public static final int MINIMO_CARACTERES_OBSERVACAO = 10;

    private Validacao() {
    }

    public static boolean temMinimo(@Nullable CharSequence texto, int minimo) {
        return texto != null && texto.toString().trim().length() >= minimo;
    }

    public static boolean nomeValido(@Nullable CharSequence texto) {
        return temMinimo(texto, MINIMO_CARACTERES_NOME);
    }

    /** Confere se o id recebido por extra aponta mesmo para uma imagem local do app. */
    public static boolean imagemValida(Context context, int imagemResId) {
        if (imagemResId == 0) {
            return false;
        }
        try {
            return "drawable".equals(context.getResources().getResourceTypeName(imagemResId));
        } catch (Resources.NotFoundException e) {
            return false;
        }
    }
}

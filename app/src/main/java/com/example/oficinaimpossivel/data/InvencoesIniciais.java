package com.example.oficinaimpossivel.data;

import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.example.oficinaimpossivel.R;
import com.example.oficinaimpossivel.model.Invencao;

import java.util.ArrayList;

/** Monta as invenções que já estão expostas quando a Feira abre. */
public final class InvencoesIniciais {

    private InvencoesIniciais() {
    }

    @NonNull
    public static ArrayList<Invencao> criar(@NonNull Context context) {
        ArrayList<Invencao> invencoes = new ArrayList<>();
        invencoes.add(criar(context,
                R.string.invencao_caldeirao_nome,
                R.string.invencao_caldeirao_inventor,
                R.string.categoria_pocoes,
                R.string.invencao_caldeirao_descricao,
                R.drawable.img_caldeirao));
        invencoes.add(criar(context,
                R.string.invencao_abobora_nome,
                R.string.invencao_abobora_inventor,
                R.string.categoria_aboboras,
                R.string.invencao_abobora_descricao,
                R.drawable.img_abobora));
        invencoes.add(criar(context,
                R.string.invencao_vassoura_nome,
                R.string.invencao_vassoura_inventor,
                R.string.categoria_transportes,
                R.string.invencao_vassoura_descricao,
                R.drawable.img_vassoura));
        invencoes.add(criar(context,
                R.string.invencao_detector_nome,
                R.string.invencao_detector_inventor,
                R.string.categoria_artefatos,
                R.string.invencao_detector_descricao,
                R.drawable.img_fantasma));
        invencoes.add(criar(context,
                R.string.invencao_morcego_nome,
                R.string.invencao_morcego_inventor,
                R.string.categoria_engenhocas,
                R.string.invencao_morcego_descricao,
                R.drawable.img_morcego));
        invencoes.add(criar(context,
                R.string.invencao_relogio_nome,
                R.string.invencao_relogio_inventor,
                R.string.categoria_engenhocas,
                R.string.invencao_relogio_descricao,
                R.drawable.img_relogio_lua));
        return invencoes;
    }

    private static Invencao criar(Context context,
                                  @StringRes int nome,
                                  @StringRes int inventor,
                                  @StringRes int categoria,
                                  @StringRes int descricao,
                                  @DrawableRes int imagem) {
        return new Invencao(
                context.getString(nome),
                context.getString(inventor),
                context.getString(categoria),
                context.getString(descricao),
                imagem);
    }
}

package com.example.oficinaimpossivel.data;

import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.example.oficinaimpossivel.R;
import com.example.oficinaimpossivel.model.Invencao;

import java.util.ArrayList;

/** Monta os projetos que já estão expostos quando a Feira de Ciências abre. */
public final class InvencoesIniciais {

    private InvencoesIniciais() {
    }

    @NonNull
    public static ArrayList<Invencao> criar(@NonNull Context context) {
        ArrayList<Invencao> invencoes = new ArrayList<>();
        invencoes.add(criar(context,
                R.string.invencao_vulcao_nome,
                R.string.invencao_vulcao_inventor,
                R.string.categoria_quimica,
                R.string.invencao_vulcao_descricao,
                R.drawable.img_vulcao));
        invencoes.add(criar(context,
                R.string.invencao_robo_nome,
                R.string.invencao_robo_inventor,
                R.string.categoria_robotica,
                R.string.invencao_robo_descricao,
                R.drawable.img_robo));
        invencoes.add(criar(context,
                R.string.invencao_foguete_nome,
                R.string.invencao_foguete_inventor,
                R.string.categoria_fisica,
                R.string.invencao_foguete_descricao,
                R.drawable.img_foguete));
        invencoes.add(criar(context,
                R.string.invencao_solar_nome,
                R.string.invencao_solar_inventor,
                R.string.categoria_energia,
                R.string.invencao_solar_descricao,
                R.drawable.img_painel_solar));
        invencoes.add(criar(context,
                R.string.invencao_hidroponia_nome,
                R.string.invencao_hidroponia_inventor,
                R.string.categoria_biologia,
                R.string.invencao_hidroponia_descricao,
                R.drawable.img_hidroponia));
        invencoes.add(criar(context,
                R.string.invencao_telescopio_nome,
                R.string.invencao_telescopio_inventor,
                R.string.categoria_astronomia,
                R.string.invencao_telescopio_descricao,
                R.drawable.img_telescopio));
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

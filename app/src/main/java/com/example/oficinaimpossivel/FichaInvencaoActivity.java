package com.example.oficinaimpossivel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oficinaimpossivel.databinding.ActivityFichaInvencaoBinding;
import com.example.oficinaimpossivel.util.ExtrasInvencao;
import com.example.oficinaimpossivel.util.TelaUtil;
import com.example.oficinaimpossivel.util.Validacao;

/** Tela 2 – Ficha da Invenção. */
public class FichaInvencaoActivity extends AppCompatActivity {

    private static final String TAG = "FichaActivity";

    private ActivityFichaInvencaoBinding binding;
    private String nome;
    private String inventor;
    private String categoria;
    private String descricao;
    private int imagemResId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TelaUtil.ativarTelaCheia(this);
        binding = ActivityFichaInvencaoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d(TAG, "onCreate");
        TelaUtil.aplicarEspacamentoDasBarras(binding.getRoot());

        if (!lerExtras(getIntent())) {
            Toast.makeText(this, R.string.erro_dados_invencao, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        exibirFicha();
        if (savedInstanceState == null) {
            binding.cardFichaInvencao.startAnimation(
                    AnimationUtils.loadAnimation(this, R.anim.entrada_ficha));
        }

        binding.btnVoltarFeira.setOnClickListener(view -> finish());
        binding.btnTestarPrototipo.setOnClickListener(view -> abrirCamaraDeTestes());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    /** @return false se algum dado obrigatório estiver ausente ou inválido. */
    private boolean lerExtras(Intent intent) {
        nome = intent.getStringExtra(ExtrasInvencao.NOME);
        inventor = intent.getStringExtra(ExtrasInvencao.INVENTOR);
        categoria = intent.getStringExtra(ExtrasInvencao.CATEGORIA);
        descricao = intent.getStringExtra(ExtrasInvencao.DESCRICAO);
        imagemResId = intent.getIntExtra(ExtrasInvencao.IMAGEM, 0);

        return Validacao.nomeValido(nome)
                && Validacao.nomeValido(inventor)
                && Validacao.nomeValido(categoria)
                && Validacao.nomeValido(descricao)
                && Validacao.imagemValida(this, imagemResId);
    }

    private void exibirFicha() {
        binding.imgInvencaoFicha.setImageResource(imagemResId);
        binding.imgInvencaoFicha.setContentDescription(getString(R.string.cd_imagem_invencao, nome));
        binding.tvNomeInvencaoFicha.setText(nome);
        binding.tvCategoriaInvencaoFicha.setText(categoria);
        binding.tvInventorFicha.setText(inventor);
        binding.tvDescricaoFicha.setText(descricao);
    }

    private void abrirCamaraDeTestes() {
        Intent intent = new Intent(this, CamaraTestesActivity.class);
        intent.putExtra(ExtrasInvencao.NOME, nome);
        intent.putExtra(ExtrasInvencao.CATEGORIA, categoria);
        intent.putExtra(ExtrasInvencao.IMAGEM, imagemResId);
        startActivity(intent);
    }
}

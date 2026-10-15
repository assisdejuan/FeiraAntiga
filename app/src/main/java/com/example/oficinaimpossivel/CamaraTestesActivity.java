package com.example.oficinaimpossivel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.oficinaimpossivel.databinding.ActivityCamaraTestesBinding;
import com.example.oficinaimpossivel.util.ExtrasInvencao;
import com.example.oficinaimpossivel.util.TelaUtil;
import com.example.oficinaimpossivel.util.Validacao;

/** Tela 3 – Câmara de Testes. */
public class CamaraTestesActivity extends AppCompatActivity {

    private static final String TAG = "CamaraTestesActivity";

    private static final String ESTADO_RESULTADO_CONFIRMADO = "estado_resultado_confirmado";

    private static final long DURACAO_SELO_MS = 650L;
    private static final float ESCALA_INICIAL_SELO = 0.2f;
    private static final float ROTACAO_INICIAL_SELO = -25f;

    private ActivityCamaraTestesBinding binding;
    private String nome;
    /** Id do RadioButton do último resultado confirmado, ou View.NO_ID. */
    @IdRes
    private int resultadoConfirmadoId = View.NO_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TelaUtil.ativarTelaCheia(this);
        binding = ActivityCamaraTestesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d(TAG, "onCreate");
        TelaUtil.aplicarEspacamentoDasBarras(binding.getRoot());

        Intent intent = getIntent();
        nome = intent.getStringExtra(ExtrasInvencao.NOME);
        String categoria = intent.getStringExtra(ExtrasInvencao.CATEGORIA);
        int imagemResId = intent.getIntExtra(ExtrasInvencao.IMAGEM, 0);
        if (!Validacao.nomeValido(nome)
                || !Validacao.nomeValido(categoria)
                || !Validacao.imagemValida(this, imagemResId)) {
            Toast.makeText(this, R.string.erro_dados_invencao, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        binding.imgInvencaoTeste.setImageResource(imagemResId);
        binding.imgInvencaoTeste.setContentDescription(getString(R.string.cd_imagem_invencao, nome));
        binding.tvNomeInvencaoTeste.setText(nome);
        binding.tvCategoriaInvencaoTeste.setText(categoria);

        binding.rgResultadoTeste.setOnCheckedChangeListener((grupo, idMarcado) ->
                binding.tvErroResultado.setVisibility(View.GONE));
        binding.btnConfirmarTeste.setOnClickListener(view -> confirmarTeste());
        binding.btnReverFicha.setOnClickListener(view -> finish());
        binding.btnEncerrarAvaliacao.setOnClickListener(view -> encerrarAvaliacao());

        if (savedInstanceState != null) {
            resultadoConfirmadoId = savedInstanceState.getInt(ESTADO_RESULTADO_CONFIRMADO, View.NO_ID);
            if (resultadoConfirmadoId != View.NO_ID) {
                mostrarSelo(false);
            }
        }
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ESTADO_RESULTADO_CONFIRMADO, resultadoConfirmadoId);
    }

    private void confirmarTeste() {
        int idMarcado = binding.rgResultadoTeste.getCheckedRadioButtonId();
        CharSequence observacao = binding.etObservacao.getText();

        boolean valido = true;
        if (idMarcado == View.NO_ID) {
            binding.tvErroResultado.setVisibility(View.VISIBLE);
            valido = false;
        } else {
            binding.tvErroResultado.setVisibility(View.GONE);
        }
        if (!Validacao.temMinimo(observacao, Validacao.MINIMO_CARACTERES_OBSERVACAO)) {
            binding.tilObservacao.setError(getString(R.string.erro_observacao));
            valido = false;
        } else {
            binding.tilObservacao.setError(null);
        }

        if (!valido) {
            binding.cardFormularioTeste.startAnimation(
                    AnimationUtils.loadAnimation(this, R.anim.tremor_erro));
            return;
        }

        resultadoConfirmadoId = idMarcado;
        TelaUtil.esconderTeclado(this);
        mostrarSelo(true);
        Toast.makeText(this, getString(R.string.msg_teste_confirmado, getString(textoDoResultado())),
                Toast.LENGTH_SHORT).show();
    }

    /** Exibe o selo do resultado; quando animado, usa ViewPropertyAnimator (escala + transparência). */
    private void mostrarSelo(boolean animar) {
        String resultado = getString(textoDoResultado());
        binding.imgSeloResultado.setImageResource(seloDoResultado());
        binding.imgSeloResultado.setContentDescription(getString(R.string.cd_selo_resultado, resultado));
        binding.tvSeloResultado.setText(getString(R.string.selo_resultado, resultado));
        binding.tvSeloResultado.setTextColor(ContextCompat.getColor(this, corDoResultado()));
        binding.layoutSeloResultado.setVisibility(View.VISIBLE);

        View selo = binding.layoutSeloResultado;
        selo.animate().cancel();
        if (!animar) {
            selo.setAlpha(1f);
            selo.setScaleX(1f);
            selo.setScaleY(1f);
            selo.setRotation(0f);
            return;
        }
        selo.setAlpha(0f);
        selo.setScaleX(ESCALA_INICIAL_SELO);
        selo.setScaleY(ESCALA_INICIAL_SELO);
        selo.setRotation(ROTACAO_INICIAL_SELO);
        selo.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .rotation(0f)
                .setDuration(DURACAO_SELO_MS)
                .setInterpolator(new OvershootInterpolator())
                .start();
        binding.scrollCamara.smoothScrollTo(0, binding.layoutSeloResultado.getBottom());
    }

    private void encerrarAvaliacao() {
        if (resultadoConfirmadoId == View.NO_ID) {
            Toast.makeText(this, R.string.erro_teste_nao_confirmado, Toast.LENGTH_SHORT).show();
            binding.btnConfirmarTeste.startAnimation(
                    AnimationUtils.loadAnimation(this, R.anim.tremor_erro));
            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(ExtrasInvencao.NOME, nome);
        intent.putExtra(ExtrasInvencao.RESULTADO, getString(textoDoResultado()));
        startActivity(intent);
    }

    // R.id não é constante em tempo de compilação no AGP 8+, por isso if/else em vez de switch.

    @StringRes
    private int textoDoResultado() {
        if (resultadoConfirmadoId == R.id.rbAprovada) {
            return R.string.resultado_aprovada;
        } else if (resultadoConfirmadoId == R.id.rbPrecisaAjustes) {
            return R.string.resultado_ajustes;
        }
        return R.string.resultado_reprovada;
    }

    @DrawableRes
    private int seloDoResultado() {
        if (resultadoConfirmadoId == R.id.rbAprovada) {
            return R.drawable.ic_selo_aprovada;
        } else if (resultadoConfirmadoId == R.id.rbPrecisaAjustes) {
            return R.drawable.ic_selo_ajustes;
        }
        return R.drawable.ic_selo_reprovada;
    }

    @ColorRes
    private int corDoResultado() {
        if (resultadoConfirmadoId == R.id.rbAprovada) {
            return R.color.selo_aprovada;
        } else if (resultadoConfirmadoId == R.id.rbPrecisaAjustes) {
            return R.color.selo_ajustes;
        }
        return R.color.selo_reprovada;
    }
}

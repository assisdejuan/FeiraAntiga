package com.example.oficinaimpossivel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BundleCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.oficinaimpossivel.adapter.InvencaoAdapter;
import com.example.oficinaimpossivel.data.InvencoesIniciais;
import com.example.oficinaimpossivel.databinding.ActivityMainBinding;
import com.example.oficinaimpossivel.model.Invencao;
import com.example.oficinaimpossivel.util.ExtrasInvencao;
import com.example.oficinaimpossivel.util.TelaUtil;
import com.example.oficinaimpossivel.util.Validacao;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

/** Tela 1 – Feira de Invenções. */
public class MainActivity extends AppCompatActivity implements InvencaoAdapter.OnInvencaoListener {

    private static final String TAG = "FeiraActivity";

    private static final String ESTADO_INVENCOES = "estado_invencoes";
    private static final String ESTADO_MODO_GRADE = "estado_modo_grade";
    private static final String ESTADO_ULTIMO_TESTE = "estado_ultimo_teste";

    private static final int POSICAO_SEM_CATEGORIA = 0;
    private static final int COLUNAS_GRADE = 2;
    private static final long DURACAO_TROCA_LAYOUT_MS = 400L;
    private static final long DURACAO_DESTAQUE_MS = 500L;

    private ActivityMainBinding binding;
    private ArrayList<Invencao> invencoes;
    private InvencaoAdapter adapter;
    private boolean modoGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TelaUtil.ativarTelaCheia(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d(TAG, "onCreate");
        TelaUtil.aplicarEspacamentoDasBarras(binding.getRoot());

        restaurarEstado(savedInstanceState);
        configurarSpinnerCategorias();
        configurarRecyclerView();

        binding.btnCadastrar.setOnClickListener(view -> cadastrarInvencao());
        binding.btnAlternarLayout.setOnClickListener(view -> alternarListaGrade());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        atualizarQuantidadePrototipos();
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

    /**
     * Chamado quando a Câmara de Testes volta para esta instância com
     * FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP (nenhuma Feira nova é criada).
     */
    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Log.d(TAG, "onNewIntent");
        mostrarUltimoTeste(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ESTADO_INVENCOES, invencoes);
        outState.putBoolean(ESTADO_MODO_GRADE, modoGrade);
        if (binding.tvUltimoTeste.getVisibility() == View.VISIBLE) {
            outState.putCharSequence(ESTADO_ULTIMO_TESTE, binding.tvUltimoTeste.getText());
        }
    }

    @SuppressWarnings("unchecked")
    private void restaurarEstado(@Nullable Bundle estado) {
        ArrayList<Invencao> salvas = null;
        if (estado != null) {
            salvas = BundleCompat.getSerializable(estado, ESTADO_INVENCOES, ArrayList.class);
            modoGrade = estado.getBoolean(ESTADO_MODO_GRADE, false);
            CharSequence ultimoTeste = estado.getCharSequence(ESTADO_ULTIMO_TESTE);
            if (ultimoTeste != null) {
                binding.tvUltimoTeste.setText(ultimoTeste);
                binding.tvUltimoTeste.setVisibility(View.VISIBLE);
            }
        }
        invencoes = salvas != null ? salvas : InvencoesIniciais.criar(this);
    }

    private void configurarSpinnerCategorias() {
        ArrayAdapter<CharSequence> categorias = ArrayAdapter.createFromResource(
                this, R.array.categorias_invencao, R.layout.item_spinner_categoria);
        categorias.setDropDownViewResource(R.layout.item_spinner_categoria_dropdown);
        binding.spCategoria.setAdapter(categorias);
        binding.spCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != POSICAO_SEM_CATEGORIA) {
                    binding.tvErroCategoria.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nada a fazer: a validação acontece ao cadastrar.
            }
        });
    }

    private void configurarRecyclerView() {
        adapter = new InvencaoAdapter(invencoes, this);
        binding.rvInvencoes.setAdapter(adapter);
        aplicarLayoutManager();
    }

    private void aplicarLayoutManager() {
        if (modoGrade) {
            binding.rvInvencoes.setLayoutManager(new GridLayoutManager(this, COLUNAS_GRADE));
            binding.btnAlternarLayout.setIconResource(R.drawable.ic_lista);
            binding.btnAlternarLayout.setText(R.string.botao_ver_lista);
            binding.btnAlternarLayout.setContentDescription(getString(R.string.cd_alternar_para_lista));
        } else {
            binding.rvInvencoes.setLayoutManager(new LinearLayoutManager(this));
            binding.btnAlternarLayout.setIconResource(R.drawable.ic_grade);
            binding.btnAlternarLayout.setText(R.string.botao_ver_grade);
            binding.btnAlternarLayout.setContentDescription(getString(R.string.cd_alternar_para_grade));
        }
        adapter.setModoGrade(modoGrade);
    }

    private void alternarListaGrade() {
        modoGrade = !modoGrade;
        aplicarLayoutManager();
        binding.rvInvencoes.setAlpha(0f);
        binding.rvInvencoes.animate()
                .alpha(1f)
                .setDuration(DURACAO_TROCA_LAYOUT_MS)
                .start();
    }

    private void cadastrarInvencao() {
        String nome = textoDe(binding.etNomeInvencao.getText());
        String inventor = textoDe(binding.etNomeInventor.getText());
        int posicaoCategoria = binding.spCategoria.getSelectedItemPosition();

        boolean valido = true;
        if (!Validacao.nomeValido(nome)) {
            binding.tilNomeInvencao.setError(getString(R.string.erro_nome_invencao));
            valido = false;
        } else {
            binding.tilNomeInvencao.setError(null);
        }
        if (!Validacao.nomeValido(inventor)) {
            binding.tilNomeInventor.setError(getString(R.string.erro_nome_inventor));
            valido = false;
        } else {
            binding.tilNomeInventor.setError(null);
        }
        if (posicaoCategoria == POSICAO_SEM_CATEGORIA) {
            binding.tvErroCategoria.setVisibility(View.VISIBLE);
            valido = false;
        } else {
            binding.tvErroCategoria.setVisibility(View.GONE);
        }

        if (!valido) {
            binding.cardFormularioCadastro.startAnimation(
                    AnimationUtils.loadAnimation(this, R.anim.tremor_erro));
            return;
        }

        String categoria = binding.spCategoria.getSelectedItem().toString();
        Invencao nova = Invencao.criarDoUsuario(nome, inventor, categoria,
                getString(R.string.descricao_padrao, inventor, categoria));

        invencoes.add(nova);
        int posicaoNova = invencoes.size() - 1;
        adapter.notifyItemInserted(posicaoNova);
        binding.rvInvencoes.smoothScrollToPosition(posicaoNova);

        limparFormulario();
        atualizarQuantidadePrototipos();
        Toast.makeText(this, getString(R.string.msg_invencao_cadastrada, nome), Toast.LENGTH_SHORT).show();
    }

    private void limparFormulario() {
        binding.etNomeInvencao.setText(null);
        binding.etNomeInventor.setText(null);
        binding.spCategoria.setSelection(POSICAO_SEM_CATEGORIA);
        TelaUtil.esconderTeclado(this);
    }

    @Override
    public void aoClicar(@NonNull Invencao invencao) {
        Intent intent = new Intent(this, FichaInvencaoActivity.class);
        intent.putExtra(ExtrasInvencao.NOME, invencao.getNome());
        intent.putExtra(ExtrasInvencao.INVENTOR, invencao.getInventor());
        intent.putExtra(ExtrasInvencao.CATEGORIA, invencao.getCategoria());
        intent.putExtra(ExtrasInvencao.DESCRICAO, invencao.getDescricao());
        intent.putExtra(ExtrasInvencao.IMAGEM, invencao.getImagemResId());
        startActivity(intent);
    }

    @Override
    public void aoClicarLongo(@NonNull Invencao invencao) {
        new MaterialAlertDialogBuilder(this)
                .setIcon(R.drawable.img_fantasma)
                .setTitle(R.string.titulo_remover)
                .setMessage(getString(R.string.mensagem_remover, invencao.getNome()))
                .setNegativeButton(R.string.botao_cancelar, null)
                .setPositiveButton(R.string.botao_remover, (dialog, which) -> removerInvencao(invencao))
                .show();
    }

    private void removerInvencao(@NonNull Invencao invencao) {
        int posicao = invencoes.indexOf(invencao);
        if (posicao == -1) {
            return;
        }
        invencoes.remove(posicao);
        adapter.notifyItemRemoved(posicao);
        atualizarQuantidadePrototipos();
        Toast.makeText(this, getString(R.string.msg_invencao_removida, invencao.getNome()),
                Toast.LENGTH_SHORT).show();
    }

    private void atualizarQuantidadePrototipos() {
        int quantidade = invencoes.size();
        binding.tvQuantidadePrototipos.setText(getResources()
                .getQuantityString(R.plurals.quantidade_prototipos, quantidade, quantidade));
    }

    private void mostrarUltimoTeste(@NonNull Intent intent) {
        String nome = intent.getStringExtra(ExtrasInvencao.NOME);
        String resultado = intent.getStringExtra(ExtrasInvencao.RESULTADO);
        if (!Validacao.nomeValido(nome) || !Validacao.nomeValido(resultado)) {
            return;
        }
        binding.tvUltimoTeste.setText(getString(R.string.ultimo_teste, nome, resultado));
        binding.tvUltimoTeste.setVisibility(View.VISIBLE);
        binding.tvUltimoTeste.setAlpha(0f);
        binding.tvUltimoTeste.setTranslationY(-binding.tvUltimoTeste.getResources()
                .getDimension(R.dimen.margem_media));
        binding.tvUltimoTeste.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(DURACAO_DESTAQUE_MS)
                .start();
    }

    @NonNull
    private static String textoDe(@Nullable CharSequence texto) {
        return texto == null ? "" : texto.toString().trim();
    }
}

package com.example.oficinaimpossivel.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oficinaimpossivel.R;
import com.example.oficinaimpossivel.databinding.ItemInvencaoGradeBinding;
import com.example.oficinaimpossivel.databinding.ItemInvencaoListaBinding;
import com.example.oficinaimpossivel.model.Invencao;

import java.util.List;

/** Adapter da vitrine: usa um layout para o modo lista e outro para o modo grade. */
public class InvencaoAdapter extends RecyclerView.Adapter<InvencaoAdapter.InvencaoViewHolder> {

    public interface OnInvencaoListener {
        void aoClicar(@NonNull Invencao invencao);

        void aoClicarLongo(@NonNull Invencao invencao);
    }

    private static final int TIPO_LISTA = 0;
    private static final int TIPO_GRADE = 1;

    private final List<Invencao> invencoes;
    private final OnInvencaoListener listener;
    private boolean modoGrade;

    public InvencaoAdapter(@NonNull List<Invencao> invencoes, @NonNull OnInvencaoListener listener) {
        this.invencoes = invencoes;
        this.listener = listener;
    }

    public void setModoGrade(boolean modoGrade) {
        if (this.modoGrade == modoGrade) {
            return;
        }
        this.modoGrade = modoGrade;
        notifyItemRangeChanged(0, getItemCount());
    }

    @Override
    public int getItemViewType(int position) {
        return modoGrade ? TIPO_GRADE : TIPO_LISTA;
    }

    @NonNull
    @Override
    public InvencaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TIPO_GRADE) {
            ItemInvencaoGradeBinding binding = ItemInvencaoGradeBinding.inflate(inflater, parent, false);
            return new InvencaoViewHolder(binding.getRoot(),
                    binding.imgInvencao, binding.tvNomeInvencao, binding.tvCategoriaInvencao);
        }
        ItemInvencaoListaBinding binding = ItemInvencaoListaBinding.inflate(inflater, parent, false);
        return new InvencaoViewHolder(binding.getRoot(),
                binding.imgInvencao, binding.tvNomeInvencao, binding.tvCategoriaInvencao);
    }

    @Override
    public void onBindViewHolder(@NonNull InvencaoViewHolder holder, int position) {
        holder.vincular(invencoes.get(position));
    }

    @Override
    public int getItemCount() {
        return invencoes.size();
    }

    class InvencaoViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imgInvencao;
        private final TextView tvNomeInvencao;
        private final TextView tvCategoriaInvencao;

        InvencaoViewHolder(@NonNull View itemView,
                           @NonNull ImageView imgInvencao,
                           @NonNull TextView tvNomeInvencao,
                           @NonNull TextView tvCategoriaInvencao) {
            super(itemView);
            this.imgInvencao = imgInvencao;
            this.tvNomeInvencao = tvNomeInvencao;
            this.tvCategoriaInvencao = tvCategoriaInvencao;

            itemView.setOnClickListener(view -> {
                int posicao = getBindingAdapterPosition();
                if (posicao != RecyclerView.NO_POSITION) {
                    listener.aoClicar(invencoes.get(posicao));
                }
            });
            itemView.setOnLongClickListener(view -> {
                int posicao = getBindingAdapterPosition();
                if (posicao == RecyclerView.NO_POSITION) {
                    return false;
                }
                listener.aoClicarLongo(invencoes.get(posicao));
                return true;
            });
        }

        void vincular(@NonNull Invencao invencao) {
            imgInvencao.setImageResource(invencao.getImagemResId());
            imgInvencao.setContentDescription(itemView.getContext()
                    .getString(R.string.cd_imagem_invencao, invencao.getNome()));
            tvNomeInvencao.setText(invencao.getNome());
            tvCategoriaInvencao.setText(invencao.getCategoria());
        }
    }
}

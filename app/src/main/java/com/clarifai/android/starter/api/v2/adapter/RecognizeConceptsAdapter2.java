package com.clarifai.android.starter.api.v2.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import clarifai2.dto.prediction.Concept;
import com.clarifai.android.starter.api.v2.R;

import java.util.ArrayList;
import java.util.List;

public class RecognizeConceptsAdapter2 extends RecyclerView.Adapter<RecognizeConceptsAdapter2.Holder> {

    @NonNull private List<Concept> concepts = new ArrayList<>();
    private Button buttons[] = null;

    public RecognizeConceptsAdapter2 setData(@NonNull List<Concept> concepts) {
        this.concepts = concepts;
        notifyDataSetChanged();

        //this.buttons[0] = (Button)findViewById(R.id.food_b0);

        return this;
    }


    @Override public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_clarification, parent, false));

    }

    @Override public void onBindViewHolder(Holder holder, int position) {
        final Concept concept = concepts.get(position);
        holder.label.setText(concept.name() != null ? concept.name() : concept.id());
        //holder.probability.setText(String.valueOf(concept.value()));
    }

    @Override public int getItemCount() {
        return concepts.size();
    }

    static final class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.label) TextView label;
        @BindView(R.id.probability) TextView probability;

        public Holder(View root) {
            super(root);
            ButterKnife.bind(this, root);
        }
    }
}

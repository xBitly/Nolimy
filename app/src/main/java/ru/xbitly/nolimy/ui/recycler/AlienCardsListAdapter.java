package ru.xbitly.nolimy.ui.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.xbitly.nolimy.R;
import ru.xbitly.nolimy.db.entities.alien.AlienCard;

public class AlienCardsListAdapter extends RecyclerView.Adapter<AlienCardsRecyclerViewHolder>{

    private List<AlienCard> alienCards;
    private final List<AlienCard> alienCardsFinal;
    private final Context context;

    public AlienCardsListAdapter(List<AlienCard> alienCards, Context context) {
        this.alienCards = alienCards;
        this.context = context;
        this.alienCardsFinal = alienCards;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.view_alien_card;
    }

    @NonNull
    @Override
    public AlienCardsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new AlienCardsRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlienCardsRecyclerViewHolder holder, int position) {
        holder.getTextName().setText(alienCards.get(position).getName());
        holder.getTextDescription().setText(alienCards.get(position).getDescription());
        if (position == getItemCount() - 1) holder.getViewLine().setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return alienCards == null ? 0 : alienCards.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void search(String text){
        List<AlienCard> newNotes = new ArrayList<>();
        text = text.toLowerCase();
        for (AlienCard alienCard : alienCardsFinal) {
            if (alienCard.getName().toLowerCase().contains(text) || alienCard.getDescription().toLowerCase().contains(text)) {
                newNotes.add(alienCard);
            }
        }
        this.alienCards = newNotes;
        notifyDataSetChanged();
    }
}

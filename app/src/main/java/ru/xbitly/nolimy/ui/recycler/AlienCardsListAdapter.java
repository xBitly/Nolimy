package ru.xbitly.nolimy.ui.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.xbitly.nolimy.R;
import ru.xbitly.nolimy.db.entities.alien.AlienCard;

public class AlienCardsListAdapter extends RecyclerView.Adapter<AlienCardsRecyclerViewHolder>{

    private List<AlienCard> alienCards;
    private final Context context;

    public AlienCardsListAdapter(List<AlienCard> alienCards, Context context) {
        this.alienCards = alienCards;
        this.context = context;
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
}

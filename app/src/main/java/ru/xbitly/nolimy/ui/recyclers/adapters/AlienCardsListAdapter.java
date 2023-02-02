package ru.xbitly.nolimy.ui.recyclers.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.xbitly.nolimy.R;
import ru.xbitly.nolimy.db.entities.alien.AlienCard;
import ru.xbitly.nolimy.ui.recyclers.holders.AlienCardsRecyclerViewHolder;

public class AlienCardsListAdapter extends RecyclerView.Adapter<AlienCardsRecyclerViewHolder> {

    private List<AlienCard> alienCards;
    private final List<AlienCard> alienCardsFinal;
    private final Context context;
    private final TextView textView;
    private boolean restored = false;

    public AlienCardsListAdapter(List<AlienCard> alienCards, Context context, TextView textView) {
        this.alienCards = alienCards;
        this.context = context;
        this.alienCardsFinal = alienCards;
        this.textView = textView;
        if (getItemCount() == 0) textView.setVisibility(View.VISIBLE);
        else textView.setVisibility(View.GONE);
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
        if (position == getItemCount() - 1)
            holder.getViewLine().setVisibility(View.INVISIBLE);
        else holder.getViewLine().setVisibility(View.VISIBLE);

        holder.getViewItem().setOnClickListener(view -> createAlienCardAlertDialog(alienCards.get(position)));
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

    @SuppressLint("NotifyDataSetChanged")
    public void removeItem(int position){
        this.alienCards.remove(position);
        notifyDataSetChanged();
        restored = false;
        if (getItemCount() == 0) textView.setVisibility(View.VISIBLE);
        else textView.setVisibility(View.GONE);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void restoreItem(AlienCard alienCard, int position) {
        this.alienCards.add(position, alienCard);
        notifyDataSetChanged();
        restored = true;
        if (getItemCount() == 0) textView.setVisibility(View.VISIBLE);
        else textView.setVisibility(View.GONE);
    }

    public boolean itemIsRestored(){
        return restored;
    }

    public List<AlienCard> getAlienCards() {
        return alienCards;
    }

    private void createAlienCardAlertDialog(AlienCard alienCard){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(dialog.getContext());
        View alienCardView = inflater.inflate(R.layout.view_alien_card_alert_dialog, null);
        dialog.setView(alienCardView);

        TextView textViewName = alienCardView.findViewById(R.id.text_name);
        TextView textViewDescription = alienCardView.findViewById(R.id.text_description);
        RecyclerView recyclerView = alienCardView.findViewById(R.id.recycler);

        textViewName.setText(alienCard.getName());
        textViewDescription.setText(alienCard.getDescription());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new CardContentListAdapter(alienCard.getContent(), context));

        AlertDialog alert = dialog.create();

        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable back = context.getDrawable(R.drawable.bg_light_gray);
        InsetDrawable inset = new InsetDrawable(back, context.getResources().getDimensionPixelSize(R.dimen.center_margin));
        alert.getWindow().setBackgroundDrawable(inset);
        alert.show();
    }
}

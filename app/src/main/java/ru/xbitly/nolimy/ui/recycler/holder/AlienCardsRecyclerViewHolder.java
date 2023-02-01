package ru.xbitly.nolimy.ui.recycler.holder;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.xbitly.nolimy.R;

public class AlienCardsRecyclerViewHolder extends RecyclerView.ViewHolder {

    private final TextView textName, textDescription;
    private final View viewLine;
    private final RelativeLayout viewItem;

    public AlienCardsRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        textName = itemView.findViewById(R.id.text_name);
        textDescription = itemView.findViewById(R.id.text_description);
        viewLine = itemView.findViewById(R.id.view_line);
        viewItem = itemView.findViewById(R.id.view_item_alien_card);
    }

    public TextView getTextName() {
        return textName;
    }

    public TextView getTextDescription() {
        return textDescription;
    }

    public View getViewLine() {
        return viewLine;
    }

    public RelativeLayout getViewItem() {
        return viewItem;
    }
}

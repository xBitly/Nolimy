package ru.xbitly.nolimy.ui.recycler.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.xbitly.nolimy.R;

public class CardContentRecyclerViewHolder extends RecyclerView.ViewHolder {

    private final TextView textTitle, textContent;

    public CardContentRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        textTitle = itemView.findViewById(R.id.text_title);
        textContent = itemView.findViewById(R.id.text_content);
    }

    public TextView getTextTitle() {
        return textTitle;
    }

    public TextView getTextContent() {
        return textContent;
    }
}
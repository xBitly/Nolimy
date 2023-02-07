package ru.xbitly.nolimy.ui.recyclers.holders;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.xbitly.nolimy.R;

public class ColorsRecyclerViewHolder extends RecyclerView.ViewHolder {

    private final RelativeLayout background;
    private final TextView text;

    public ColorsRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        background = itemView.findViewById(R.id.background);
        text = itemView.findViewById(R.id.text);
    }

    public TextView getText() {
        return text;
    }

    public RelativeLayout getBackground() {
        return background;
    }
}
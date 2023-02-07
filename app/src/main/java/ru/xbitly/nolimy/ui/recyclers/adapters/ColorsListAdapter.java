package ru.xbitly.nolimy.ui.recyclers.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.xbitly.nolimy.R;
import ru.xbitly.nolimy.ui.recyclers.holders.ColorsRecyclerViewHolder;

public class ColorsListAdapter extends RecyclerView.Adapter<ColorsRecyclerViewHolder> {

    private final int[] resIds;
    private final String[] resNames;
    private final Context context;
    private final ImageView imageView;
    private int color;

    public ColorsListAdapter(int[] resIds, String[] resNames, Context context, ImageView imageView) {
        this.resIds = resIds;
        this.resNames = resNames;
        this.context = context;
        this.imageView = imageView;
        this.color = resIds[0];
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.view_colors;
    }

    @NonNull
    @Override
    public ColorsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ColorsRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorsRecyclerViewHolder holder, int position) {
        holder.getText().setText(resNames[position]);
        holder.getBackground().setBackgroundResource(resIds[position]);
        holder.getBackground().setOnClickListener(view -> {
            imageView.setImageResource(resIds[position]);
            color = resIds[position];
        });
    }

    @Override
    public int getItemCount() {
        return resIds.length;
    }

    public int getColor(){
        return color;
    }
}
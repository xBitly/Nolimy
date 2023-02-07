package ru.xbitly.nolimy.ui.recyclers.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.xbitly.nolimy.R;
import ru.xbitly.nolimy.ui.recyclers.holders.ContentEditsRecyclerViewHolder;

public class ContentEditsListAdapter extends RecyclerView.Adapter<ContentEditsRecyclerViewHolder> {

    private List<Map.Entry<String, String>> listContent = null;
    private final Context context;
    private int count = 0;

    public ContentEditsListAdapter(Context context) {
        this.context = context;
    }

    public ContentEditsListAdapter(Map<String, String> mapContent, Context context) {
        this.context = context;
        this.listContent = new ArrayList<>(mapContent.entrySet());
        this.count = listContent.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCount(int count) {
        this.count = count;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.view_content_edits;
    }

    @NonNull
    @Override
    public ContentEditsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ContentEditsRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentEditsRecyclerViewHolder holder, int position) {
        if (listContent != null && position < listContent.size()) {
            holder.getEditTextTitle().setText(listContent.get(position).getKey());
            holder.getEditTextContent().setText(listContent.get(position).getValue());
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }
}
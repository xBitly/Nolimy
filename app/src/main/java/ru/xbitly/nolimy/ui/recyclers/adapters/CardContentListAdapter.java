package ru.xbitly.nolimy.ui.recyclers.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.xbitly.nolimy.R;
import ru.xbitly.nolimy.ui.recyclers.holders.CardContentRecyclerViewHolder;

public class CardContentListAdapter extends RecyclerView.Adapter<CardContentRecyclerViewHolder> {

    private final List<Map.Entry<String, String>> listContent;
    private final Context context;

    public CardContentListAdapter(Map<String, String> mapContent, Context context) {
        this.listContent = new ArrayList<>(mapContent.entrySet());
        this.context = context;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.view_card_content;
    }

    @NonNull
    @Override
    public CardContentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new CardContentRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardContentRecyclerViewHolder holder, int position) {
        Map.Entry<String, String> entry = listContent.get(position);

        String htmlContent  = "<u>" + entry.getValue() + "</u>";
        String textTitle = entry.getKey() + ": ";
        Spanned textContent  =  android.text.Html.fromHtml(htmlContent);

        holder.getTextTitle().setText(textTitle);
        holder.getTextContent().setText(textContent);

        holder.getTextContent().setOnClickListener(view ->
                copyToClipBoard(holder.getTextContent().getText().toString()));
    }

    @Override
    public int getItemCount() {
        return listContent == null ? 0 : listContent.size();
    }

    private void copyToClipBoard(String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Nolimy profile", text);
        clipboard.setPrimaryClip(clip);
    }
}
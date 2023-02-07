package ru.xbitly.nolimy.ui.recyclers.holders;

import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.xbitly.nolimy.R;

public class ContentEditsRecyclerViewHolder extends RecyclerView.ViewHolder {

    private final EditText editTextTitle, editTextContent;

    public ContentEditsRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        editTextTitle = itemView.findViewById(R.id.edit_text_title);
        editTextContent = itemView.findViewById(R.id.edit_text_content);
    }

    public EditText getEditTextTitle() {
        return editTextTitle;
    }

    public EditText getEditTextContent() {
        return editTextContent;
    }
}
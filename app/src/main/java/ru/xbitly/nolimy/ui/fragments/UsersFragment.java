package ru.xbitly.nolimy.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import ru.xbitly.nolimy.R;
import ru.xbitly.nolimy.db.entities.alien.AlienCardGet;
import ru.xbitly.nolimy.ui.recycler.AlienCardsListAdapter;

public class UsersFragment extends Fragment {

    private Context context;
    private AlienCardGet alienCardGet;

    public UsersFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        EditText editTextSearch = view.findViewById(R.id.edit_text_search);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new AlienCardsListAdapter(null, context));
        recyclerView.setNestedScrollingEnabled(false);

        alienCardGet = new AlienCardGet(context);
        alienCardGet.setRecyclerView(recyclerView);
        alienCardGet.execute();

        editTextSearch.addTextChangedListener(editTextSearchWatcher);
    }

    private final TextWatcher editTextSearchWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(alienCardGet.getAdapter() == null) return;
            alienCardGet.getAdapter().search(editable.toString());
        }
    };
}
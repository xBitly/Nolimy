package ru.xbitly.nolimy.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ru.xbitly.nolimy.R;
import ru.xbitly.nolimy.db.entities.my.MyCard;
import ru.xbitly.nolimy.db.entities.my.MyCardDelete;
import ru.xbitly.nolimy.db.entities.my.MyCardEdit;
import ru.xbitly.nolimy.db.entities.my.MyCardSave;
import ru.xbitly.nolimy.ui.elements.NolimySnackbar;
import ru.xbitly.nolimy.ui.recyclers.adapters.ColorsListAdapter;
import ru.xbitly.nolimy.ui.recyclers.adapters.ContentEditsListAdapter;

public class CreateActivity extends AppCompatActivity {

    private MyCard myCard;
    private boolean isSave = true;
    private boolean isEdit;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        myCard = (MyCard) getIntent().getSerializableExtra("card");
        int[] resIds = {R.drawable.bg_first_gradient_card, R.drawable.bg_second_gradient_card, R.drawable.bg_third_gradient_card};
        String[] resNames = {"Violet", "Peach", "Grass"};

        EditText editTextName = findViewById(R.id.edit_text_name);
        EditText editTextDescription = findViewById(R.id.edit_text_description);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        RecyclerView recyclerViewColors = findViewById(R.id.recycler_colors);
        ImageView imageView = findViewById(R.id.image_view);
        ImageButton buttonBack = findViewById(R.id.button_back);
        Button buttonAddContent = findViewById(R.id.button_add_content);
        Button buttonSave = findViewById(R.id.button_save);
        Button buttonDelete = findViewById(R.id.button_delete);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewColors.setHasFixedSize(true);
        recyclerViewColors.setLayoutManager(new LinearLayoutManager(this));

        ContentEditsListAdapter contentEditsListAdapter;
        ColorsListAdapter colorsListAdapter = new ColorsListAdapter(resIds, resNames, this, imageView);

        if (myCard != null) {
            contentEditsListAdapter = new ContentEditsListAdapter(myCard.getContent(), this);
            editTextName.setText(myCard.getName());
            editTextDescription.setText(myCard.getDescription());
            imageView.setImageResource(myCard.getBackground());
            buttonDelete.setVisibility(View.VISIBLE);
            buttonSave.setVisibility(View.VISIBLE);
            isEdit = true;
        } else {
            myCard = new MyCard();
            contentEditsListAdapter = new ContentEditsListAdapter(this);
            buttonDelete.setVisibility(View.GONE);
            buttonSave.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.bg_first_gradient_card);
            myCard.setBackground(R.drawable.bg_first_gradient_card);
            isEdit = false;
        }

        recyclerView.setAdapter(contentEditsListAdapter);
        recyclerViewColors.setAdapter(colorsListAdapter);

        buttonAddContent.setOnClickListener(view -> contentEditsListAdapter.setCount(contentEditsListAdapter.getItemCount() + 1));

        buttonDelete.setOnClickListener(view -> {
            MyCardDelete myCardDelete = new MyCardDelete(this, myCard);
            myCardDelete.execute();
            onBackPressed();
        });

        buttonSave.setOnClickListener(view -> {
            String name = editTextName.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();
            Map<String, String> mapContent = new HashMap<>();
            if (name.isEmpty() || description.isEmpty()) {
                notFilled(view);
                return;
            }
            for (int i = 0; i < contentEditsListAdapter.getItemCount(); i++) {
                View view1 = Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(i)).itemView;
                EditText editTextTitle = view1.findViewById(R.id.edit_text_title);
                EditText editTextContent = view1.findViewById(R.id.edit_text_content);
                String title = editTextTitle.getText().toString().trim();
                String content = editTextContent.getText().toString().trim();
                if (!title.isEmpty() && !content.isEmpty()) {
                    mapContent.put(title, content);
                } else if (!(title.isEmpty() && content.isEmpty())){
                    notFilled(view);
                    break;
                }
            }
            if(!isSave) return;
            myCard.setName(name);
            myCard.setDescription(description);
            myCard.setContent(mapContent);
            myCard.setBackground(colorsListAdapter.getColor());
            NolimySnackbar nolimySnackbar = new NolimySnackbar();
            if (isEdit){
                MyCardEdit myCardEdit = new MyCardEdit(this, myCard);
                myCardEdit.execute();
            } else {
                MyCardSave myCardSave = new MyCardSave(this, myCard);
                myCardSave.execute();
                editor.putInt("position_my_card", -1);
                editor.apply();
            }
            nolimySnackbar.createSuccessSnackbar(this, view);
            nolimySnackbar.getTextSuccess().setText(getText(R.string.saved));
            nolimySnackbar.show();
            onBackPressed();
        });

        buttonBack.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(CreateActivity.this, MainActivity.class);
        editor.putInt("fragment", R.id.profile);
        editor.apply();
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private void notFilled(View view){
        isSave = false;
        NolimySnackbar nolimySnackbar = new NolimySnackbar();
        nolimySnackbar.createErrorSnackbar(this, view);
        nolimySnackbar.getTextError().setText(getText(R.string.not_field));
        nolimySnackbar.show();
    }
}
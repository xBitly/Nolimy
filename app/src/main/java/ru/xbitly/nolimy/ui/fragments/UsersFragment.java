package ru.xbitly.nolimy.ui.fragments;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import ru.xbitly.nolimy.R;
import ru.xbitly.nolimy.db.entities.alien.AlienCard;
import ru.xbitly.nolimy.db.entities.alien.AlienCardDelete;
import ru.xbitly.nolimy.db.entities.alien.AlienCardGet;
import ru.xbitly.nolimy.ui.elements.NolimySnackbar;
import ru.xbitly.nolimy.ui.recyclers.adapters.AlienCardsListAdapter;

public class UsersFragment extends Fragment {

    private final Context context;
    private AlienCardGet alienCardGet;
    private AlienCardsListAdapter adapter;
    private AlienCard alienCard;

    private boolean snackbarIsDismissed = false;

    public UsersFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);

        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        EditText editTextSearch = view.findViewById(R.id.edit_text_search);
        TextView textViewNoCards = view.findViewById(R.id.text_no_cards);
        ImageButton imageButton =  view.findViewById(R.id.button_qrcode);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new AlienCardsListAdapter(null, context, textViewNoCards));
        recyclerView.setNestedScrollingEnabled(false);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        alienCardGet = new AlienCardGet(context, textViewNoCards);
        alienCardGet.setRecyclerView(recyclerView);
        alienCardGet.execute();

        editTextSearch.addTextChangedListener(editTextSearchWatcher);

        if (nfcAdapter == null){
            imageButton.setVisibility(View.GONE);
        }
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

    private final ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if(alienCardGet.getAdapter() == null) return;

            int position = viewHolder.getAdapterPosition();
            adapter = alienCardGet.getAdapter();
            alienCard = adapter.getAlienCards().get(position);

            if (direction == ItemTouchHelper.LEFT){
                adapter.removeItem(position);
                NolimySnackbar nolimySnackbar = new NolimySnackbar();
                nolimySnackbar.createActionSnackbar(context, requireView());
                nolimySnackbar.getButtonAction().setOnClickListener(v -> {
                    if(!snackbarIsDismissed) adapter.restoreItem(alienCard, position);
                    snackbarIsDismissed = true;
                    nolimySnackbar.dismiss();
                });
                nolimySnackbar.getTextAction().setText(getText(R.string.deleted));
                nolimySnackbar.getButtonAction().setText(getText(R.string.undo));
                nolimySnackbar.show();
                nolimySnackbar.addSnackbarCallback(snackbarCallback);
            } else {
                copyToClipBoard(alienCard.toString());
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            Bitmap icon;
            Paint paint = new Paint();
            if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                View itemView = viewHolder.itemView;
                float height = (float) itemView.getBottom() - (float) itemView.getTop();
                float width = height / 3;

                if(dX < 0) {
                    paint.setColor(context.getColor(R.color.red));
                    RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                    c.drawRect(background, paint);
                    Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_delete);
                    assert drawable != null;
                    icon = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(icon);
                    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    drawable.draw(canvas);
                    RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                    c.drawBitmap(icon, null, icon_dest, paint);
                } else {
                    paint.setColor(context.getColor(R.color.blue));
                    RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                    c.drawRect(background,paint);
                    Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_copy);
                    assert drawable != null;
                    icon = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(icon);
                    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    drawable.draw(canvas);
                    RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                    c.drawBitmap(icon, null, icon_dest, paint);
                }
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private final Snackbar.Callback snackbarCallback = new Snackbar.Callback() {
        @Override
        public void onDismissed(Snackbar snackbar, int event) {
            if(adapter == null || alienCard == null) return;
            if (!adapter.itemIsRestored()){
                AlienCardDelete alienCardDelete = new AlienCardDelete(context, alienCard);
                alienCardDelete.execute();
            }
            snackbarIsDismissed = false;
        }

        @Override
        public void onShown(Snackbar snackbar) {
        }
    };

    private void copyToClipBoard(String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Nolimy profile", text);
        clipboard.setPrimaryClip(clip);
        NolimySnackbar nolimySnackbar = new NolimySnackbar();
        nolimySnackbar.createSuccessSnackbar(context, requireView());
        nolimySnackbar.getTextSuccess().setText(getText(R.string.copied));
        nolimySnackbar.show();
    }
}
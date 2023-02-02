package ru.xbitly.nolimy.ui.elements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import ru.xbitly.nolimy.R;

public class NolimySnackbar {

    private Context context;
    private Snackbar snackbar;
    private Button buttonAction;
    private TextView textSuccess;
    private TextView textInfo;
    private TextView textAction;
    private View snackView;
    private Snackbar.SnackbarLayout layout;

    public NolimySnackbar(){}

    @SuppressLint("InflateParams")
    public void createActionSnackbar(Context context, View view){
        this.context = context;
        this.snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);
        layout = (Snackbar.SnackbarLayout) snackbar.getView();

        snackView = LayoutInflater.from(context).inflate(R.layout.view_action_snackbar, null);
        buttonAction = snackView.findViewById(R.id.button_action);
        textAction = snackView.findViewById(R.id.text_action);
    }

    @SuppressLint("InflateParams")
    public void createSuccessSnackbar(Context context, View view){
        this.context = context;
        this.snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT);
        layout = (Snackbar.SnackbarLayout) snackbar.getView();

        snackView = LayoutInflater.from(context).inflate(R.layout.view_success_snackbar, null);
        textSuccess = snackView.findViewById(R.id.text_success);

        getSnackView().setOnClickListener(view1 -> dismiss());
    }

    @SuppressLint("InflateParams")
    public void createInfoSnackbar(Context context, View view){
        this.context = context;
        this.snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT);
        layout = (Snackbar.SnackbarLayout) snackbar.getView();

        snackView = LayoutInflater.from(context).inflate(R.layout.view_info_snackbar, null);
        textInfo = snackView.findViewById(R.id.text_info);

        getSnackView().setOnClickListener(view1 -> dismiss());
    }

    public void addSnackbarCallback(Snackbar.Callback callback){
        snackbar.addCallback(callback);
    }

    public Button getButtonAction(){
        return buttonAction;
    }

    public TextView getTextAction(){
        return textAction;
    }

    public View getSnackView(){
        return snackView;
    }

    public TextView getTextSuccess(){
        return textSuccess;
    }

    public TextView getTextInfo(){
        return textInfo;
    }

    public void show(){
        layout.addView(snackView, 0);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackbar.getView().getLayoutParams();
        params.setMargins(context.getResources().getDimensionPixelOffset(R.dimen.center_margin) , context.getResources().getDimensionPixelOffset(R.dimen.center_margin), context.getResources().getDimensionPixelOffset(R.dimen.center_margin), context.getResources().getDimensionPixelOffset(R.dimen.horizontal_margin));
        snackbar.getView().setLayoutParams(params);
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        snackbar.show();
    }

    public void dismiss(){
        snackbar.dismiss();
    }
}

package ru.xbitly.nolimy.ui.pagers.adapters;

import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import ru.xbitly.nolimy.R;
public class CardsPagerAdapter extends PagerAdapter{
    private Context context;
    private int[] resIds;

    public CardsPagerAdapter(int[] resIds, Context context) {
        this.context = context;
        this.resIds = resIds;
        Log.e("r", String.valueOf(getCount()));
    }

    @Override
    public int getCount() {
        return resIds != null ? resIds.length : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.view_card_pager, container, false);

        imageView = itemView.findViewById(R.id.image_view);
        imageView.setImageResource(resIds[position]);

        //imageView.getLayoutParams().height = imageView.getLayoutParams().height;
        //imageView.requestLayout();

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
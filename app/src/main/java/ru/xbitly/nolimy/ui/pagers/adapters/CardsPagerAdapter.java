package ru.xbitly.nolimy.ui.pagers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import ru.xbitly.nolimy.R;
public class CardsPagerAdapter extends PagerAdapter{
    private final Context context;
    private final int[] resIds;

    public CardsPagerAdapter(int[] resIds, Context context) {
        this.context = context;
        this.resIds = resIds;
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

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
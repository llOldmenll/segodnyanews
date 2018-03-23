package com.oldmen.segodnyanewstest3;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class StartRecyclerHolder extends RecyclerView.ViewHolder {

    TextView smallCategory;
    TextView smallTitle;
    TextView smallDate;
    private ImageView smallImageView;
    CardView smallCardView;

    public StartRecyclerHolder(View itemView) {
        super(itemView);

        smallCategory = (TextView) itemView.findViewById(R.id.small_category_type);
        smallTitle = (TextView) itemView.findViewById(R.id.small_title);
        smallImageView = (ImageView) itemView.findViewById(R.id.small_imageView);
        smallDate = (TextView) itemView.findViewById(R.id.small_date);
        smallCardView = (CardView) itemView.findViewById(R.id.small_card_view);
    }

    public void bindStartNews(final ItemInfo itemInfo, final Context context) {

        smallCategory.setText(StartActivity.category(itemInfo.getCategory()));
        smallTitle.setText(itemInfo.getTitle());
        Picasso.get()
                .load(itemInfo.getImageUrl())
                .into(smallImageView);
        smallDate.setText(itemInfo.getDate());

    }


}

package com.oldmen.segodnyanewstest3.drawer_menu;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldmen.segodnyanewstest3.R;


public class HolderNavigation extends RecyclerView.ViewHolder {

    TextView txtCategory;
    ImageView categoryIcon;

    public HolderNavigation(View itemView) {
        super(itemView);

        txtCategory = (TextView) itemView.findViewById(R.id.txtCategory);
        categoryIcon = (ImageView) itemView.findViewById(R.id.category_icon);
    }

    public void bindCategory(String str, int imageResource) {
        txtCategory.setText(str);
        categoryIcon.setImageResource(imageResource);
    }
}

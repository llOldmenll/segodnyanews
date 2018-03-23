package com.oldmen.segodnyanewstest3.drawer_menu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldmen.segodnyanewstest3.CategoryIcon;
import com.oldmen.segodnyanewstest3.CategoryState;
import com.oldmen.segodnyanewstest3.OnMenuDrawerItemClickListener;
import com.oldmen.segodnyanewstest3.R;
import com.oldmen.segodnyanewstest3.RecyclerCategoriesState;
import com.oldmen.segodnyanewstest3.StartActivity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class AdapterNavigation extends RecyclerView.Adapter<HolderNavigation> {

    private List<String> categoryNameArray = new ArrayList<>();
    private TextView lastClickedCategory;
    private ImageView lastClickedIcon;
    private int lastClickedPosition;
    private CategoryIcon lastIcon;
    private Context context;
    private OnMenuDrawerItemClickListener itemClicked;
    private boolean isItFirstAppStart;
    private Realm realm = Realm.getDefaultInstance();
    private RealmResults<CategoryIcon> categoryIcons = realm.where(CategoryIcon.class).findAllAsync();
    private Typeface custom_font;


    public int getLastClickedPosition() {
        return lastClickedPosition;
    }

    public CategoryIcon getLastIcon() {
        return lastIcon;
    }

    public AdapterNavigation(List<String> categoryNameArray, Context context, boolean isItFirstAppStart, RecyclerCategoriesState categoriesState) {
        this.categoryNameArray = categoryNameArray;
        this.context = context;
        this.isItFirstAppStart = isItFirstAppStart;
        itemClicked = (OnMenuDrawerItemClickListener) this.context;

        if (categoriesState != null && categoriesState.getCategoryIcon() != null) {
            lastClickedPosition = categoriesState.getPosition();
            lastIcon = categoriesState.getCategoryIcon();
        }
    }

    @Override
    public HolderNavigation onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.drawer_item, parent, false);
        return new HolderNavigation(view);
    }

    @Override
    public void onBindViewHolder(HolderNavigation holder, final int position) {
        final CategoryIcon currentIcon = categoryIcons.get(position);
        final String categoryName = categoryNameArray.get(position);
        custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Futura.ttc");
        final TextView txtC = holder.txtCategory;
        holder.txtCategory.setTypeface(custom_font, Typeface.BOLD);
        final ImageView imgC = holder.categoryIcon;

        if (!isItFirstAppStart) {
            final CategoryState categoryState = StartActivity.currentCategoryStateList.get(position);
            holder.bindCategory(categoryName, categoryState.getImgSrc());
            txtC.setTextColor(categoryState.getTxtColorRes());
            System.out.println(categoryState.getTxtColorRes());
            if (lastClickedPosition == position) {
                lastClickedIcon = imgC;
                lastClickedCategory = txtC;
            }
        }

        if (isItFirstAppStart) {

            if (categoryName.equals("Последние новости")) {
                holder.bindCategory(categoryName, currentIcon.getIconBlue());
                txtC.setTextColor(Color.BLUE);
                lastClickedCategory = txtC;
                lastClickedIcon = imgC;
                lastIcon = currentIcon;
                lastClickedPosition = 0;
            } else
                holder.bindCategory(categoryName, currentIcon.getIconBlack());
        }


        txtC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lastClickedCategory != null && lastClickedCategory != txtC) {
                    lastClickedCategory.setTextColor(Color.GRAY);
                    lastClickedIcon.setImageResource(lastIcon.getIconGray());
                    StartActivity.currentCategoryStateList.get(lastClickedPosition)
                            .setImgSrc(lastIcon.getIconGray());
                    StartActivity.currentCategoryStateList.get(lastClickedPosition)
                            .setTxtColorRes(Color.GRAY);
                }

                txtC.setTextColor(Color.BLUE);
                imgC.setImageResource(currentIcon.getIconBlue());
                StartActivity.currentCategoryStateList.get(position)
                        .setImgSrc(currentIcon.getIconBlue());
                StartActivity.currentCategoryStateList.get(position)
                        .setTxtColorRes(Color.BLUE);

                lastClickedCategory = txtC;
                lastClickedIcon = imgC;
                lastIcon = currentIcon;
                lastClickedPosition = position;

                itemClicked.onItemClicked(categoryName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryNameArray.size();
    }

}

package com.oldmen.segodnyanewstest3;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class StartRecyclerAdapter extends RecyclerView.Adapter<StartRecyclerHolder> {

    private ArrayList<ItemInfo> itemArray = new ArrayList<>();
    private Context context;
    private OnCardClickListener cardClick;

    public StartRecyclerAdapter(ArrayList<ItemInfo> itemArray, Context context, OnCardClickListener cardClick) {
        this.itemArray = itemArray;
        this.context = context;
        this.cardClick = cardClick;
    }

    @Override
    public StartRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_start_activity_item, parent, false);
        return new StartRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(StartRecyclerHolder holder, final int position) {

        holder.bindStartNews(itemArray.get(position + 3), context);
        holder.smallTitle.setMaxLines(context.getResources().getInteger(R.integer.lines_number));

        final CardView clickedCardView = holder.smallCardView;

        clickedCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardClick.cardClicked(itemArray.get(position + 3));
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemArray.size() - 3;
    }


}

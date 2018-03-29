package com.tokopedia.topads.sdk.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.view.adapter.factory.FeedNewAdapterTypeFactory;
import com.tokopedia.topads.sdk.view.adapter.viewholder.feednew.ShopFeedNewViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by milhamj on 29/03/18.
 */

public class FeedNewAdsItemAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Item> list;
    private FeedNewAdapterTypeFactory typeFactory;
    private int clickPosition;
    private int adapterPosition;

    public FeedNewAdsItemAdapter(Context context) {
        this.list = new ArrayList<>();
        this.typeFactory = new FeedNewAdapterTypeFactory(context, clickPosition);
    }

    public void setList(List<Item> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addItem(Item item){
        this.list.add(item);
        notifyDataSetChanged();
    }

    public Item getItem(int position) {
        return list.get(position);
    }

    public void setItemClickListener(LocalAdsClickListener itemClickListener) {
        typeFactory.setItemClickListener(itemClickListener);
        notifyDataSetChanged();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder((ViewGroup) view, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(list.get(position));
        if (holder instanceof ShopFeedNewViewHolder) {
            ((ShopFeedNewViewHolder) holder).setAdapterPosition(adapterPosition);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void clearData() {
        list.clear();
    }

    public void setPosition(int adapterPosition) {
        typeFactory.setClickPosition(adapterPosition);
    }

    public void setAdapterPosition(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }
}

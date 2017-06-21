package com.tokopedia.topads.sdk.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.data.ModelConverter;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.adapter.factory.AdsAdapterTypeFactory;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by errysuprayogi on 3/29/17.
 */

public class AdsItemAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Item> list;
    private AdsAdapterTypeFactory typeFactory;
    private int clickPosition;

    public AdsItemAdapter(Context context) {
        this.list = new ArrayList<>();
        this.typeFactory = new AdsAdapterTypeFactory(context, clickPosition);
    }

    public void setList(List<Item> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addItem(Item item){
        this.list.add(item);
        notifyDataSetChanged();
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void switchDisplayMode(final DisplayMode displayMode) {
        ModelConverter.convertList(list, displayMode);
    }

    public void clearData() {
        list.clear();
    }

    public void setPosition(int adapterPosition) {
        typeFactory.setClickPosition(adapterPosition);
    }
}

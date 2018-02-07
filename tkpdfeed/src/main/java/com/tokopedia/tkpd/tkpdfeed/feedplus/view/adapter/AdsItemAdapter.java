package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.topads.AdsTypeFactoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.topads.ShopFeedViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.LocalAdsClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milhamj on 18/01/18.
 */

public class AdsItemAdapter  extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> list;
    private AdsTypeFactoryImpl typeFactory;
    private int clickPosition;
    private int adapterPosition;

    public AdsItemAdapter(Context context) {
        this.list = new ArrayList<>();
        this.typeFactory = new AdsTypeFactoryImpl(context, clickPosition);
    }

    public void setList(List<Visitable> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addItem(Visitable item){
        this.list.add(item);
        notifyDataSetChanged();
    }

    public Visitable getItem(int position) {
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
        if (holder instanceof ShopFeedViewHolder) {
            ((ShopFeedViewHolder) holder).setAdapterPosition(adapterPosition);
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

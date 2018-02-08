package com.tokopedia.ride.history.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.ride.history.view.adapter.factory.RideHistoryTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 4/11/17.
 */

public class RideHistoryAdapter extends RecyclerView.Adapter<AbstractViewHolder> {
    private List<Visitable> mVisitables;
    private final RideHistoryTypeFactory typeFactory;

    public RideHistoryAdapter(RideHistoryTypeFactory rideHistoryAdapterTypeFactory) {
        this.typeFactory = rideHistoryAdapterTypeFactory;
        mVisitables = new ArrayList<>();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(mVisitables.get(position));
    }

    @Override
    public int getItemCount() {
        return mVisitables.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getItemViewType(int position) {
        return mVisitables.get(position).type(typeFactory);
    }


    public void setElement(List<Visitable> data) {
        mVisitables = data;
        notifyDataSetChanged();
    }

    public void addElement(Visitable visitable) {
        mVisitables.add(visitable);
        notifyDataSetChanged();
    }

    public void addElements(List<Visitable> data) {
        mVisitables.addAll(data);
        notifyDataSetChanged();
    }

    public List<Visitable> getElements() {
        return mVisitables;
    }

    public void setChangedItem(int position, Visitable visitable) {
        if (position < mVisitables.size()) {
            mVisitables.remove(position);
            mVisitables.add(position, visitable);
            notifyDataSetChanged();
        }
    }

    public void removeItem(int position) {
        mVisitables.remove(position);
        notifyDataSetChanged();
    }

    public void clearData() {
        mVisitables.clear();
    }
}

package com.tokopedia.ride.bookingride.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.ride.bookingride.view.adapter.factory.RideProductTypeFactory;

import java.util.Collections;
import java.util.List;

/**
 * Created by alvarisi on 3/16/17.
 */

public class RideProductAdapter extends RecyclerView.Adapter<AbstractViewHolder> {
    private List<Visitable> mVisitables;
    private final RideProductTypeFactory typeFactory;

    public RideProductAdapter(RideProductTypeFactory typeFactory) {
        this.typeFactory = typeFactory;
        mVisitables = Collections.emptyList();
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

    public void removeItem(Visitable visitable) {
        mVisitables.remove(visitable);
        notifyDataSetChanged();
    }

    public void clearData() {
        mVisitables.clear();
    }
}

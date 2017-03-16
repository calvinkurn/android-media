package com.tokopedia.ride.bookingride.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.ride.bookingride.view.adapter.factory.PlaceAutoCompleteTypeFactory;

import java.util.Collections;
import java.util.List;

/**
 * Created by alvarisi on 3/15/17.
 */

public class PlaceAutoCompleteAdapter extends RecyclerView.Adapter<AbstractViewHolder> {
    private List<Visitable> mVisitables;
    private final PlaceAutoCompleteTypeFactory typeFactory;

    public PlaceAutoCompleteAdapter(PlaceAutoCompleteTypeFactory typeFactory) {
        this.typeFactory = typeFactory;
        mVisitables = Collections.emptyList();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(mVisitables.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getItemViewType(int position) {
        return mVisitables.get(position).type(typeFactory);
    }

    public void setElement(int position, Visitable element) {
        mVisitables.set(position, element);
        notifyItemInserted(position);
    }

    public void setElement(List<Visitable> data) {
        mVisitables.addAll(data);
        notifyDataSetChanged();
    }

    public void clearData() {
        mVisitables.clear();
    }
}

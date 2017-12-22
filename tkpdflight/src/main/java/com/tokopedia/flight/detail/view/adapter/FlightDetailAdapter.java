package com.tokopedia.flight.detail.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseAdapter;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

import java.util.List;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailAdapter extends BaseAdapter {
    private FlightDetailRouteTypeFactory flightDetailRouteTypeFactory;

    public FlightDetailAdapter(FlightDetailRouteTypeFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
        flightDetailRouteTypeFactory = adapterTypeFactory;
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return flightDetailRouteTypeFactory.createViewHolder(view, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(visitables.get(position));
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getItemViewType(int position) {
        return visitables.get(position).type(flightDetailRouteTypeFactory);
    }

}

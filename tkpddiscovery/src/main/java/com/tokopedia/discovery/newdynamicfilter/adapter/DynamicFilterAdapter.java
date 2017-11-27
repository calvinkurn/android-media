package com.tokopedia.discovery.newdynamicfilter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.discovery.newdynamicfilter.adapter.typefactory.DynamicFilterTypeFactory;
import com.tokopedia.discovery.newdynamicfilter.adapter.viewholder.DynamicFilterViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 8/11/17.
 */

public class DynamicFilterAdapter extends RecyclerView.Adapter<DynamicFilterViewHolder>{

    private List<Filter> list = new ArrayList<>();
    private DynamicFilterTypeFactory typeFactory;

    public DynamicFilterAdapter(DynamicFilterTypeFactory typeFactory) {
        this.typeFactory = typeFactory;
    }

    @Override
    public DynamicFilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(DynamicFilterViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return typeFactory.type(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setFilterList(List<Filter> filterList) {
        list = filterList;
        notifyDataSetChanged();
    }

    public List<Filter> getFilterList() {
        return list;
    }

    public int getItemPosition(Filter filter) {
        return list.indexOf(filter);
    }
}

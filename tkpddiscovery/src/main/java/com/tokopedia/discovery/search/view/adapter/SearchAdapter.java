package com.tokopedia.discovery.search.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.search.view.adapter.factory.SearchTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author erry on 23/02/17.
 */

public class SearchAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> list;
    private final SearchTypeFactory typeFactory;

    public SearchAdapter(SearchTypeFactory typeFactory) {
        this.typeFactory = typeFactory;
        this.list = new ArrayList<>();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<Visitable> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(Visitable visitable){
        this.list.add(visitable);
        notifyItemInserted(list.size());
    }

    public void addAll(List<Visitable> list) {
        int positionStart = this.list.size();
        this.list.addAll(list);
        notifyItemRangeInserted(positionStart, list.size());
    }

    public void clearData() {
        this.list.clear();
        notifyDataSetChanged();
    }

}

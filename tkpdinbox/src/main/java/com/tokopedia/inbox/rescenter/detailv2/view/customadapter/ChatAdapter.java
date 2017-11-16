package com.tokopedia.inbox.rescenter.detailv2.view.customadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.rescenter.detailv2.view.typefactory.DetailChatTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 17/10/17.
 */

public class ChatAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> list = new ArrayList<>();
    private DetailChatTypeFactory typeFactory;

    public ChatAdapter(DetailChatTypeFactory typeFactory) {
        list = new ArrayList<>();
        this.typeFactory = typeFactory;
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

    public void addAllItems(List<Visitable> items) {
        this.list.addAll(items);
    }

    public void addItem(Visitable item) {
        this.list.add(item);
    }

    public void clearData() {
        this.list.clear();
        notifyDataSetChanged();
    }
    public void deleteLastItem() {
        this.list.remove(this.list.size()-1);
    }
}

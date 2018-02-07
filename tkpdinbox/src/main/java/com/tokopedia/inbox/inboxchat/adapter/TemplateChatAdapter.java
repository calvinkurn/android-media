package com.tokopedia.inbox.inboxchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.viewmodel.TemplateChatModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevenfredian on 11/29/17.
 */

public class TemplateChatAdapter extends RecyclerView.Adapter<AbstractViewHolder>{

    private final TemplateChatTypeFactory typeFactory;
    private List<Visitable> list;

    public TemplateChatAdapter(TemplateChatTypeFactory typeFactory) {
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
        holder.bind(list.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<Visitable> getList() {
        return list;
    }

    public void setList(List<Visitable> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void update(ArrayList<String> strings) {
        list.clear();
        for (int i = 0; i < strings.size(); i++) {
            list.add(new TemplateChatModel(strings.get(i)));
        }
        list.add(new TemplateChatModel(false));
        notifyDataSetChanged();
    }
}

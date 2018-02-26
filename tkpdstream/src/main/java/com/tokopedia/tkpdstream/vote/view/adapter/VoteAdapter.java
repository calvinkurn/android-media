package com.tokopedia.tkpdstream.vote.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdstream.vote.view.adapter.typefactory.VoteTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class VoteAdapter extends RecyclerView.Adapter<AbstractViewHolder>{

    private List<Visitable> list;
    private VoteTypeFactory typeFactory;
    private EmptyModel emptyModel;
    private LoadingModel loadingModel;


    public static VoteAdapter createInstance(VoteTypeFactory voteTypeFactory) {
        return new VoteAdapter(voteTypeFactory);
    }
    
    public VoteAdapter(VoteTypeFactory typeFactory) {
        this.list = new ArrayList<>();
        this.typeFactory = typeFactory;
        this.emptyModel = new EmptyModel();
        this.loadingModel = new LoadingModel();
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

    public void addList(List<Visitable> listChat) {
        this.list.addAll(listChat);
        notifyDataSetChanged();
    }
}

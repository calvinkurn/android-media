package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.feeddetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.model.LoadingModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 5/18/17.
 */

public class DetailFeedAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> list;
    private EmptyModel emptyModel;
    private LoadingModel loadingModel;
    private final FeedPlusDetailTypeFactory typeFactory;

    public DetailFeedAdapter(FeedPlusDetailTypeFactory typeFactory) {
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
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }

    public void addList(ArrayList<Visitable> list) {
        this.list.addAll(list);
    }

    public void add(Visitable item) {
        this.list.add(item);
    }

    public void showEmpty() {
        this.list.add(emptyModel);
        notifyDataSetChanged();
    }

    public void dismissEmpty() {
        this.list.remove(emptyModel);
        notifyDataSetChanged();
    }

    public void showLoading() {
        this.list.add(loadingModel);
        notifyDataSetChanged();
    }

    public void dismissLoading() {
        this.list.remove(loadingModel);
        notifyDataSetChanged();
    }

    public boolean isLoading() {
        return this.list.contains(loadingModel);
    }

    public List<Visitable> getList() {
        return list;
    }
}

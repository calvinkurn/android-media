package com.tokopedia.inbox.rescenter.detailv2.view.customadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.model.LoadingModel;
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
    private LoadingModel loadingModel;
    private boolean isChatLoadingShown = false;

    public ChatAdapter(DetailChatTypeFactory typeFactory) {
        list = new ArrayList<>();
        this.typeFactory = typeFactory;
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

    public void addAllItems(List<Visitable> items) {
        this.list.addAll(items);
    }

    public void addItem(Visitable item) {
        this.list.add(item);
    }

    public void addAllItemsOnPosition(int position, List<Visitable> items) {
        this.list.addAll(position, items);
        notifyItemRangeInserted(position, items.size());
    }

    public void addItemOnPosition(Visitable item, int position) {
        this.list.add(position, item);
        notifyItemInserted(position);
    }

    public void removeItemOnPosition(int position) {
        this.list.remove(position);
        notifyItemRemoved(position);
    }

    public void showLoading() {
        if (!isChatLoadingShown) {
            addItemOnPosition(loadingModel, 0);
            isChatLoadingShown = true;
        }
    }

    public void removeLoading() {
        if (isChatLoadingShown) {
            removeItemOnPosition(0);
            isChatLoadingShown = false;
        }
    }

    public void clearData() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public void deleteLastItem() {
        this.list.remove(this.list.size() - 1);
        notifyItemRemoved(this.list.size() - 1);
    }
}

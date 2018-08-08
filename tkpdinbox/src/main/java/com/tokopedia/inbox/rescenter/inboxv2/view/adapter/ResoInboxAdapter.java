package com.tokopedia.inbox.rescenter.inboxv2.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.rescenter.inboxv2.view.adapter.typefactory.ResoInboxTypeFactory;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.FilterListViewModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfsx on 01/02/18.
 */

public class ResoInboxAdapter extends RecyclerView.Adapter<AbstractViewHolder> {
    private List<Visitable> list;
    private final ResoInboxTypeFactory typeFactory;
    private EmptyModel emptyModel;
    private LoadingModel loadingModel;
    private boolean canLoadMore;

    public ResoInboxAdapter(ResoInboxTypeFactory typeFactory) {
        this.typeFactory = typeFactory;
        this.list = new ArrayList<>();
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
        return  list.size();
    }

    public void setList(List<Visitable> list) {
        this.list = list;
    }

    public void addList(List<Visitable> list) {
        this.list.addAll(list);

    }

    public void clearData() {
        this.list.clear();
    }

    public void showEmpty() {
        this.list.add(emptyModel);
    }

    public void removeEmpty() {
        this.list.remove(emptyModel);
    }

    public void showLoading() {
        this.list.add(loadingModel);
    }

    public void removeLoading() {
        this.list.remove(loadingModel);
    }

    public boolean isLoading() {
        return this.list.contains(loadingModel);
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    public boolean isCanLoadMore() {
        return canLoadMore;
    }

    public List<Visitable> getlist() {
        return list;
    }


    public void addItem(Visitable item) {
        this.list.add(item);
    }

    public void updateSingleInboxItem(InboxItemViewModel model) {
        int pos = 0;
        for (Visitable item : list) {
            if (item instanceof InboxItemViewModel) {
                if (((InboxItemViewModel) item).getId() == model.getId()) {
                    ((InboxItemViewModel) item).updateItem(model);
                    notifyDataSetChanged();
                    break;
                }
            }
            pos++;
        }
    }

    public void updateQuickFilter(FilterListViewModel filterListModel) {
        if (list.get(0) instanceof FilterListViewModel) {
            FilterListViewModel newModel = new FilterListViewModel(filterListModel.getFilterList());
            list.remove(0);
            list.add(0, newModel);
            notifyItemChanged(0);
        }
    }
}

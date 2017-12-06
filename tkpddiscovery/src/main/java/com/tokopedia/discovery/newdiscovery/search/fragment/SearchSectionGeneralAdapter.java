package com.tokopedia.discovery.newdiscovery.search.fragment;

import android.support.v7.widget.RecyclerView;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.model.LoadingModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.HeaderViewModel;

import java.util.List;

/**
 * Created by henrypriyono on 10/16/17.
 */

public abstract class SearchSectionGeneralAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private OnItemChangeView itemChangeView;
    public EmptyModel emptyModel;

    public SearchSectionGeneralAdapter(OnItemChangeView itemChangeView) {
        this.itemChangeView = itemChangeView;
        emptyModel = new EmptyModel();
    }

    public void changeListView() {
        getTypeFactory().setRecyclerViewItem(TkpdState.RecyclerView.VIEW_PRODUCT);
        itemChangeView.onChangeList();
    }

    public void changeDoubleGridView() {
        getTypeFactory().setRecyclerViewItem(TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2);
        itemChangeView.onChangeDoubleGrid();
    }

    public void changeSingleGridView() {
        getTypeFactory().setRecyclerViewItem(TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1);
        itemChangeView.onChangeSingleGrid();
    }


    public int getTitleTypeRecyclerView() {
        switch (getTypeFactory().getRecyclerViewItem()) {
            case TkpdState.RecyclerView.VIEW_PRODUCT:
                return R.string.list;
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2:
                return R.string.grid;
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1:
                return R.string.grid;
            default:
                return R.string.grid;
        }
    }

    public int getIconTypeRecyclerView() {
        switch (getTypeFactory().getRecyclerViewItem()) {
            case TkpdState.RecyclerView.VIEW_PRODUCT:
                return R.drawable.ic_list;
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2:
                return R.drawable.ic_grid_default;
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1:
                return R.drawable.ic_grid_box;
            default:
                return R.drawable.ic_grid_default;
        }
    }

    public BrowseProductRouter.GridType getCurrentLayoutType() {
        switch (getTypeFactory().getRecyclerViewItem()) {
            case TkpdState.RecyclerView.VIEW_PRODUCT:
                return BrowseProductRouter.GridType.GRID_1;
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2:
                return BrowseProductRouter.GridType.GRID_2;
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1:
                return BrowseProductRouter.GridType.GRID_3;
            default:
                return BrowseProductRouter.GridType.GRID_1;
        }
    }

    public void showEmpty() {
        getItemList().add(emptyModel);
        notifyDataSetChanged();
    }

    public void removeEmpty() {
        getItemList().remove(emptyModel);
        notifyDataSetChanged();
    }

    public boolean isLoading(int position) {
        return checkDataSize(position) && getItemList().get(position) instanceof LoadingModel;
    }


    public boolean isHeaderBanner(int position) {
        if (checkDataSize(position))
            return getItemList().get(position) instanceof HeaderViewModel;
        return false;
    }

    public boolean isEmptyItem(int position) {
        return checkDataSize(position) && getItemList().get(position) instanceof EmptyModel;
    }

    public void clearData() {
        getItemList().clear();
    }

    public boolean isListEmpty() {
        return getItemList().isEmpty();
    }

    protected boolean checkDataSize(int position) {
        return getItemList() != null && getItemList().size() > 0
                && position > -1 && position < getItemList().size();
    }

    public abstract List<Visitable> getItemList();

    protected abstract SearchSectionTypeFactory getTypeFactory();



    public interface OnItemChangeView {
        void onChangeList();

        void onChangeDoubleGrid();

        void onChangeSingleGrid();
    }
}

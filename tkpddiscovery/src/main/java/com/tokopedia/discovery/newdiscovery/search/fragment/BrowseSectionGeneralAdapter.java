package com.tokopedia.discovery.newdiscovery.search.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.model.LoadingModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.EmptySearchModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.HeaderViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.RelatedSearchModel;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterFlagSelectedModel;

import java.util.List;

/**
 * Created by henrypriyono on 10/16/17.
 */

public abstract class BrowseSectionGeneralAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private OnItemChangeView itemChangeView;
    public EmptyModel emptyModel;

    public BrowseSectionGeneralAdapter(OnItemChangeView itemChangeView) {
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
                return BrowseProductRouter.GridType.GRID_2;
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

    public boolean isRelatedSearch(int position) {
        if (checkDataSize(position))
            return getItemList().get(position) instanceof RelatedSearchModel;
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

    public void showEmptyState(Context context, String query, boolean isFilterActive,
                               FilterFlagSelectedModel filterFlagSelectedModel, String sectionTitle) {
        clearData();
        getItemList().add(mappingEmptySearch(context, query, isFilterActive, filterFlagSelectedModel, sectionTitle));
        notifyDataSetChanged();
    }

    protected EmptySearchModel mappingEmptySearch(Context context, String query, boolean isFilterActive,
                                                FilterFlagSelectedModel filterFlagSelectedModel, String sectionTitle) {
        EmptySearchModel emptySearchModel = new EmptySearchModel();
        emptySearchModel.setImageRes(R.drawable.ic_empty_search);
        if (isFilterActive) {
            emptySearchModel.setTitle(getEmptySearchTitle(context, sectionTitle));
            emptySearchModel.setContent(String.format(context.getString(R.string.msg_empty_search_with_filter_2), query));
            emptySearchModel.setFilterFlagSelectedModel(filterFlagSelectedModel);
        } else {
            emptySearchModel.setTitle(getEmptySearchTitle(context, sectionTitle));
            emptySearchModel.setContent(String.format(context.getString(R.string.empty_search_content_template), query));
            emptySearchModel.setButtonText(context.getString(R.string.empty_search_button_text));
        }
        return emptySearchModel;
    }

    private String getEmptySearchTitle(Context context, String sectionTitle) {
        String templateText = context.getString(R.string.msg_empty_search_with_filter_1);
        return String.format(templateText, sectionTitle);
    }

    public abstract List<Visitable> getItemList();

    protected abstract BrowseSectionTypeFactory getTypeFactory();



    public interface OnItemChangeView {
        void onChangeList();

        void onChangeDoubleGrid();

        void onChangeSingleGrid();
    }
}

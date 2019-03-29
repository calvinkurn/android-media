package com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.model.LoadingModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionGeneralAdapter;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionTypeFactory;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.EmptySearchModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.typefactory.ShopListTypeFactory;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.viewmodel.ShopViewModel;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterFlagSelectedModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 10/13/17.
 */

public class ShopListAdapter extends SearchSectionGeneralAdapter {
    private List<Visitable> list = new ArrayList<>();
    private LoadingModel loadingModel;
    private ShopListTypeFactory typeFactory;

    public ShopListAdapter(OnItemChangeView itemChangeView, ShopListTypeFactory typeFactory) {
        super(itemChangeView);
        this.typeFactory = typeFactory;
        loadingModel = new LoadingModel();
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

    public void appendItems(List<ShopViewModel.ShopItem> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addLoading() {
        this.list.add(loadingModel);
        notifyDataSetChanged();
    }

    public void removeLoading() {
        this.list.remove(loadingModel);
        notifyDataSetChanged();
    }

    public void setFavoriteButtonEnabled(int adapterPosition, boolean isEnabled) {
        if (list.get(adapterPosition) instanceof ShopViewModel.ShopItem) {
            ((ShopViewModel.ShopItem) list.get(adapterPosition)).setFavoriteButtonEnabled(isEnabled);
            notifyItemChanged(adapterPosition);
        }
    }

    public void updateFavoritedStatus(boolean targetFavoritedStatus, int adapterPosition) {
        if (list.get(adapterPosition) instanceof ShopViewModel.ShopItem) {
            ((ShopViewModel.ShopItem) list.get(adapterPosition)).setFavorited(targetFavoritedStatus);
            notifyItemChanged(adapterPosition);
        }
    }

    public boolean isShopItem(int position) {
        return position < list.size() && list.get(position) instanceof ShopViewModel.ShopItem;
    }

    @Override
    public List<Visitable> getItemList() {
        return list;
    }

    @Override
    protected SearchSectionTypeFactory getTypeFactory() {
        return typeFactory;
    }

    @Override
    public boolean isEmptyItem(int position) {
        return checkDataSize(position) && getItemList().get(position) instanceof EmptySearchModel;
    }

    @Override
    public int getIconTypeRecyclerView() {
        switch (getTypeFactory().getRecyclerViewItem()) {
            case TkpdState.RecyclerView.VIEW_PRODUCT:
                return R.drawable.ic_list_green;
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2:
                return R.drawable.ic_grid_default_green;
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1:
                return R.drawable.ic_grid_box_green;
            default:
                return R.drawable.ic_grid_default_green;
        }
    }
}

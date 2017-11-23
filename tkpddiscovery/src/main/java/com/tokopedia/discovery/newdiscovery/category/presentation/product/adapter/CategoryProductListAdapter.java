package com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.typefactory.CategoryProductListTypeFactory;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionGeneralAdapter;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 10/9/17.
 * Edited to handle Category by alifa
 */

public class CategoryProductListAdapter extends SearchSectionGeneralAdapter {

    private CategoryHeaderModel categoryHeaderModel;
    private List<Visitable> list = new ArrayList<>();
    private EmptyModel emptyModel;
    private CategoryProductListTypeFactory typeFactory;

    public CategoryProductListAdapter(OnItemChangeView itemChangeView,
                                      CategoryHeaderModel categoryHeaderModel,
                                      List<ProductItem> productItemList,
                                      CategoryProductListTypeFactory typeFactory) {
        super(itemChangeView);
        this.list.add(categoryHeaderModel);
        this.list.addAll(productItemList);
        this.categoryHeaderModel = categoryHeaderModel;
        this.typeFactory = typeFactory;
        emptyModel = new EmptyModel();
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

    @Override
    public void clearData() {
        super.clearData();
        notifyDataSetChanged();
    }

    public void appendItems(List<Visitable> list) {
        int start = getItemCount();
        this.list.addAll(list);
        notifyItemRangeInserted(start, list.size());
    }

    public void showEmpty() {
        this.list.add(emptyModel);
        notifyDataSetChanged();
    }

    public void removeEmpty() {
        this.list.remove(emptyModel);
        notifyDataSetChanged();
    }

    public void setWishlistButtonEnabled(int adapterPosition, boolean isEnabled) {
        if (list.get(adapterPosition) instanceof ProductItem) {
            ((ProductItem) list.get(adapterPosition)).setWishlistButtonEnabled(isEnabled);
            notifyItemChanged(adapterPosition);
        }
    }

    public void updateWishlistStatus(int adapterPosition, boolean isWishlisted) {
        if (list.get(adapterPosition) instanceof ProductItem) {
            ((ProductItem) list.get(adapterPosition)).setWishlisted(isWishlisted);
            notifyItemChanged(adapterPosition);
        }
    }

    @Override
    public List<Visitable> getItemList() {
        return list;
    }

    @Override
    protected SearchSectionTypeFactory getTypeFactory() {
        return typeFactory;
    }

    public boolean isProductItem(int position) {
        return position < list.size() && list.get(position) instanceof ProductItem;
    }

    public boolean isCategoryHeader(int position) {
        return position < list.size() && list.get(position) instanceof CategoryHeaderModel;
    }
}
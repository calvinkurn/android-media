package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionGeneralAdapter;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionTypeFactory;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.typefactory.ProductListTypeFactory;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.EmptySearchModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.GuidedSearchViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.HeaderViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 10/9/17.
 */

public class ProductListAdapter extends SearchSectionGeneralAdapter {

    private static final int ADAPTER_POSITION_HEADER = 0;
    private List<Visitable> list = new ArrayList<>();
    private EmptySearchModel emptySearchModel;
    private ProductListTypeFactory typeFactory;
    private int startFrom;
    private int totalData;
    private Context context;
    private GuidedSearchViewModel guidedSearch;

    public ProductListAdapter(Context context, OnItemChangeView itemChangeView, ProductListTypeFactory typeFactory) {
        super(itemChangeView);
        this.context = context;
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

    public void clear() {
        this.list.clear();
    }

    public void appendItems(List<Visitable> list) {
        int start = getItemCount();
        this.list.addAll(list);
        notifyItemRangeInserted(start, list.size());
    }

    public void incrementStart() {
        setStartFrom(getStartFrom() + Integer.parseInt(BrowseApi.DEFAULT_VALUE_OF_PARAMETER_ROWS));
    }

    public boolean isEvenPage() {
        return getStartFrom() / Integer.parseInt(BrowseApi.DEFAULT_VALUE_OF_PARAMETER_ROWS) % 2 == 0;
    }

    public int getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(int start) {
        this.startFrom = start;
    }

    public void setWishlistButtonEnabled(int adapterPosition, boolean isEnabled) {
        if (list.get(adapterPosition) instanceof ProductItem) {
            ((ProductItem) list.get(adapterPosition)).setWishlistButtonEnabled(isEnabled);
        }
    }

    public void updateWishlistStatus(int adapterPosition, boolean isWishlisted) {
        if (list.get(adapterPosition) instanceof ProductItem) {
            ((ProductItem) list.get(adapterPosition)).setWishlisted(isWishlisted);
        }
    }

    public void showEmpty(String query) {
        clearData();
        list.add(mappingEmptySearch(query));
        notifyDataSetChanged();
    }

    private EmptySearchModel mappingEmptySearch(String query) {
        emptySearchModel = new EmptySearchModel();
        emptySearchModel.setImageRes(R.drawable.ic_empty_search);
        emptySearchModel.setTitle(context.getString(R.string.msg_empty_search_1));
        emptySearchModel.setContent(String.format(context.getString(R.string.empty_search_content_template), query));
        emptySearchModel.setButtonText(context.getString(R.string.empty_search_button_text));
        return emptySearchModel;
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
        return checkDataSize(position) && list.get(position) instanceof ProductItem;
    }

    @Override
    public boolean isEmptyItem(int position) {
        return checkDataSize(position) && getItemList().get(position) instanceof EmptySearchModel;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public int getTotalData() {
        return totalData;
    }

    public boolean hasNextPage() {
        return getStartFrom() < getTotalData();
    }

    @Override
    public void clearData() {
        super.clearData();
        setStartFrom(0);
        setTotalData(0);
    }

    public boolean hasHeader() {
        return checkDataSize(0) && getItemList().get(0) instanceof HeaderViewModel;
    }

    public void addGuidedSearch() {
        if (guidedSearch != null && !guidedSearch.getItemList().isEmpty()) {
            int start = getItemCount();
            list.add(guidedSearch);
            notifyItemInserted(start);
        }
    }

    public void setGuidedSearch(GuidedSearchViewModel guidedSearch) {
        this.guidedSearch = guidedSearch;
    }

    public boolean isGuidedSearch(int position) {
        return checkDataSize(position) && getItemList().get(position) instanceof GuidedSearchViewModel;
    }

    public boolean hasGuidedSearch() {
        return guidedSearch != null;
    }

    public void updateHeaderModel(HeaderViewModel headerViewModel) {
        if (!list.isEmpty() && list.get(ADAPTER_POSITION_HEADER) instanceof HeaderViewModel) {
            list.set(ADAPTER_POSITION_HEADER, headerViewModel);
            notifyItemChanged(ADAPTER_POSITION_HEADER);
        }
    }
}
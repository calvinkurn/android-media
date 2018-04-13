package com.tokopedia.discovery.imagesearch.search.fragment.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.imagesearch.search.fragment.product.adapter.typefactory.ImageProductListTypeFactory;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionGeneralAdapter;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionTypeFactory;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.EmptySearchModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.GuidedSearchViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sachinbansal on 4/13/18.
 */

public class ImageProductListAdapter extends SearchSectionGeneralAdapter {

    private static final int ADAPTER_POSITION_HEADER = 0;
    private List<Visitable> list = new ArrayList<>();
    private EmptySearchModel emptySearchModel;
    private ImageProductListTypeFactory typeFactory;
    private int startFrom;
    private int totalData;
    private Context context;
    private GuidedSearchViewModel guidedSearch;

    public ImageProductListAdapter(Context context, OnItemChangeView itemChangeView, ImageProductListTypeFactory typeFactory) {
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

    public void showEmpty() {
        clearData();
        list.add(mappingEmptySearch());
        notifyDataSetChanged();
    }

    private EmptySearchModel mappingEmptySearch() {
        emptySearchModel = new EmptySearchModel();
        emptySearchModel.setImageRes(R.drawable.ic_empty_search);
        emptySearchModel.setTitle(context.getString(R.string.msg_empty_search_1));
        emptySearchModel.setContent(context.getString(R.string.empty_search_content_template));
        emptySearchModel.setButtonText(context.getString(R.string.empty_search_button_text));
        return emptySearchModel;
    }

    private EmptySearchModel mappingEmptySearch(String query) {
        emptySearchModel = new EmptySearchModel();
        emptySearchModel.setImageRes(R.drawable.ic_empty_search);
        emptySearchModel.setTitle(context.getString(R.string.msg_empty_search_1));
        emptySearchModel.setContent(String.format(context.getString(R.string.empty_search_content_template), query));
        emptySearchModel.setButtonText(context.getString(R.string.empty_search_button_text));
        return emptySearchModel;
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

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public boolean isProductItem(int position) {
        return checkDataSize(position) && list.get(position) instanceof ProductItem;
    }

    @Override
    public List<Visitable> getItemList() {
        return null;
    }

    @Override
    protected SearchSectionTypeFactory getTypeFactory() {
        return null;
    }

}

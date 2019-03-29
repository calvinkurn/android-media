package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.model.LoadingModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionGeneralAdapter;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionTypeFactory;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.typefactory.ProductListTypeFactory;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.TopAdsSwitcher;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.TopAdsViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.EmptySearchModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.GuidedSearchViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.HeaderViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.TopAdsViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.TopAdsViewModel;
import com.tokopedia.discovery.newdynamicfilter.helper.FilterFlagSelectedModel;
import com.tokopedia.topads.sdk.view.DisplayMode;

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
    private LoadingModel loadingModel;
    private TopAdsSwitcher topAdsSwitcher;

    public ProductListAdapter(Context context, OnItemChangeView itemChangeView, ProductListTypeFactory typeFactory) {
        super(itemChangeView);
        this.context = context;
        this.typeFactory = typeFactory;
        loadingModel = new LoadingModel();
    }

    public void setTopAdsSwitcher(TopAdsSwitcher topAdsSwitcher) {
        this.topAdsSwitcher = topAdsSwitcher;
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        AbstractViewHolder viewHolder = typeFactory.createViewHolder(view, viewType);
        if(viewHolder instanceof TopAdsViewHolder){
            setTopAdsSwitcher((TopAdsViewHolder) viewHolder);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public void changeListView() {
        super.changeListView();
        if(topAdsSwitcher!=null) {
            topAdsSwitcher.switchDisplay(DisplayMode.LIST);
        }
    }

    @Override
    public void changeDoubleGridView() {
        super.changeDoubleGridView();
        if(topAdsSwitcher!=null) {
            topAdsSwitcher.switchDisplay(DisplayMode.GRID);
        }
    }

    @Override
    public void changeSingleGridView() {
        super.changeSingleGridView();
        if(topAdsSwitcher!=null) {
            topAdsSwitcher.switchDisplay(DisplayMode.BIG);
        }
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

    public void setWishlistButtonEnabled(String productId, boolean isEnabled) {

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof ProductItem) {
                ProductItem model = ((ProductItem) list.get(i));
                if (productId.equals(model.getProductID())) {
                    model.setWishlistButtonEnabled(isEnabled);
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    public void updateWishlistStatus(String productId, boolean isWishlisted) {

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof ProductItem) {
                ProductItem model = ((ProductItem) list.get(i));
                if (productId.equals(model.getProductID())) {
                    model.setWishlisted(isWishlisted);
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    public void updateWishlistStatus(int adapterPosition, boolean isWishlisted) {
        if (adapterPosition >= 0 && list.get(adapterPosition) instanceof ProductItem) {
            ((ProductItem) list.get(adapterPosition)).setWishlisted(isWishlisted);
            notifyItemChanged(adapterPosition);
        }
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

    public boolean isTopAds(int position) {
        return checkDataSize(position) && getItemList().get(position) instanceof TopAdsViewModel;
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

    public void addLoading() {
        this.list.add(loadingModel);
        notifyDataSetChanged();
    }

    public void removeLoading() {
        this.list.remove(loadingModel);
        notifyDataSetChanged();
    }
}
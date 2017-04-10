package com.tokopedia.topads.sdk.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.topads.sdk.base.adapter.Visitable;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ProductGridViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ProductListViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ShopGridViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ShopListViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by errysuprayogi on 3/29/17.
 */

public class TopAdsItemAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> list;
    private AdapterTypeFactory typeFactory;

    public TopAdsItemAdapter(Context context) {
        this.list = new ArrayList<>();
        this.typeFactory = new AdapterTypeFactory(context);
    }

    public void setList(List<Visitable> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setItemClickListener(LocalAdsClickListener itemClickListener) {
        typeFactory.setItemClickListener(itemClickListener);
        notifyDataSetChanged();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void switchDisplayMode(int displayMode) {
        for (int i = 0; i < list.size(); i++) {
            Visitable visitable = list.get(i);
            if(displayMode == DisplayMode.GRID && visitable instanceof ProductListViewModel){
                list.set(i, convertToProductGridViewModel(((ProductListViewModel) list.get(i)).getData()));
            } else if(displayMode == DisplayMode.LIST && visitable instanceof ProductGridViewModel){
                list.set(i, convertToProductListViewModel(((ProductGridViewModel) list.get(i)).getData()));
            } else if(displayMode == DisplayMode.GRID && visitable instanceof ShopListViewModel){
                list.set(i, convertToShopGridViewModel(((ShopListViewModel) list.get(i)).getData()));
            } else if(displayMode == DisplayMode.LIST && visitable instanceof ShopGridViewModel){
                list.set(i, convertToShopListViewModel(((ShopGridViewModel) list.get(i)).getData()));
            }
        }
    }


    private ProductGridViewModel convertToProductGridViewModel(Data data) {
        ProductGridViewModel viewModel = new ProductGridViewModel();
        viewModel.setData(data);
        return viewModel;
    }

    private ProductListViewModel convertToProductListViewModel(Data data) {
        ProductListViewModel viewModel = new ProductListViewModel();
        viewModel.setData(data);
        return viewModel;
    }

    private ShopGridViewModel convertToShopGridViewModel(Data data) {
        ShopGridViewModel viewModel = new ShopGridViewModel();
        viewModel.setData(data);
        return viewModel;
    }

    private ShopListViewModel convertToShopListViewModel(Data data) {
        ShopListViewModel viewModel = new ShopListViewModel();
        viewModel.setData(data);
        return viewModel;
    }

    public void clearData() {
        list.clear();
    }
}

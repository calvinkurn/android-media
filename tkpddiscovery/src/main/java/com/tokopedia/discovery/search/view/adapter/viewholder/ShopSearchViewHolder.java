package com.tokopedia.discovery.search.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tokopedia.core.R2;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;
import com.tokopedia.discovery.search.view.adapter.ShopSearchResultAdapter;
import com.tokopedia.discovery.search.view.adapter.viewmodel.ShopViewModel;

import butterknife.BindView;

/**
 * @author erry on 20/02/17.
 */

public class ShopSearchViewHolder extends AbstractViewHolder<ShopViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.shop_search_parent_list_item;
    private static final String TAG = ShopSearchViewHolder.class.getSimpleName();

    @BindView(R2.id.list)
    RecyclerView recyclerView;
    private Context context;
    private final ShopSearchResultAdapter adapter;

    public ShopSearchViewHolder(View itemView, ItemClickListener clickListener) {
        super(itemView);
        context = itemView.getContext();
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ShopSearchResultAdapter(context, clickListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(ShopViewModel element) {
        if(element.getSearchItems().size()>0){
            adapter.setSearchTerm(element.getSearchTerm());
            adapter.setItems(element.getSearchItems());
        }
    }
}
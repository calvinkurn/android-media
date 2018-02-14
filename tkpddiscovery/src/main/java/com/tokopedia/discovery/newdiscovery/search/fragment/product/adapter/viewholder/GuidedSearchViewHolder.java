package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder;

import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.GuidedSearchViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.HeaderViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 14/02/18.
 */

public class GuidedSearchViewHolder extends AbstractViewHolder<GuidedSearchViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.guided_search_layout;

    RecyclerView recyclerView;
    GuidedSearchAdapter adapter;

    public GuidedSearchViewHolder(View itemView, ItemClickListener itemClickListener) {
        super(itemView);
        recyclerView = itemView.findViewById(R.id.recyclerView);
        adapter = new GuidedSearchAdapter(itemClickListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(GuidedSearchViewModel element) {
        adapter.setItemList(element.getItemList());
    }

    public static class GuidedSearchAdapter extends RecyclerView.Adapter<ViewHolder> {

        List<GuidedSearchViewModel.Item> itemList = new ArrayList<>();
        ItemClickListener itemClickListener;

        public GuidedSearchAdapter(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public void setItemList(List<GuidedSearchViewModel.Item> itemList) {
            this.itemList = itemList;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guided_search_item, parent, false);
            return new ViewHolder(view, itemClickListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(itemList.get(position));
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ItemClickListener itemClickListener;

        public ViewHolder(View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            textView = itemView.findViewById(R.id.guided_search_text);
            this.itemClickListener = itemClickListener;
        }

        public void bind(final GuidedSearchViewModel.Item item) {
            textView.setText(item.getKeyword());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(item.getUrl());
                    String query = uri.getQueryParameter(BrowseApi.Q);
                    itemClickListener.onSearchGuideClicked(query);
                }
            });
        }
    }
}

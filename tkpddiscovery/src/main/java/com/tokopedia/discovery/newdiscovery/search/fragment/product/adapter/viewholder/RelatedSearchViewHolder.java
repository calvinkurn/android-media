package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder;

import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.itemdecoration.LinearHorizontalSpacingDecoration;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.ProductListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.helper.ListHelper;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.RelatedSearchModel;

import java.util.ArrayList;
import java.util.List;

public class RelatedSearchViewHolder extends AbstractViewHolder<RelatedSearchModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.related_search_layout;

    RecyclerView recyclerView;
    RelatedSearchAdapter adapter;
    TextView relatedSearchTitle;

    public RelatedSearchViewHolder(View itemView, ProductListener itemClickListener) {
        super(itemView);
        recyclerView = itemView.findViewById(R.id.recyclerView);
        relatedSearchTitle = itemView.findViewById(R.id.relatedSearchTitle);
        adapter = new RelatedSearchAdapter(itemClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new LinearHorizontalSpacingDecoration(
                itemView.getContext().getResources().getDimensionPixelSize(R.dimen.dp_8),
                itemView.getContext().getResources().getDimensionPixelSize(R.dimen.dp_16)
        ));
    }

    @Override
    public void bind(RelatedSearchModel element) {
        if (ListHelper.isContainItems(element.getOtherRelated())) {
            recyclerView.setVisibility(View.VISIBLE);
            relatedSearchTitle.setVisibility(View.VISIBLE);
            adapter.setItemList(element.getOtherRelated());
        } else {
            recyclerView.setVisibility(View.GONE);
            relatedSearchTitle.setVisibility(View.GONE);
        }
    }

    public static class RelatedSearchAdapter extends RecyclerView.Adapter<ViewHolder> {

        List<RelatedSearchModel.OtherRelated> itemList = new ArrayList<>();
        ProductListener itemClickListener;

        public RelatedSearchAdapter(ProductListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public void setItemList(List<RelatedSearchModel.OtherRelated> itemList) {
            this.itemList = itemList;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.related_search_item, parent, false);
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
        ProductListener itemClickListener;

        public ViewHolder(View itemView, ProductListener itemClickListener) {
            super(itemView);
            textView = itemView.findViewById(R.id.related_search_text);
            this.itemClickListener = itemClickListener;
        }

        public void bind(final RelatedSearchModel.OtherRelated item) {
            textView.setText(item.getKeyword());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(item.getUrl());
                    String keyword = uri.getQueryParameter(BrowseApi.Q);
                    itemClickListener.onRelatedSearchClicked(uri.getEncodedQuery(), keyword);
                }
            });
        }
    }
}


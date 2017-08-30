package com.tokopedia.topads.sdk.view.adapter.factory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.topads.sdk.base.adapter.exception.TypeNotSupportedException;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.view.adapter.viewholder.LoadingViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.TopAdsViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ClientViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.LoadingViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.TopAdsViewModel;

/**
 * @author by errysuprayogi on 4/13/17.
 */

public class TopAdsAdapterTypeFactory implements TopAdsTypeFactory {

    private LocalAdsClickListener itemClickListener;
    public static int CLIENT_ADAPTER_VIEW_TYPE = -7238;

    public void setItemClickListener(LocalAdsClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int type(ClientViewModel viewModel) {
        return CLIENT_ADAPTER_VIEW_TYPE;
    }

    @Override
    public int type(TopAdsViewModel viewModel) {
        return TopAdsViewHolder.LAYOUT;
    }

    @Override
    public int type(LoadingViewModel viewModel) {
        return LoadingViewHolder.LAYOUT;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup view, int viewType) {
        if (viewType == TopAdsViewHolder.LAYOUT) {
            return new TopAdsViewHolder(view, itemClickListener);
        } else if(viewType == LoadingViewHolder.LAYOUT){
            return new LoadingViewHolder(view);
        } else {
            throw TypeNotSupportedException.create("Layout not supported");
        }
    }
}

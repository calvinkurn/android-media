package com.tokopedia.topads.sdk.view.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.RecyclerViewAdapterWrapper;
import com.tokopedia.topads.sdk.view.AdsView;
import com.tokopedia.topads.sdk.view.TopAdsView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author by errysuprayogi on 4/10/17.
 */

public class TopAdsAdapterWrapper<T> extends RecyclerViewAdapterWrapper implements AdapterView {
    public static final int TYPE_PENDING = 999;
    public static final int TYPE_TOPADS = 888;
    private final Context context;
    private final int pendingViewResId;
    private AtomicBoolean keepOnAppending;
    private AtomicBoolean dataPending;
    private RequestToLoadMoreListener requestToLoadMoreListener;

    public TopAdsAdapterWrapper(Context context, Adapter wrapped, List<T> datas,
                                RequestToLoadMoreListener requestToLoadMoreListener,
                                @LayoutRes int pendingViewResId, boolean keepOnAppending) {
        super(wrapped, datas);
        this.context = context;
        this.requestToLoadMoreListener = requestToLoadMoreListener;
        this.pendingViewResId = pendingViewResId;
        this.keepOnAppending = new AtomicBoolean(keepOnAppending);
        dataPending = new AtomicBoolean(false);
    }

    public TopAdsAdapterWrapper(Context context, Adapter wrapped, List<T> datas,
                                RequestToLoadMoreListener requestToLoadMoreListener) {
        this(context, wrapped, datas, requestToLoadMoreListener, R.layout.item_loading, true);
    }

    private void stopAppending() {
        setKeepOnAppending(false);
    }

    /**
     * Let the adapter know that data is load and ready to view.
     *
     * @param keepOnAppending whether the adapter should request to load more when scrolling to the bottom.
     */
    public void onDataReady(boolean keepOnAppending) {
        dataPending.set(false);
        setKeepOnAppending(keepOnAppending);
    }

    private void setKeepOnAppending(boolean newValue) {
        keepOnAppending.set(newValue);
        getWrappedAdapter().notifyDataSetChanged();
    }

    /**
     *
     */
    public void restartAppending() {
        dataPending.set(false);
        setKeepOnAppending(true);
    }

    private View getPendingView(ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(pendingViewResId, viewGroup, false);
    }

    private View getTopAdsView(ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_ads, viewGroup, false);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1 + (keepOnAppending.get() ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getWrappedAdapter().getItemCount()) {
            return TYPE_PENDING;
        } else if (position % 10 == 0) {
            return TYPE_TOPADS;
        }
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PENDING) {
            return new PendingViewHolder(getPendingView(parent));
        } else if (viewType == TYPE_TOPADS) {
            return new TopAdsViewHolder(getTopAdsView(parent));
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_PENDING) {
            if (!dataPending.get()) {
                dataPending.set(true);
                requestToLoadMoreListener.onLoadMoreRequested();
            }
        } else if (getItemViewType(position) == TYPE_TOPADS) {
//            ((AbstractAdsViewHolder) holder).load();
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    public interface RequestToLoadMoreListener {
        /**
         * The adapter requests to load more data.
         */
        void onLoadMoreRequested();
    }

    static class PendingViewHolder extends ViewHolder {

        public PendingViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class TopAdsViewHolder extends ViewHolder {

        private TopAdsView adsView;

        public TopAdsViewHolder(View itemView) {
            super(itemView);
            adsView = (TopAdsView) itemView.findViewById(R.id.ads);
            load();
        }

        public void load() {
            adsView.loadTopAds();
        }
    }
}

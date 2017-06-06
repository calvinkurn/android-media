package com.tokopedia.topads.sdk.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.adapter.ObserverType;
import com.tokopedia.topads.sdk.listener.TopAdsInfoClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.utils.EndlessScrollRecycleListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.adapter.factory.TopAdsAdapterTypeFactory;
import com.tokopedia.topads.sdk.view.adapter.viewholder.LoadingViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewholder.TopAdsViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.LoadingViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.TopAdsViewModel;


/**
 * @author by errysuprayogi on 4/11/17.
 */

public class TopAdsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements TopAdsPlacer.DataObserver {

    private static final String TAG = TopAdsRecyclerAdapter.class.getSimpleName();

    @NonNull
    private final RecyclerView.AdapterDataObserver mAdapterDataObserver;
    @Nullable
    private RecyclerView recyclerView;
    @NonNull
    private final RecyclerView.Adapter mOriginalAdapter;
    private final Context mContext;
    private TopAdsAdapterTypeFactory typeFactory;
    private TopAdsInfoClickListener adsInfoClickListener;
    private OnLoadListener loadListener;
    private boolean loadMore = false;
    private GridLayoutManager.SpanSizeLookup spanSizeLookup;
    private LoadingViewModel loadingViewModel = new LoadingViewModel();
    private TopAdsPlacer placer;
    private EndlessScrollRecycleListener endlessScrollListener = new EndlessScrollRecycleListener() {
        @Override
        public void onLoadMore(int page, int totalItemsCount) {
            if (loadMore)
                return;
            if (loadListener != null) {
                showLoading();
                loadListener.onLoad(placer.getPage(), totalItemsCount);
            }
        }
    };

    public TopAdsRecyclerAdapter(
            @NonNull Context context, @NonNull final RecyclerView.Adapter originalAdapter) {

        mOriginalAdapter = originalAdapter;
        mContext = context;
        typeFactory = new TopAdsAdapterTypeFactory(context);
        placer = new TopAdsPlacer(context, typeFactory, this);
        mAdapterDataObserver = new RecyclerView.AdapterDataObserver() {

            @Override
            public void onChanged() {
                placer.setAjustedItemCount(mOriginalAdapter.getItemCount());
                placer.onChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                positionStart = placer.getPositionStart(positionStart);
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(final int positionStart, final int itemCount) {
                placer.onItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                notifyItemRangeRemoved(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                notifyDataSetChanged();
            }
        };
        this.mOriginalAdapter.registerAdapterDataObserver(mAdapterDataObserver);

    }

    @Override
    public void onStreamLoaded(int type) {
        switch (type) {
            case ObserverType.CHANGE:
                notifyDataSetChanged();
                break;
            case ObserverType.ITEM_RANGE_INSERTED:
                notifyItemRangeInserted(
                        placer.getAjustedPositionStart(), placer.getAjustedItemCount());
                break;
            case ObserverType.ITEM_RANGE_CHANGE:

                break;
        }
        hideLoading();
    }

    public void setConfig(Config config) {
        placer.setConfig(config);
    }

    public Config getConfig(){
        return placer.getConfig();
    }

    public void setHasHeader(boolean hasHeader) {
        placer.setHasHeader(hasHeader);
    }

    public void setSpanSizeLookup(GridLayoutManager.SpanSizeLookup spanSizeLookup) {
        this.spanSizeLookup = spanSizeLookup;
    }

    public void setOnLoadListener(OnLoadListener loadListener) {
        this.loadListener = loadListener;
    }

    public void setAdsInfoClickListener(TopAdsInfoClickListener adsInfoClickListener) {
        this.adsInfoClickListener = adsInfoClickListener;
    }

    public void setAdsItemClickListener(TopAdsItemClickListener adsItemClickListener) {
        placer.setAdsItemClickListener(adsItemClickListener);
    }

    public void setTopAdsListener(TopAdsListener topAdsListener) {
        placer.setTopAdsListener(topAdsListener);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        setLayoutManager(this.recyclerView.getLayoutManager());
        setEndlessScrollListener();
    }

    public void setEndlessScrollListenerVisibleThreshold(int threshold){
        this.endlessScrollListener.setVisibleThreshold(threshold);
    }

    public void unsetEndlessScrollListener(){
        recyclerView.removeOnScrollListener(endlessScrollListener);
    }

    public void setEndlessScrollListener(){
        recyclerView.addOnScrollListener(endlessScrollListener);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    class SpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
        GridLayoutManager gridLayoutManager;

        public SpanSizeLookup(GridLayoutManager gridLayoutManager) {
            this.gridLayoutManager = gridLayoutManager;
        }

        @Override
        public int getSpanSize(int position) {
            if (placer.getItem(position) instanceof TopAdsViewModel) {
                return gridLayoutManager.getSpanCount();
            } else {
                return 1;
            }
        }
    }

    public boolean isTopAdsViewHolder(int position) {
        if (placer.getItemCount() > 0)
            return placer.getItem(position) instanceof TopAdsViewModel;
        return false;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if (viewType == TopAdsViewHolder.LAYOUT || viewType == LoadingViewHolder.LAYOUT) {
            View view = LayoutInflater.from(context).inflate(viewType, parent, false);
            return typeFactory.createViewHolder((ViewGroup) view, viewType);
        } else {
            return mOriginalAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int originalPos = getOriginalPosition(position);
        if (originalPos == TopAdsViewModel.TOP_ADS_POSITION_TYPE) {
            TopAdsViewHolder topAdsViewHolder = (TopAdsViewHolder) holder;
            TopAdsViewModel adsViewModel = (TopAdsViewModel) placer.getItem(position);
            topAdsViewHolder.setDisplayMode(placer.getDisplayMode());
            topAdsViewHolder.bind(adsViewModel);
        } else if (originalPos == LoadingViewModel.LOADING_POSITION_TYPE) {
            return;
        } else {
            mOriginalAdapter.onBindViewHolder(holder, originalPos);
        }
    }

    private int getOriginalPosition(int position) {
        return placer.getItem(position).originalPos();
    }

    @Override
    public int getItemViewType(final int position) {
        int viewType = placer.getItem(position).type(typeFactory);
        if (viewType == TopAdsAdapterTypeFactory.CLIENT_ADAPTER_VIEW_TYPE) {
            return mOriginalAdapter.getItemViewType(getOriginalPosition(position));
        }
        return viewType;
    }

    @Override
    public int getItemCount() {
        return placer.getItemCount();
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            if(getConfig().getDisplayMode() == DisplayMode.FEED){
                placer.setDisplayMode(getConfig().getDisplayMode());
            } else {
                placer.setDisplayMode(DisplayMode.GRID);
            }
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            if (spanSizeLookup != null) {
                gridLayoutManager.setSpanSizeLookup(spanSizeLookup);
            } else {
                gridLayoutManager.setSpanSizeLookup(new SpanSizeLookup(gridLayoutManager));
            }
            this.recyclerView.setLayoutManager(gridLayoutManager);
        } else if (layoutManager instanceof LinearLayoutManager) {
            if(getConfig().getDisplayMode() == DisplayMode.FEED){
                placer.setDisplayMode(getConfig().getDisplayMode());
            } else {
                placer.setDisplayMode(DisplayMode.LIST);
            }
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            this.recyclerView.setLayoutManager(linearLayoutManager);
        }
    }

    public boolean isLoading(int position) {
        return position == placer.getItems().indexOf(loadingViewModel);
    }

    public void reset() {
        placer.reset();
        if (this.recyclerView != null) this.recyclerView.removeAllViews();
        notifyDataSetChanged();
        endlessScrollListener.resetState();
    }

    public void showLoading() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (!placer.getItems().contains(loadingViewModel)) {
                    placer.getItems().add(loadingViewModel);
                    notifyItemInserted(placer.getItemCount() + 1);
                    loadMore = true;
                }
            }
        });
    }

    public void hideLoading() {
        if (placer.getItems().contains(loadingViewModel)) {
            placer.getItems().remove(loadingViewModel);
            notifyItemRemoved(placer.getItemCount());
            loadMore = false;
        }
    }

    public void shouldLoadAds(boolean loadAds){
        placer.setShouldLoadAds(loadAds);
    }

    public interface OnLoadListener {

        void onLoad(int page, int totalCount);

    }

}

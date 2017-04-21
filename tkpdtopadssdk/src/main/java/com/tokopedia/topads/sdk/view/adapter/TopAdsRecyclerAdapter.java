package com.tokopedia.topads.sdk.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsInfoClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.presenter.TopAdsPresenter;
import com.tokopedia.topads.sdk.view.AdsView;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.adapter.factory.TopAdsAdapterTypeFactory;
import com.tokopedia.topads.sdk.view.adapter.viewholder.TopAdsViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ClientViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.TopAdsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by errysuprayogi on 4/11/17.
 */

public class TopAdsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AdsView,
        LocalAdsClickListener {

    private static final String TAG = TopAdsRecyclerAdapter.class.getSimpleName();

    @NonNull
    private final RecyclerView.AdapterDataObserver mAdapterDataObserver;
    @Nullable
    private RecyclerView recyclerView;
    @NonNull
    private final RecyclerView.Adapter mOriginalAdapter;
    private final Context mContext;
    private List<Item> items = new ArrayList<>();
    private TopAdsAdapterTypeFactory typeFactory;
    private TopAdsPresenter presenter;
    private int mPositionStart = 0;
    private int mPage = 1;
    private int mItemCount;
    private TopAdsParams adsParams;
    private TopAdsInfoClickListener adsInfoClickListener;
    private TopAdsItemClickListener adsItemClickListener;
    private TopAdsListener topAdsListener;
    private boolean hasTopAds = false;
    private boolean finish = true;

    public TopAdsRecyclerAdapter(@NonNull Context context, @NonNull final RecyclerView.Adapter originalAdapter) {
        mOriginalAdapter = originalAdapter;
        mContext = context;
        typeFactory = new TopAdsAdapterTypeFactory(context);
        adsParams = new TopAdsParams();
        initPresenter();
        mItemCount = mOriginalAdapter.getItemCount();
        mAdapterDataObserver = new RecyclerView.AdapterDataObserver() {

            @Override
            public void onChanged() {
                Log.d(TAG, "onChanged");
                if (hasTopAds) {

                    super.onChanged();
                } else {
                    mItemCount = mOriginalAdapter.getItemCount();
                    loadTopAds();
                }
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                Log.d(TAG, "onItemRangeChanged positionStart " + positionStart + " itemCount " + itemCount);
                notifyItemChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(final int positionStart, final int itemCount) {
                Log.d(TAG, "onItemRangeInserted positionStart " + positionStart + " itemCount " + itemCount);
                loadTopAds();
                mPositionStart = positionStart;
                mItemCount = itemCount;
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            }
        };
        this.mOriginalAdapter.registerAdapterDataObserver(mAdapterDataObserver);

    }

    public void setAdsInfoClickListener(TopAdsInfoClickListener adsInfoClickListener) {
        this.adsInfoClickListener = adsInfoClickListener;
    }

    public void setAdsItemClickListener(TopAdsItemClickListener adsItemClickListener) {
        this.adsItemClickListener = adsItemClickListener;
        typeFactory.setItemClickListener(this);
    }

    public void setTopAdsListener(TopAdsListener topAdsListener) {
        this.topAdsListener = topAdsListener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        setLayoutManager(this.recyclerView.getLayoutManager());
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
            if (items.get(position) instanceof TopAdsViewModel) {
                return gridLayoutManager.getSpanCount();
            } else {
                return 1;
            }
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TopAdsViewHolder.LAYOUT) {
            Context context = parent.getContext();
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
            TopAdsViewModel adsViewModel = (TopAdsViewModel) items.get(position);
            topAdsViewHolder.setDisplayMode(presenter.getDisplayMode());
            topAdsViewHolder.bind(adsViewModel);
        } else {
            mOriginalAdapter.onBindViewHolder(holder, originalPos);
        }
    }

    private int getOriginalPosition(int position) {
        return items.get(position).originalPos();
    }

    @Override
    public int getItemViewType(final int position) {
        int viewType = items.get(position).type(typeFactory);
        if (viewType == TopAdsAdapterTypeFactory.CLIENT_ADAPTER_VIEW_TYPE) {
            return mOriginalAdapter.getItemViewType(position);
        }
        return viewType;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void initPresenter() {
        presenter = new TopAdsPresenter(mContext);
        presenter.attachView(this);
    }

    @Override
    public void setMaxItems(int items) {
        presenter.setMaxItems(items);
    }

    @Override
    public void setTopAdsParams(TopAdsParams adsParams) {
        presenter.setParams(adsParams);
    }

    @Override
    public void setDisplayMode(int displayMode) {
        presenter.setDisplayMode(displayMode);
        for (Item visitable : items) {
            if (visitable instanceof TopAdsViewModel) {
                TopAdsViewModel topAdsViewModel = (TopAdsViewModel) visitable;
                topAdsViewModel.switchDisplayMode(displayMode);
            }
        }
    }

    @Override
    public void loadTopAds() {
        adsParams.getParam().put(TopAdsParams.KEY_PAGE, String.valueOf(mPage));
        adsParams.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID, "66");
        adsParams.getParam().put(TopAdsParams.KEY_USER_ID, "3589675");
        presenter.setParams(adsParams);
        presenter.loadTopAds();
    }

    @Override
    public void displayAds(List<Item> list) {
        if (list.size() > 0) {
            hasTopAds = true;
            ArrayList<Item> arrayList = new ArrayList<>();
            arrayList.add(new TopAdsViewModel(list));
            for (int i = mPositionStart; i < (mPositionStart + mItemCount); i++) {
                ClientViewModel model = new ClientViewModel();
                model.setPosition(i);
                arrayList.add(model);
            }
            int positionStart = items.size();
            int itemCount = arrayList.size();
            items.addAll(arrayList);
            super.notifyItemRangeInserted(positionStart, itemCount);
            mPage++;
            if (topAdsListener != null) {
                topAdsListener.onTopAdsLoaded();
            }
        }
    }

    @Override
    public void notifyAdsErrorLoaded(int errorCode, String message) {
        if (topAdsListener != null) {
            topAdsListener.onTopAdsFailToLoad(errorCode, message);
        }
    }

    @Override
    public void notifyProductClickListener(Product product) {
        if (adsItemClickListener != null) {
            adsItemClickListener.onProductItemClicked(product);
        }
    }

    @Override
    public void notifyShopClickListener(Shop shop) {
        if (adsItemClickListener != null) {
            adsItemClickListener.onShopItemClicked(shop);
        }
    }

    @Override
    public void onShopItemClicked(int position, Data data) {
        presenter.openShopTopAds(data.getShopClickUrl(), data.getShop());
    }

    @Override
    public void onProductItemClicked(int position, Data data) {
        presenter.openProductTopAds(data.getProductClickUrl(), data.getProduct());
    }

    @Override
    public void onAddFavorite(int position, Shop shop) {
        if (adsItemClickListener != null) {
            adsItemClickListener.onAddFavorite(shop);
        }
    }

    @Override
    public void initLoading() {
        finish = false;
    }

    @Override
    public void finishLoading() {
        finish = true;
    }

    @Override
    public void showLoading(boolean showLoading) {
    }

    @Override
    public void setSessionId(String sessionId) {
        presenter.setSessionId(sessionId);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            setDisplayMode(DisplayMode.GRID);
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new SpanSizeLookup(gridLayoutManager));
            this.recyclerView.setLayoutManager(gridLayoutManager);
        } else if (layoutManager instanceof LinearLayoutManager) {
            setDisplayMode(DisplayMode.LIST);
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            this.recyclerView.setLayoutManager(linearLayoutManager);
        }
    }

    public boolean isFinish() {
        return finish;
    }
}

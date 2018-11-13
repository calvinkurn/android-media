package com.tokopedia.tkpdpdp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry;
import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.tkpdpdp.customview.bottomsheetreview.BottomSheetReviewImageSlider;
import com.tokopedia.tkpdpdp.decoration.GalleryItemDecoration;
import com.tokopedia.tkpdpdp.responsemodel.ResponseModel;
import com.tokopedia.tkpdpdp.service.ImageService;
import com.tokopedia.tkpdpdp.viewmodel.ProductItem;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReviewGalleryActivity extends AppCompatActivity implements BottomSheetReviewImageSlider.Callback, GalleryView {

    private static final int SPAN_COUNT = 3;

    private RecyclerView recyclerView;
    private BottomSheetReviewImageSlider bottomSheetReviewImageSlider;
    private GalleryAdapter galleryAdapter;
    private SnackbarRetry snackbarRetry;

    private EndlessRecyclerViewScrollListener loadMoreTriggerListener;
    private Subscription subscription;
    private SwipeRefreshLayout refreshLayout;
    private boolean isLoadingFromNetwork = false;
    private View backButton;

    public static void moveTo(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, ReviewGalleryActivity.class);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_gallery);
        bindView();
        initListeners();
        setupRecyclerView();
        setupBottomSheet();
        loadDataFromNetworkFirstPage();
    }

    private void bindView() {
        recyclerView = findViewById(R.id.galleryRecyclerView);
        bottomSheetReviewImageSlider = findViewById(R.id.bottomSheetImageSlider);
        backButton = findViewById(R.id.top_bar_close_button);
        refreshLayout = findViewById(R.id.swipe_refresh_layout);
    }

    private void initListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataFromNetworkFirstPage();
            }
        });
    }

    private void setupRecyclerView() {
        galleryAdapter = new GalleryAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, SPAN_COUNT);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (galleryAdapter.isGalleryItem(position)) {
                    return 1;
                } else {
                    return SPAN_COUNT;
                }
            }
        });
        loadMoreTriggerListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (isAllowLoadMore()) {
                    loadDataFromNetwork(galleryAdapter.getGalleryItemCount());
                } else {
                    updateStateAfterGetData();
                }
            }
        };
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new GalleryItemDecoration(getResources().getDimensionPixelSize(R.dimen.gallery_item_spacing)));
        recyclerView.setAdapter(galleryAdapter);
        recyclerView.addOnScrollListener(loadMoreTriggerListener);
    }

    @Override
    public boolean isAllowLoadMore() {
        return galleryAdapter.isLoadingItemEnabled()
                && !refreshLayout.isRefreshing()
                && !isLoadingFromNetwork;
    }

    private void setupBottomSheet() {
        bottomSheetReviewImageSlider.setup(this);
    }

    private void loadDataFromNetworkFirstPage() {
        resetState();
        loadDataFromNetwork(0);
    }

    private void resetState() {
        dismissRetrySnackbar();
        unsubscribeNetworkRequests();
        galleryAdapter.resetState();
        loadMoreTriggerListener.resetState();
        bottomSheetReviewImageSlider.resetState();
        isLoadingFromNetwork = false;
    }

    private void dismissRetrySnackbar() {
        if (snackbarRetry != null && snackbarRetry.isShown()) {
            snackbarRetry.hideRetrySnackbar();
        }
    }

    private void unsubscribeNetworkRequests() {
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }

    private void loadDataFromNetwork(final int startRow) {
        if (isLoadingFromNetwork) {
            return;
        }
        isLoadingFromNetwork = true;
        ImageService imageService = RetrofitHelper.createRetrofit(this).create(ImageService.class);
        subscription = imageService.loadData(startRow)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        bottomSheetReviewImageSlider.onLoadDataFailed();
                        loadMoreTriggerListener.updateStateAfterGetData();
                        hideRefreshLayout();
                        galleryAdapter.removeLoading();

                        snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(ReviewGalleryActivity.this, new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                loadDataFromNetwork(startRow);
                                bottomSheetReviewImageSlider.onLoadDataRetry();
                                galleryAdapter.addLoading();
                            }
                        });
                        snackbarRetry.showRetrySnackbar();
                        isLoadingFromNetwork = false;
                    }

                    @Override
                    public void onNext(ResponseModel response) {
                        if (!response.getData().getProductItems().isEmpty()) {
                            galleryAdapter.appendItems(response.getData().getProductItems());
                            bottomSheetReviewImageSlider.onLoadDataSuccess(response.getData().getProductItems());
                            loadMoreTriggerListener.updateStateAfterGetData();
                            loadMoreTriggerListener.setHasNextPage(true);
                            hideRefreshLayout();
                        } else {
                            bottomSheetReviewImageSlider.onLoadDataEmpty();
                            loadMoreTriggerListener.updateStateAfterGetData();
                            loadMoreTriggerListener.setHasNextPage(false);
                            galleryAdapter.removeLoading();
                        }
                        isLoadingFromNetwork = false;
                    }
                });
    }

    private void hideRefreshLayout() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onBackPressed() {
        if (!bottomSheetReviewImageSlider.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestLoadMore() {
        loadDataFromNetwork(galleryAdapter.getGalleryItemCount());
    }

    @Override
    public void onGalleryItemClicked(int position) {
        bottomSheetReviewImageSlider.displayImage(position);
    }

    public static class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        protected List<ProductItem> productItemList = new ArrayList<>();
        private GalleryView galleryView;
        private boolean loadingItemEnabled = true;

        public GalleryAdapter(GalleryView galleryView) {
            this.galleryView = galleryView;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(viewType, parent, false);
            if (viewType == getItemLayoutRes()) {
                return getItemViewHolder(view);
            } else {
                return getLoadingViewHolder(view);
            }
        }

        protected int getItemLayoutRes() {
            return GalleryItemViewHolder.LAYOUT;
        }

        protected int getLoadingLayoutRes() {
            return LoadingViewHolder.LAYOUT;
        }

        protected RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new GalleryItemViewHolder(view, galleryView);
        }

        protected RecyclerView.ViewHolder getLoadingViewHolder(View view) {
            return new LoadingViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof GalleryItemViewHolder) {
                ((GalleryItemViewHolder) holder).bind(productItemList.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return loadingItemEnabled ? productItemList.size() + 1 : productItemList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (isGalleryItem(position)) {
                return getItemLayoutRes();
            } else {
                return getLoadingLayoutRes();
            }
        }

        public void appendItems(List<ProductItem> productItems) {
            productItemList.addAll(productItems);
            notifyDataSetChanged();
        }

        public void resetState() {
            productItemList.clear();
            loadingItemEnabled = true;
            notifyDataSetChanged();
        }

        public boolean isGalleryItem(int position) {
            return position < productItemList.size();
        }

        public void removeLoading() {
            loadingItemEnabled = false;
            notifyDataSetChanged();
        }

        public void addLoading() {
            loadingItemEnabled = true;
            notifyDataSetChanged();
        }

        public boolean isLoadingItemEnabled() {
            return loadingItemEnabled;
        }

        public int getGalleryItemCount() {
            return productItemList.size();
        }
    }

    public static class GalleryItemViewHolder extends RecyclerView.ViewHolder {

        @LayoutRes
        public static final int LAYOUT = R.layout.gallery_item;

        ImageView galleryImage;
        private GalleryView galleryView;

        public GalleryItemViewHolder(View itemView, GalleryView galleryView) {
            super(itemView);
            galleryImage = itemView.findViewById(R.id.galleryImage);
            this.galleryView = galleryView;
        }

        public void bind(ProductItem productItem) {
            ImageHandler.LoadImage(galleryImage, productItem.getImageUrl());
            galleryImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    galleryView.onGalleryItemClicked(getAdapterPosition());
                }
            });
        }
    }

    private static class LoadingViewHolder extends RecyclerView.ViewHolder {

        @LayoutRes
        public static final int LAYOUT = R.layout.gallery_loading_item;

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}

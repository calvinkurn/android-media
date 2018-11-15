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
import com.tokopedia.tkpdpdp.customview.bottomsheetimagereview.BottomSheetImageReviewSlider;
import com.tokopedia.tkpdpdp.decoration.GalleryItemDecoration;
import com.tokopedia.tkpdpdp.presenter.ReviewGalleryPresenter;
import com.tokopedia.tkpdpdp.presenter.ReviewGalleryPresenterImpl;
import com.tokopedia.tkpdpdp.viewmodel.ImageReviewItem;

import java.util.ArrayList;
import java.util.List;

public class ImageReviewGalleryActivity extends AppCompatActivity implements BottomSheetImageReviewSlider.Callback, GalleryView {

    private static final int SPAN_COUNT = 3;

    private RecyclerView recyclerView;
    private BottomSheetImageReviewSlider bottomSheetImageReviewSlider;
    private GalleryAdapter galleryAdapter;
    private SnackbarRetry snackbarRetry;

    private EndlessRecyclerViewScrollListener loadMoreTriggerListener;
    private SwipeRefreshLayout refreshLayout;
    private boolean isLoadingFromNetwork = false;
    private View backButton;
    private ReviewGalleryPresenter presenter;

    public static void moveTo(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, ImageReviewGalleryActivity.class);
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
        initPresenter();
        loadDataFromNetworkFirstPage();
    }

    private void bindView() {
        recyclerView = findViewById(R.id.galleryRecyclerView);
        bottomSheetImageReviewSlider = findViewById(R.id.bottomSheetImageSlider);
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
        bottomSheetImageReviewSlider.setup(this);
    }

    private void initPresenter() {
        presenter = new ReviewGalleryPresenterImpl();
    }

    private void loadDataFromNetworkFirstPage() {
        resetState();
        loadDataFromNetwork(0);
    }

    private void resetState() {
        dismissRetrySnackbar();
        presenter.cancelLoadDataRequest();
        galleryAdapter.resetState();
        loadMoreTriggerListener.resetState();
        bottomSheetImageReviewSlider.resetState();
        isLoadingFromNetwork = false;
    }

    private void dismissRetrySnackbar() {
        if (snackbarRetry != null && snackbarRetry.isShown()) {
            snackbarRetry.hideRetrySnackbar();
        }
    }

    private void loadDataFromNetwork(final int startRow) {
        if (isLoadingFromNetwork) {
            return;
        }
        isLoadingFromNetwork = true;
        presenter.loadData(this, 266635420, startRow, new ReviewGalleryPresenter.LoadDataListener() {
            @Override
            public void onSuccess(List<ImageReviewItem> imageReviewItemList) {
                if (!imageReviewItemList.isEmpty()) {
                    galleryAdapter.appendItems(imageReviewItemList);
                    bottomSheetImageReviewSlider.onLoadDataSuccess(imageReviewItemList);
                    loadMoreTriggerListener.updateStateAfterGetData();
                    loadMoreTriggerListener.setHasNextPage(true);
                    hideRefreshLayout();
                } else {
                    bottomSheetImageReviewSlider.onLoadDataEmpty();
                    loadMoreTriggerListener.updateStateAfterGetData();
                    loadMoreTriggerListener.setHasNextPage(false);
                    galleryAdapter.removeLoading();
                }
                isLoadingFromNetwork = false;
            }

            @Override
            public void onFailed() {
                bottomSheetImageReviewSlider.onLoadDataFailed();
                loadMoreTriggerListener.updateStateAfterGetData();
                hideRefreshLayout();
                galleryAdapter.removeLoading();

                snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(ImageReviewGalleryActivity.this, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        loadDataFromNetwork(startRow);
                        bottomSheetImageReviewSlider.onLoadDataRetry();
                        galleryAdapter.addLoading();
                    }
                });
                snackbarRetry.showRetrySnackbar();
                isLoadingFromNetwork = false;
            }
        });
    }

    private void hideRefreshLayout() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onBackPressed() {
        if (!bottomSheetImageReviewSlider.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestLoadMore() {
        loadDataFromNetwork(galleryAdapter.getGalleryItemCount());
    }

    @Override
    public void onGalleryItemClicked(int position) {
        bottomSheetImageReviewSlider.displayImage(position);
    }

    public static class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        protected List<ImageReviewItem> imageReviewItemList = new ArrayList<>();
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
                ((GalleryItemViewHolder) holder).bind(imageReviewItemList.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return loadingItemEnabled ? imageReviewItemList.size() + 1 : imageReviewItemList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (isGalleryItem(position)) {
                return getItemLayoutRes();
            } else {
                return getLoadingLayoutRes();
            }
        }

        public void appendItems(List<ImageReviewItem> imageReviewItems) {
            imageReviewItemList.addAll(imageReviewItems);
            notifyDataSetChanged();
        }

        public void resetState() {
            imageReviewItemList.clear();
            loadingItemEnabled = true;
            notifyDataSetChanged();
        }

        public boolean isGalleryItem(int position) {
            return position < imageReviewItemList.size();
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
            return imageReviewItemList.size();
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

        public void bind(ImageReviewItem imageReviewItem) {
            ImageHandler.LoadImage(galleryImage, imageReviewItem.getImageUrlThumbnail());
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

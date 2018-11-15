package com.tokopedia.tkpdpdp.customview.bottomsheetimagereview;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.tkpdpdp.GalleryView;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.ImageReviewGalleryActivity;
import com.tokopedia.tkpdpdp.customview.RatingView;
import com.tokopedia.tkpdpdp.viewmodel.ImageReviewItem;

import java.util.List;

/**
 * Created by henrypriyono on 12/03/18.
 */

public class BottomSheetImageReviewSlider extends FrameLayout implements ImageReviewSliderView {

    private UserLockBottomSheetBehavior bottomSheetBehavior;
    private View rootView;
    private View backButton;
    private View bottomSheetLayout;
    private RecyclerView recyclerView;
    private SliderAdapter adapter;

    private Callback callback;

    private EndlessRecyclerViewScrollListener loadMoreTriggerListener;

    public BottomSheetImageReviewSlider(@NonNull Context context) {
        super(context);
        init();
    }

    public BottomSheetImageReviewSlider(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BottomSheetImageReviewSlider(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
    }

    private void init() {
        bindView();
        initRecyclerView();
    }

    private void bindView() {
        rootView = inflate(getContext(), R.layout.review_image_slider, this);
        recyclerView = rootView.findViewById(R.id.review_image_slider_recycler_view);
        backButton = rootView.findViewById(R.id.backButton);
        bottomSheetLayout = this;
    }

    private void initRecyclerView() {
        adapter = new SliderAdapter(null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        loadMoreTriggerListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (callback.isAllowLoadMore()) {
                    callback.onRequestLoadMore();
                } else {
                    updateStateAfterGetData();
                }
            }
        };
        recyclerView.addOnScrollListener(loadMoreTriggerListener);
    }

    public void setup(Callback callback) {
        this.callback = callback;
        initListener();
    }

    public void closeView() {
        if (bottomSheetBehavior != null
                && bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    private void initListener() {
        bottomSheetBehavior = (UserLockBottomSheetBehavior) UserLockBottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeView();
            }
        });
    }

    public boolean isBottomSheetShown() {
        return bottomSheetBehavior != null
                && bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN;
    }

    @Override
    public boolean onBackPressed() {
        if (isBottomSheetShown()) {
            closeView();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void resetState() {
        adapter.resetState();
        loadMoreTriggerListener.resetState();
    }

    @Override
    public void onLoadDataEmpty() {
        adapter.removeLoading();
    }

    @Override
    public void onLoadDataRetry() {
        adapter.addLoading();
    }

    @Override
    public void displayImage(int position) {
        recyclerView.scrollToPosition(position);
        showBottomSheet();
    }

    private void showBottomSheet() {
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public void onLoadDataSuccess(List<ImageReviewItem> imageReviewItems) {
        adapter.appendItems(imageReviewItems);
        loadMoreTriggerListener.updateStateAfterGetData();
    }

    @Override
    public void onLoadDataFailed() {
        loadMoreTriggerListener.updateStateAfterGetData();
        recyclerView.scrollToPosition(adapter.getGalleryItemCount() - 1);
        adapter.removeLoading();
    }

    public interface Callback {
        void onRequestLoadMore();
        boolean isAllowLoadMore();
    }

    private static class SliderAdapter extends ImageReviewGalleryActivity.GalleryAdapter {

        public SliderAdapter(GalleryView galleryView) {
            super(galleryView);
        }

        @Override
        protected int getItemLayoutRes() {
            return ImageSliderViewHolder.LAYOUT;
        }

        protected int getLoadingLayoutRes() {
            return LoadingSliderViewHolder.LAYOUT;
        }

        protected RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ImageSliderViewHolder(view);
        }

        protected RecyclerView.ViewHolder getLoadingViewHolder(View view) {
            return new LoadingSliderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ImageSliderViewHolder) {
                ((ImageSliderViewHolder) holder).bind(imageReviewItemList.get(position));
            }
        }
    }

    private static class ImageSliderViewHolder extends RecyclerView.ViewHolder {

        @LayoutRes
        public static final int LAYOUT = R.layout.review_image_slider_item;

        private ImageView imageView;
        private TextView date;
        private TextView name;
        private ImageView rating;

        public ImageSliderViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.review_image_slider_item_image_view);
            date = itemView.findViewById(R.id.review_image_slider_date);
            name = itemView.findViewById(R.id.review_image_slider_name);
            rating = itemView.findViewById(R.id.review_image_slider_rating);
        }

        public void bind(ImageReviewItem item) {
            ImageHandler.LoadImage(imageView, item.getImageUrlLarge());
            name.setText(item.getReviewerName());
            date.setText(item.getFormattedDate());
            rating.setImageResource(RatingView.getRatingDrawable(item.getRating()));
        }
    }

    private static class LoadingSliderViewHolder extends RecyclerView.ViewHolder {

        @LayoutRes
        public static final int LAYOUT = R.layout.review_image_slider_loading;

        public LoadingSliderViewHolder(View itemView) {
            super(itemView);
        }
    }
}

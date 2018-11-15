package com.tokopedia.tkpdpdp.customview.bottomsheetimagereview;

import com.tokopedia.tkpdpdp.viewmodel.ImageReviewItem;

import java.util.List;

public interface ImageReviewSliderView {
        void displayImage(int position);
        void onLoadDataSuccess(List<ImageReviewItem> imageReviewItems);
        void onLoadDataFailed();
        boolean onBackPressed();
        void resetState();
        void onLoadDataEmpty();

        void onLoadDataRetry();
}

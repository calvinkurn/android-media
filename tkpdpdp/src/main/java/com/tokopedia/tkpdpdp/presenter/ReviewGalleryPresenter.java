package com.tokopedia.tkpdpdp.presenter;

import android.content.Context;

import com.tokopedia.tkpdpdp.viewmodel.ImageReviewItem;

import java.util.List;

public interface ReviewGalleryPresenter {
    void cancelLoadDataRequest();
    void loadData(Context context, int productId, int startRow, LoadDataListener loadDataListener);

    interface LoadDataListener {
        void onSuccess(List<ImageReviewItem> imageReviewItemList);
        void onFailed();
    }
}

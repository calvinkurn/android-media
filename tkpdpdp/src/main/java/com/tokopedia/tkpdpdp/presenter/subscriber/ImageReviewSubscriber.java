package com.tokopedia.tkpdpdp.presenter.subscriber;

import com.tokopedia.gallery.viewmodel.ImageReviewItem;
import com.tokopedia.gallery.viewmodel.ImageReviewListModel;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import java.util.List;

import rx.Subscriber;

public class ImageReviewSubscriber extends Subscriber<ImageReviewListModel> {
    private final ProductDetailView viewListener;

    public ImageReviewSubscriber(ProductDetailView viewListener){
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(ImageReviewListModel imageReviewListModels) {
        List<ImageReviewItem> imageReviewItems = imageReviewListModels.getImageReviewItemList();
        if(imageReviewItems != null &&
                imageReviewItems.size() != 0){
            viewListener.onImageReviewLoaded(
                    imageReviewItems
            );
        }
    }
}
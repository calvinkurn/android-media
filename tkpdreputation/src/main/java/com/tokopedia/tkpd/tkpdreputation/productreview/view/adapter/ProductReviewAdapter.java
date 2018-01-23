package com.tokopedia.tkpd.tkpdreputation.productreview.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;

import java.util.Iterator;

/**
 * Created by zulfikarrahman on 1/22/18.
 */

public class ProductReviewAdapter<T extends ProductReviewModel, F extends ProductReviewTypeFactoryAdapter> extends BaseListAdapter<T, F> {
    private static final int LIKE_STATUS_ACTIVE = 1;

    public ProductReviewAdapter(F baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory);
    }

    public ProductReviewAdapter(F baseListAdapterTypeFactory, OnAdapterInteractionListener<T> onAdapterInteractionListener) {
        super(baseListAdapterTypeFactory, onAdapterInteractionListener);
    }

    public void updateLikeStatus(int likeStatus, int totalLike, String reviewId) {
        int i = 0;
        for (Iterator<T> it = getData().iterator(); it.hasNext(); ) {
            ProductReviewModel productReviewModel = it.next();
            if(productReviewModel instanceof ProductReviewModelContent) {
                ProductReviewModelContent productReviewModelContent = (ProductReviewModelContent)productReviewModel;
                if (productReviewModelContent.getReviewId().equalsIgnoreCase(reviewId)) {
                    productReviewModelContent.setLikeStatus(likeStatus == LIKE_STATUS_ACTIVE);
                    productReviewModelContent.setTotalLike(totalLike);
                    notifyItemChanged(i);
                    return;
                }
            }
            i++;
        }
    }

    public void updateDeleteReview(String reviewId) {
        int i = 0;
        for (Iterator<T> it = getData().iterator(); it.hasNext(); ) {
            ProductReviewModel productReviewModel = it.next();
            if(productReviewModel instanceof ProductReviewModelContent) {
                ProductReviewModelContent productReviewModelContent = (ProductReviewModelContent)productReviewModel;
                if (productReviewModelContent.getReviewId().equalsIgnoreCase(reviewId)) {
                    productReviewModelContent.setReviewHasReplied(false);
                    notifyItemChanged(i);
                    return;
                }
            }
            i++;
        }
    }
}

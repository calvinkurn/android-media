package com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter;

import android.view.View;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductModelContent;

/**
 * Created by normansyahputa on 2/14/18.
 */

public class ReviewShopSeeMoreViewHolder extends AbstractViewHolder<ReviewShopSeeMoreModelContent> {
    public static final int LAYOUT = R.layout.item_shop_review_see_more;
    private final Button buttonSeeMore;
    private ShopReviewSeeMoreHolderListener shopReviewSeeMoreHolderListener;

    public ReviewShopSeeMoreViewHolder(View itemView, ShopReviewSeeMoreHolderListener shopReviewSeeMoreHolderListener) {
        super(itemView);

        buttonSeeMore = itemView.findViewById(R.id.button_see_more);
        this.shopReviewSeeMoreHolderListener = shopReviewSeeMoreHolderListener;
    }

    @Override
    public void bind(ReviewShopSeeMoreModelContent reviewShopSeeMoreModelContent) {

    }

    public interface ShopReviewSeeMoreHolderListener{
        void onGoToMoreReview();
    }
}

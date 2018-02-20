package com.tokopedia.tkpd.tkpdreputation.review.shop.view;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter.ReviewShopInfoTypeFactoryAdapter;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter.ReviewShopModelContent;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter.ReviewShopSeeMoreModelContent;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter.ReviewShopSeeMoreViewHolder;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter.ReviewShopTypeFactoryAdapter;

import java.util.List;

/**
 * Created by normansyahputa on 2/14/18.
 */

public class ReviewShopInfoFragment extends ReviewShopFragment implements ReviewShopSeeMoreViewHolder.ShopReviewSeeMoreHolderListener {

    public static ReviewShopInfoFragment createInstance(String shopId, String shopDomain) {
        ReviewShopInfoFragment shopReviewFragment = new ReviewShopInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SHOP_ID, shopId);
        bundle.putString(SHOP_DOMAIN, shopDomain);
        shopReviewFragment.setArguments(bundle);
        return shopReviewFragment;
    }

    @Override
    protected ReviewShopTypeFactoryAdapter getAdapterTypeFactory() {
        return new ReviewShopInfoTypeFactoryAdapter(this, this, this);
    }

    @Override
    public void onGoToMoreReview() {
        startActivity(ReviewShopInfoActivity.createIntent(getActivity(), shopId, shopDomain));
    }

    @Override
    protected boolean isLoadMoreEnabledByDefault() {
        return false;
    }


    @Override
    public void renderList(@NonNull List<ReviewShopModelContent> list, boolean b) {
        boolean isSeeMoreEnabled = false;
        if(!list.isEmpty() && list.size() >= 10 && b){
            // add see more
            isSeeMoreEnabled = true;

            list = list.subList(0,10);
        }
        super.renderList(list, b);
        if(isSeeMoreEnabled) {
            getAdapter().addElement(new ReviewShopSeeMoreModelContent());
        }
    }
}

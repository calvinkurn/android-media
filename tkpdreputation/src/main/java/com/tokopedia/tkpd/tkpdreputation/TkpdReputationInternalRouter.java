package com.tokopedia.tkpd.tkpdreputation;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationActivity;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.ReviewProductActivity;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.ReviewShopFragment;


public class TkpdReputationInternalRouter {

    public static Intent getProductReviewIntent(Context context, String productId, String productName) {
        return ReviewProductActivity.createIntent(context, productId, productName);
    }

    public static Fragment getReviewShopFragment(String shopId, String shopDomain) {
        return ReviewShopFragment.createInstance(shopId, shopDomain);
    }

    public static Intent getInboxReputationActivityIntent(Context context) {
        return new Intent(context, InboxReputationActivity.class);
    }

}

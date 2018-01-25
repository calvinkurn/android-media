package com.tokopedia.tkpd.tkpdreputation;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.tkpd.tkpdreputation.productreview.view.ProductReviewActivity;

public class TkpdReputationInternalRouter {

    public static Intent getProductReviewIntent(Context context, String productId) {
        return ProductReviewActivity.createIntent(context, productId);
    }

}

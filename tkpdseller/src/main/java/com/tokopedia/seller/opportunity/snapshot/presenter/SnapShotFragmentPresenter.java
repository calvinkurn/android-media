package com.tokopedia.seller.opportunity.snapshot.presenter;

import android.content.Context;

import com.tokopedia.core.router.productdetail.passdata.ProductPass;

/**
 * Created by hangnadi on 3/1/17.
 */
public interface SnapShotFragmentPresenter {
    void processDataPass(ProductPass productPass);

    void requestProductDetail(Context context, ProductPass productPass, int type, boolean forceNetwork);

}

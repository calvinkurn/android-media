package com.tokopedia.seller.opportunity.snapshot.listener;

import android.os.Bundle;

import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.seller.opportunity.presentation.ActionViewData;

/**
 * Created by hangnadi on 3/1/17.
 */
public interface SnapShotFragmentView {
    void onProductDetailLoaded(ProductDetailData productData);

    void onProductPictureClicked(Bundle bundle);

    void showProgressLoading();

    void hideProgressLoading();

    void showProductDetailRetry();

    void onNullData();

    void showToastMessage(String message);

    void closeView();

    void showFullScreenError();

    void renderTempProductData(ProductPass productPass);

    void onProductShopNameClicked(Bundle bundle);

    void onProductShopRatingClicked(Bundle bundle);

    void onProductShopAvatarClicked(Bundle bundle);
}

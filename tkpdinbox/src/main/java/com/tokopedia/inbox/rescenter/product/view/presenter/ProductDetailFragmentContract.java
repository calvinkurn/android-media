package com.tokopedia.inbox.rescenter.product.view.presenter;

import com.tokopedia.inbox.rescenter.product.view.model.ProductDetailViewData;

/**
 * Created by hangnadi on 3/28/17.
 */

public interface ProductDetailFragmentContract {
    interface Presenter {
        void onFirstTimeLaunched();

        void setOnDestroyView();
    }

    interface ViewListener {

        String getResolutionID();

        void setResolutionID(String resolutionID);

        String getTroubleID();

        void setTroubleID(String troubleID);

        void setLoadingView(boolean show);

        void setMainView(boolean show);

        void setViewData(ProductDetailViewData viewData);

        ProductDetailViewData getViewData();

        void renderData();

        void onGetProductDetailFailed(String messageError);

        void onGetProductDetailTimeOut();
    }
}

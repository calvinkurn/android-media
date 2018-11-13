package com.tokopedia.tkpdpdp;

import com.tokopedia.tkpdpdp.viewmodel.ProductItem;

import java.util.List;

public interface ReviewImageSliderView {
        void displayImage(int position);
        void onLoadDataSuccess(List<ProductItem> productItems);
        void onLoadDataFailed();
        boolean onBackPressed();
        void resetState();
        void onLoadDataEmpty();

        void onLoadDataRetry();
}

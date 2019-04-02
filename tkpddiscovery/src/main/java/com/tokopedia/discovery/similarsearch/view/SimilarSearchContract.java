package com.tokopedia.discovery.similarsearch.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.discovery.similarsearch.model.ProductsItem;

import java.util.List;

public interface SimilarSearchContract {
    public interface View extends CustomerView {

        @Nullable Context getAppContext();

        String getProductID();

        void setProductList(List<ProductsItem> productList);
        void setEmptyLayoutVisible();
        void setContentLayoutGone();

        boolean isUserHasLogin();

        void disableWishlistButton(int adapterPosition);
        void enableWishlistButton(int adapterPosition);
        String getUserId();
        void launchLoginActivity(Bundle extras);


        String getScreenName();
    }

    public interface Presenter {
        public void handleWishlistButtonClicked(ProductsItem productItem,int position) ;

    }
}

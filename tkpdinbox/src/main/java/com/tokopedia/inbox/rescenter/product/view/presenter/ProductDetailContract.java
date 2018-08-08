package com.tokopedia.inbox.rescenter.product.view.presenter;

import android.app.Fragment;

/**
 * Created by hangnadi on 3/28/17.
 */

public interface ProductDetailContract {
    interface Presenter {
        void generateFragment();
    }

    interface ViewListener {
        Fragment getFragment();

        void setFragment(Fragment fragment);

        String getResolutionID();

        void setResolutionID(String resolutionID);

        String getTroubleID();

        void setTroubleID(String troubleID);

        String getProductName();

        void setProductName(String productName);

        void inflateFragment();

    }
}

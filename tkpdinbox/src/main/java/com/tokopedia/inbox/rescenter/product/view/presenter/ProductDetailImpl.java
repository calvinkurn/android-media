package com.tokopedia.inbox.rescenter.product.view.presenter;

import com.tokopedia.inbox.rescenter.product.ProductDetailFragment;

/**
 * Created by hangnadi on 3/28/17.
 */

public class ProductDetailImpl implements ProductDetailContract.Presenter {

    private final ProductDetailContract.ViewListener viewListener;

    public ProductDetailImpl(ProductDetailContract.ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void generateFragment() {
        viewListener.setFragment(
                ProductDetailFragment.createInstance(
                        viewListener.getResolutionID(),
                        viewListener.getTroubleID()
                )
        );
    }

    private String generateExtras() {
        return viewListener.getResolutionID();
    }
}

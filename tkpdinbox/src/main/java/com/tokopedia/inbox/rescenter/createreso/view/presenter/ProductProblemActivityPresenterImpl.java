package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import android.content.Context;

import com.tokopedia.inbox.rescenter.createreso.view.activity.ProductProblemActivity;
import com.tokopedia.inbox.rescenter.createreso.view.activity.ProductProblemView;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.ChooseProductAndProblemFragment;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblemActivityPresenter;

/**
 * Created by yoasfs on 15/08/17.
 */

public class ProductProblemActivityPresenterImpl implements ProductProblemActivityPresenter {
    private Context context;
    private ProductProblemView mainView;

    public ProductProblemActivityPresenterImpl(Context context, ProductProblemView productProblemView) {
        this.context = context;
        this.mainView = productProblemView;
    }

    @Override
    public void initFragment() {
        mainView.inflateFragment(ChooseProductAndProblemFragment.newInstance(),
                ChooseProductAndProblemFragment.class.getSimpleName());
    }
}

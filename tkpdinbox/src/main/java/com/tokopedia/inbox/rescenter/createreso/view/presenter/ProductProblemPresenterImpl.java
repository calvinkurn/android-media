package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.inbox.rescenter.createreso.view.activity.ProductProblemView;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.ChooseProductAndProblemFragment;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.CreateResolutionCenterFragment;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ProductProblemPresenterImpl implements ProductProblemPresenter {


    private final ProductProblemView mainView;

    public ProductProblemPresenterImpl(ProductProblemView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void initFragment(@NonNull Context context, Uri uriData, Bundle bundleData) {
        mainView.inflateFragment(ChooseProductAndProblemFragment.newInstance(),
                ChooseProductAndProblemFragment.class.getSimpleName());
    }
}

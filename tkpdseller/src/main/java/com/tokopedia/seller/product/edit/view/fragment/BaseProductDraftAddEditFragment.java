package com.tokopedia.seller.product.edit.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.view.presenter.ProductDraftPresenter;
import com.tokopedia.seller.product.edit.view.presenter.ProductDraftView;

/**
 * Created by nakama on 12/02/18.
 */

public abstract class BaseProductDraftAddEditFragment<T extends ProductDraftPresenter>
        extends BaseProductEditFragment<T>
        implements ProductDraftView {

    protected static final String DRAFT_PRODUCT_ID = "DRAFT_PRODUCT_ID";

    private long draftProductId;

    @Override
    public boolean isNeedGetCategoryRecommendation() {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        draftProductId = getArguments().getLong(DRAFT_PRODUCT_ID);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void fetchInputData() {
        showLoading();
        presenter.fetchDraftData(draftProductId);
    }

    @Override
    public long getProductDraftId() {
        return draftProductId;
    }

    @Override
    public String getErrorLoadProductString() {
        return getString(R.string.product_draft_error_cannot_load_draft);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(DRAFT_PRODUCT_ID, draftProductId);
    }
}

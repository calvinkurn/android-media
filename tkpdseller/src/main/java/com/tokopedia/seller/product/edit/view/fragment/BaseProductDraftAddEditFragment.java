package com.tokopedia.seller.product.edit.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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

    public static final String DRAFT_PRODUCT_ID = "DRAFT_PRODUCT_ID";

    private String draftId;

    @Override
    public boolean isNeedGetCategoryRecommendation() {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        draftId = getArguments().getString(DRAFT_PRODUCT_ID);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void fetchInputData() {
        showLoading();
        presenter.fetchDraftData(draftId);
    }

    @Override
    public long getProductDraftId() {
        if (TextUtils.isEmpty(draftId)) {
            return 0;
        }
        try {
            return Long.valueOf(draftId);
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public String getErrorLoadProductString() {
        return getString(R.string.product_draft_error_cannot_load_draft);
    }
}

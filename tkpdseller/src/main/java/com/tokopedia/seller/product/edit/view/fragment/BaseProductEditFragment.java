package com.tokopedia.seller.product.edit.view.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.constant.UploadToTypeDef;
import com.tokopedia.seller.product.edit.view.model.edit.ProductVideoViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.presenter.ProductAddPresenter;
import com.tokopedia.seller.product.edit.view.presenter.ProductEditView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseProductEditFragment<T extends ProductAddPresenter>
        extends BaseProductAddEditFragment<T>
        implements ProductEditView{

    private TkpdProgressDialog tkpdProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (savedInstanceState == null) {
            fetchInputData();
        }
        return view;
    }

    public abstract void fetchInputData();

    @CallSuper
    public void onSuccessLoadProduct(ProductViewModel model) {
        super.onSuccessLoadProduct(model);
        hideLoading();
    }

    protected void showLoading() {
        if (tkpdProgressDialog==null) {
            tkpdProgressDialog = new TkpdProgressDialog(getActivity(),
                    TkpdProgressDialog.NORMAL_PROGRESS, getString(R.string.edit_product));
        }
        tkpdProgressDialog.showDialog();
    }

    protected void hideLoading() {
        if (tkpdProgressDialog!= null) {
            tkpdProgressDialog.dismiss();
        }
    }

    @Override
    public void onErrorFetchEditProduct(Throwable throwable) {
        hideLoading();
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                showLoading();
                fetchInputData();
            }
        }).showRetrySnackbar();
    }

    @Override
    public void onErrorLoadProduct(Throwable throwable) {
        hideLoading();
        CommonUtils.UniversalToast(getActivity(), getErrorLoadProductString());
        getActivity().finish();
    }

    public abstract String getErrorLoadProductString();

}

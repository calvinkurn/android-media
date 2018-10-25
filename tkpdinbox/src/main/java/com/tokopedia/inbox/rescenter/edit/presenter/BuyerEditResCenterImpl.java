package com.tokopedia.inbox.rescenter.edit.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core2.R;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.DetailResCenterData;
import com.tokopedia.inbox.rescenter.edit.facade.NetworkParam;
import com.tokopedia.inbox.rescenter.edit.interactor.RetrofitInteractor;
import com.tokopedia.inbox.rescenter.edit.interactor.RetrofitInteractorImpl;
import com.tokopedia.inbox.rescenter.edit.listener.BuyerEditResCenterListener;
import com.tokopedia.inbox.rescenter.edit.model.responsedata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;

/**
 * Created on 8/24/16.
 */
public class BuyerEditResCenterImpl implements BuyerEditResCenterPresenter {

    private static final int ARGS_PARAM_RECEIVED = 1;
    private static final int ARGS_PARAM_NOT_RECEIVED = 0;
    private final BuyerEditResCenterListener listener;
    private final RetrofitInteractor retrofit;

    public BuyerEditResCenterImpl(BuyerEditResCenterListener listener) {
        this.listener = listener;
        this.retrofit = new RetrofitInteractorImpl();
    }

    @Override
    public void setOnLaunching(@NonNull Context context) {
        listener.setLoading(false);
        listener.setMainView(true);

        listener.setCategoryTroubleViewVisibility(false);
        listener.setProductTroubleListViewVisibility(false);
        listener.setTroubleViewVisibility(false);

        listener.renderPackageReceivedFormView();
    }

    @Override
    public void renderView(EditResCenterFormData formData) {
        listener.renderCategoryTroubleView(formData);
        listener.renderProductTroubleListView(formData);
        listener.renderTroubleView(formData);
        listener.renderInvoice(formData);
        listener.renderShop(formData);
        listener.setPassData(storeFormData(formData));
        listener.setCategoryTroubleViewVisibility(true);
    }

    @Override
    public void setOnRadioPackageStatus(boolean received) {
        if (received) {
            this.setOnRadioPackageReceived();
        } else {
            this.setOnRadioPackageNotReceived();
        }
    }

    @Override
    public void setOnRadioPackageReceived() {
        getEditResCenterFormData(
                listener.getBaseContext(),
                listener.getDetailData(),
                ARGS_PARAM_RECEIVED);
    }

    @Override
    public void setOnRadioPackageNotReceived() {
        getEditResCenterFormData(
                listener.getBaseContext(),
                listener.getDetailData(),
                ARGS_PARAM_NOT_RECEIVED);
    }

    private void getEditResCenterFormData(Context context, DetailResCenterData detailData, int stateReceived) {
        retrofit.getEditResolutionForm(context,
                NetworkParam.paramEditResCenter(detailData, stateReceived),
                new RetrofitInteractor.GetEditResolutionFormListener() {
                    @Override
                    public void onStart() {
                        listener.setLoading(true);
                        listener.setMainView(false);
                    }

                    @Override
                    public void onSuccess(EditResCenterFormData formData) {
                        listener.setLoading(false);
                        listener.setMainView(true);
                        renderView(formData);
                    }

                    @Override
                    public void onTimeOut(NetworkErrorHelper.RetryClickedListener rcListener) {
                        listener.setLoading(false);
                        listener.setMainView(false);
                        listener.setTimeOutView(rcListener);
                    }

                    @Override
                    public void onError(String message) {
                        listener.setLoading(false);
                        listener.setMainView(false);
                        listener.setErrorView(message);
                    }

                    @Override
                    public void onFailAuth() {
                        listener.setLoading(false);
                        listener.setMainView(false);
                        listener.setTimeOutView(null);
                    }
                });
    }

    private ActionParameterPassData storeFormData(EditResCenterFormData formData) {
        ActionParameterPassData passData = listener.getPassData();
        passData.setFormData(formData);
        return passData;
    }

    @Override
    public void setOnCategoryTroubleSelected(EditResCenterFormData.TroubleCategoryData troubleCategoryData) {
        if (troubleCategoryData.getProductRelated() == 1) {
            listener.setProductTroubleListViewVisibility(true);
            listener.setTroubleViewVisibility(false);
            listener.getEditTroubleView().resetSpinner();
        } else {
            listener.setProductTroubleListViewVisibility(false);
            listener.setTroubleViewVisibility(true);
            listener.getEditTroubleView().renderSpinner(troubleCategoryData);
            listener.getEditTroubleView().setPreviousData();
        }
    }

    @Override
    public void setOnCategoryTroubleNothingSelected() {
        listener.setProductTroubleListViewVisibility(false);
        listener.setTroubleViewVisibility(false);
        listener.getEditTroubleView().resetSpinner();
    }

    @Override
    public void setOnButtonNextClick(@NonNull Context context) {
        if (checkValidation(context)) {
            collectInputData();
            openNextStep();
        }
    }

    private void openNextStep() {
        if (isRelatedToProduct()) {
            listener.openProductDetailTroubleFragment();
        } else {
            listener.openSolutionFragment();
        }
    }

    private ActionParameterPassData collectInputData() {
        ActionParameterPassData passData = listener.getPassData();

        passData.setPackageStatus(listener.getEditPackageStatusView().getPackageState());
        passData.setTroubleCategoryChoosen(listener.getEditCategorySectionView().getTroubleCategoryChoosen());

        passData.setTroubleChoosen(!isRelatedToProduct() ? listener.getEditTroubleView().getTroubleChoosen() : null);
        passData.setInputDescription(!isRelatedToProduct() ? listener.getEditTroubleView().getDescription() : null);

        passData.setProductTroubleChoosenList(isRelatedToProduct() ? listener.getEditProductTroubleView().getProductTrouble() : null);

        listener.setPassData(passData);
        return passData;
    }

    private boolean isRelatedToProduct() {
        return listener.getEditCategorySectionView().getTroubleCategoryChoosen().getProductRelated() == 1;
    }

    private boolean checkValidation(Context context) {
        EditResCenterFormData.TroubleCategoryData categoryData = listener.getEditCategorySectionView().getTroubleCategoryChoosen();

        if (categoryData == null) {
            listener.setSnackBar(context.getString(R.string.error_choose_trouble_category));
            return false;
        }

        if (categoryData.getProductRelated() == 1
                && listener.getEditProductTroubleView().getProductTrouble().isEmpty()) {
            listener.setSnackBar(context.getString(R.string.error_choose_product_trouble));
            return false;
        }

        if (categoryData.getProductRelated() == 0
                && listener.getEditTroubleView().getTroubleChoosen() == null) {
            listener.setSnackBar(context.getString(R.string.error_choose_trouble_single));
            return false;
        }

        if (categoryData.getProductRelated() == 0
                && (listener.getEditTroubleView().getDescription() == null || listener.getEditTroubleView().getDescription().isEmpty())) {
            listener.setSnackBar(context.getString(R.string.error_input_desc_single));
            return false;
        }

        return true;

    }

    @Override
    public void unsubscribe() {
        retrofit.unsubscribe();
    }
}

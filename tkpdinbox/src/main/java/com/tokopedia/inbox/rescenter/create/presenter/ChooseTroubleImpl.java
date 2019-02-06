package com.tokopedia.inbox.rescenter.create.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core2.R;
import com.tokopedia.core.network.NetworkErrorHelper.RetryClickedListener;
import com.tokopedia.inbox.rescenter.create.facade.NetworkParam;
import com.tokopedia.inbox.rescenter.create.interactor.RetrofitInteractor;
import com.tokopedia.inbox.rescenter.create.interactor.RetrofitInteractorImpl;
import com.tokopedia.inbox.rescenter.create.listener.ChooseTroubleListener;
import com.tokopedia.inbox.rescenter.create.model.passdata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.create.model.responsedata.CreateResCenterFormData;

/**
 * Created on 6/16/16.
 */
public class ChooseTroubleImpl implements ChooseTroublePresenter {

    @SuppressWarnings("unused")
    private static final String TAG = ChooseTroublePresenter.class.getSimpleName();

    private final ChooseTroubleListener listener;
    private final RetrofitInteractor retrofit;

    public ChooseTroubleImpl(ChooseTroubleListener listener) {
        this.listener = listener;
        this.retrofit = new RetrofitInteractorImpl();
    }

    @Override
    public void setOnChooseSolutionClick(@NonNull Context context) {
        if (isRelatedToProduct(context)) {
            if (validationRelatedToProduct(context)) {
                listener.openProductDetailTroubleFragment();
            }
        } else {
            if (validationRelatedToSomeElse(context)) {
                listener.openSolutionFragment();
            }
        }
    }

    private boolean isRelatedToProduct(Context context) {
        if (listener.collectInputData().getTroubleCategoryChoosen() == null) {
            listener.showErrorMessage(context.getString(R.string.error_choose_trouble_category));
            return false;
        }
        return listener.collectInputData().getTroubleCategoryChoosen().getProductRelated() == 1;
    }

    private boolean validationRelatedToProduct(Context context) {
        ActionParameterPassData passData = listener.collectInputData();

        if (passData.getProductTroubleChoosenList().isEmpty()) {
            listener.showErrorMessage(context.getString(R.string.error_choose_product_trouble));
            return false;
        }

        return true;
    }

    private boolean validationRelatedToSomeElse(Context context) {
        ActionParameterPassData passData = listener.collectInputData();

        if (passData.getTroubleChoosen() == null) {
            listener.showErrorMessage(context.getString(R.string.error_choose_trouble_single));
            return false;
        }

        if (passData.getInputDescription() == null || passData.getInputDescription().isEmpty()) {
            listener.showErrorMessage(context.getString(R.string.error_input_desc_single));
            return false;
        }

        if (passData.getInputDescription().length() < 5) {
            listener.showErrorMessage(context.getString(R.string.error_min_5));
            return false;
        }

        return true;
    }

    @Override
    public void setOnFirstTimeLaunched(@NonNull Context context, ActionParameterPassData passData) {
        listener.showLoading(true);
        listener.showMainView(false);
        this.requestInitData(context, passData);
    }

    private void requestInitData(Context context, ActionParameterPassData passData) {
        retrofit.getFormCreateResCenter(context, NetworkParam.getFormCreateResCenter(passData),
                new RetrofitInteractor.FormResCenterListener() {
                    @Override
                    public void onSuccess(CreateResCenterFormData form) {
                        listener.renderData(form);
                        listener.showLoading(false);
                        listener.showMainView(true);
                    }

                    @Override
                    public void onTimeout(RetryClickedListener clickedListener) {
                        listener.setTimeOutFullView(clickedListener);
                        listener.showLoading(false);
                    }

                    @Override
                    public void onError(String error, RetryClickedListener clickedListener) {
                        listener.showLoading(false);
                        if (clickedListener == null) {
                            listener.setErrorFullView(error, new RetryClickedListener() {
                                @Override
                                public void onRetryClicked() {
                                    listener.finishActivity();
                                }
                            });
                        } else {
                            listener.setErrorFullView(error, clickedListener);
                        }
                    }

                    @Override
                    public void onNullData() {
                        listener.showLoading(false);
                        listener.setErrorFullView(null, new RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                listener.finishActivity();
                            }
                        });
                    }
                }
        );
    }

    @Override
    public void onsubscribe() {
        retrofit.unSubscribe();
    }
}

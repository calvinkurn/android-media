package com.tokopedia.inbox.rescenter.create.presenter;

import android.content.Context;

import com.tokopedia.core2.R;
import com.tokopedia.inbox.rescenter.create.listener.ChoooseProductTroubleListener;
import com.tokopedia.inbox.rescenter.create.listener.ChooseProductTroubleListener;
import com.tokopedia.inbox.rescenter.create.model.passdata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.create.model.passdata.PassProductTrouble;

/**
 * Created on 8/2/16.
 */
public class ChooseProductTroubleImpl implements ChoooseProductTroubleListener {

    private final ChooseProductTroubleListener listener;

    public ChooseProductTroubleImpl(ChooseProductTroubleListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSubmitButtonClicked(Context context) {
        ActionParameterPassData passData = listener.collectInputData();
        if (isValid(context, passData)) {
            listener.openSolutionFragment();
        }
    }

    private boolean isValid(Context context, ActionParameterPassData passData) {

        for (PassProductTrouble var : passData.getProductTroubleChoosenList()) {
            if (var.getTroubleData() == null) {
                listener.showErrorMessage(context.getString(R.string.error_choose_trouble));
                return false;
            }

            if (var.getInputDescription() == null || var.getInputDescription().isEmpty()) {
                listener.showErrorMessage(context.getString(R.string.error_input_desc_product_trouble));
                return false;
            }

            if (var.getInputDescription().trim().isEmpty()) {
                listener.showErrorMessage(context.getString(R.string.empty_desc));
                return false;
            }

            if (var.getInputDescription().trim().length() < 10) {
                listener.showErrorMessage(context.getString(R.string.error_desc_min_10));
                return false;
            }
        }

        return true;
    }
}

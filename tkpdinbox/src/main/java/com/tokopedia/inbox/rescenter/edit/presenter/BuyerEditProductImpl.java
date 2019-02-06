package com.tokopedia.inbox.rescenter.edit.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core2.R;
import com.tokopedia.inbox.rescenter.edit.customadapter.ProductAdapter;
import com.tokopedia.inbox.rescenter.edit.listener.BuyerEditProductListener;
import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;
import com.tokopedia.inbox.rescenter.edit.model.responsedata.PassProductTrouble;

import java.util.List;

/**
 * Created on 8/26/16.
 */
public class BuyerEditProductImpl implements BuyerEditProductPresenter {

    private static final String TAG = BuyerEditProductImpl.class.getSimpleName();

    private final BuyerEditProductListener listener;

    public BuyerEditProductImpl(BuyerEditProductListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSubmitButtonClicked(@NonNull Context context) {
        collectInputData();
        if (checkValid(context)) {
            listener.openSolutionFragment();
        }
    }

    private void collectInputData() {
        List<PassProductTrouble> list = listener.getPassData().getProductTroubleChoosenList();
        for (int i = 0; i < listener.getAdapter().getItemCount(); i++) {
            ProductAdapter.FormViewHolder holder = (ProductAdapter.FormViewHolder) listener.getProductRecyclerView().getChildViewHolder(listener.getProductRecyclerView().getChildAt(i));

            Log.d(TAG, "collectInputData: " + String.valueOf(holder.boxDesc.getText()));
            list.get(i).setInputQuantity(Integer.parseInt(String.valueOf(holder.value.getText())));

            Log.d(TAG, "collectInputData: " + String.valueOf(holder.boxDesc.getText()));
            list.get(i).setInputDescription(String.valueOf(holder.boxDesc.getText()));
            list.get(i).setTroubleData((EditResCenterFormData.TroubleData) holder.troubleSpinner.getItemAtPosition(holder.troubleSpinner.getSelectedItemPosition() - 1));
        }

        listener.getPassData().setProductTroubleChoosenList(list);
    }

    private boolean checkValid(Context context) {
        for (PassProductTrouble var : listener.getPassData().getProductTroubleChoosenList()) {
            if (var.getTroubleData() == null) {
                listener.showErrorMessage(context.getString(R.string.error_choose_trouble));
                return false;
            }

            if (var.getInputDescription() == null || var.getInputDescription().isEmpty()) {
                listener.showErrorMessage(context.getString(R.string.error_input_desc_product_trouble));
                return false;
            }

            if (var.getInputDescription().length() < 10) {
                listener.showErrorMessage(context.getString(R.string.error_min_10));
                return false;
            }
        }

        return true;
    }
}

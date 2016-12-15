package com.tokopedia.inbox.rescenter.shipping.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.rescenter.shipping.model.InputShippingParamsModel;
import com.tokopedia.inbox.rescenter.shipping.model.ResCenterKurir;

import java.util.List;

/**
 * Created by hangnadi on 12/13/16.
 */
public interface InputShippingRefNumView {

    Activity getActivity();

    InputShippingParamsModel getParamsModel();

    void setParamsModel(InputShippingParamsModel paramsModel);

    void renderSpinner(List<ResCenterKurir.Kurir> shippingList);

    void showTimeOutMessage(NetworkErrorHelper.RetryClickedListener listener);

    void showErrorMessage(String message);

    void showLoading(boolean isVisible);

    void showMainPage(boolean isVisible);

    void startActivityForResult(Intent intent, int requestCode);

    void startActivity(Intent intent);

    void renderInputShippingRefNum(String text);
}

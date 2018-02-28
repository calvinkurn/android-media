package com.tokopedia.transaction.checkout.view.view;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.transaction.checkout.view.data.RecipientAddressModel;

import java.util.List;

/**
 * Created by Irfan Khoirul on 05/02/18.
 */

public interface ICartAddressChoiceView extends CustomerView {

    void showLoading();

    void hideLoading();

    void showNoConnection(String message);

    void renderRecipientData(List<RecipientAddressModel> recipientAddressModels);

    Activity getActivity();
}

package com.tokopedia.transaction.checkout.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.transaction.checkout.view.data.RecipientAddressModel;
import com.tokopedia.transaction.checkout.view.view.ICartAddressChoiceView;

import java.util.List;

/**
 * Created by Irfan Khoirul on 05/02/18.
 */

public interface ICartAddressChoicePresenter extends CustomerPresenter<ICartAddressChoiceView> {

    void getAddressShortedList(Context context);

    void setSelectedRecipientAddress(RecipientAddressModel model);

    RecipientAddressModel getSelectedRecipientAddress();
}

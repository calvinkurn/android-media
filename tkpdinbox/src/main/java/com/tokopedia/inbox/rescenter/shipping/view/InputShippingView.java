package com.tokopedia.inbox.rescenter.shipping.view;

import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.inbox.rescenter.shipping.model.InputShippingParamsGetModel;

/**
 * Created by hangnadi on 12/13/16.
 */
public interface InputShippingView {

    FragmentManager getFragmentManager();

    InputShippingParamsGetModel getParamsModel();

    void setParamsModel(InputShippingParamsGetModel paramsModel);

    Bundle getBundleExtras();

    Uri getUriData();
}

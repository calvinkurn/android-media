package com.tokopedia.inbox.rescenter.shipping.presenter;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.shipping.fragment.InputShippingFragment;
import com.tokopedia.inbox.rescenter.shipping.model.InputShippingParamsGetModel;
import com.tokopedia.inbox.rescenter.shipping.view.InputShippingView;

import static com.tokopedia.inbox.rescenter.shipping.activity.InputShippingActivity.EXTRA_PARAM_CONVERSATION_ID;
import static com.tokopedia.inbox.rescenter.shipping.activity.InputShippingActivity.EXTRA_PARAM_EDIT;
import static com.tokopedia.inbox.rescenter.shipping.activity.InputShippingActivity.EXTRA_PARAM_FROM_CHAT;
import static com.tokopedia.inbox.rescenter.shipping.activity.InputShippingActivity.EXTRA_PARAM_RESOLUTION_ID;
import static com.tokopedia.inbox.rescenter.shipping.activity.InputShippingActivity.EXTRA_PARAM_SHIPPING_ID;
import static com.tokopedia.inbox.rescenter.shipping.activity.InputShippingActivity.EXTRA_PARAM_SHIPPING_REFNUM;

/**
 * Created by hangnadi on 12/13/16.
 */
public class InputShippingImpl implements InputShippingPresenter {

    private final InputShippingView view;

    public InputShippingImpl(InputShippingView view) {
        this.view = view;
        this.view.setParamsModel(generateInputParamsModel());
    }

    private InputShippingParamsGetModel generateInputParamsModel() {
        InputShippingParamsGetModel paramsModel = new InputShippingParamsGetModel();
        Bundle bundleExtras = view.getBundleExtras();
        Uri uriData = view.getUriData();
        if (bundleExtras != null) {
            paramsModel.setResolutionID(bundleExtras.getString(EXTRA_PARAM_RESOLUTION_ID));
            paramsModel.setConversationID(bundleExtras.getString(EXTRA_PARAM_CONVERSATION_ID, ""));
            paramsModel.setShippingID(bundleExtras.getString(EXTRA_PARAM_SHIPPING_ID, ""));
            paramsModel.setShippingRefNum(bundleExtras.getString(EXTRA_PARAM_SHIPPING_REFNUM, ""));
            paramsModel.setFromChat(bundleExtras.getBoolean(EXTRA_PARAM_FROM_CHAT, false));
            paramsModel.setEdit(bundleExtras.getBoolean(EXTRA_PARAM_EDIT, false));
        } else if (uriData != null) {
            throw new RuntimeException("unhandled extra uri data");
        } else {
            throw new RuntimeException("no bundle extras nor extra uri data");
        }
        return paramsModel;
    }

    @Override
    public void initView(Context context) {
        if (view.getFragmentManager().findFragmentByTag(getInputShippingFragmentName()) == null) {
            view.getFragmentManager().beginTransaction()
                    .add(R.id.container, getInputShippingFragmentInstance(), getInputShippingFragmentName())
                    .commit();
        }
    }

    private String getInputShippingFragmentName() {
        return InputShippingFragment.class.getSimpleName();
    }

    private Fragment getInputShippingFragmentInstance() {
        return InputShippingFragment.newInstance(view.getParamsModel());
    }
}

package com.tokopedia.inbox.rescenter.shipping.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.shipping.presenter.InputShippingPresenter;
import com.tokopedia.inbox.rescenter.shipping.presenter.InputShippingImpl;
import com.tokopedia.inbox.rescenter.shipping.view.InputShippingView;

/**
 * Created by hangnadi on 12/13/16.
 */

public class InputShippingActivity extends BasePresenterActivity<InputShippingPresenter>
        implements InputShippingView {

    public static Intent createNewPageIntent(Context context,
                                             String resolutionID) {
        Intent intent = new Intent(context, InputShippingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("resolution_id", resolutionID);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent createEditPageIntent(Context context,
                                              String resolutionID,
                                              String conversationID,
                                              String shippingID,
                                              String shippingRefNum) {
        Intent intent = new Intent(context, InputShippingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("resolution_id", resolutionID);
        bundle.putString("conversation_id", conversationID);
        bundle.putString("shipping_id", shippingID);
        bundle.putString("shipping_ref", shippingRefNum);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        presenter = new InputShippingImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_input_shipping;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_RESOLUTION_CENTER_INPUT_SHIPPING;
    }

}

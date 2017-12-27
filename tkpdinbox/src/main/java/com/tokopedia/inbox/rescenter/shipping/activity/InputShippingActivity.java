package com.tokopedia.inbox.rescenter.shipping.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.shipping.model.InputShippingParamsGetModel;
import com.tokopedia.inbox.rescenter.shipping.presenter.InputShippingPresenter;
import com.tokopedia.inbox.rescenter.shipping.presenter.InputShippingImpl;
import com.tokopedia.inbox.rescenter.shipping.view.InputShippingView;

/**
 * Created by hangnadi on 12/13/16.
 */

public class InputShippingActivity extends BasePresenterActivity<InputShippingPresenter>
        implements InputShippingView {

    public static final java.lang.String EXTRA_PARAM_RESOLUTION_ID = "resolution_id";
    public static final java.lang.String EXTRA_PARAM_CONVERSATION_ID = "conversation_id";
    public static final java.lang.String EXTRA_PARAM_SHIPPING_ID = "shipping_id";
    public static final java.lang.String EXTRA_PARAM_SHIPPING_REFNUM = "shipping_ref";

    private InputShippingParamsGetModel paramsModel;
    private Bundle bundleExtras;
    private Uri uriData;

    public static Intent createNewPageIntent(Context context,
                                             String resolutionID) {
        Intent intent = new Intent(context, InputShippingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM_RESOLUTION_ID, resolutionID);
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
        bundle.putString(EXTRA_PARAM_RESOLUTION_ID, resolutionID);
        bundle.putString(EXTRA_PARAM_CONVERSATION_ID, conversationID);
        bundle.putString(EXTRA_PARAM_SHIPPING_ID, shippingID);
        bundle.putString(EXTRA_PARAM_SHIPPING_REFNUM, shippingRefNum);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {
        this.uriData = data;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.bundleExtras = extras;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_RESOLUTION_CENTER_INPUT_SHIPPING;
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
        presenter.initView(this);
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
    protected void setupToolbar() {
        super.setupToolbar();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black);
    }

    @Override
    public InputShippingParamsGetModel getParamsModel() {
        return paramsModel;
    }

    @Override
    public void setParamsModel(InputShippingParamsGetModel paramsModel) {
        this.paramsModel = paramsModel;
    }

    @Override
    public Bundle getBundleExtras() {
        return bundleExtras;
    }

    @Override
    public Uri getUriData() {
        return uriData;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    public void getBottomBackSheetActivityTransition() {
        overridePendingTransition(R.anim.push_down, R.anim.pull_up);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getBottomBackSheetActivityTransition();
    }
}

package com.tokopedia.inbox.rescenter.edit.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.DetailResCenterData;
import com.tokopedia.inbox.rescenter.detail.model.passdata.ActivityParamenterPassData;
import com.tokopedia.inbox.rescenter.edit.fragment.AppealResCenterFragment;
import com.tokopedia.inbox.rescenter.edit.fragment.BuyerEditResCenterFormFragment;
import com.tokopedia.inbox.rescenter.edit.fragment.SellerEditResCenterFormFragment;
import com.tokopedia.inbox.rescenter.edit.listener.EditResCenterListener;
import com.tokopedia.inbox.rescenter.edit.model.responsedata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.edit.presenter.EditResCenterImpl;
import com.tokopedia.inbox.rescenter.edit.presenter.EditResCenterPresenter;

/**
 * Created on 8/24/16.
 */
public class EditResCenterActivity extends BasePresenterActivity<EditResCenterPresenter>
        implements EditResCenterListener {

    private static final String ARGS_PARAM_PASS_DATA = "ARGS_PARAM_PASS_DATA";
    private static final String ARGS_PARAM_FLAG_EDIT = "ARGS_PARAM_FLAG_EDIT";
    private static final String ARGS_PARAM_DETAIL_DATA = "ARGS_PARAM_DETAIL_DATA";

    private ActionParameterPassData passData;
    private boolean isEdit;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_RESOLUTION_CENTER_EDIT;
    }

    private static Parcelable generateEditPassData(ActivityParamenterPassData passData,
                                                   DetailResCenterData detailData) {
        ActionParameterPassData editPassData = new ActionParameterPassData();
        editPassData.setResolutionID(passData.getResCenterId());
        editPassData.setDetailData(detailData);
        return editPassData;
    }

    private static Parcelable generateEditPassData(String resolutionID,
                                                   String orderID,
                                                   boolean isSeller,
                                                   boolean isReceived) {
        ActionParameterPassData editPassData = new ActionParameterPassData();
        editPassData.setResolutionID(resolutionID);
        DetailResCenterData data = new DetailResCenterData();
        DetailResCenterData.Detail detail = new DetailResCenterData.Detail();

        DetailResCenterData.ResolutionBy resolutionBy = new DetailResCenterData.ResolutionBy();
        resolutionBy.setByCustomer(isSeller ? 0 : 1);
        detail.setResolutionBy(resolutionBy);

        DetailResCenterData.ResolutionOrder resolutionOrder = new DetailResCenterData.ResolutionOrder();
        resolutionOrder.setOrderId(orderID);
        detail.setResolutionOrder(resolutionOrder);

        DetailResCenterData.ResolutionLast resolutionLast = new DetailResCenterData.ResolutionLast();
        resolutionLast.setLastFlagReceived(isReceived ? 1 : 0);
        resolutionLast.setLastResolutionId(resolutionID);
        detail.setResolutionLast(resolutionLast);

        data.setDetail(detail);

        editPassData.setDetailData(data);
        return editPassData;
    }

    public static Intent newBuyerInstance(Context context,
                                          ActivityParamenterPassData passData,
                                          DetailResCenterData detailData) {
        Intent intent = new Intent(context, EditResCenterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARGS_PARAM_FLAG_EDIT, true);
        bundle.putParcelable(ARGS_PARAM_PASS_DATA, generateEditPassData(passData, detailData));
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newBuyerInstance(Context context,
                                          String resolutionID,
                                          String orderID,
                                          boolean isReceived) {
        Intent intent = new Intent(context, EditResCenterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARGS_PARAM_FLAG_EDIT, true);
        bundle.putParcelable(ARGS_PARAM_PASS_DATA,
                generateEditPassData(resolutionID, orderID, false, isReceived));
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newSellerInstance(Context context,
                                           ActivityParamenterPassData passData,
                                           DetailResCenterData detailData) {
        Intent intent = new Intent(context, EditResCenterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARGS_PARAM_FLAG_EDIT, true);
        bundle.putParcelable(ARGS_PARAM_PASS_DATA, generateEditPassData(passData, detailData));
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newSellerInstance(Context context,
                                           String resolutionID,
                                           String orderID,
                                           boolean isReceived) {
        Intent intent = new Intent(context, EditResCenterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARGS_PARAM_FLAG_EDIT, true);
        bundle.putParcelable(ARGS_PARAM_PASS_DATA,
                generateEditPassData(resolutionID, orderID, true, isReceived));
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newAppealInstance(Context context,
                                           ActivityParamenterPassData passData,
                                           DetailResCenterData detailData) {
        Intent intent = new Intent(context, EditResCenterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_PARAM_PASS_DATA, generateEditPassData(passData, detailData));
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newAppealInstance(Context context,
                                           String resolutionID,
                                           String orderID,
                                           boolean seller,
                                           boolean isReceived) {
        Intent intent = new Intent(context, EditResCenterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_PARAM_PASS_DATA,
                generateEditPassData(resolutionID, orderID, seller, isReceived));
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        isEdit = extras.getBoolean(ARGS_PARAM_FLAG_EDIT, false);
        passData = extras.getParcelable(ARGS_PARAM_PASS_DATA);
    }

    @Override
    public boolean isCustomer() {
        return passData.getDetailData().getDetail().getResolutionBy().getByCustomer() == 1;
    }

    @Override
    public boolean isEdit() {
        return isEdit;
    }

    @Override
    protected void initialPresenter() {
        presenter = new EditResCenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_res_center_detail;
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
    public void finishActivity() {
        finish();
    }

    @Override
    public void inflateBuyerEditResolutionForm() {
        if (getFragmentManager().findFragmentByTag(BuyerEditResCenterFormFragment.class.getSimpleName()) == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container,
                            BuyerEditResCenterFormFragment.newInstance(passData),
                            BuyerEditResCenterFormFragment.class.getSimpleName())
                    .commit();
        }
    }

    @Override
    public void inflateSellerEditResolutionForm() {
        if (getFragmentManager().findFragmentByTag(SellerEditResCenterFormFragment.class.getSimpleName()) == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container,
                            SellerEditResCenterFormFragment.newInstance(passData),
                            SellerEditResCenterFormFragment.class.getSimpleName())
                    .commit();
        }
    }

    @Override
    public void inflateAppealFragment() {
        if (getFragmentManager().findFragmentByTag(AppealResCenterFragment.class.getSimpleName()) == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container,
                            AppealResCenterFragment.newInstance(passData),
                            AppealResCenterFragment.class.getSimpleName())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}

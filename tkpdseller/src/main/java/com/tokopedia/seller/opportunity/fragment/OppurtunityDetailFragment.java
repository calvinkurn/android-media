package com.tokopedia.seller.opportunity.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.snapshot.SnapShotProduct;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.opportunity.customview.OppurtunityDetailButtonView;
import com.tokopedia.seller.opportunity.customview.OppurtunityDetailProductView;
import com.tokopedia.seller.opportunity.customview.OppurtunityDetailStatusView;
import com.tokopedia.seller.opportunity.customview.OppurtunityDetailSummaryView;
import com.tokopedia.seller.opportunity.listener.OppurtunityView;
import com.tokopedia.seller.opportunity.presentation.ActionViewData;
import com.tokopedia.seller.opportunity.presenter.OppurtunityImpl;
import com.tokopedia.seller.opportunity.presenter.OppurtunityPresenter;

import butterknife.BindView;

/**
 * Created by hangnadi on 2/27/17.
 */

public class OppurtunityDetailFragment extends BasePresenterFragment<OppurtunityPresenter>
        implements OppurtunityView {

    private static final int STATUS_SUCCESS = 1900;
    private static final int STATUS_ERROR_MESSAGE = 1901;
    private static final int STATUS_ERROR_NETWORK = 1902;
    private static final int STATUS_ERROR_UNKNOWN = 1903;
    private static final int STATUS_TIMEOUT = 1904;

    @BindView(R2.id.customview_oppurtunity_detail_button_view)
    OppurtunityDetailButtonView buttonView;
    @BindView(R2.id.customview_oppurtunity_detail_status_view)
    OppurtunityDetailStatusView statusView;
    @BindView(R2.id.customview_oppurtunity_detail_product_view)
    OppurtunityDetailProductView productView;
    @BindView(R2.id.customview_oppurtunity_detail_summary_view)
    OppurtunityDetailSummaryView summaryView;

    private ActionViewData actionViewData;

    @Override
    public ActionViewData getActionViewData() {
        return actionViewData;
    }

    @Override
    public void setActionViewData(ActionViewData actionViewData) {
        this.actionViewData = actionViewData;
    }

    public OppurtunityDetailFragment() {
    }

    @Override
    public void onActionDeleteClicked() {

    }

    @Override
    public void onActionSubmitClicked() {
        presenter.setOnSubmitClickListener();
    }

    @Override
    public void onActionSeeDetailProduct(String productId) {
        startActivity(SnapShotProduct.createIntent(getActivity(), productId));
    }

    public static Fragment createInstance() {
        OppurtunityDetailFragment fragment = new OppurtunityDetailFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new OppurtunityImpl(getActivity(), this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_oppurtunity_detail;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setViewListener() {
        buttonView.setListener(this);
        statusView.setListener(this);
        productView.setListener(this);
        summaryView.setListener(this);
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void setOnAcceptOppurtunityComplete() {
        switch (generateActionStatus()) {
            case STATUS_SUCCESS:
                setOnActionSuccess();
                break;
            case STATUS_ERROR_MESSAGE:
                setOnActionServerError();
                break;
            case STATUS_ERROR_NETWORK:
                setOnActionNetworkError();
                break;
            case STATUS_ERROR_UNKNOWN:
                setOnActionUnknownError();
                break;
            case STATUS_TIMEOUT:
                setOnActionTimeOut();
                break;
            default:
                break;
        }
    }

    private void setOnActionSuccess() {

    }

    private void setOnActionServerError() {
        NetworkErrorHelper.showSnackbar(getActivity(), actionViewData.getMessageError());
    }

    private void setOnActionNetworkError() {
        new ErrorHandler(new ErrorListener() {
            @Override
            public void onUnknown() {

            }

            @Override
            public void onTimeout() {

            }

            @Override
            public void onServerError() {

            }

            @Override
            public void onBadRequest() {

            }

            @Override
            public void onForbidden() {

            }
        }, actionViewData.getErrorCode());
    }

    private void setOnActionTimeOut() {
        setOnActionUnknownError();
    }

    private void setOnActionUnknownError() {
        NetworkErrorHelper.showSnackbar(getActivity());
    }

    private int generateActionStatus() {
        if (actionViewData != null) {
            if (actionViewData.isSuccess()) {
                return STATUS_SUCCESS;
            } else {
                if (actionViewData.isTimeOut()) {
                    return STATUS_TIMEOUT;
                } else if (actionViewData.getMessageError() == null) {
                    return STATUS_ERROR_MESSAGE;
                } else if (actionViewData.getErrorCode() == 0){
                    return STATUS_ERROR_NETWORK;
                }
            }
        }

        return STATUS_ERROR_UNKNOWN;
    }

}

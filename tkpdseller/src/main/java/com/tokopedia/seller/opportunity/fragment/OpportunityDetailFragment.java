package com.tokopedia.seller.opportunity.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.snapshot.SnapShotProduct;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.OpportunityDetailActivity;
import com.tokopedia.seller.opportunity.customview.OpportunityDetailButtonView;
import com.tokopedia.seller.opportunity.customview.OpportunityDetailProductView;
import com.tokopedia.seller.opportunity.customview.OpportunityDetailStatusView;
import com.tokopedia.seller.opportunity.customview.OpportunityDetailSummaryView;
import com.tokopedia.seller.opportunity.listener.OpportunityView;
import com.tokopedia.seller.opportunity.presentation.ActionViewData;
import com.tokopedia.seller.opportunity.presenter.OpportunityImpl;
import com.tokopedia.seller.opportunity.presenter.OpportunityPresenter;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityItemViewModel;

/**
 * Created by hangnadi on 2/27/17.
 */

public class OpportunityDetailFragment extends BasePresenterFragment<OpportunityPresenter>
        implements OpportunityView {

    private static final int STATUS_SUCCESS = 1900;
    private static final int STATUS_ERROR_MESSAGE = 1901;
    private static final int STATUS_ERROR_NETWORK = 1902;
    private static final int STATUS_ERROR_UNKNOWN = 1903;
    private static final int STATUS_TIMEOUT = 1904;
    private static final int RESULT_DELETED = 8881;

    OpportunityDetailButtonView buttonView;
    OpportunityDetailStatusView statusView;
    OpportunityDetailProductView productView;
    OpportunityDetailSummaryView summaryView;

    private ActionViewData actionViewData;
    private OpportunityItemViewModel data;

    public static Fragment createInstance(Bundle bundle) {
        OpportunityDetailFragment fragment = new OpportunityDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public ActionViewData getActionViewData() {
        return actionViewData;
    }

    @Override
    public void setActionViewData(ActionViewData actionViewData) {
        this.actionViewData = actionViewData;
    }

    public OpportunityDetailFragment() {
    }

    @Override
    public void onActionDeleteClicked() {
        getActivity().setResult(RESULT_DELETED);
        getActivity().finish();
    }

    @Override
    public void onActionConfirmClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.message_dialog_accept_opportunity);
        builder.setPositiveButton(R.string.action_agree, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.setOnSubmitClickListener();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(R.string.action_back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public void onActionSeeDetailProduct(String productId) {
        startActivity(SnapShotProduct.createIntent(getActivity(), productId));
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
        presenter = new OpportunityImpl(getActivity(), this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_opportunity_detail;
    }

    @Override
    protected void initView(View view) {
        buttonView = (OpportunityDetailButtonView)
                view.findViewById(R.id.customview_opportunity_detail_button_view);
        statusView = (OpportunityDetailStatusView)
                view.findViewById(R.id.customview_opportunity_detail_status_view);
        productView = (OpportunityDetailProductView)
                view.findViewById(R.id.customview_opportunity_detail_product_view);
        summaryView = (OpportunityDetailSummaryView)
                view.findViewById(R.id.customview_opportunity_detail_summary_view);

        data = getArguments().getParcelable(OpportunityDetailActivity.OPPORTUNITY_EXTRA_PARAM);
        if (data != null) {
            statusView.renderData(data);
            productView.renderData(data);
            summaryView.renderData(data);
            buttonView.renderData(data);
        }
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
    public void setOnAcceptOpportunityComplete() {
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

    @Override
    public String getOpportunityId() {
        return data.getOrderOrderId();
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
                setOnActionUnknownError();
            }

            @Override
            public void onTimeout() {
                setOnActionTimeOut();
            }

            @Override
            public void onServerError() {
                setOnActionUnknownError();
            }

            @Override
            public void onBadRequest() {
                setOnActionUnknownError();
            }

            @Override
            public void onForbidden() {
                setOnActionUnknownError();
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
                } else if (actionViewData.getErrorCode() == 0) {
                    return STATUS_ERROR_NETWORK;
                }
            }
        }

        return STATUS_ERROR_UNKNOWN;
    }

}

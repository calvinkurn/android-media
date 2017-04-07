package com.tokopedia.seller.opportunity.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
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

    public static final int RESULT_DELETED = 8881;

    OpportunityDetailButtonView buttonView;
    OpportunityDetailStatusView statusView;
    OpportunityDetailProductView productView;
    OpportunityDetailSummaryView summaryView;
    TkpdProgressDialog progressDialog;

    private ActionViewData actionViewData;
    private OpportunityItemViewModel data;

    public static Fragment createInstance(Bundle bundle) {
        OpportunityDetailFragment fragment = new OpportunityDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public OpportunityDetailFragment() {
    }

    @Override
    public void onActionDeleteClicked() {
        Intent intent = getActivity().getIntent();
        getActivity().setResult(RESULT_DELETED, intent);
        getActivity().finish();
    }

    @Override
    public void onActionConfirmClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.message_dialog_accept_opportunity);
        builder.setPositiveButton(R.string.action_agree, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.acceptOpportunity();
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
    public String getOpportunityId() {
        return String.valueOf(data.getOrderReplacementId());
    }

    @Override
    public void showLoadingProgress() {
        if (progressDialog == null && getActivity() != null)
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);

        if (progressDialog != null && getActivity() != null)
            progressDialog.showDialog();
    }

    @Override
    public void onSuccessTakeOpportunity(ActionViewData actionViewData) {
        finishLoadingProgress();
        CommonUtils.UniversalToast(getActivity(), actionViewData.getMessage());
    }

    @Override
    public void onErrorTakeOpportunity(String errorMessage) {
        finishLoadingProgress();
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    private void finishLoadingProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unsubscribeObservable();
    }
}

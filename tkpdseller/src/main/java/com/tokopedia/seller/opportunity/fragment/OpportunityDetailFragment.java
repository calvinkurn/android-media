package com.tokopedia.seller.opportunity.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.seller.opportunity.activity.OpportunityTncActivity;
import com.tokopedia.seller.opportunity.analytics.OpportunityTrackingEventLabel;
import com.tokopedia.seller.opportunity.customview.OpportunityValueBottomSheet;
import com.tokopedia.seller.opportunity.di.component.OpportunityComponent;
import com.tokopedia.seller.opportunity.di.module.OpportunityModule;
import com.tokopedia.seller.opportunity.snapshot.SnapShotProduct;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.activity.OpportunityDetailActivity;
import com.tokopedia.seller.opportunity.customview.OpportunityDetailProductView;
import com.tokopedia.seller.opportunity.customview.OpportunityDetailStatusView;
import com.tokopedia.seller.opportunity.customview.OpportunityDetailSummaryView;
import com.tokopedia.seller.opportunity.listener.OpportunityView;
import com.tokopedia.seller.opportunity.presentation.ActionViewData;
import com.tokopedia.seller.opportunity.presenter.OpportunityImpl;
import com.tokopedia.seller.opportunity.presenter.OpportunityPresenter;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityItemViewModel;
import com.tokopedia.seller.opportunity.di.component.DaggerOpportunityComponent;

import javax.inject.Inject;

/**
 * Created by hangnadi on 2/27/17.
 */

public class OpportunityDetailFragment extends BasePresenterFragment<OpportunityPresenter>
        implements OpportunityView {

    public static final int RESULT_DELETED = 8881;
    private static final int REQUEST_OPEN_SNAPSHOT = 1919;
    private static final int REQUEST_OPEN_TNC = 1920;

    View buttonView;
    OpportunityDetailStatusView statusView;
    OpportunityDetailProductView productView;
    OpportunityDetailSummaryView summaryView;
    TkpdProgressDialog progressDialog;

    private OpportunityComponent opportunityComponent;

    @Inject
    OpportunityPresenter presenter;

    private OpportunityItemViewModel oppItemViewModel;



    public static Fragment createInstance(Bundle bundle) {
        OpportunityDetailFragment fragment = new OpportunityDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActionDeleteClicked() {
        Intent intent = getActivity().getIntent();
        getActivity().setResult(RESULT_DELETED, intent);
        getActivity().finish();
    }

    public void onActionConfirmClicked() {
        UnifyTracking.eventOpportunity(
                OpportunityTrackingEventLabel.EventName.CLICK_OPPORTUNITY_TAKE,
                OpportunityTrackingEventLabel.EventCategory.OPPORTUNITY_FILTER,
                AppEventTracking.Action.CLICK,
                OpportunityTrackingEventLabel.EventLabel.TAKE_OPPORTUNITY
        );
        if (TextUtils.isEmpty(oppItemViewModel.getReplacementTnc())){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.message_dialog_accept_opportunity);
            builder.setPositiveButton(R.string.action_agree, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    UnifyTracking.eventOpportunity(
                            OpportunityTrackingEventLabel.EventName.CLICK_OPPORTUNITY_TAKE_YES,
                            OpportunityTrackingEventLabel.EventCategory.OPPORTUNITY_FILTER,
                            AppEventTracking.Action.CLICK,
                            OpportunityTrackingEventLabel.EventLabel.YES
                    );

                    presenter.acceptOpportunity();
                    dialogInterface.dismiss();
                }
            });
            builder.setNegativeButton(R.string.action_back, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    UnifyTracking.eventOpportunity(
                            OpportunityTrackingEventLabel.EventName.CLICK_OPPORTUNITY_TAKE_NO,
                            OpportunityTrackingEventLabel.EventCategory.OPPORTUNITY_FILTER,
                            AppEventTracking.Action.CLICK,
                            OpportunityTrackingEventLabel.EventLabel.NO
                    );
                    dialogInterface.dismiss();
                }
            });
            Dialog dialog = builder.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
        } else {
            Intent intent = OpportunityTncActivity.createIntent(getActivity(), oppItemViewModel);
            startActivityForResult(intent, REQUEST_OPEN_TNC);
        }
    }

    @Override
    public void onReputationLabelClicked() {
        BottomSheetView bottomSheetView = new BottomSheetView(getActivity());
        bottomSheetView.renderBottomSheet(new BottomSheetView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(getActivity().getString(R.string.title_tooltip_reputation_multiplier))
                .setBody(getActivity().getString(R.string.body_tooltip_reputation_multiplier))
                .setImg(R.drawable.ic_reputation_value)
                .build());
        bottomSheetView.show();
    }

    public void onReputationShippingFee(){
        OpportunityValueBottomSheet.showShippingFee(getActivity()).show();
    }

    public void onReputationProductPrice(){
        OpportunityValueBottomSheet.showProductPrice(getActivity()).show();
    }

    @Override
    public void onActionSeeDetailProduct(String productId) {
        UnifyTracking.eventOpportunity(
                OpportunityTrackingEventLabel.EventName.CLICK_OPPORTUNITY_PRODUCT,
                OpportunityTrackingEventLabel.EventCategory.OPPORTUNITY_FILTER,
                AppEventTracking.Action.CLICK,
                OpportunityTrackingEventLabel.EventLabel.SEE_PRODUCT
        );

        startActivityForResult(
                SnapShotProduct.createIntent(getActivity(), productId, getOpportunityId()),
                REQUEST_OPEN_SNAPSHOT
        );
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
        opportunityComponent.inject(this);
        presenter.attachView(this);
    }

    @Override
    protected void initialListener(Activity activity) {
        if(activity != null && activity instanceof BaseActivity){
            opportunityComponent = DaggerOpportunityComponent
                    .builder()
                    .opportunityModule(new OpportunityModule())
                    .appComponent(((BaseActivity)activity).getApplicationComponent())
                    .build();
        }
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
        buttonView = view.findViewById(R.id.button_take_opportunity);
        statusView = view.findViewById(R.id.customview_opportunity_detail_status_view);
        productView = view.findViewById(R.id.customview_opportunity_detail_product_view);
        summaryView = view.findViewById(R.id.customview_opportunity_detail_summary_view);

        oppItemViewModel = getArguments().getParcelable(OpportunityDetailActivity.OPPORTUNITY_EXTRA_PARAM);
        if (oppItemViewModel != null) {
            statusView.renderData(oppItemViewModel);
            productView.renderData(oppItemViewModel);
            summaryView.renderData(oppItemViewModel);
            presenter.getNewPriceInfo();
            buttonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onActionConfirmClicked();
                }
            });
        }
    }


    @Override
    protected void setViewListener() {
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
        return String.valueOf(oppItemViewModel.getOrderReplacementId());
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
        getActivity().finish();
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
    public void onPause() {
        super.onPause();
        presenter.unsubscribeObservable();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_OPEN_SNAPSHOT:
                case REQUEST_OPEN_TNC:
                    getActivity().finish();
                    break;
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}

package com.tokopedia.seller.opportunity.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.activity.OpportunityDetailActivity;
import com.tokopedia.seller.opportunity.activity.OpportunityTncActivity;
import com.tokopedia.seller.opportunity.analytics.OpportunityTrackingEventLabel;
import com.tokopedia.seller.opportunity.customview.OpportunityDetailProductView;
import com.tokopedia.seller.opportunity.customview.OpportunityDetailStatusView;
import com.tokopedia.seller.opportunity.customview.OpportunityDetailSummaryView;
import com.tokopedia.seller.opportunity.customview.PriceDifferentInfoView;
import com.tokopedia.seller.opportunity.data.OpportunityNewPriceData;
import com.tokopedia.seller.opportunity.di.component.DaggerOpportunityComponent;
import com.tokopedia.seller.opportunity.di.component.OpportunityComponent;
import com.tokopedia.seller.opportunity.di.module.OpportunityModule;
import com.tokopedia.seller.opportunity.listener.OpportunityView;
import com.tokopedia.seller.opportunity.presentation.ActionViewData;
import com.tokopedia.seller.opportunity.presenter.OpportunityPresenter;
import com.tokopedia.seller.opportunity.snapshot.SnapShotProduct;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityItemViewModel;

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
    PriceDifferentInfoView productPriceDifferentInfoView;
    PriceDifferentInfoView deliveryPriceDifferentInfoView;

    private OpportunityComponent opportunityComponent;

    @Inject
    OpportunityPresenter presenter;

    private OpportunityItemViewModel oppItemViewModel;
    private CardView priceInfoContainer;


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
                getActivity(),
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
                            getActivity(),
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
                            getActivity(),
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
                .setTitle(getString(R.string.opportunity_detail_info_reputation_multiplier_title))
                .setBody(getString(R.string.opportunity_detail_info_reputation_multiplier_content))
                .setImg(R.drawable.ic_reputation_value)
                .build());
        bottomSheetView.show();
    }

    @Override
    public void onActionSeeDetailProduct(String productId) {
        UnifyTracking.eventOpportunity(
                getActivity(),
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
        priceInfoContainer = view.findViewById(R.id.price_info_container);

        productPriceDifferentInfoView = view.findViewById(R.id.price_item);
        deliveryPriceDifferentInfoView = view.findViewById(R.id.shipping_fee);

        oppItemViewModel = getArguments().getParcelable(OpportunityDetailActivity.OPPORTUNITY_EXTRA_PARAM);
        if (oppItemViewModel != null) {
            statusView.renderData(oppItemViewModel);
            productView.renderData(oppItemViewModel);
            summaryView.renderData(oppItemViewModel);
            showLoadingProgress();
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
    public void onSuccessNewPrice(final OpportunityNewPriceData opportunityNewPriceData) {
        priceInfoContainer.setVisibility(View.VISIBLE);
        finishLoadingProgress();
        if (opportunityNewPriceData.getNewItemPrice() >= 0 && !TextUtils.isEmpty(opportunityNewPriceData.getNewItemPriceIdr())) {
            displayDetailPrice(productPriceDifferentInfoView, opportunityNewPriceData.getNewItemPriceIdr(), opportunityNewPriceData.getOldItemPriceIdr(),
                    getString(R.string.opportunity_detail_info_product_price_title),
                    R.drawable.ic_product_price,
                    getString(R.string.opportunity_detail_info_product_price_content),
                    getString(R.string.opportunity_detail_info_different_product_price_content));
        }
        if (opportunityNewPriceData.getNewShippingPrice() >= 0 && !TextUtils.isEmpty(opportunityNewPriceData.getNewShippingPriceIdr())) {
            displayDetailPrice(deliveryPriceDifferentInfoView, opportunityNewPriceData.getNewShippingPriceIdr(), opportunityNewPriceData.getOldShippingPriceIdr(),
                    getString(R.string.opportunity_detail_info_shipping_price_title),
                    R.drawable.ic_shipping_fee,
                    getString(R.string.opportunity_detail_info_delivery_price_content),
                    getString(R.string.opportunity_detail_info_different_delivery_price_content));
        }
    }

    private void displayDetailPrice(PriceDifferentInfoView priceDifferentInfoView, final String newPrice, final String oldPrice,
                                    final String titleInfo, final @DrawableRes int imageResource, final String defaultInfo, final String differentPriceInfo) {
        priceDifferentInfoView.setVisibility(View.VISIBLE);
        priceDifferentInfoView.setNewPrice(newPrice);
        if (isPriceDifferent(newPrice, oldPrice)) {
            priceDifferentInfoView.setOldPrice(oldPrice);
        }
        priceDifferentInfoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String infoContent = defaultInfo;
                if (isPriceDifferent(newPrice, oldPrice)){
                    infoContent = differentPriceInfo;
                }
                BottomSheetView bottomSheetView = new BottomSheetView(getActivity());
                bottomSheetView.renderBottomSheet(new BottomSheetView.BottomSheetField
                        .BottomSheetFieldBuilder()
                        .setTitle(titleInfo)
                        .setBody(infoContent)
                        .setImg(imageResource)
                        .build());
                bottomSheetView.show();
            }
        });
    }

    private boolean isPriceDifferent(String newPrice, String oldPrice) {
        if (TextUtils.isEmpty(newPrice) || TextUtils.isEmpty(oldPrice)) {
            return false;
        }
        return !newPrice.equalsIgnoreCase(oldPrice);
    }

    @Override
    public void onErrorTakeOpportunity(String errorMessage) {
        finishLoadingProgress();
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onErrorPriceInfo(String errorMessage) {
        priceInfoContainer.setVisibility(View.GONE);

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
                case REQUEST_OPEN_TNC:
                    Intent intent = new Intent();
                    intent.putExtra(OpportunityTncFragment.ACCEPTED_OPPORTUNITY, true);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                    break;
                case REQUEST_OPEN_SNAPSHOT:
                default:
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                    break;
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}

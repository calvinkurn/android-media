package com.tokopedia.transaction.checkout.view.view.shipmentform;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.data.mapper.ShipmentRatesDataMapper;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressPriceSummaryData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressShipmentAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.ShipmentDetailData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.transaction.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.transaction.checkout.router.ICartCheckoutModuleRouter;
import com.tokopedia.transaction.checkout.view.adapter.MultipleAddressShipmentAdapter;
import com.tokopedia.transaction.checkout.view.di.component.DaggerMultipleAddressShipmentComponent;
import com.tokopedia.transaction.checkout.view.di.component.MultipleAddressShipmentComponent;
import com.tokopedia.transaction.checkout.view.di.module.MultipleAddressShipmentModule;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.transaction.checkout.view.view.shippingoptions.ShipmentDetailActivity;
import com.tokopedia.transaction.pickuppoint.domain.model.Store;
import com.tokopedia.transaction.pickuppoint.domain.usecase.GetPickupPointsUseCase;
import com.tokopedia.transaction.pickuppoint.view.activity.PickupPointActivity;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.transaction.checkout.view.view.shippingoptions.ShipmentDetailActivity.EXTRA_POSITION;
import static com.tokopedia.transaction.checkout.view.view.shippingoptions.ShipmentDetailActivity.EXTRA_SHIPMENT_DETAIL_DATA;
import static com.tokopedia.transaction.pickuppoint.view.contract.PickupPointContract.Constant.INTENT_DATA_POSITION;
import static com.tokopedia.transaction.pickuppoint.view.contract.PickupPointContract.Constant.INTENT_DATA_STORE;

/**
 * Created by kris on 1/23/18. Tokopedia
 */

public class MultipleAddressShipmentFragment extends BasePresenterFragment implements
        IMultipleAddressShipmentView, MultipleAddressShipmentAdapter.MultipleAddressShipmentAdapterListener {
    public static final String ARG_EXTRA_SHIPMENT_FORM_DATA = "ARG_EXTRA_SHIPMENT_FORM_DATA";
    public static final String ARG_EXTRA_CART_PROMO_SUGGESTION = "ARG_EXTRA_CART_PROMO_SUGGESTION";
    public static final String ARG_EXTRA_PROMO_CODE_APPLIED_DATA = "ARG_EXTRA_PROMO_CODE_APPLIED_DATA";
    private static final String FONT_FAMILY_SANS_SERIF_MEDIUM = "sans-serif-medium";

    private static final int REQUEST_CODE_SHIPMENT_DETAIL = 11;
    private static final int REQUEST_CHOOSE_PICKUP_POINT = 12;

    @Inject
    IMultipleAddressShipmentPresenter presenter;

    private ICartShipmentActivity cartShipmentActivity;

    private TextView totalPayment;
    private TextView promoMessage;
    private MultipleAddressShipmentAdapter shipmentAdapter;

    private PromoCodeAppliedData promoCodeAppliedData;
    private CartShipmentAddressFormData cartShipmentAddressFormData;
    private CartPromoSuggestion cartPromoSuggestion;


    public static MultipleAddressShipmentFragment newInstance(CartShipmentAddressFormData cartShipmentAddressFormData,
                                                              PromoCodeAppliedData promoCodeAppliedData,
                                                              CartPromoSuggestion cartPromoSuggestionData) {
        MultipleAddressShipmentFragment fragment = new MultipleAddressShipmentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_EXTRA_SHIPMENT_FORM_DATA, cartShipmentAddressFormData);
        bundle.putParcelable(ARG_EXTRA_CART_PROMO_SUGGESTION, cartPromoSuggestionData);
        bundle.putParcelable(ARG_EXTRA_PROMO_CODE_APPLIED_DATA, promoCodeAppliedData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
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

    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        promoCodeAppliedData = getArguments().getParcelable(ARG_EXTRA_PROMO_CODE_APPLIED_DATA);
        cartShipmentAddressFormData = getArguments().getParcelable(ARG_EXTRA_SHIPMENT_FORM_DATA);
        cartPromoSuggestion = getArguments().getParcelable(ARG_EXTRA_CART_PROMO_SUGGESTION);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.multiple_address_shipment_fragment;
    }

    @Override
    protected void initView(View view) {
        CartItemPromoHolderData cartItemPromoHolderData = new CartItemPromoHolderData();
        if (promoCodeAppliedData != null) {
            if (promoCodeAppliedData.getTypeVoucher() == CartItemPromoHolderData.TYPE_PROMO_COUPON) {
                cartItemPromoHolderData.setPromoCouponType(
                        promoCodeAppliedData.getCouponTitle(),
                        promoCodeAppliedData.getPromoCode(),
                        promoCodeAppliedData.getDescription(),
                        promoCodeAppliedData.getAmount());
            } else if (promoCodeAppliedData.getTypeVoucher() == CartItemPromoHolderData.TYPE_PROMO_VOUCHER) {
                cartItemPromoHolderData.setPromoVoucherType(
                        promoCodeAppliedData.getPromoCode(),
                        promoCodeAppliedData.getDescription(),
                        promoCodeAppliedData.getAmount());
            }
        }
        totalPayment = view.findViewById(R.id.total_payment_text_view);
        promoMessage = view.findViewById(R.id.tv_promo_message);
        ViewGroup totalPaymentLayout = view.findViewById(R.id.total_payment_layout);
        ViewGroup confirmButton = view.findViewById(R.id.confirm_payment_button);
        RecyclerView orderAddressList = view.findViewById(R.id.order_shipment_list);
        orderAddressList.setLayoutManager(new LinearLayoutManager(getActivity()));
        shipmentAdapter = new MultipleAddressShipmentAdapter(
                cartPromoSuggestion,
                cartItemPromoHolderData,
                presenter.initiateAdapterData(cartShipmentAddressFormData),
                this);
        orderAddressList.setAdapter(shipmentAdapter);
        orderAddressList.addOnScrollListener(onRecyclerViewScrolledListener(totalPaymentLayout));
        totalPayment.setText(shipmentAdapter.getTotalPayment());
        confirmButton.setOnClickListener(onConfirmedButtonClicked(
                shipmentAdapter.getAddressDataList(),
                shipmentAdapter.getPriceSummaryData()));
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected void initInjector() {
        super.initInjector();
        MultipleAddressShipmentComponent component = DaggerMultipleAddressShipmentComponent
                .builder()
                .multipleAddressShipmentModule(new MultipleAddressShipmentModule(this))
                .build();
        component.inject(this);
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    private void showCancelPickupBoothDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.label_dialog_title_cancel_pickup);
        builder.setMessage(R.string.label_dialog_message_cancel_pickup_booth);
        builder.setPositiveButton(R.string.title_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shipmentAdapter.unSetPickupPoint(position);
                shipmentAdapter.notifyItemChanged(position);
            }
        });
        builder.setNegativeButton(R.string.title_no, null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    private View.OnClickListener onConfirmedButtonClicked(
            final List<MultipleAddressShipmentAdapterData> addressDataList,
            final MultipleAddressPriceSummaryData data) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartShipmentActivity.checkoutCart(
                        presenter.generateCheckoutRequest(addressDataList, data)
                );
            }
        };

    }

    @Override
    public void onChooseShipment(MultipleAddressShipmentAdapterData addressAdapterData, int position) {
        ShipmentDetailData shipmentDetailData;
        if (addressAdapterData.getSelectedShipmentDetailData() != null) {
            shipmentDetailData = addressAdapterData.getSelectedShipmentDetailData();
        } else {
            ShipmentRatesDataMapper shipmentRatesDataMapper = new ShipmentRatesDataMapper();
            shipmentDetailData = shipmentRatesDataMapper.getShipmentDetailData(addressAdapterData);
        }
        startActivityForResult(ShipmentDetailActivity.createInstance(
                getActivity(), shipmentDetailData, position), REQUEST_CODE_SHIPMENT_DETAIL);
    }

    @Override
    public void onAllShipmentChosen(List<MultipleAddressShipmentAdapterData> adapterDataList) {
        CartItemPromoHolderData appliedPromo = shipmentAdapter.getAppliedPromo();
        if (appliedPromo != null && appliedPromo.getTypePromo() != CartItemPromoHolderData.TYPE_PROMO_NOT_ACTIVE) {
            cartShipmentActivity.checkPromoCodeShipment(
                    presenter.checkPromoSubscription(appliedPromo),
                    presenter.generateCheckPromoRequest(adapterDataList, appliedPromo)
            );
        }
    }

    @Override
    public void onChoosePickupPoint(MultipleAddressShipmentAdapterData addressAdapterData, int position) {
        startActivityForResult(PickupPointActivity.createInstance(
                getActivity(),
                position,
                addressAdapterData.getDestinationDistrictName(),
                GetPickupPointsUseCase.generateParams(addressAdapterData)
        ), REQUEST_CHOOSE_PICKUP_POINT);
    }

    @Override
    public void onClearPickupPoint(MultipleAddressShipmentAdapterData addressAdapterData, int position) {
        showCancelPickupBoothDialog(position);
    }

    @Override
    public void onEditPickupPoint(MultipleAddressShipmentAdapterData addressAdapterData, int position) {
        startActivityForResult(PickupPointActivity.createInstance(
                getActivity(),
                position,
                addressAdapterData.getDestinationDistrictName(),
                GetPickupPointsUseCase.generateParams(addressAdapterData)
        ), REQUEST_CHOOSE_PICKUP_POINT);
    }

    @Override
    public void onPromoSuggestionClicked(CartPromoSuggestion cartPromoSuggestion) {

    }

    @Override
    public void onPromoSuggestionCancelled() {

    }

    @Override
    public void onHachikoClicked(MultipleAddressPriceSummaryData addressPriceSummaryData) {
        if (getActivity().getApplication() instanceof ICartCheckoutModuleRouter) {
            startActivityForResult(
                    ((ICartCheckoutModuleRouter) getActivity().getApplication())
                            .tkpdCartCheckoutGetLoyaltyNewCheckoutMarketplaceCartShipmentIntent(
                                    getActivity(), "", addressPriceSummaryData.isCouponActive()
                            ), IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE
            );
        }
    }

    @Override
    public void onShowPromo(String promoMessageString) {
        formatPromoMessage(promoMessage, promoMessageString);
        this.promoMessage.setVisibility(View.VISIBLE);
        shipmentAdapter.hidePromoSuggestion();
    }

    @Override
    public void onRemovePromo() {
        shipmentAdapter.showPromoSuggestion();
        shipmentAdapter.getPriceSummaryData().setAppliedPromo(null);
        shipmentAdapter.notifyDataSetChanged();
        promoMessage.setText("");
        promoMessage.setVisibility(View.GONE);
    }

    private RecyclerView.OnScrollListener onRecyclerViewScrolledListener(
            final ViewGroup totalPaymentLayout
    ) {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findLastVisibleItemPosition();
                CommonUtils.dumper("totalItemCount " + totalItemCount);
                CommonUtils.dumper("lastVisibleItem " + lastVisibleItem);
                if (lastVisibleItem == totalItemCount - 1) {
                    totalPaymentLayout.setVisibility(View.GONE);
                    promoMessage.setVisibility(View.GONE);
                } else {
                    totalPaymentLayout.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(promoMessage.getText())) {
                        promoMessage.setVisibility(View.VISIBLE);
                    } else {
                        promoMessage.setVisibility(View.GONE);
                    }
                }
            }
        };
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.cartShipmentActivity = (ICartShipmentActivity) activity;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CHOOSE_PICKUP_POINT:
                    Store pickupBooth = data.getParcelableExtra(INTENT_DATA_STORE);
                    int pickupPointPosition = data.getIntExtra(INTENT_DATA_POSITION, 0);
                    shipmentAdapter.setPickupPoint(pickupBooth, pickupPointPosition);
                    shipmentAdapter.notifyItemChanged(pickupPointPosition);
                    totalPayment.setText(shipmentAdapter.getTotalPayment());
                    break;
                case REQUEST_CODE_SHIPMENT_DETAIL:
                    ShipmentDetailData shipmentDetailData = data.getParcelableExtra(EXTRA_SHIPMENT_DETAIL_DATA);
                    int shipmentPosition = data.getIntExtra(EXTRA_POSITION, 0);
                    shipmentAdapter.setShipmentDetailData(shipmentPosition, shipmentDetailData);
                    shipmentAdapter.notifyDataSetChanged();
                    totalPayment.setText(shipmentAdapter.getTotalPayment());
                    break;
            }
        }
    }

    @Override
    public void showPromoMessage(CheckPromoCodeCartShipmentResult checkPromoCodeCartShipmentResult,
                                 CartItemPromoHolderData cartItemPromoHolderData) {
        shipmentAdapter.getPriceSummaryData().setAppliedPromo(checkPromoCodeCartShipmentResult);
        shipmentAdapter.notifyDataSetChanged();
        onShowPromo(checkPromoCodeCartShipmentResult.getDataVoucher().getVoucherPromoDesc());
    }

    @Override
    public void showPromoError(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(),
                !TextUtils.isEmpty(message) ? message : "Terjadi kesalahan");
    }

    private Spannable formatPromoMessage(TextView textView, String promoMessage) {
        String formatText = " Hapus";
        promoMessage += formatText;
        int startSpan = promoMessage.indexOf(formatText);
        int endSpan = promoMessage.indexOf(formatText) + formatText.length();
        Spannable formattedPromoMessage = new SpannableString(promoMessage);
        final int color = ContextCompat.getColor(textView.getContext(), R.color.black_54);
        formattedPromoMessage.setSpan(new ForegroundColorSpan(color), startSpan, endSpan,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        formattedPromoMessage.setSpan(new StyleSpan(Typeface.BOLD), startSpan, endSpan,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setTypeface(Typeface.create(FONT_FAMILY_SANS_SERIF_MEDIUM, Typeface.NORMAL));
        formattedPromoMessage.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(color);
                textPaint.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {
                onRemovePromo();
            }
        }, startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(formattedPromoMessage);
        return formattedPromoMessage;
    }
}

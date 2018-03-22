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
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.data.mapper.ShipmentRatesDataMapper;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressShipmentAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.ShipmentDetailData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.transaction.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.transaction.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.transaction.checkout.router.ICartCheckoutModuleRouter;
import com.tokopedia.transaction.checkout.view.adapter.MultipleAddressShipmentAdapter;
import com.tokopedia.transaction.checkout.view.di.component.DaggerMultipleAddressShipmentComponent;
import com.tokopedia.transaction.checkout.view.di.component.MultipleAddressShipmentComponent;
import com.tokopedia.transaction.checkout.view.di.module.MultipleAddressShipmentModule;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemTickerErrorHolderData;
import com.tokopedia.transaction.checkout.view.view.cartlist.CartItemDecoration;
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
    private ViewGroup totalPaymentLayout;
    private TextView confirmButton;
    private RecyclerView orderAddressList;
    private TkpdProgressDialog progressDialogNormal;


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
        progressDialogNormal = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
        totalPayment = view.findViewById(R.id.total_payment_text_view);
        promoMessage = view.findViewById(R.id.tv_promo_message);
        totalPaymentLayout = view.findViewById(R.id.total_payment_layout);
        confirmButton = view.findViewById(R.id.confirm_payment_button);
        orderAddressList = view.findViewById(R.id.order_shipment_list);
    }

    @Override
    protected void setViewListener() {
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
        orderAddressList.setLayoutManager(new LinearLayoutManager(getActivity()));
        shipmentAdapter = new MultipleAddressShipmentAdapter(
                cartPromoSuggestion,
                cartItemPromoHolderData,
                presenter.initiateAdapterData(cartShipmentAddressFormData),
                this);
        orderAddressList.setAdapter(shipmentAdapter);
        orderAddressList.addOnScrollListener(onRecyclerViewScrolledListener(totalPaymentLayout));
        orderAddressList.addItemDecoration(
                new CartItemDecoration((int) getResources().getDimension(R.dimen.new_margin_med),
                        false, 0));
        totalPayment.setText(shipmentAdapter.getTotalPayment());
        confirmButton.setOnClickListener(onConfirmedButtonClicked());
    }

    @Override
    protected void initialVar() {
        getActivity().setTitle(getString(R.string.toolbar_title_shipment_courier));
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

    @Override
    public void showLoading() {
        progressDialogNormal.showDialog();
    }

    @Override
    public void hideLoading() {
        progressDialogNormal.dismiss();
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

    private View.OnClickListener onConfirmedButtonClicked() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.processCheckShipmentFormPrepareCheckout();
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
            createPromoRequest(adapterDataList, appliedPromo);
        }
    }

    private void createPromoRequest(List<MultipleAddressShipmentAdapterData> adapterDataList, CartItemPromoHolderData appliedPromo) {
        cartShipmentActivity.checkPromoCodeShipment(
                presenter.checkPromoSubscription(appliedPromo),
                presenter.generateCheckPromoRequest(adapterDataList, appliedPromo)
        );
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
    public void onShowPromo(String promoMessageString) {
        formatPromoMessage(promoMessage, promoMessageString);
        this.promoMessage.setVisibility(View.VISIBLE);
        shipmentAdapter.hidePromoSuggestion();
    }

    @Override
    public void onRemovePromo() {
        shipmentAdapter.showPromoSuggestion();
        shipmentAdapter.setAppliedPromoData(null);
        promoMessage.setText("");
        promoMessage.setVisibility(View.GONE);
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams
    ) {
        return originParams == null ? AuthUtil.generateParamsNetwork(getActivity()) :
                AuthUtil.generateParamsNetwork(getActivity(), originParams);
    }

    @Override
    public void renderCheckPromoCodeFromSuggestedPromoSuccess(PromoCodeCartListData promoCodeCartListData) {
        this.promoCodeAppliedData = new PromoCodeAppliedData.Builder()
                .typeVoucher(PromoCodeAppliedData.TYPE_VOUCHER)
                .promoCode(promoCodeCartListData.getDataVoucher().getCode())
                .description(promoCodeCartListData.getDataVoucher().getMessageSuccess())
                .amount(promoCodeCartListData.getDataVoucher().getCashbackAmount())
                .build();
        CartItemPromoHolderData cartItemPromoHolderData = new CartItemPromoHolderData();
        cartItemPromoHolderData.setPromoVoucherType(promoCodeAppliedData.getPromoCode(),
                promoCodeAppliedData.getDescription(), promoCodeAppliedData.getAmount());
        checkAppliedPromo(cartItemPromoHolderData);
    }

    @Override
    public void renderErrorCheckPromoCodeFromSuggestedPromo(String message) {
        View view = getView();
        if (view != null) NetworkErrorHelper.showRedCloseSnackbar(view, message);
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
                    totalPayment.setText(shipmentAdapter.getTotalPayment());
                    break;
            }
        } else if (requestCode == IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE) {
            onResultFromRequestCodeLoyalty(resultCode, data);
        }
    }

    private void onResultFromRequestCodeLoyalty(int resultCode, Intent data) {
        if (resultCode == IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.VOUCHER_RESULT_CODE) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String voucherCode = bundle.getString(
                        IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_CODE, "");
                String voucherMessage = bundle.getString(
                        IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_MESSAGE, "");
                long voucherDiscountAmount = bundle.getLong(
                        IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_DISCOUNT_AMOUNT);
                this.promoCodeAppliedData = new PromoCodeAppliedData.Builder()
                        .typeVoucher(PromoCodeAppliedData.TYPE_VOUCHER)
                        .promoCode(voucherCode)
                        .description(voucherMessage)
                        .amount((int) voucherDiscountAmount)
                        .build();
                CartItemPromoHolderData cartPromo = new CartItemPromoHolderData();
                cartPromo.setPromoVoucherType(voucherCode, voucherMessage, voucherDiscountAmount);

                checkAppliedPromo(cartPromo);
            }
        } else if (resultCode == IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.COUPON_RESULT_CODE) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String couponTitle = bundle.getString(
                        IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_TITLE, "");
                String couponMessage = bundle.getString(
                        IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_MESSAGE, "");
                String couponCode = bundle.getString(
                        IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_CODE, "");
                long couponDiscountAmount = bundle.getLong(
                        IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_DISCOUNT_AMOUNT);
                this.promoCodeAppliedData = new PromoCodeAppliedData.Builder()
                        .typeVoucher(PromoCodeAppliedData.TYPE_COUPON)
                        .promoCode(couponCode)
                        .couponTitle(couponTitle)
                        .description(couponMessage)
                        .amount((int) couponDiscountAmount)
                        .build();
                CartItemPromoHolderData cartPromo = new CartItemPromoHolderData();
                cartPromo.setPromoCouponType(
                        couponTitle, couponCode, couponMessage, couponDiscountAmount
                );

                checkAppliedPromo(cartPromo);
            }
        }
    }

    private void checkAppliedPromo(CartItemPromoHolderData cartPromo) {
        shipmentAdapter.setPromo(cartPromo);
        if (shipmentAdapter.hasSelectAllCourier()) {
            createPromoRequest(shipmentAdapter.getAddressDataList(), cartPromo);
        }
    }


    @Override
    public void showPromoMessage(CheckPromoCodeCartShipmentResult checkPromoCodeCartShipmentResult,
                                 CartItemPromoHolderData cartItemPromoHolderData) {

        shipmentAdapter.setAppliedPromoData(checkPromoCodeCartShipmentResult);
        onShowPromo(checkPromoCodeCartShipmentResult.getDataVoucher().getVoucherPromoDesc());
    }

    @Override
    public void showPromoError(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(),
                !TextUtils.isEmpty(message) ? message : "Terjadi kesalahan");
    }

    @Override
    public void renderCheckShipmentPrepareCheckoutSuccess() {
        cartShipmentActivity.checkoutCart(
                presenter.generateCheckoutRequest(shipmentAdapter.getAddressDataList(),
                        shipmentAdapter.getPriceSummaryData())
        );
    }

    @Override
    public void renderErrorDataHasChangedCheckShipmentPrepareCheckout(
            CartShipmentAddressFormData cartShipmentAddressFormData
    ) {
        this.cartShipmentAddressFormData = cartShipmentAddressFormData;
        setViewListener();
    }

    @Override
    public void renderErrorCheckShipmentPrepareCheckout(String message) {

    }

    @Override
    public void renderErrorHttpCheckShipmentPrepareCheckout(String message) {

    }

    @Override
    public void renderErrorNoConnectionCheckShipmentPrepareCheckout(String message) {

    }

    @Override
    public void renderErrorTimeoutConnectionCheckShipmentPrepareCheckout(String message) {

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

    @Override
    public void onCartPromoSuggestionActionClicked(CartPromoSuggestion cartPromoSuggestion, int position) {
        presenter.processCheckPromoCodeFromSuggestedPromo(cartPromoSuggestion.getPromoCode());
    }

    @Override
    public void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestion cartPromoSuggestion, int position) {
        cartPromoSuggestion.setVisible(false);
        shipmentAdapter.notifyDataSetChanged();
        shipmentAdapter.checkAvailableForCheckout();
    }

    @Override
    public void onCartPromoUseVoucherPromoClicked(CartItemPromoHolderData cartPromo, int position) {
        if (getActivity().getApplication() instanceof ICartCheckoutModuleRouter) {
            startActivityForResult(
                    ((ICartCheckoutModuleRouter) getActivity().getApplication())
                            .tkpdCartCheckoutGetLoyaltyNewCheckoutMarketplaceCartListIntent(
                                    getActivity(), true
                            ), IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE
            );
        }
    }

    @Override
    public void onCartPromoCancelVoucherPromoClicked(CartItemPromoHolderData cartPromo, int position) {
        onRemovePromo();
    }

    @Override
    public void onCartPromoTrackingSuccess(CartItemPromoHolderData cartPromo, int position) {

    }

    @Override
    public void onCartPromoTrackingCancelled(CartItemPromoHolderData cartPromo, int position) {

    }

    @Override
    public void onCartItemTickerErrorActionClicked(CartItemTickerErrorHolderData data, int position) {

    }

    @Override
    public void onCartDataEnableToCheckout() {
        confirmButton.setBackgroundResource(R.drawable.bg_button_orange_enabled);
        confirmButton.setTextColor(getResources().getColor(R.color.white));
        confirmButton.setClickable(true);
    }

    @Override
    public void onCartDataDisableToCheckout() {
        confirmButton.setBackgroundResource(R.drawable.bg_button_disabled);
        confirmButton.setTextColor(getResources().getColor(R.color.grey_500));
        confirmButton.setClickable(false);
    }
}

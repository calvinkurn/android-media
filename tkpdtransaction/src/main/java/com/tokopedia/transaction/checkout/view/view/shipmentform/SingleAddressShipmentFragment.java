package com.tokopedia.transaction.checkout.view.view.shipmentform;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest.Data;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.data.entity.request.CheckoutRequest;
import com.tokopedia.transaction.checkout.data.entity.request.DataCheckoutRequest;
import com.tokopedia.transaction.checkout.data.mapper.ShipmentRatesDataMapper;
import com.tokopedia.transaction.checkout.domain.datamodel.ShipmentDetailData;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.CartSellerItemModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.SingleShipmentData;
import com.tokopedia.transaction.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.transaction.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.transaction.checkout.domain.mapper.CartShipmentAddressFormDataConverter;
import com.tokopedia.transaction.checkout.router.ICartCheckoutModuleRouter;
import com.tokopedia.transaction.checkout.view.adapter.SingleAddressShipmentAdapter;
import com.tokopedia.transaction.checkout.view.di.component.DaggerSingleAddressShipmentComponent;
import com.tokopedia.transaction.checkout.view.di.component.SingleAddressShipmentComponent;
import com.tokopedia.transaction.checkout.view.di.module.SingleAddressShipmentModule;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.transaction.checkout.view.view.addressoptions.CartAddressChoiceActivity;
import com.tokopedia.transaction.checkout.view.view.shippingoptions.ShipmentDetailActivity;
import com.tokopedia.transaction.pickuppoint.domain.model.Store;
import com.tokopedia.transaction.pickuppoint.domain.usecase.GetPickupPointsUseCase;
import com.tokopedia.transaction.pickuppoint.view.activity.PickupPointActivity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import static com.tokopedia.transaction.checkout.view.view.shippingoptions.ShipmentDetailActivity.EXTRA_POSITION;
import static com.tokopedia.transaction.checkout.view.view.shippingoptions.ShipmentDetailActivity.EXTRA_SHIPMENT_DETAIL_DATA;
import static com.tokopedia.transaction.pickuppoint.view.contract.PickupPointContract.Constant.INTENT_DATA_STORE;

/**
 * @author Aghny A. Putra on 24/1/18
 */

public class SingleAddressShipmentFragment extends BasePresenterFragment
        implements ICartSingleAddressView, SingleAddressShipmentAdapter.ActionListener {

    private static final String FONT_FAMILY_SANS_SERIF_MEDIUM = "sans-serif-medium";
    public static final String ARG_EXTRA_SHIPMENT_FORM_DATA = "ARG_EXTRA_SHIPMENT_FORM_DATA";
    public static final String ARG_EXTRA_CART_PROMO_SUGGESTION = "ARG_EXTRA_CART_PROMO_SUGGESTION";
    public static final String ARG_EXTRA_PROMO_CODE_APPLIED_DATA = "ARG_EXTRA_PROMO_CODE_APPLIED_DATA";

    private static final Locale LOCALE_ID = new Locale("in", "ID");
    private static final NumberFormat CURRENCY_ID = NumberFormat.getCurrencyInstance(LOCALE_ID);

    private static final String TAG = SingleAddressShipmentFragment.class.getSimpleName();

    private static final int REQUEST_CODE_SHIPMENT_DETAIL = 11;
    private static final int REQUEST_CHOOSE_PICKUP_POINT = 12;

    private RecyclerView mRvCartOrderDetails;
    private TextView mTvSelectPaymentMethod;
    private LinearLayout mLlTotalPaymentLayout;
    private TextView mTvTotalPayment;
    private TextView mTvPromoMessage;
    private CardView mCvBottomLayout;
    private TkpdProgressDialog progressDialogNormal;

    @Inject
    SingleAddressShipmentAdapter mSingleAddressShipmentAdapter;

    @Inject
    SingleAddressShipmentPresenter mSingleAddressShipmentPresenter;

    @Inject
    CartShipmentAddressFormDataConverter mCartShipmentAddressFormDataConverter;

    private ICartShipmentActivity cartShipmentActivityListener;

    private PromoCodeAppliedData promoCodeAppliedData;
    private CartPromoSuggestion cartPromoSuggestion;
    private SingleShipmentData singleShipmentData;

    private List<DataCheckoutRequest> mCheckoutRequestData;
    private List<Data> mPromoRequestData;

    public static SingleAddressShipmentFragment newInstance(CartShipmentAddressFormData cartShipmentAddressFormData,
                                                            PromoCodeAppliedData promoCodeCartListData,
                                                            CartPromoSuggestion cartPromoSuggestionData) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_EXTRA_SHIPMENT_FORM_DATA, cartShipmentAddressFormData);
        bundle.putParcelable(ARG_EXTRA_CART_PROMO_SUGGESTION, cartPromoSuggestionData);
        bundle.putParcelable(ARG_EXTRA_PROMO_CODE_APPLIED_DATA, promoCodeCartListData);

        SingleAddressShipmentFragment fragment = new SingleAddressShipmentFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        SingleAddressShipmentComponent component = DaggerSingleAddressShipmentComponent.builder()
                .singleAddressShipmentModule(new SingleAddressShipmentModule(this))
                .build();
        component.inject(this);
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
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


    protected boolean getOptionsMenuEnable() {
        return false;
    }


    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {
        cartShipmentActivityListener = (ICartShipmentActivity) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        CartShipmentAddressFormData cartShipmentAddressFormData
                = arguments.getParcelable(ARG_EXTRA_SHIPMENT_FORM_DATA);

        singleShipmentData = mCartShipmentAddressFormDataConverter.convert(
                cartShipmentAddressFormData
        );
        promoCodeAppliedData = arguments.getParcelable(ARG_EXTRA_PROMO_CODE_APPLIED_DATA);
        cartPromoSuggestion = arguments.getParcelable(ARG_EXTRA_CART_PROMO_SUGGESTION);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_cart_single_address;
    }

    @Override
    protected void initView(View view) {
        progressDialogNormal = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
        mRvCartOrderDetails = view.findViewById(R.id.rv_cart_order_details);
        mTvSelectPaymentMethod = view.findViewById(R.id.tv_select_payment_method);
        mLlTotalPaymentLayout = view.findViewById(R.id.ll_total_payment_layout);
        mTvTotalPayment = view.findViewById(R.id.tv_total_payment);
        mTvPromoMessage = view.findViewById(R.id.tv_promo_message);
        mCvBottomLayout = view.findViewById(R.id.bottom_layout);
    }

    @Override
    protected void setViewListener() {
        mRvCartOrderDetails.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvCartOrderDetails.setAdapter(mSingleAddressShipmentAdapter);

        mRvCartOrderDetails.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mRvCartOrderDetails != null) {
                    boolean isReachBottomEnd = mRvCartOrderDetails.canScrollVertically(1);
                    mLlTotalPaymentLayout.setVisibility(isReachBottomEnd ? View.VISIBLE : View.GONE);
                    if (!TextUtils.isEmpty(mTvPromoMessage.getText().toString())) {
                        mTvPromoMessage.setVisibility(isReachBottomEnd ? View.VISIBLE : View.GONE);
                    } else {
                        mTvPromoMessage.setVisibility(View.GONE);
                    }
                }
            }
        });

        mTvSelectPaymentMethod.setOnClickListener(getOnClickListenerButtonCheckout());

        mTvTotalPayment.setText("-");
        mCvBottomLayout.setVisibility(View.VISIBLE);

        mSingleAddressShipmentAdapter.addPromoVoucherData(
                CartItemPromoHolderData.createInstanceFromAppliedPromo(promoCodeAppliedData)
        );
        if (promoCodeAppliedData != null) {
            cartPromoSuggestion.setVisible(false);
        }
        mSingleAddressShipmentAdapter.addPromoSuggestionData(cartPromoSuggestion);
        mSingleAddressShipmentAdapter.addAddressShipmentData(singleShipmentData.getRecipientAddress());
        mSingleAddressShipmentAdapter.addCartItemDataList(singleShipmentData.getCartItem());
        mSingleAddressShipmentAdapter.addShipmentCostData(singleShipmentData.getShipmentCost());

    }

    @NonNull
    private View.OnClickListener getOnClickListenerButtonCheckout() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSingleAddressShipmentPresenter.processCheckShipmentPrepareCheckout();
            }
        };
    }

    @Override
    protected void initialVar() {
        getActivity().setTitle("Kurir Pengiriman");
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams
    ) {
        return originParams == null ? AuthUtil.generateParamsNetwork(getActivity()) :
                AuthUtil.generateParamsNetwork(getActivity(), originParams);
    }

    @Override
    public void showLoading() {
        progressDialogNormal.showDialog();
    }

    @Override
    public void hideLoading() {
        progressDialogNormal.dismiss();
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
        updateAppliedPromo(cartItemPromoHolderData);
    }

    @Override
    public void renderErrorCheckPromoCodeFromSuggestedPromo(String message) {
        View view = getView();
        if (view != null) NetworkErrorHelper.showRedCloseSnackbar(view, message);
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void showCancelPickupBoothDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.label_dialog_title_cancel_pickup)
                .setMessage(R.string.label_dialog_message_cancel_pickup_booth)
                .setPositiveButton(R.string.title_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mSingleAddressShipmentAdapter.unSetPickupPoint();
                    }
                })
                .setNegativeButton(R.string.title_no, null)
                .create();

        alertDialog.show();
    }

    @Override
    public void onAddOrChangeAddress() {
        Intent intent = CartAddressChoiceActivity.createInstance(getActivity(),
                CartAddressChoiceActivity.TYPE_REQUEST_SELECT_ADDRESS_FROM_SHORT_LIST);

        startActivityForResult(intent, CartAddressChoiceActivity.REQUEST_CODE);
    }

    @Override
    public void onChooseShipment(int position, CartSellerItemModel cartSellerItemModel,
                                 RecipientAddressModel recipientAddressModel) {
        ShipmentDetailData shipmentDetailData;
        if (cartSellerItemModel.getSelectedShipmentDetailData() != null) {
            shipmentDetailData = cartSellerItemModel.getSelectedShipmentDetailData();
        } else {
            ShipmentRatesDataMapper shipmentRatesDataMapper = new ShipmentRatesDataMapper();
            shipmentDetailData = shipmentRatesDataMapper.getShipmentDetailData(cartSellerItemModel,
                    recipientAddressModel);
        }

        Intent intent = ShipmentDetailActivity.createInstance(getActivity(), shipmentDetailData,
                position);
        startActivityForResult(intent, REQUEST_CODE_SHIPMENT_DETAIL);
    }

    @Override
    public void onChoosePickupPoint(RecipientAddressModel addressAdapterData) {
        startActivityForResult(PickupPointActivity.createInstance(getActivity(),
                addressAdapterData.getDestinationDistrictName(),
                GetPickupPointsUseCase.generateParams(addressAdapterData)
        ), REQUEST_CHOOSE_PICKUP_POINT);
    }

    @Override
    public void onClearPickupPoint(RecipientAddressModel addressAdapterData) {
        showCancelPickupBoothDialog();
    }

    @Override
    public void onEditPickupPoint(RecipientAddressModel addressAdapterData) {
        startActivityForResult(PickupPointActivity.createInstance(getActivity(),
                addressAdapterData.getDestinationDistrictName(),
                GetPickupPointsUseCase.generateParams(addressAdapterData)
        ), REQUEST_CHOOSE_PICKUP_POINT);
    }

    @Override
    public void onCartPromoSuggestionActionClicked(CartPromoSuggestion data, int position) {
        mSingleAddressShipmentPresenter.processCheckPromoCodeFromSuggestedPromo(data.getPromoCode());
    }

    @Override
    public void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestion data, int position) {
        mSingleAddressShipmentAdapter.removeData(position);
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
        onRemovePromoCode();
        cartPromo.setPromoNotActive();
        mSingleAddressShipmentAdapter.updatePromo(null);
        mSingleAddressShipmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCartPromoTrackingSuccess(CartItemPromoHolderData cartPromo, int position) {

    }

    @Override
    public void onCartPromoTrackingCancelled(CartItemPromoHolderData cartPromo, int position) {

    }

    @Override
    public void onTotalPaymentChange(ShipmentCostModel shipmentCostModel) {
        double price = shipmentCostModel.getTotalPrice();
        mTvTotalPayment.setText(price == 0 ? "-" : CURRENCY_ID.format(price));
    }

    @Override
    public void onFinishChoosingShipment(List<Data> promoRequestData,
                                         List<DataCheckoutRequest> checkoutRequestData) {
        mPromoRequestData = promoRequestData;
        mCheckoutRequestData = checkoutRequestData;
        if (promoCodeAppliedData != null && mSingleAddressShipmentAdapter.hasAppliedPromoCode()) {
            requestPromo();
        }
    }

    private void requestPromo() {
        cartShipmentActivityListener.checkPromoCodeShipment(
                mSingleAddressShipmentPresenter.getSubscriberCheckPromoShipment(),
                new CheckPromoCodeCartShipmentRequest.Builder()
                        .promoCode(promoCodeAppliedData.getPromoCode())
                        .data(mPromoRequestData)
                        .build()
        );
    }

    @Override
    public void onCartDataEnableToCheckout() {
        mTvSelectPaymentMethod.setBackgroundResource(R.drawable.medium_green_button_rounded);
        mTvSelectPaymentMethod.setTextColor(getResources().getColor(R.color.white));
        mTvSelectPaymentMethod.setOnClickListener(getOnClickListenerButtonCheckout());
    }

    @Override
    public void onCartDataDisableToCheckout() {
        mTvSelectPaymentMethod.setBackgroundResource(R.drawable.bg_grey_button_rounded);
        mTvSelectPaymentMethod.setTextColor(getResources().getColor(R.color.grey_500));
        mTvSelectPaymentMethod.setOnClickListener(null);
    }


    @Override
    public void onShowPromoMessage(String promoMessage) {
        formatPromoMessage(mTvPromoMessage, promoMessage);
        mTvPromoMessage.setVisibility(View.VISIBLE);
    }

    private void formatPromoMessage(TextView textView, String promoMessage) {
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
                onRemovePromoCode();
            }
        }, startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(formattedPromoMessage);
    }

    @Override
    public void onHidePromoMessage() {
        mTvPromoMessage.setVisibility(View.GONE);
    }

    @Override
    public void onRemovePromoCode() {
        mTvPromoMessage.setText("");
        mTvPromoMessage.setVisibility(View.GONE);
        mSingleAddressShipmentAdapter.updatePromo(null);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.cartShipmentActivityListener = (ICartShipmentActivity) activity;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CartAddressChoiceActivity.REQUEST_CODE) {
            onResultFromRequestCodeAddressOptions(resultCode, data);
        } else if ((requestCode == REQUEST_CHOOSE_PICKUP_POINT
                || requestCode == REQUEST_CODE_SHIPMENT_DETAIL) && resultCode == Activity.RESULT_OK) {
            onResultFromRequestCodeCourierOptions(requestCode, data);
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

                updateAppliedPromo(cartPromo);
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

                updateAppliedPromo(cartPromo);
            }
        }
    }

    private void updateAppliedPromo(CartItemPromoHolderData cartPromo) {
        mSingleAddressShipmentAdapter.updateItemPromoVoucher(cartPromo);
        if (mSingleAddressShipmentAdapter.hasSetAllCourier()) {
            SingleAddressShipmentAdapter.RequestData requestData =
                    mSingleAddressShipmentAdapter.getRequestPromoData(
                            mSingleAddressShipmentAdapter.getCartSellerItemModelList());
            mPromoRequestData = requestData.getPromoRequestData();
            requestPromo();
        }
        mSingleAddressShipmentAdapter.notifyDataSetChanged();
    }

    private void onResultFromRequestCodeCourierOptions(int requestCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHOOSE_PICKUP_POINT:
                Store pickupBooth = data.getParcelableExtra(INTENT_DATA_STORE);
                mSingleAddressShipmentAdapter.setPickupPoint(pickupBooth);
                break;
            case REQUEST_CODE_SHIPMENT_DETAIL:
                ShipmentDetailData shipmentDetailData = data.getParcelableExtra(EXTRA_SHIPMENT_DETAIL_DATA);
                int position = data.getIntExtra(EXTRA_POSITION, 0);
                mSingleAddressShipmentAdapter.updateSelectedShipment(position, shipmentDetailData);
            default:
                break;
        }
    }

    private void onResultFromRequestCodeAddressOptions(int resultCode, Intent data) {
        switch (resultCode) {
            case CartAddressChoiceActivity.RESULT_CODE_ACTION_SELECT_ADDRESS:
                RecipientAddressModel selectedAddress = data.getParcelableExtra(
                        CartAddressChoiceActivity.EXTRA_SELECTED_ADDRESS_DATA);

                mSingleAddressShipmentAdapter.updateSelectedAddress(selectedAddress);
                break;
            case CartAddressChoiceActivity.RESULT_CODE_ACTION_TO_MULTIPLE_ADDRESS_FORM:
                Intent intent = new Intent();
                intent.putExtra(CartShipmentActivity.EXTRA_SELECTED_ADDRESS_RECIPIENT_DATA,
                        mSingleAddressShipmentAdapter.getSelectedAddressReceipent());
                cartShipmentActivityListener.closeWithResult(
                        CartShipmentActivity.RESULT_CODE_ACTION_TO_MULTIPLE_ADDRESS_FORM, intent);
                break;

            default:
                break;
        }
    }

    private CheckoutRequest generateCheckoutRequest(String promoCode, int isDonation) {
        if (mCheckoutRequestData == null) {
            // Show error cant checkout
            return null;
        }

        return new CheckoutRequest.Builder()
                .promoCode(promoCode)
                .isDonation(isDonation)
                .data(mCheckoutRequestData)
                .build();
    }


    @Override
    public void renderCheckPromoShipmentDataSuccess(
            CheckPromoCodeCartShipmentResult checkPromoCodeCartShipmentResult
    ) {
        mSingleAddressShipmentAdapter.updatePromo(checkPromoCodeCartShipmentResult.getDataVoucher());
    }

    @Override
    public void renderErrorCheckPromoShipmentData(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
    }

    @Override
    public void renderErrorHttpCheckPromoShipmentData(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
    }

    @Override
    public void renderErrorNoConnectionCheckPromoShipmentData(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
    }

    @Override
    public void renderErrorTimeoutConnectionCheckPromoShipmentData(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
    }

    @Override
    public void renderCheckShipmentPrepareCheckoutSuccess() {
        CheckoutRequest checkoutData = generateCheckoutRequest(
                promoCodeAppliedData != null && promoCodeAppliedData.getPromoCode() != null ?
                        promoCodeAppliedData.getPromoCode() : "", 0
        );
        if (checkoutData != null) cartShipmentActivityListener.checkoutCart(checkoutData);
    }

    @Override
    public void renderErrorDataHasChangedCheckShipmentPrepareCheckout(
            CartShipmentAddressFormData cartShipmentAddressFormData
    ) {
        SingleShipmentData singleShipmentData = mCartShipmentAddressFormDataConverter.convert(
                cartShipmentAddressFormData
        );
        this.singleShipmentData.setError(singleShipmentData.isError());
        this.singleShipmentData.setErrorMessage(singleShipmentData.getErrorMessage());
        this.singleShipmentData.setWarning(singleShipmentData.isWarning());
        this.singleShipmentData.setWarningMessage(singleShipmentData.getWarningMessage());

        List<CartSellerItemModel> cartItem = singleShipmentData.getCartItem();
        for (int i = 0, cartItemSize = cartItem.size(); i < cartItemSize; i++) {
            CartSellerItemModel data = cartItem.get(i);
            this.singleShipmentData.getCartItem().get(i).setError(data.isError());
            this.singleShipmentData.getCartItem().get(i).setErrorMessage(data.getErrorMessage());
            this.singleShipmentData.getCartItem().get(i).setWarning(data.isWarning());
            this.singleShipmentData.getCartItem().get(i).setWarningMessage(data.getWarningMessage());
        }

        mSingleAddressShipmentAdapter.clearData();

        mSingleAddressShipmentAdapter.addPromoVoucherData(
                CartItemPromoHolderData.createInstanceFromAppliedPromo(promoCodeAppliedData)
        );
        mSingleAddressShipmentAdapter.addPromoSuggestionData(cartPromoSuggestion);
        mSingleAddressShipmentAdapter.addAddressShipmentData(singleShipmentData.getRecipientAddress());
        mSingleAddressShipmentAdapter.addCartItemDataList(singleShipmentData.getCartItem());
        mSingleAddressShipmentAdapter.addShipmentCostData(singleShipmentData.getShipmentCost());
    }

    @Override
    public void renderErrorCheckShipmentPrepareCheckout(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
    }

    @Override
    public void renderErrorHttpCheckShipmentPrepareCheckout(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
    }

    @Override
    public void renderErrorNoConnectionCheckShipmentPrepareCheckout(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
    }

    @Override
    public void renderErrorTimeoutConnectionCheckShipmentPrepareCheckout(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
    }
}
package com.tokopedia.transaction.checkout.view.view.shipmentform;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.data.mapper.ShipmentRatesDataMapper;
import com.tokopedia.transaction.checkout.domain.datamodel.ShipmentDetailData;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.CartPayableDetailModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.CartSellerItemModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.CartSingleAddressData;
import com.tokopedia.transaction.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.transaction.checkout.domain.mapper.SingleAddressShipmentDataConverter;
import com.tokopedia.transaction.checkout.router.ICartCheckoutModuleRouter;
import com.tokopedia.transaction.checkout.view.adapter.SingleAddressShipmentAdapter;
import com.tokopedia.transaction.checkout.view.di.component.DaggerSingleAddressShipmentComponent;
import com.tokopedia.transaction.checkout.view.di.component.SingleAddressShipmentComponent;
import com.tokopedia.transaction.checkout.view.di.module.SingleAddressShipmentModule;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartPromo;
import com.tokopedia.transaction.checkout.view.view.addressoptions.CartAddressChoiceActivity;
import com.tokopedia.transaction.checkout.view.view.shippingoptions.ShipmentDetailActivity;
import com.tokopedia.transaction.pickuppoint.domain.model.Store;
import com.tokopedia.transaction.pickuppoint.domain.usecase.GetPickupPointsUseCase;
import com.tokopedia.transaction.pickuppoint.view.activity.PickupPointActivity;

import java.text.NumberFormat;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tokopedia.transaction.checkout.view.view.shippingoptions.ShipmentDetailActivity.EXTRA_SHIPMENT_DETAIL_DATA;
import static com.tokopedia.transaction.checkout.view.view.shippingoptions.ShipmentDetailActivity.EXTRA_SINGLE_ADDRESS_POSITION;
import static com.tokopedia.transaction.pickuppoint.view.contract.PickupPointContract.Constant.INTENT_DATA_STORE;

/**
 * @author Aghny A. Putra on 24/1/18
 */

public class SingleAddressShipmentFragment extends BasePresenterFragment
        implements ICartSingleAddressView<CartSingleAddressData>,
        SingleAddressShipmentAdapter.SingleAddressShipmentAdapterListener {

    public static final String ARG_EXTRA_SHIPMENT_FORM_DATA = "ARG_EXTRA_SHIPMENT_FORM_DATA";
    public static final String ARG_EXTRA_CART_PROMO_SUGGESTION = "ARG_EXTRA_CART_PROMO_SUGGESTION";
    public static final String ARG_EXTRA_PROMO_CODE_APPLIED_DATA = "ARG_EXTRA_PROMO_CODE_APPLIED_DATA";

    private static final Locale LOCALE_ID = new Locale("in", "ID");
    private static final NumberFormat CURRENCY_ID = NumberFormat.getCurrencyInstance(LOCALE_ID);

    private static final String TAG = SingleAddressShipmentFragment.class.getSimpleName();

    private static final int REQUEST_CODE_SHIPMENT_DETAIL = 11;
    private static final int REQUEST_CHOOSE_PICKUP_POINT = 12;

    @BindView(R2.id.rv_cart_order_details)
    RecyclerView mRvCartOrderDetails;
    @BindView(R2.id.tv_select_payment_method)
    TextView mTvSelectPaymentMethod;
    @BindView(R2.id.ll_total_payment_layout)
    LinearLayout mLlTotalPaymentLayout;
    @BindView(R2.id.tv_total_payment)
    TextView mTvTotalPayment;

    @Inject
    SingleAddressShipmentAdapter mSingleAddressShipmentAdapter;
    @Inject
    SingleAddressShipmentPresenter mSingleAddressShipmentPresenter;
    @Inject
    SingleAddressShipmentDataConverter mSingleAddressShipmentDataConverter;

    ICartShipmentActivity cartShipmentActivityListener;

    private CartSingleAddressData mCartSingleAddressData;
    private PromoCodeCartListData promoCodeAppliedData;

    public static SingleAddressShipmentFragment newInstance(CartShipmentAddressFormData cartShipmentAddressFormData,
                                                            PromoCodeCartListData promoCodeCartListData,
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
    protected String getScreenName() {
        return TAG;
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

    /**
     * apakah fragment ini support options menu?
     *
     * @return iya atau tidak
     */
    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    /**
     * instantiate presenter disini. sesuai dengan Type param di class
     */
    @Override
    protected void initialPresenter() {
    }

    /**
     * Cast si activity ke listener atau bisa juga ini untuk context activity
     *
     * @param activity si activity yang punya fragment
     */
    @Override
    protected void initialListener(Activity activity) {
    }

    /**
     * kalau memang argument tidak kosong. ini data argumentnya
     *
     * @param arguments argument nya
     */
    @Override
    protected void setupArguments(Bundle arguments) {
        CartShipmentAddressFormData cartShipmentAddressFormData
                = arguments.getParcelable(ARG_EXTRA_SHIPMENT_FORM_DATA);
        promoCodeAppliedData = arguments.getParcelable(ARG_EXTRA_PROMO_CODE_APPLIED_DATA);
        mCartSingleAddressData = mSingleAddressShipmentDataConverter.convert(cartShipmentAddressFormData);
        CartPromoSuggestion cartPromoSuggestion = arguments.getParcelable(ARG_EXTRA_CART_PROMO_SUGGESTION);
        mCartSingleAddressData.setCartPromoSuggestion(cartPromoSuggestion);
        mCartSingleAddressData.setCartPromo(new CartPromo());
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_cart_single_address;
    }

    @Override
    protected void initView(View view) {
        mRvCartOrderDetails.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvCartOrderDetails.setAdapter(mSingleAddressShipmentAdapter);
        mRvCartOrderDetails.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mRvCartOrderDetails != null) {
                    boolean isReachBottomEnd = mRvCartOrderDetails.canScrollVertically(1);
                    mLlTotalPaymentLayout.setVisibility(isReachBottomEnd ? View.VISIBLE : View.GONE);
                }
            }
        });
        mSingleAddressShipmentPresenter.attachView(this);

        onTotalPaymentChange(mCartSingleAddressData.getCartPayableDetailModel());
    }

    /**
     * set listener atau attribute si view. misalkan texView.setText("blablalba");
     */
    @Override
    protected void setViewListener() {
        mSingleAddressShipmentAdapter.setViewListener(this);
        mSingleAddressShipmentPresenter.getCartShipmentData(mCartSingleAddressData);
    }

    /**
     * initial Variabel di fragment, selain yg sifatnya widget. Misal: variable state, handler dll
     */
    @Override
    protected void initialVar() {
        getActivity().setTitle("Kurir Pengiriman");
    }

    /**
     * setup aksi, attr, atau listener untuk si variable. misal. appHandler.startAction();
     */
    @Override
    protected void setActionVar() {
    }

    @Override
    public void show(CartSingleAddressData cartSingleAddressData) {
        mSingleAddressShipmentAdapter.updateData(cartSingleAddressData);
        mSingleAddressShipmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {

    }

    private void showCancelPickupBoothDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.label_dialog_title_cancel_pickup)
                .setMessage(R.string.label_dialog_message_cancel_pickup_booth)
                .setPositiveButton(R.string.title_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mSingleAddressShipmentAdapter.unSetPickupPoint();
                        mSingleAddressShipmentAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.title_no, null)
                .create();

        alertDialog.show();
    }

    @Override
    public void onAddOrChangeAddress() {
        startActivityForResult(CartAddressChoiceActivity.createInstance(getActivity(),
                CartAddressChoiceActivity.TYPE_REQUEST_FULL_SELECTION,
                mCartSingleAddressData.getRecipientAddressModel()),
                CartAddressChoiceActivity.REQUEST_CODE);
    }

    @Override
    public void onChooseShipment(int position, CartSellerItemModel cartSellerItemModel) {
        ShipmentDetailData shipmentDetailData;
        if (cartSellerItemModel.getSelectedShipmentDetailData() != null) {
            shipmentDetailData = cartSellerItemModel.getSelectedShipmentDetailData();
        } else {
            ShipmentRatesDataMapper shipmentRatesDataMapper = new ShipmentRatesDataMapper();
            shipmentDetailData = shipmentRatesDataMapper.getShipmentDetailData(cartSellerItemModel);
        }

        startActivityForResult(ShipmentDetailActivity.createInstance(
                getActivity(), shipmentDetailData, position), REQUEST_CODE_SHIPMENT_DETAIL);
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

    }

    @Override
    public void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestion data, int position) {

    }

    @Override
    public void onCartPromoUseVoucherPromoClicked(CartPromo cartPromo, int position) {
        if (getActivity().getApplication() instanceof ICartCheckoutModuleRouter) {
            startActivityForResult(
                    ((ICartCheckoutModuleRouter) getActivity().getApplication())
                            .tkpdCartCheckoutGetLoyaltyNewCheckoutMarketplaceCartShipmentIntent(
                                    getActivity(), "", true
                            ), IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE
            );
        }
    }

    @Override
    public void onCartPromoCancelVoucherPromoClicked(CartPromo cartPromo, int position) {
        cartPromo.setPromoNotActive();
        mSingleAddressShipmentAdapter.notifyItemChanged(position);
    }

    @Override
    public void onCartPromoTrackingSuccess(CartPromo cartPromo, int position) {

    }

    @Override
    public void onCartPromoTrackingCancelled(CartPromo cartPromo, int position) {

    }

    @Override
    public void onTotalPaymentChange(CartPayableDetailModel cartPayableDetailModel) {
        double price = cartPayableDetailModel.getTotalPrice();
        mTvTotalPayment.setText(CURRENCY_ID.format(price));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.cartShipmentActivityListener = (ICartShipmentActivity) activity;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CartAddressChoiceActivity.REQUEST_CODE) {
            switch (resultCode) {
                case CartAddressChoiceActivity.RESULT_CODE_ACTION_SELECT_ADDRESS:
                    RecipientAddressModel thisSelectedAddressData = data.getParcelableExtra(
                            CartAddressChoiceActivity.EXTRA_SELECTED_ADDRESS_DATA);

                    mCartSingleAddressData.setRecipientAddressModel(thisSelectedAddressData);
                    mSingleAddressShipmentPresenter.getCartShipmentData(mCartSingleAddressData);

                    break;

                case CartAddressChoiceActivity.RESULT_CODE_ACTION_TO_MULTIPLE_ADDRESS_FORM:
                    Intent intent = new Intent();
                    intent.putExtra(CartShipmentActivity.EXTRA_SELECTED_ADDRESS_RECIPIENT_DATA,
                            mCartSingleAddressData.getRecipientAddressModel());
                    cartShipmentActivityListener.closeWithResult(
                            CartShipmentActivity.RESULT_CODE_ACTION_TO_MULTIPLE_ADDRESS_FORM, intent);
                    break;

                default:
                    break;
            }
        }


        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CHOOSE_PICKUP_POINT:
                    Store pickupBooth = data.getParcelableExtra(INTENT_DATA_STORE);
                    mSingleAddressShipmentAdapter.setPickupPoint(pickupBooth);
                    mSingleAddressShipmentAdapter.notifyDataSetChanged();
                    break;

                case REQUEST_CODE_SHIPMENT_DETAIL:
                    ShipmentDetailData shipmentDetailData = data.getParcelableExtra(EXTRA_SHIPMENT_DETAIL_DATA);
                    int position = data.getIntExtra(EXTRA_SINGLE_ADDRESS_POSITION, 0);
                    mSingleAddressShipmentAdapter.updateSelectedShipment(position, shipmentDetailData);
                    mSingleAddressShipmentAdapter.notifyDataSetChanged();
                default:
                    break;
            }
        }

    }

}

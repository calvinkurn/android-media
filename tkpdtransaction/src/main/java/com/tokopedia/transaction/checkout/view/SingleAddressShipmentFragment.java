package com.tokopedia.transaction.checkout.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.di.component.DaggerSingleAddressShipmentComponent;
import com.tokopedia.transaction.checkout.di.component.SingleAddressShipmentComponent;
import com.tokopedia.transaction.checkout.di.module.SingleAddressShipmentModule;
import com.tokopedia.transaction.checkout.domain.SingleAddressShipmentDataConverter;
import com.tokopedia.transaction.checkout.view.activity.CartAddressChoiceActivity;
import com.tokopedia.transaction.checkout.view.activity.ShipmentDetailActivity;
import com.tokopedia.transaction.checkout.view.adapter.SingleAddressShipmentAdapter;
import com.tokopedia.transaction.checkout.view.data.CartItemData;
import com.tokopedia.transaction.checkout.view.data.CartPromoSuggestion;
import com.tokopedia.transaction.checkout.view.data.CartSellerItemModel;
import com.tokopedia.transaction.checkout.view.data.CartSingleAddressData;
import com.tokopedia.transaction.checkout.view.data.ShipmentRecipientModel;
import com.tokopedia.transaction.checkout.view.presenter.SingleAddressShipmentPresenter;
import com.tokopedia.transaction.checkout.view.view.ICartSingleAddressView;
import com.tokopedia.transaction.pickuppoint.domain.model.Store;
import com.tokopedia.transaction.pickuppoint.domain.usecase.GetPickupPointsUseCase;
import com.tokopedia.transaction.pickuppoint.view.activity.PickupPointActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tokopedia.transaction.pickuppoint.view.contract.PickupPointContract.Constant.INTENT_DATA_STORE;

/**
 * @author Aghny A. Putra on 24/1/18
 */
public class SingleAddressShipmentFragment extends BasePresenterFragment
        implements ICartSingleAddressView<CartSingleAddressData>,
        SingleAddressShipmentAdapter.SingleAddressShipmentAdapterListener {

    public static final String ARG_EXTRA_CART_DATA_LIST = "ARG_EXTRA_CART_DATA_LIST";
    public static final String ARG_EXTRA_CART_PROMO_SUGGESTION = "ARG_EXTRA_CART_PROMO_SUGGESTION";

    private static final Locale LOCALE_ID = new Locale("in", "ID");
    private static final NumberFormat CURRENCY_ID = NumberFormat.getCurrencyInstance(LOCALE_ID);

    private static final String TAG = SingleAddressShipmentFragment.class.getSimpleName();
    private static final int REQUEST_CODE_SHIPMENT_DETAIL = 11;
    private static final int REQUEST_CHOOSE_PICKUP_POINT = 12;
    private static final int REQUEST_CODE_CHOOSE_ADDRESS = 13;

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

    private CartSingleAddressData mCartSingleAddressData;

    public static SingleAddressShipmentFragment newInstance(List<CartItemData> cartItemDataList,
                                                            CartPromoSuggestion cartPromoSuggestionData) {
        SingleAddressShipmentFragment fragment = new SingleAddressShipmentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARG_EXTRA_CART_DATA_LIST,
                (ArrayList<? extends Parcelable>) cartItemDataList);
        bundle.putParcelable(ARG_EXTRA_CART_PROMO_SUGGESTION, cartPromoSuggestionData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        SingleAddressShipmentComponent component = DaggerSingleAddressShipmentComponent.builder()
                .singleAddressShipmentModule(new SingleAddressShipmentModule())
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
        List<CartItemData> cartDataList = arguments.getParcelableArrayList(ARG_EXTRA_CART_DATA_LIST);
        mCartSingleAddressData = mSingleAddressShipmentDataConverter.convert(cartDataList);
        CartPromoSuggestion cartPromoSuggestion = arguments.getParcelable(ARG_EXTRA_CART_PROMO_SUGGESTION);

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_cart_single_address;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);

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

        double price = mCartSingleAddressData.getCartPayableDetailModel().getTotalPrice();
        mTvTotalPayment.setText(CURRENCY_ID.format(price));
    }

    /**
     * set listener atau attribute si view. misalkan texView.setText("blablalba");
     */
    @Override
    protected void setViewListener() {
        mSingleAddressShipmentAdapter.setViewListener(this);
        mSingleAddressShipmentPresenter.getCartSingleAddressItemView(mCartSingleAddressData);
    }

    /**
     * initial Variabel di fragment, selain yg sifatnya widget. Misal: variable state, handler dll
     */
    @Override
    protected void initialVar() {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.label_dialog_title_cancel_pickup);
        builder.setMessage(R.string.label_dialog_message_cancel_pickup_booth);
        builder.setPositiveButton(R.string.title_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSingleAddressShipmentAdapter.unSetPickupPoint();
                mSingleAddressShipmentAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(R.string.title_no, null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onAddOrChangeAddress(List<CartSellerItemModel> sellerItemModels) {
        startActivityForResult(CartAddressChoiceActivity
                .createInstance(getActivity(), sellerItemModels), REQUEST_CODE_CHOOSE_ADDRESS);
    }

    @Override
    public void onChooseShipment() {
        startActivityForResult(ShipmentDetailActivity.createInstance(getActivity()), REQUEST_CODE_SHIPMENT_DETAIL);
    }

    @Override
    public void onChoosePickupPoint(ShipmentRecipientModel addressAdapterData) {
        startActivityForResult(PickupPointActivity.createInstance(
                getActivity(),
                addressAdapterData.getDestinationDistrictName(),
                GetPickupPointsUseCase.generateParams(addressAdapterData)
        ), REQUEST_CHOOSE_PICKUP_POINT);
    }

    @Override
    public void onClearPickupPoint(ShipmentRecipientModel addressAdapterData) {
        showCancelPickupBoothDialog();
    }

    @Override
    public void onEditPickupPoint(ShipmentRecipientModel addressAdapterData) {
        startActivityForResult(PickupPointActivity.createInstance(
                getActivity(),
                addressAdapterData.getDestinationDistrictName(),
                GetPickupPointsUseCase.generateParams(addressAdapterData)
        ), REQUEST_CHOOSE_PICKUP_POINT);
    }

    @Override
    public void onTotalPaymentUpdate(String priceFormat) {
        mTvTotalPayment.setText(priceFormat);
    }

    @Override
    public void onRecyclerViewReachBottom() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CHOOSE_PICKUP_POINT:
                    Store pickupBooth = data.getParcelableExtra(INTENT_DATA_STORE);
                    mSingleAddressShipmentAdapter.setPickupPoint(pickupBooth);
                    mSingleAddressShipmentAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }

    }

}

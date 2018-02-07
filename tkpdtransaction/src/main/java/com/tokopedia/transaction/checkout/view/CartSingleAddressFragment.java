package com.tokopedia.transaction.checkout.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.di.component.CartSingleAddressComponent;
import com.tokopedia.transaction.checkout.di.component.DaggerCartSingleAddressComponent;
import com.tokopedia.transaction.checkout.di.module.CartSingleAddressModule;
import com.tokopedia.transaction.checkout.view.activity.CartAddressChoiceActivity;
import com.tokopedia.transaction.checkout.view.activity.ShipmentDetailActivity;
import com.tokopedia.transaction.checkout.view.adapter.CartSingleAddressAdapter;
import com.tokopedia.transaction.checkout.view.adapter.ShipmentAddressListAdapter;
import com.tokopedia.transaction.checkout.view.data.CartSingleAddressData;
import com.tokopedia.transaction.checkout.view.data.ShipmentRecipientModel;
import com.tokopedia.transaction.checkout.view.presenter.CartSingleAddressPresenter;
import com.tokopedia.transaction.checkout.view.view.ICartSingleAddressView;
import com.tokopedia.transaction.pickuppoint.domain.model.Store;
import com.tokopedia.transaction.pickuppoint.domain.usecase.GetPickupPointsUseCase;
import com.tokopedia.transaction.pickuppoint.view.activity.PickupPointActivity;
import com.tokopedia.transaction.utils.TkpdRxBus;
import com.tokopedia.transaction.utils.RxBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.tokopedia.transaction.pickuppoint.view.contract.PickupPointContract.Constant.INTENT_DATA_STORE;

/**
 * @author Aghny A. Putra on 24/1/18
 */
public class CartSingleAddressFragment extends BasePresenterFragment
        implements ICartSingleAddressView<CartSingleAddressData>,
        CartSingleAddressAdapter.SingleAddressShipmentAdapterListener {

    private static final String TAG = CartSingleAddressFragment.class.getSimpleName();
    private static final int REQUEST_CODE_SHIPMENT_DETAIL = 11;
    private static final int REQUEST_CHOOSE_PICKUP_POINT = 12;
    private static final int REQUEST_CODE_CHOOSE_ADDRESS = 13;

    @BindView(R2.id.rv_cart_order_details) RecyclerView mRvCartOrderDetails;

    @Inject CartSingleAddressAdapter mCartSingleAddressAdapter;
    @Inject CartSingleAddressPresenter mCartSingleAddressPresenter;

    private static RxBus sRxBus;

    public static CartSingleAddressFragment newInstance() {
        sRxBus = RxBus.instanceOf();
        return new CartSingleAddressFragment();
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        CartSingleAddressComponent component = DaggerCartSingleAddressComponent.builder()
                .cartSingleAddressModule(new CartSingleAddressModule())
                .build();
        component.inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        sRxBus.getEvents()
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        if (o instanceof ShipmentAddressListAdapter.Event) {
                            String msg = ((ShipmentAddressListAdapter.Event) o).getMessage();
                            ShipmentRecipientModel recipientModel = (ShipmentRecipientModel)((ShipmentAddressListAdapter.Event) o).getObject();
                            Log.d(TAG, msg + " " + recipientModel.getRecipientName());
                        }
                    }
                });
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
    protected void onFirstTimeLaunched() {}

    @Override
    public void onSaveState(Bundle state) {}

    @Override
    public void onRestoreState(Bundle savedState) {}

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
    protected void initialPresenter() {}

    /**
     * Cast si activity ke listener atau bisa juga ini untuk context activity
     *
     * @param activity si activity yang punya fragment
     */
    @Override
    protected void initialListener(Activity activity) {}

    /**
     * kalau memang argument tidak kosong. ini data argumentnya
     *
     * @param arguments argument nya
     */
    @Override
    protected void setupArguments(Bundle arguments) {}

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_cart_single_address;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);

        mRvCartOrderDetails.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvCartOrderDetails.setAdapter(mCartSingleAddressAdapter);

        mCartSingleAddressPresenter.attachView(this);
        mCartSingleAddressAdapter.setViewListener(this);
    }

    /**
     * set listener atau attribute si view. misalkan texView.setText("blablalba");
     */
    @Override
    protected void setViewListener() {
        mCartSingleAddressPresenter.getCartSingleAddressItemView();
    }

    /**
     * initial Variabel di fragment, selain yg sifatnya widget. Misal: variable state, handler dll
     */
    @Override
    protected void initialVar() {}

    /**
     * setup aksi, attr, atau listener untuk si variable. misal. appHandler.startAction();
     */
    @Override
    protected void setActionVar() {}

    @OnClick(R2.id.btn_next_to_payment_option)
    protected void onClickToPaymentSection() {
        Toast.makeText(getActivity(), "Select Payment Options", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void show(CartSingleAddressData cartSingleAddressData) {
        mCartSingleAddressAdapter.updateData(cartSingleAddressData);
        mCartSingleAddressAdapter.notifyDataSetChanged();
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
                mCartSingleAddressAdapter.unSetPickupPoint();
                mCartSingleAddressAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(R.string.title_no, null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onAddOrChangeAddress(ShipmentRecipientModel shipmentRecipientModel) {
        startActivityForResult(CartAddressChoiceActivity.createInstance(getActivity(), shipmentRecipientModel), REQUEST_CODE_CHOOSE_ADDRESS);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CHOOSE_PICKUP_POINT:
                    Store pickupBooth = data.getParcelableExtra(INTENT_DATA_STORE);
                    mCartSingleAddressAdapter.setPickupPoint(pickupBooth);
                    mCartSingleAddressAdapter.notifyDataSetChanged();
                    break;
            }
        }

    }

}

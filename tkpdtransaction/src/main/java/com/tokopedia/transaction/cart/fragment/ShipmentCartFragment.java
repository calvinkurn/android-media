package com.tokopedia.transaction.cart.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.manage.people.address.activity.AddAddressActivity;
import com.tokopedia.core.manage.people.address.activity.ChooseAddressActivity;
import com.tokopedia.transaction.cart.listener.IShipmentCartView;
import com.tokopedia.transaction.cart.model.ShipmentCartPassData;
import com.tokopedia.transaction.cart.model.shipmentcart.ShipmentCartData;
import com.tokopedia.transaction.cart.presenter.IShipmentCartPresenter;

import butterknife.Bind;

/**
 * @author anggaprasetiyo on 11/2/16.
 *         modified by alvarisi
 */

public class ShipmentCartFragment extends BasePresenterFragment<IShipmentCartPresenter>
        implements IShipmentCartView {
    public static final int CHOOSE_ADDRESS = 0;
    private static final String ARG_PARAM_EXTRA_SHIPMENT_CART_PASS_DATA =
            "ARG_PARAM_EXTRA_SHIPMENT_CART_PASS_DATA";

    @Bind(R2.id.tv_title_address)
    TextView tvTitleAddress;
    @Bind(R2.id.tv_detail_address)
    TextView tvDetailAddress;
    @Bind(R2.id.btn_choose_address)
    TextView btnChooseAddress;
    @Bind(R2.id.btn_add_address)
    TextView btnAddAddress;
    @Bind(R2.id.cv_address)
    CardView cvAddress;
    @Bind(R2.id.sp_shipment)
    AppCompatSpinner spShipment;
    @Bind(R2.id.sp_shipment_package)
    AppCompatSpinner spShipmentPackage;
    @Bind(R2.id.cv_shipment)
    CardView cvShipment;
    @Bind(R2.id.iv_icon_geo_location)
    ImageView ivIconGeoLocation;
    @Bind(R2.id.btn_change_value_location)
    ImageView btnChangeValueLocation;
    @Bind(R2.id.tv_value_location)
    AppCompatTextView tvValueLocation;
    @Bind(R2.id.layout_value_location)
    RelativeLayout layoutValueLocation;
    @Bind(R2.id.cv_geo_location)
    CardView cvGeoLocation;
    @Bind(R2.id.pb_price)
    ProgressBar pbPrice;
    @Bind(R2.id.tv_price)
    TextView tvPrice;
    @Bind(R2.id.holder_price)
    RelativeLayout holderPrice;
    @Bind(R2.id.cb_prices)
    CardView cbPrices;
    @Bind(R2.id.btn_save)
    AppCompatTextView btnSave;
    @Bind(R2.id.container)
    LinearLayout holderContainer;

    private ShipmentCartPassData shipmentCart;

    public static ShipmentCartFragment newInstance(ShipmentCartPassData passData) {
        ShipmentCartFragment fragment = new ShipmentCartFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PARAM_EXTRA_SHIPMENT_CART_PASS_DATA, passData);
        fragment.setArguments(bundle);
        return fragment;
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
        shipmentCart = arguments.getParcelable(ARG_PARAM_EXTRA_SHIPMENT_CART_PASS_DATA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_shipment_cart;
    }

    @Override
    protected void initView(View view) {

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
    public void renderShipmentCart(@NonNull ShipmentCartData data) {

    }

    @Override
    public void renderSpinnerShipment() {

    }

    @Override
    public void renderCostShipment() {

    }

    @Override
    public void renderErrorCalculateShipment(String error) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismisLoading() {

    }

    @Override
    public void renderResultChangeAddress(@NonNull Bundle bundle) {

    }

    @Override
    public void showGeolocationMap() {

    }

    @Override
    public void navigateToChooseAddress() {
        Intent intent = new Intent(getActivity(), ChooseAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ChooseAddressActivity.REQUEST_CODE_PARAM_ADDRESS, shipmentCart.getAddressId());
        intent.putExtras(bundle);
        startActivityForResult(intent, ShipmentCartFragment.CHOOSE_ADDRESS);
    }

    @Override
    public void navigateToAddAddress() {
        Intent intent = new Intent(getActivity(), AddAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("page", 1);
        bundle.putBoolean(ManageAddressConstant.IS_EDIT, false);
        intent.putExtras(bundle);
        startActivityForResult(intent, ManageAddressConstant.REQUEST_CODE_PARAM_CREATE);
    }
}

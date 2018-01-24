package com.tokopedia.transaction.checkout.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.view.view.IShipmentDetailView;

import butterknife.BindView;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public class ShipmentDetailFragment extends BasePresenterFragment implements IShipmentDetailView {

    @BindView(R2.id.sp_shipment_choice)
    Spinner spShipmentChoice;
    @BindView(R2.id.rv_courier_choice)
    RecyclerView rvCourierChoice;
    @BindView(R2.id.tv_shipment_information)
    TextView tvShipmentInformation;
    @BindView(R2.id.map_view_pinpoint)
    MapView mapViewPinpoint;
    @BindView(R2.id.bt_change_pinpoint)
    Button btChangePinpoint;
    @BindView(R2.id.tv_no_ponpoint_information)
    TextView tvNoPonpointInformation;
    @BindView(R2.id.bt_choose_pinpoint)
    Button btChoosePinpoint;
    @BindView(R2.id.fl_pinpoint_map)
    FrameLayout flPinpointMap;
    @BindView(R2.id.tv_shipment_address)
    TextView tvShipmentAddress;
    @BindView(R2.id.tv_label_insurance)
    TextView tvLabelInsurance;
    @BindView(R2.id.img_bt_insurance_info)
    ImageButton imgBtInsuranceInfo;
    @BindView(R2.id.switch_insurance)
    Switch switchInsurance;
    @BindView(R2.id.tv_special_insurance_condition)
    TextView tvSpecialInsuranceCondition;
    @BindView(R2.id.ll_insurance)
    LinearLayout llInsurance;
    @BindView(R2.id.separator_insurance)
    View separatorInsurance;
    @BindView(R2.id.img_bt_partly_accept)
    ImageButton imgBtPartlyAccept;
    @BindView(R2.id.switch_partly_accept)
    Switch switchPartlyAccept;
    @BindView(R2.id.ll_partial_order)
    LinearLayout llPartialOrder;
    @BindView(R2.id.separator_partial_order)
    View separatorPartialOrder;
    @BindView(R2.id.img_bt_dropshipper_info)
    ImageButton imgBtDropshipperInfo;
    @BindView(R2.id.switch_dropshipper)
    Switch switchDropshipper;
    @BindView(R2.id.ll_dropshipper)
    LinearLayout llDropshipper;
    @BindView(R2.id.et_shipper_name)
    EditText etShipperName;
    @BindView(R2.id.text_input_layout_shipper_name)
    TextInputLayout textInputLayoutShipperName;
    @BindView(R2.id.et_shipper_phone)
    EditText etShipperPhone;
    @BindView(R2.id.text_input_layout_shipper_phone)
    TextInputLayout textInputLayoutShipperPhone;
    @BindView(R2.id.ll_dropshipper_info)
    LinearLayout llDropshipperInfo;
    @BindView(R2.id.tv_total_shipping_fee)
    TextView tvTotalShippingFee;
    @BindView(R2.id.bt_save)
    Button btSave;

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

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_shipment_detail;
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
}

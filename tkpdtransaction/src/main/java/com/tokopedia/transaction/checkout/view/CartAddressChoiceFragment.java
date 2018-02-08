package com.tokopedia.transaction.checkout.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.manage.people.address.activity.AddAddressActivity;
import com.tokopedia.core.manage.people.address.model.AddressModel;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.view.data.ShipmentRecipientModel;
import com.tokopedia.transaction.checkout.view.presenter.CartAddressChoicePresenter;
import com.tokopedia.transaction.checkout.view.presenter.ICartAddressChoicePresenter;
import com.tokopedia.transaction.checkout.view.view.ICartAddressChoiceView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tokopedia.transaction.checkout.view.activity.CartAddressChoiceActivity.INTENT_EXTRA_RECIPIENT;

/**
 * Created by Irfan Khoirul on 05/02/18.
 */

public class CartAddressChoiceFragment extends BasePresenterFragment<ICartAddressChoicePresenter>
        implements ICartAddressChoiceView {

    private static final String ARG_SHIPMENT_RECIPIENT = "recipient";

    @BindView(R2.id.rb_check_address)
    RadioButton rbCheckAddress;
    @BindView(R2.id.ll_address_radio_button_container)
    LinearLayout llAddressRadioButtonContainer;
    @BindView(R2.id.tv_text_address_description)
    TextView tvTextAddressDescription;
    @BindView(R2.id.tv_recipient_name)
    TextView tvRecipientName;
    @BindView(R2.id.tv_recipient_address)
    TextView tvRecipientAddress;
    @BindView(R2.id.rl_recipient_address)
    RelativeLayout rlRecipientAddress;
    @BindView(R2.id.rl_shipment_recipient_address_header)
    RelativeLayout rlShipmentRecipientAddressHeader;
    @BindView(R2.id.tv_change_address)
    TextView tvChangeAddress;
    @BindView(R2.id.tv_choose_other_address)
    TextView tvChooseOtherAddress;
    @BindView(R2.id.ll_add_new_address)
    LinearLayout llAddNewAddress;
    @BindView(R2.id.ll_send_to_multiple_address)
    LinearLayout llSendToMultipleAddress;
    @BindView(R2.id.bt_send_to_current_address)
    Button btSendToCurrentAddress;
    @BindView(R2.id.tv_phone_number)
    TextView tvPhoneNumber;

    public static CartAddressChoiceFragment newInstance(ShipmentRecipientModel shipmentRecipientModel) {
        CartAddressChoiceFragment fragment = new CartAddressChoiceFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_SHIPMENT_RECIPIENT, shipmentRecipientModel);
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
        presenter = new CartAddressChoicePresenter();
        presenter.attachView(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_cart_address_choice;
    }

    @Override
    protected void initView(View view) {
        rbCheckAddress.setVisibility(View.GONE);
        presenter.setRecipientAddress(
                (ShipmentRecipientModel) getArguments().getParcelable(ARG_SHIPMENT_RECIPIENT)
        );
    }

    @Override
    public void renderRecipientData(ShipmentRecipientModel shipmentRecipientModel) {
        tvTextAddressDescription.setText(shipmentRecipientModel.getRecipientAddressDescription());
        tvRecipientName.setText(shipmentRecipientModel.getRecipientName());
        tvRecipientAddress.setText(shipmentRecipientModel.getRecipientAddress());
        tvPhoneNumber.setText(shipmentRecipientModel.getRecipientPhoneNumber());
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

    @OnClick(R2.id.tv_choose_other_address)
    void onChooseOtherAddressClick() {
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        Fragment fragment = ShipmentAddressListFragment.newInstance();

        String backStateName = fragment.getClass().getName();

        boolean isFragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
        if (!isFragmentPopped) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(backStateName)
                    .commit();
        }
    }

    @OnClick(R2.id.tv_change_address)
    void onChangeAddressClick() {
        AddressModel data = new AddressModel();
//        data.setAddressId();
//        data.setAddressName();
//        data.setAddressStatus();
//        data.setAddressStreet();
//        data.setCityId();
//        data.setCityName();
//        data.setDistrictId();
//        data.setDistrictName();
        startActivityForResult(AddAddressActivity.createInstance(getActivity(), data), ManageAddressConstant.REQUEST_CODE_PARAM_EDIT);
    }

    @OnClick(R2.id.ll_add_new_address)
    void onAddNewAddress() {
        startActivityForResult(AddAddressActivity.createInstance(getActivity()), ManageAddressConstant.REQUEST_CODE_PARAM_CREATE);
    }

    @OnClick(R2.id.ll_send_to_multiple_address)
    void onSendToMultipleAddress() {
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        Fragment fragment = MultipleAddressFragment.newInstance();

        String backStateName = fragment.getClass().getName();

        boolean isFragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
        if (!isFragmentPopped) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(backStateName)
                    .commit();
        }
    }

    @OnClick(R2.id.bt_send_to_current_address)
    void onSendToCurrentAddress() {
        Intent intent = new Intent();
        intent.putExtra(INTENT_EXTRA_RECIPIENT, presenter.getRecipientAddress());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
}

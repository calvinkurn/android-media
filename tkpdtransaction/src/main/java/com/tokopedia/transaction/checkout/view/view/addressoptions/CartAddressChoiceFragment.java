package com.tokopedia.transaction.checkout.view.view.addressoptions;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.manage.people.address.activity.AddAddressActivity;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.data.mapper.AddressModelMapper;
import com.tokopedia.transaction.checkout.view.adapter.ShipmentAddressListAdapter;
import com.tokopedia.transaction.checkout.view.data.CartSellerItemModel;
import com.tokopedia.transaction.checkout.view.data.RecipientAddressModel;
import com.tokopedia.transaction.checkout.view.presenter.CartAddressChoicePresenter;
import com.tokopedia.transaction.checkout.view.presenter.ICartAddressChoicePresenter;
import com.tokopedia.transaction.checkout.view.view.ICartAddressChoiceView;
import com.tokopedia.transaction.checkout.view.view.multipleaddressform.MultipleAddressFormActivity;
import com.tokopedia.transaction.checkout.view.view.multipleaddressform.MultipleAddressFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Irfan Khoirul on 05/02/18.
 */

public class CartAddressChoiceFragment extends BasePresenterFragment<ICartAddressChoicePresenter>
        implements ICartAddressChoiceView, ShipmentAddressListAdapter.ActionListener,
        ShipmentAddressListFragment.FragmentListener {

    public static String INTENT_EXTRA_SELECTED_RECIPIENT_ADDRESS = "selectedAddress";

    @BindView(R2.id.tv_choose_other_address)
    TextView tvChooseOtherAddress;
    @BindView(R2.id.ll_add_new_address)
    LinearLayout llAddNewAddress;
    @BindView(R2.id.ll_send_to_multiple_address)
    LinearLayout llSendToMultipleAddress;
    @BindView(R2.id.bt_send_to_current_address)
    Button btSendToCurrentAddress;
    @BindView(R2.id.rv_address)
    RecyclerView rvAddress;

    private ShipmentAddressListAdapter recipientAdapter;

    public static CartAddressChoiceFragment newInstance() {
        return new CartAddressChoiceFragment();
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

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_cart_address_choice;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);
        presenter = new CartAddressChoicePresenter();
        setupRecyclerView();
        presenter.attachView(this);
        presenter.loadAddresses();
        setShowCase();
    }

    @Override
    public void renderRecipientData() {
        setupRecyclerView();
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

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        recipientAdapter = new ShipmentAddressListAdapter(this);
        recipientAdapter.setAddressList(presenter.getRecipientAddresses());
        rvAddress.setLayoutManager(linearLayoutManager);
        rvAddress.setAdapter(recipientAdapter);
    }

    @OnClick(R2.id.tv_choose_other_address)
    void onChooseOtherAddressClick() {
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        ShipmentAddressListFragment fragment = ShipmentAddressListFragment.newInstance();
        fragment.setFragmentListener(this);

        String backStateName = fragment.getClass().getName();

        boolean isFragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
        if (!isFragmentPopped) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(backStateName)
                    .commit();
        }
    }

    @OnClick(R2.id.ll_add_new_address)
    void onAddNewAddress() {
        startActivityForResult(AddAddressActivity.createInstance(getActivity()),
                ManageAddressConstant.REQUEST_CODE_PARAM_CREATE);
    }

    @OnClick(R2.id.bt_send_to_current_address)
    void onSendToCurrentAddress() {
        Intent intent = new Intent();
        intent.putExtra(INTENT_EXTRA_SELECTED_RECIPIENT_ADDRESS, presenter.getSelectedRecipientAddress());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onAddressContainerClicked(RecipientAddressModel model) {
        presenter.setSelectedRecipientAddress(model);
    }

    @Override
    public void onEditClick(RecipientAddressModel model) {
        AddressModelMapper mapper = new AddressModelMapper();

        startActivityForResult(AddAddressActivity.createInstance(getActivity(),
                mapper.transform(model)), ManageAddressConstant.REQUEST_CODE_PARAM_EDIT);
    }

    @Override
    public void onAddressClick(RecipientAddressModel mode) {
        // Update View
        Log.e("Update View", "Recipient Address Model");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ManageAddressConstant.REQUEST_CODE_PARAM_CREATE:
                    // Reload list address
                    break;
                case ManageAddressConstant.REQUEST_CODE_PARAM_EDIT:
                    // Reload list address
                    break;
            }
        }
    }


    private void setShowCase() {
        ShowCaseObject showCase = new ShowCaseObject(
                tvChooseOtherAddress, "Kirim Barang Sama ke Bebeberapa\n" +
                "Alamat.", "Klik tombol untuk mengirim barang yang sama ke beda alamat.",
                ShowCaseContentPosition.UNDEFINED);

        ArrayList<ShowCaseObject> showCaseObjectList = new ArrayList<>();

        showCaseObjectList.add(showCase);

        ShowCaseDialog showCaseDialog = createShowCaseDialog();

        if(!ShowCasePreference.hasShown(getActivity(), CartAddressChoiceFragment.class.getName()))
            showCaseDialog.show(
                    getActivity(),
                    CartAddressChoiceFragment.class.getName(),
                    showCaseObjectList
            );
    }

    private ShowCaseDialog createShowCaseDialog() {
        return new ShowCaseBuilder()
                .customView(R.layout.show_case_checkout)
                .titleTextColorRes(R.color.white)
                .spacingRes(R.dimen.spacing_show_case)
                .arrowWidth(R.dimen.arrow_width_show_case)
                .textColorRes(R.color.grey_400)
                .shadowColorRes(R.color.shadow)
                .backgroundContentColorRes(R.color.black)
                .circleIndicatorBackgroundDrawableRes(R.drawable.selector_circle_green)
                .textSizeRes(R.dimen.fontvs)
                .finishStringRes(R.string.show_case_finish)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .build();
    }
}

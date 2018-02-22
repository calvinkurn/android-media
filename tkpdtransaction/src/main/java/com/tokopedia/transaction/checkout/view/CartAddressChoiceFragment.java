package com.tokopedia.transaction.checkout.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.manage.people.address.activity.AddAddressActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.view.adapter.ShipmentAddressListAdapter;
import com.tokopedia.transaction.checkout.view.data.CartSellerItemModel;
import com.tokopedia.transaction.checkout.view.data.ShipmentRecipientModel;
import com.tokopedia.transaction.checkout.view.presenter.CartAddressChoicePresenter;
import com.tokopedia.transaction.checkout.view.presenter.ICartAddressChoicePresenter;
import com.tokopedia.transaction.checkout.view.view.ICartAddressChoiceView;
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
        implements ICartAddressChoiceView, ShipmentAddressListAdapter.ActionListener {

    public static String INTENT_EXTRA_SELECTED_RECIPIENT_ADDRESS = "selectedAddress";
    private static String CART_ITEM_LIST_EXTRA = "CART_ITEM_LIST_EXTRA";

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

    public static CartAddressChoiceFragment newInstance(
            List<CartSellerItemModel> cartSellerItemModelList
    ) {
        CartAddressChoiceFragment fragment = new CartAddressChoiceFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(
                CART_ITEM_LIST_EXTRA,
                (ArrayList<? extends Parcelable>) cartSellerItemModelList
        );
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

    @OnClick(R2.id.ll_add_new_address)
    void onAddNewAddress() {
        startActivityForResult(AddAddressActivity.createInstance(getActivity()), ManageAddressConstant.REQUEST_CODE_PARAM_CREATE);
    }

    @OnClick(R2.id.ll_send_to_multiple_address)
    void onSendToMultipleAddress() {
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        List<CartSellerItemModel> cartSellerItemModels = getArguments()
                .getParcelableArrayList(CART_ITEM_LIST_EXTRA);
        Fragment fragment = MultipleAddressFragment.newInstance(
                cartSellerItemModels,
                presenter.getSelectedRecipientAddress()
        );

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
        intent.putExtra(INTENT_EXTRA_SELECTED_RECIPIENT_ADDRESS, presenter.getSelectedRecipientAddress());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onAddressContainerClicked(ShipmentRecipientModel model) {
        presenter.setSelectedRecipientAddress(model);
    }

    @Override
    public void onEditClick(ShipmentRecipientModel model) {
        startActivityForResult(AddAddressActivity.createInstance(getActivity(),
                model.convertToAddressModel()), ManageAddressConstant.REQUEST_CODE_PARAM_EDIT);
    }
}

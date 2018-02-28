package com.tokopedia.transaction.checkout.view.view.addressoptions;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
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
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.transaction.checkout.view.adapter.ShipmentAddressListAdapter;
import com.tokopedia.transaction.checkout.view.di.component.CartAddressChoiceComponent;
import com.tokopedia.transaction.checkout.view.di.component.DaggerCartAddressChoiceComponent;
import com.tokopedia.transaction.checkout.view.di.module.CartAddressChoiceModule;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tokopedia.transaction.checkout.view.view.addressoptions.CartAddressChoiceActivity.EXTRA_DEFAULT_SELECTED_ADDRESS;
import static com.tokopedia.transaction.checkout.view.view.addressoptions.CartAddressChoiceActivity.EXTRA_SELECTED_ADDRESS_DATA;
import static com.tokopedia.transaction.checkout.view.view.addressoptions.CartAddressChoiceActivity.RESULT_CODE_ACTION_SELECT_ADDRESS;

/**
 * @author Irfan Khoirul on 05/02/18
 *         Aghny A. Putra on 27/02/18
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
    @BindView(R2.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R2.id.ll_network_error_view)
    LinearLayout llNetworkErrorView;
    @BindView(R2.id.ll_content)
    LinearLayout llContent;

    private ICartAddressChoiceActivityListener cartAddressChoiceListener;

    @Inject
    CartAddressChoicePresenter mCartAddressChoicePresenter;

    @Inject
    ShipmentAddressListAdapter mShipmentAddressListAdapter;

    public static CartAddressChoiceFragment newInstance() {
        return new CartAddressChoiceFragment();
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        CartAddressChoiceComponent component = DaggerCartAddressChoiceComponent.builder()
                .cartAddressChoiceModule(new CartAddressChoiceModule(this))
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
        RecipientAddressModel model = arguments.getParcelable(EXTRA_DEFAULT_SELECTED_ADDRESS);
        mCartAddressChoicePresenter.setSelectedRecipientAddress(model);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_cart_address_choice;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);
        setupRecyclerView();
        mCartAddressChoicePresenter.attachView(this);
        setShowCase();
    }

    @Override
    public void renderRecipientData(List<RecipientAddressModel> recipientAddressModels) {
        mShipmentAddressListAdapter.setAddressList(recipientAddressModels);
        mShipmentAddressListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {
        mCartAddressChoicePresenter.getAddressShortedList(getActivity());
    }

    @Override
    public void showLoading() {
        llContent.setVisibility(View.GONE);
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pbLoading.setVisibility(View.GONE);
        llContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoConnection(@NonNull String message) {
        llContent.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(getActivity(), llNetworkErrorView, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        mCartAddressChoicePresenter.getAddressShortedList(getActivity());
                    }
                });
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);

        rvAddress.setLayoutManager(linearLayoutManager);
        rvAddress.setAdapter(mShipmentAddressListAdapter);
    }

    @OnClick(R2.id.tv_choose_other_address)
    void onChooseOtherAddressClick() {
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        ShipmentAddressListFragment fragment = ShipmentAddressListFragment.newInstance();
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

    @OnClick(R2.id.ll_send_to_multiple_address)
    void onSendToMultipleAddress() {
        cartAddressChoiceListener.finishSendResultActionToMultipleAddressForm();
    }

    @OnClick(R2.id.bt_send_to_current_address)
    void onSendToCurrentAddress() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_ADDRESS_DATA,
                mCartAddressChoicePresenter.getSelectedRecipientAddress());
        getActivity().setResult(RESULT_CODE_ACTION_SELECT_ADDRESS, intent);
        getActivity().finish();
    }

    @Override
    public void onAddressContainerClicked(RecipientAddressModel model) {
        mCartAddressChoicePresenter.setSelectedRecipientAddress(model);
    }

    @Override
    public void onEditClick(RecipientAddressModel model) {
        AddressModelMapper mapper = new AddressModelMapper();

        startActivityForResult(AddAddressActivity.createInstance(getActivity(),
                mapper.transform(model)), ManageAddressConstant.REQUEST_CODE_PARAM_EDIT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ManageAddressConstant.REQUEST_CODE_PARAM_CREATE:
                    mCartAddressChoicePresenter.getAddressShortedList(getActivity());
                    break;
                case ManageAddressConstant.REQUEST_CODE_PARAM_EDIT:
                    mCartAddressChoicePresenter.getAddressShortedList(getActivity());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        cartAddressChoiceListener = (ICartAddressChoiceActivityListener) activity;
    }

    private void setShowCase() {
        ShowCaseObject showCase = new ShowCaseObject(
                tvChooseOtherAddress, "Kirim Barang Sama ke Bebeberapa\n" +
                "Alamat.", "Klik tombol untuk mengirim barang yang sama ke beda alamat.",
                ShowCaseContentPosition.UNDEFINED);

        ArrayList<ShowCaseObject> showCaseObjectList = new ArrayList<>();

        showCaseObjectList.add(showCase);

        ShowCaseDialog showCaseDialog = createShowCaseDialog();

        if (!ShowCasePreference.hasShown(getActivity(), CartAddressChoiceFragment.class.getName())) {
            showCaseDialog.show(
                    getActivity(),
                    CartAddressChoiceFragment.class.getName(),
                    showCaseObjectList
            );
        }
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

package com.tokopedia.transaction.checkout.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.manage.people.address.activity.AddAddressActivity;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.di.component.DaggerShipmentAddressListComponent;
import com.tokopedia.transaction.checkout.di.component.ShipmentAddressListComponent;
import com.tokopedia.transaction.checkout.di.module.ShipmentAddressListModule;
import com.tokopedia.transaction.checkout.view.adapter.ShipmentAddressListAdapter;
import com.tokopedia.transaction.checkout.view.data.ShipmentRecipientModel;
import com.tokopedia.transaction.checkout.view.presenter.ShipmentAddressListPresenter;
import com.tokopedia.transaction.checkout.view.view.ISearchAddressListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Aghny A. Putra on 25/01/18
 */
public class ShipmentAddressListFragment extends BasePresenterFragment
        implements ISearchAddressListView<List<ShipmentRecipientModel>>,
        SearchInputView.Listener,
        SearchInputView.ResetListener,
        FragmentManager.OnBackStackChangedListener, ShipmentAddressListAdapter.ActionListener {

    private static final String TAG = ShipmentAddressListFragment.class.getSimpleName();

    @BindView(R2.id.rv_address_list) RecyclerView mRvRecipientAddressList;
    @BindView(R2.id.sv_address_search_box) SearchInputView mSvAddressSearchBox;

    @Inject ShipmentAddressListAdapter mShipmentAddressListAdapter;
    @Inject ShipmentAddressListPresenter mShipmentAddressListPresenter;

    public static ShipmentAddressListFragment newInstance() {
        return new ShipmentAddressListFragment();
    }

    public static ShipmentAddressListFragment newInstance(Map<String, String> params) {
        ShipmentAddressListFragment fragment = new ShipmentAddressListFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        ShipmentAddressListComponent component = DaggerShipmentAddressListComponent.builder()
                .shipmentAddressListModule(new ShipmentAddressListModule(this))
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

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_shipment_address_list;
    }

    /**
     * initial view atau widget.. misalkan textView = (TextView) findById...
     *
     * @param view root view si fragment
     */
    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);

        mRvRecipientAddressList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvRecipientAddressList.setAdapter(mShipmentAddressListAdapter);

        mShipmentAddressListPresenter.attachView(this);

        initSearchView();
    }

    /**
     * set listener atau attribute si view. misalkan texView.setText("blablalba");
     */
    @Override
    protected void setViewListener() {
        mShipmentAddressListPresenter.getAddressList();
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

    @OnClick(R2.id.tv_add_new_address)
    protected void addNewAddress() {

    }

    @Override
    public void showList(List<ShipmentRecipientModel> shipmentRecipientModels) {
        mShipmentAddressListAdapter.setAddressList(shipmentRecipientModels);
        mShipmentAddressListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showListEmpty() {
        mShipmentAddressListAdapter.setAddressList(new ArrayList<ShipmentRecipientModel>());
        mShipmentAddressListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {

    }

    private void initSearchView() {
        mSvAddressSearchBox.getSearchTextView().setOnClickListener(onSearchViewClickListener());
        mSvAddressSearchBox.getSearchTextView().setOnTouchListener(onSearchViewTouchListener());

        mSvAddressSearchBox.setListener(this);
        mSvAddressSearchBox.setResetListener(this);
        mSvAddressSearchBox.setSearchHint("Cari Nama Penerima/Alamat/Kota");
    }

    private View.OnTouchListener onSearchViewTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mSvAddressSearchBox.getSearchTextView().setCursorVisible(true);
                openSoftKeyboard();
                return false;
            }
        };
    }

    private View.OnClickListener onSearchViewClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSvAddressSearchBox.getSearchTextView().setCursorVisible(true);
                openSoftKeyboard();
            }
        };
    }

    @Override
    public void onSearchSubmitted(String text) {
        performSearch(text);
        closeSoftKeyboard();
    }

    @Override
    public void onSearchTextChanged(String text) {
        openSoftKeyboard();
        performSearch(text);
    }

    @Override
    public void onSearchReset() {
        mShipmentAddressListPresenter.resetSearch();
    }

    @Override
    public void onBackStackChanged() {

    }

    private void performSearch(String keyword) {
        if (!keyword.isEmpty()) {
            mShipmentAddressListPresenter.initSearch(keyword);
        } else {
            onSearchReset();
        }
    }

    private void openSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(mSvAddressSearchBox.getSearchTextView(), InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void closeSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mSvAddressSearchBox.getSearchTextView().getWindowToken(), 0);
        }
    }

    @Override
    public void onAddressContainerClicked(ShipmentRecipientModel model) {

    }

    @Override
    public void onEditClick(ShipmentRecipientModel model) {
        startActivityForResult(AddAddressActivity.createInstance(getActivity(),
                model.convertToAddressModel()), ManageAddressConstant.REQUEST_CODE_PARAM_EDIT);

    }
}

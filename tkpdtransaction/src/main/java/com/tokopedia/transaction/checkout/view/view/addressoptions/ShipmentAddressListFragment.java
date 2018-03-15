package com.tokopedia.transaction.checkout.view.view.addressoptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.manage.people.address.activity.AddAddressActivity;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.data.mapper.AddressModelMapper;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.transaction.checkout.view.adapter.ShipmentAddressListAdapter;
import com.tokopedia.transaction.checkout.view.di.component.DaggerShipmentAddressListComponent;
import com.tokopedia.transaction.checkout.view.di.component.ShipmentAddressListComponent;
import com.tokopedia.transaction.checkout.view.di.module.ShipmentAddressListModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Aghny A. Putra on 25/01/18
 */

public class ShipmentAddressListFragment extends BasePresenterFragment implements
        ISearchAddressListView<List<RecipientAddressModel>>,
        SearchInputView.Listener,
        SearchInputView.ResetListener,
        ShipmentAddressListAdapter.ActionListener {

    private static final String TAG = ShipmentAddressListFragment.class.getSimpleName();

    private static final int ORDER_ASC = 1;
    private static final String PARAMS = "params";

    RecyclerView mRvRecipientAddressList;
    SearchInputView mSvAddressSearchBox;
    TextView mTvAddNewAddress;
    SwipeToRefresh swipeToRefreshLayout;
    LinearLayout llNetworkErrorView;
    RelativeLayout rlContent;

    InputMethodManager mInputMethodManager;
    ICartAddressChoiceActivityListener mCartAddressChoiceActivityListener;

    @Inject
    ShipmentAddressListAdapter mShipmentAddressListAdapter;

    @Inject
    ShipmentAddressListPresenter mShipmentAddressListPresenter;

    public static ShipmentAddressListFragment newInstance() {
        return new ShipmentAddressListFragment();
    }

    public static ShipmentAddressListFragment newInstance(HashMap<String, String> params) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAMS, params);

        ShipmentAddressListFragment fragment = new ShipmentAddressListFragment();
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
        mCartAddressChoiceActivityListener = (ICartAddressChoiceActivityListener) activity;
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
        mRvRecipientAddressList = view.findViewById(R.id.rv_address_list);
        mSvAddressSearchBox = view.findViewById(R.id.sv_address_search_box);
        mTvAddNewAddress = view.findViewById(R.id.tv_add_new_address);
        swipeToRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        llNetworkErrorView = view.findViewById(R.id.ll_network_error_view);
        rlContent = view.findViewById(R.id.rl_content);
        swipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String keyword = mSvAddressSearchBox.getSearchText();
                performSearch(!TextUtils.isEmpty(keyword) ? keyword : "");
            }
        });
    }

    /**
     * set listener atau attribute si view. misalkan texView.setText("blablalba");
     */
    @Override
    protected void setViewListener() {
        mShipmentAddressListPresenter.attachView(this);
        mInputMethodManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**
     * initial Variabel di fragment, selain yg sifatnya widget. Misal: variable state, handler dll
     */
    @Override
    protected void initialVar() {
        mRvRecipientAddressList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvRecipientAddressList.setAdapter(mShipmentAddressListAdapter);
    }

    /**
     * setup aksi, attr, atau listener untuk si variable. misal. appHandler.startAction();
     */
    @Override
    protected void setActionVar() {
        initSearchView();
        onSearchReset();
    }

    @Override
    public void showList(List<RecipientAddressModel> recipientAddressModels) {
        mShipmentAddressListAdapter.setAddressList(recipientAddressModels);
        mShipmentAddressListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showListEmpty() {
        mShipmentAddressListAdapter.setAddressList(new ArrayList<RecipientAddressModel>());
        mShipmentAddressListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        rlContent.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.VISIBLE);
        swipeToRefreshLayout.setEnabled(true);
        NetworkErrorHelper.showEmptyState(getActivity(), llNetworkErrorView, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        String keyword = mSvAddressSearchBox.getSearchText();
                        performSearch(!TextUtils.isEmpty(keyword) ? keyword : "");
                    }
                });
    }

    @Override
    public void showLoading() {
        rlContent.setVisibility(View.GONE);
        swipeToRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        rlContent.setVisibility(View.VISIBLE);
        llNetworkErrorView.setVisibility(View.GONE);
        swipeToRefreshLayout.setRefreshing(false);
        swipeToRefreshLayout.setEnabled(false);
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
//        performSearch(text);
    }

    @Override
    public void onSearchReset() {
        mShipmentAddressListPresenter.resetAddressList(getActivity(), ORDER_ASC);
        closeSoftKeyboard();
    }

    private void performSearch(String query) {
        if (!query.isEmpty()) {
            mShipmentAddressListPresenter.getAddressList(getActivity(), ORDER_ASC, query);
        } else {
            onSearchReset();
        }
    }

    private void openSoftKeyboard() {
        if (mInputMethodManager != null) {
            mInputMethodManager.showSoftInput(
                    mSvAddressSearchBox.getSearchTextView(), InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void closeSoftKeyboard() {
        if (mInputMethodManager != null) {
            mInputMethodManager.hideSoftInputFromWindow(
                    mSvAddressSearchBox.getSearchTextView().getWindowToken(), 0);
        }
    }

    @Override
    public void onAddressContainerClicked(RecipientAddressModel model) {
        if (mCartAddressChoiceActivityListener != null) {
            mCartAddressChoiceActivityListener.finishSendResultActionSelectedAddress(model);
        }
    }

    @Override
    public void onEditClick(RecipientAddressModel model) {
        AddressModelMapper mapper = new AddressModelMapper();

        Intent intent = AddAddressActivity.createInstance(getActivity(), mapper.transform(model));
        startActivityForResult(intent, ManageAddressConstant.REQUEST_CODE_PARAM_EDIT);
    }

}
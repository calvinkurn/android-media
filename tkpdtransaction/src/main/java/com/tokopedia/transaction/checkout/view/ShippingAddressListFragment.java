package com.tokopedia.transaction.checkout.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.di.component.CartAddressListComponent;
import com.tokopedia.transaction.checkout.di.component.DaggerCartAddressListComponent;
import com.tokopedia.transaction.checkout.di.module.CartAddressListModule;
import com.tokopedia.transaction.checkout.view.adapter.CartAddressListAdapter;
import com.tokopedia.transaction.checkout.view.data.ShippingRecipientModel;
import com.tokopedia.transaction.checkout.view.presenter.CartAddressListPresenter;
import com.tokopedia.transaction.checkout.view.view.ISearchAddressListView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Aghny A. Putra on 25/01/18
 */
public class ShippingAddressListFragment extends BasePresenterFragment implements ISearchAddressListView<List<ShippingRecipientModel>> {

    private static final String SCREEN_NAME = ShippingAddressListFragment.class.getSimpleName();

    @BindView(R2.id.rv_address_list)
    RecyclerView mRvRecipientAddressList;

    @Inject
    CartAddressListAdapter mCartAddressListAdapter;
    @Inject
    CartAddressListPresenter mCartAddressListPresenter;

    public static ShippingAddressListFragment newInstance() {
        return new ShippingAddressListFragment();
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        CartAddressListComponent component = DaggerCartAddressListComponent.builder()
                .cartAddressListModule(new CartAddressListModule())
                .build();
        component.inject(this);
    }

    @Override
    protected String getScreenName() {
        return SCREEN_NAME;
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
        mRvRecipientAddressList.setAdapter(mCartAddressListAdapter);

        mCartAddressListPresenter.attachView(this);
        mCartAddressListPresenter.getAddressList();
    }

    /**
     * set listener atau attribute si view. misalkan texView.setText("blablalba");
     */
    @Override
    protected void setViewListener() {

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
    public void showList(List<ShippingRecipientModel> shippingRecipientModels) {
        mCartAddressListAdapter.setAddressList(shippingRecipientModels);
        mCartAddressListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showListEmpty() {

    }

    @Override
    public void showError() {

    }

}

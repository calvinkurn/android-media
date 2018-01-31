package com.tokopedia.transaction.checkout.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.view.adapter.CartSingleAddressAdapter;
import com.tokopedia.transaction.checkout.view.data.factory.CartSingleAddressDataFactory;
import com.tokopedia.transaction.checkout.view.presenter.CartSingleAddressPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Aghny A. Putra on 24/1/18
 */
public class CartSingleAddressFragment extends BasePresenterFragment {

    private static final String SCREEN_NAME = CartSingleAddressFragment.class.getSimpleName();

    @BindView(R2.id.rv_cart_order_details) RecyclerView mRvCartOrderDetails;

    CartSingleAddressAdapter mCartSingleAddressAdapter;
    CartSingleAddressPresenter mCartSingleAddressPresenter;

    public static CartSingleAddressFragment newInstance() {
        return new CartSingleAddressFragment();
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
        return R.layout.fragment_single_address_shipment;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);

        mCartSingleAddressAdapter = new CartSingleAddressAdapter();

        mRvCartOrderDetails.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvCartOrderDetails.setAdapter(mCartSingleAddressAdapter);

        mCartSingleAddressAdapter.updateData(CartSingleAddressDataFactory.getDummyCartSingleAddressData());
        mCartSingleAddressAdapter.notifyDataSetChanged();
    }

    /**
     * set listener atau attribute si view. misalkan texView.setText("blablalba");
     */
    @Override
    protected void setViewListener() {}

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

}

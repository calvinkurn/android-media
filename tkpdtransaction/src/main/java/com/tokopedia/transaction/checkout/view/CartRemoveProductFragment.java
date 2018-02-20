package com.tokopedia.transaction.checkout.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.di.component.CartRemoveProductComponent;
import com.tokopedia.transaction.checkout.di.component.DaggerCartRemoveProductComponent;
import com.tokopedia.transaction.checkout.di.module.CartRemoveProductModule;
import com.tokopedia.transaction.checkout.view.adapter.CartRemoveProductAdapter;
import com.tokopedia.transaction.checkout.view.data.CartItemData;
import com.tokopedia.transaction.checkout.view.data.CheckedCartItemData;
import com.tokopedia.transaction.checkout.view.presenter.CartRemoveProductPresenter;
import com.tokopedia.transaction.checkout.view.view.IRemoveProductListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tokopedia.transaction.checkout.view.SingleAddressShipmentFragment.ARG_EXTRA_CART_DATA_LIST;

/**
 * @author Aghny A. Putra on 05/02/18
 */
public class CartRemoveProductFragment extends BasePresenterFragment
        implements IRemoveProductListView<List<CartItemData>>,
        CartRemoveProductAdapter.CartRemoveProductActionListener {

    private static final Locale LOCALE_ID = new Locale("in", "ID");
    private static final String TAG = CartRemoveProductFragment.class.getSimpleName();

    @BindView(R2.id.rv_cart_remove_product)
    RecyclerView mRvCartRemoveProduct;
    @BindView(R2.id.tv_remove_product)
    TextView mTvRemoveProduct;

    @Inject
    CartRemoveProductAdapter mCartRemoveProductAdapter;
    @Inject
    CartRemoveProductPresenter mCartRemoveProductPresenter;

    private int mCheckedCartItem = 0;

    private List<CartItemData> mCartItemDataList = new ArrayList<>();
    private List<CheckedCartItemData> mCheckedCartItemList = new ArrayList<>();

    public static CartRemoveProductFragment newInstance(List<CartItemData> cartItemDataList) {
        CartRemoveProductFragment fragment = new CartRemoveProductFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARG_EXTRA_CART_DATA_LIST,
                (ArrayList<? extends Parcelable>) cartItemDataList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        CartRemoveProductComponent component = DaggerCartRemoveProductComponent.builder()
                .cartRemoveProductModule(new CartRemoveProductModule(this))
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
        mCartItemDataList = arguments.getParcelableArrayList(ARG_EXTRA_CART_DATA_LIST);
        if (mCartItemDataList != null) {
            for (CartItemData cartItemData : mCartItemDataList) {
                mCheckedCartItemList.add(new CheckedCartItemData(false, cartItemData));
            }
        }
    }

    /**
     * Layout xml untuk si fragment
     *
     * @return layout id
     */
    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_cart_remove_product;
    }

    /**
     * initial view atau widget.. misalkan textView = (TextView) findById...
     *
     * @param view root view si fragment
     */
    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);

        mRvCartRemoveProduct.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvCartRemoveProduct.setAdapter(mCartRemoveProductAdapter);

        mCartRemoveProductPresenter.attachView(this);
    }

    /**
     * set listener atau attribute si view. misalkan texView.setText("blablalba");
     */
    @Override
    protected void setViewListener() {
        mCartRemoveProductPresenter.getCartItems(mCartItemDataList);
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
    public void showList(List<CartItemData> cartItemDataList) {
        mCartRemoveProductAdapter.updateData(cartItemDataList);
        mCartRemoveProductAdapter.notifyDataSetChanged();
    }

    @Override
    public void showListEmpty() {

    }

    @Override
    public void showError() {

    }

    @OnClick(R2.id.tv_remove_product)
    public void removeCheckedProducts() {
        for (CheckedCartItemData checkedCartItemData : mCheckedCartItemList) {
            if (checkedCartItemData.isChecked()) {
                mCartItemDataList.remove(checkedCartItemData.getCartItemData());
            }
    @Override
    public List<CartItemData> getSelectedCartList() {
        List<CartItemData> cartItemDataList = new ArrayList<>();
        for (Integer index : mSetCheckedCartItemIndex) {
            cartItemDataList.add(mCartItemDataList.get(index));
        }
        return cartItemDataList;
    }

    @Override
    public TKPDMapParam<String, String> getGenerateParamAuth(TKPDMapParam<String, String> param) {
        return param == null ? com.tokopedia.core.network.retrofit.utils.AuthUtil.generateParamsNetwork(getActivity())
                : com.tokopedia.core.network.retrofit.utils.AuthUtil.generateParamsNetwork(getActivity(), param);
    }

    @Override
    public void renderSuccessDeleteCart(String message) {
        for (Integer index : mSetCheckedCartItemIndex) {
            mCartItemDataList.remove((int) index);
        }
    }

    @OnClick(R2.id.tv_remove_product)
    public void removeCheckedProducts() {
//        for (Integer index : mSetCheckedCartItemIndex) {
//            mCartItemDataList.remove((int) index);
//        }

        mCartRemoveProductPresenter.processDeleteCart(true);

        mCartRemoveProductAdapter.notifyDataSetChanged();
    }

    /**
     * Executed when state of checkbox is changed
     *
     * @param checked  state of checkbox
     * @param position index of list where the state of checkbox is changed
     */
    @Override
    public void onCheckBoxStateChangedListener(boolean checked, int position) {
        mCheckedCartItemList.get(position).setChecked(checked);
        mCheckedCartItem = checked ? mCheckedCartItem + 1 : mCheckedCartItem - 1;

        String btnText = mCheckedCartItem == 0 ? "Hapus" :
                String.format(LOCALE_ID, "Hapus (%d)", mCheckedCartItem);

        mTvRemoveProduct.setText(btnText);
    }

}

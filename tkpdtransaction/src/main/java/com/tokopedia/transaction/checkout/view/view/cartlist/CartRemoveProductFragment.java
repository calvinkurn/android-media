package com.tokopedia.transaction.checkout.view.view.cartlist;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CheckedCartItemData;
import com.tokopedia.transaction.checkout.view.adapter.CartRemoveProductAdapter;
import com.tokopedia.transaction.checkout.view.di.component.CartRemoveProductComponent;
import com.tokopedia.transaction.checkout.view.di.component.DaggerCartRemoveProductComponent;
import com.tokopedia.transaction.checkout.view.di.module.CartRemoveProductModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author Aghny A. Putra on 05/02/18
 */
public class CartRemoveProductFragment extends BasePresenterFragment
        implements IRemoveProductListView<List<CartItemData>>,
        CartRemoveProductAdapter.CartRemoveProductActionListener {

    private static final Locale LOCALE_ID = new Locale("in", "ID");
    private static final String TAG = CartRemoveProductFragment.class.getSimpleName();
    private static final String ARG_EXTRA_CART_DATA_LIST = "ARG_EXTRA_CART_DATA_LIST";

    @BindView(R2.id.rv_cart_remove_product)
    RecyclerView mRvCartRemoveProduct;
    @BindView(R2.id.tv_remove_product)
    TextView mTvRemoveProduct;

    @Inject
    CartRemoveProductAdapter mCartRemoveProductAdapter;
    @Inject
    CartRemoveProductPresenter mCartRemoveProductPresenter;
    @Inject
    RecyclerView.ItemDecoration itemDecoration;

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
        mRvCartRemoveProduct.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvCartRemoveProduct.setAdapter(mCartRemoveProductAdapter);
        mRvCartRemoveProduct.addItemDecoration(itemDecoration);
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
        setHasOptionsMenu(true);
        getActivity().setTitle("Hapus");
        getActivity().invalidateOptionsMenu();
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
        List<CartItemData> selectedCartList = new ArrayList<>();
        List<CartItemData> unselectedCartList = new ArrayList<>();

        for (CheckedCartItemData checkedCartItemData : mCheckedCartItemList) {
            if (checkedCartItemData.isChecked()) {
                selectedCartList.add(checkedCartItemData.getCartItemData());
            } else {
                unselectedCartList.add(checkedCartItemData.getCartItemData());
            }
        }

        showDeleteCartItemDialog(selectedCartList, unselectedCartList);
    }

    @Override
    public TKPDMapParam<String, String> getGenerateParamAuth(TKPDMapParam<String, String> param) {
        return param == null ? com.tokopedia.core.network.retrofit.utils.AuthUtil.generateParamsNetwork(getActivity())
                : com.tokopedia.core.network.retrofit.utils.AuthUtil.generateParamsNetwork(getActivity(), param);
    }

    @Override
    public void renderSuccessDeletePartialCart(String message) {
        performDeleteCart(message);
        getActivity().onBackPressed();
    }

    @Override
    public void renderSuccessDeleteAllCart(String message) {
        performDeleteCart(message);
        getActivity().onBackPressed();
    }

    @Override
    public void renderOnFailureDeleteCart(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public List<CartItemData> getAllCartItemList() {
        return mCartItemDataList;
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


    private void showDeleteCartItemDialog(
            final List<CartItemData> removedCartItemList, List<CartItemData> updatedCartItemList
    ) {
        DialogFragment dialog = CartRemoveItemDialog.newInstance(
                removedCartItemList,
                updatedCartItemList,
                getCallbackActionDialogRemoveCart()
        );

        dialog.show(getFragmentManager(), "dialog");
    }

    @NonNull
    private CartRemoveItemDialog.CartItemRemoveCallbackAction getCallbackActionDialogRemoveCart() {
        return new CartRemoveItemDialog.CartItemRemoveCallbackAction() {
            @Override
            public void onDeleteSingleItemClicked(
                    CartItemData removedCartItem, List<CartItemData> updatedCartItem
            ) {
                List<CartItemData> cartItemDataList
                        = new ArrayList<>(Collections.singletonList(removedCartItem));
                mCartRemoveProductPresenter.processDeleteCart(
                        cartItemDataList, updatedCartItem, false
                );
            }

            @Override
            public void onDeleteSingleItemWithWishListClicked(
                    CartItemData removedCartItem, List<CartItemData> updatedCartItem
            ) {
                List<CartItemData> cartItemDataList
                        = new ArrayList<>(Collections.singletonList(removedCartItem));
                mCartRemoveProductPresenter.processDeleteCart(
                        cartItemDataList, updatedCartItem, true
                );
            }

            @Override
            public void onDeleteMultipleItemClicked(
                    List<CartItemData> removedCartItem, List<CartItemData> updatedCartItem
            ) {
                mCartRemoveProductPresenter.processDeleteCart(
                        removedCartItem, updatedCartItem, false
                );
            }

            @Override
            public void onDeleteMultipleItemWithWishListClicked(
                    List<CartItemData> removedCartItem, List<CartItemData> updatedCartItem
            ) {
                mCartRemoveProductPresenter.processDeleteCart(
                        removedCartItem, updatedCartItem, true
                );
            }
        };
    }

    private void performDeleteCart(String message) {
        for (CheckedCartItemData checkedCartItemData : mCheckedCartItemList) {
            if (checkedCartItemData.isChecked())
                mCartItemDataList.remove(checkedCartItemData.getCartItemData());
            mCartRemoveProductAdapter.notifyDataSetChanged();
        }
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }
}

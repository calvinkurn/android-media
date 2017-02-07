package com.tokopedia.seller.gmsubscribe.view.product.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmsubscribe.view.product.fragment.GMCurrentProductFragment;
import com.tokopedia.seller.gmsubscribe.view.product.fragment.GMExtendProductFragment;
import com.tokopedia.seller.gmsubscribe.view.product.fragment.GMProductFragment;
import com.tokopedia.seller.gmsubscribe.view.product.fragment.GMProductFragmentListener;

public class GMProductActivity extends BasePresenterActivity implements GMProductFragmentListener {

    public static final String PRODUCT_SELECTION_TYPE = "PRODUCT_SELECTION_TYPE";
    public static final int FIRST_SELECT_PRODUCT = 10;
    public static final int CHANGE_SELECT_PRODUCT = 11;
    public static final int FIRST_SELECT_AUTO_PRODUCT = 12;
    public static final int CHANGE_SELECT_AUTO_PRODUCT = 13;
    public static final String SELECT_PRODUCT_TAG = "SELECT_PRODUCT_TAG";
    public static final int UNSELECTED = -1;
    public static final String SELECTED_PRODUCT = "SELECTED_PRODUCT";

    private FragmentManager fragmentManager;
    private int selectionType;
    private int currentSelected;

    public static Intent selectProductFirstTime(Context context) {
        Intent intent = new Intent(context, GMProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(PRODUCT_SELECTION_TYPE, FIRST_SELECT_PRODUCT);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent changeProductSelected(Context context, Integer selectedProduct) {
        Intent intent = new Intent(context, GMProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(PRODUCT_SELECTION_TYPE, CHANGE_SELECT_PRODUCT);
        bundle.putInt(SELECTED_PRODUCT, selectedProduct);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent selectAutoProductFirstTime(Context context) {
        Intent intent = new Intent(context, GMProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(PRODUCT_SELECTION_TYPE, FIRST_SELECT_AUTO_PRODUCT);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent changeAutoProductSelected(Context context, Integer selectedAutoProduct) {
        Intent intent = new Intent(context, GMProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(PRODUCT_SELECTION_TYPE, CHANGE_SELECT_AUTO_PRODUCT);
        bundle.putInt(SELECTED_PRODUCT, selectedAutoProduct);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri uri) {

    }

    @Override
    protected void setupBundlePass(Bundle bundle) {
        selectionType = bundle.getInt(PRODUCT_SELECTION_TYPE);
        currentSelected = bundle.getInt(SELECTED_PRODUCT, UNSELECTED);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gmsubscribe;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {
        fragmentManager = getFragmentManager();
    }

    @Override
    protected void setActionVar() {
        switch (selectionType){
            case FIRST_SELECT_PRODUCT:
                selectCurrentSelectedFirstTime();
                break;
            case CHANGE_SELECT_PRODUCT:
                changeCurrentSelected(currentSelected);
                break;
            case FIRST_SELECT_AUTO_PRODUCT:
                selectAutoSubscribePackageFirstTime();
                break;
            case CHANGE_SELECT_AUTO_PRODUCT:
                changeAutoSubscribePackage(currentSelected);
                break;
        }

    }

    public void selectCurrentSelectedFirstTime() {
        inflateFragment(
                GMCurrentProductFragment.createFragment(
                        getString(R.string.gmsubscribe_product_button_subscribe),
                        GMProductFragment.UNDEFINED_DEFAULT_SELECTED,
                        FIRST_SELECT_PRODUCT),
                true,
                SELECT_PRODUCT_TAG);
    }

    public void changeCurrentSelected(Integer selectedProduct) {
        inflateFragment(
                GMCurrentProductFragment.createFragment(
                        getString(R.string.gmsubscribe_product_button_change),
                        selectedProduct,
                        CHANGE_SELECT_PRODUCT),
                false,
                SELECT_PRODUCT_TAG);
    }

    public void selectAutoSubscribePackageFirstTime() {
        inflateFragment(
                GMExtendProductFragment.createFragment(
                        getString(R.string.gmsubscribe_product_button_activate),
                        GMProductFragment.UNDEFINED_DEFAULT_SELECTED,
                        FIRST_SELECT_AUTO_PRODUCT),
                false,
                SELECT_PRODUCT_TAG);
    }

    public void changeAutoSubscribePackage(Integer autoExtendSelectedProduct) {
        inflateFragment(
                GMExtendProductFragment.createFragment(
                        getString(R.string.gmsubscribe_product_button_change),
                        autoExtendSelectedProduct,
                        CHANGE_SELECT_AUTO_PRODUCT),
                false,
                SELECT_PRODUCT_TAG);
    }

    private void inflateFragment(Fragment fragment, boolean isAddToBackStack, String tag) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.parent_view, fragment, tag);
        if(isAddToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public void changeActionBarTitle(String title) {
        toolbar.setTitleText(title);
    }

    @Override
    public void setDrawer(boolean isShown) {

    }

    @Override
    public void finishProductSelection(int productId, int returnType) {
        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(SELECTED_PRODUCT, productId);
        bundle.putInt(PRODUCT_SELECTION_TYPE, returnType);
        returnIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}

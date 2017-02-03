package com.tokopedia.seller.gmsubscribe.view.product.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.gmsubscribe.di.GMProductDependencyInjection;
import com.tokopedia.seller.gmsubscribe.view.product.presenter.GMProductPresenter;
import com.tokopedia.seller.gmsubscribe.view.product.presenter.GMProductPresenterImpl;
import com.tokopedia.seller.gmsubscribe.view.product.presenter.GMProductView;
import com.tokopedia.seller.gmsubscribe.view.product.recyclerview.GMProductAdapter;
import com.tokopedia.seller.gmsubscribe.view.product.recyclerview.GMProductAdapterCallback;
import com.tokopedia.seller.gmsubscribe.view.product.viewmodel.GMProductViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by sebastianuskh on 11/23/16.
 */

public abstract class GMProductFragment
        extends BasePresenterFragment<GMProductPresenterImpl>
        implements GMProductView, GMProductAdapterCallback {

    public static final String TAG = "GMProductFragment";
    private static final String STRING_BUTTON_SELECT = "STRING_BUTTON_SELECT";
    public static final String DEFAULT_SELECTED_PRODUCT = "DEFAULT_SELECTED_PRODUCT";
    public static final int UNDEFINED_DEFAULT_SELECTED = -1;
    public static final String RETURN_TYPE = "RETURN_TYPE";

    @BindView(R2.id.recyclerview_package_chooser)
    RecyclerView recyclerView;

    @BindView(R2.id.button_select_product)
    Button buttonSelectProduct;

    @OnClick(R2.id.button_select_product)
    void confirmSelection() {
        Log.d(TAG, "Selected data now is : " + currentSelectedProductId);
        listener.finishProductSelection(currentSelectedProductId, returnType);
    }

    private CompositeSubscription subscriber;
    private String stringButton;
    private int returnType;
    private Integer currentSelectedProductId;
    private RecyclerView.LayoutManager layoutManager;
    private GMProductAdapter adapter;
    private GMProductFragmentListener listener;
    private TkpdProgressDialog progressDialog;

    public static GMProductFragment createFragment(GMProductFragment fragment,
                                                   String buttonString,
                                                   int defaultSelected,
                                                   int returnType){
        Bundle args = new Bundle();
        args.putString(STRING_BUTTON_SELECT, buttonString);
        args.putInt(RETURN_TYPE, returnType);
        args.putInt(DEFAULT_SELECTED_PRODUCT, defaultSelected);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        listener.setDrawer(false);
        listener.changeActionBarTitle(getTitle());
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle bundle) {
        presenter.detachView();
    }

    @Override
    public void onRestoreState(Bundle bundle) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        subscriber = RxUtils.getNewCompositeSubIfUnsubscribed(subscriber);
        presenter = GMProductDependencyInjection.getPresenter();
    }

    @Override
    protected void initialListener(Activity activity) {
        if (activity instanceof GMProductFragmentListener) {
            listener = (GMProductFragmentListener) activity;
        } else {
            throw new RuntimeException("Please implement GMProductFragmentListener in the Activity");
        }
    }

    @Override
    protected void setupArguments(Bundle bundle) {
        stringButton = bundle.getString(STRING_BUTTON_SELECT);
        returnType = bundle.getInt(RETURN_TYPE);
        currentSelectedProductId = bundle.getInt(DEFAULT_SELECTED_PRODUCT, UNDEFINED_DEFAULT_SELECTED);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_gmsubscribe_product;
    }

    @Override
    protected void initView(View view) {
        presenter.attachView(this);
        buttonSelectProduct.setText(stringButton);
        adapter = new GMProductAdapter(this);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.MAIN_PROGRESS);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {
        getPackage();
    }

    @Override
    public void onPause() {
        super.onPause();
        RxUtils.unsubscribeIfNotNull(subscriber);
    }

    @Override
    public void renderProductList(List<GMProductViewModel> gmProductDomainModels) {
        Log.d(TAG, "data rendered");
        adapter.addItem(gmProductDomainModels);
    }

    @Override
    public void errorGetProductList() {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                getPackage();
            }
        });
    }

    @Override
    public void selectedProductId(Integer selectedProductId){
        currentSelectedProductId = selectedProductId;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.showDialog();
    }

    @Override
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public Integer getSelectedProductId(){
        return currentSelectedProductId;
    }

    protected abstract String getTitle();

    protected abstract void getPackage();

}

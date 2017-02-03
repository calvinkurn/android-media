package com.tokopedia.seller.gmsubscribe.view.checkout.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmsubscribe.di.GMCheckoutDependencyInjection;
import com.tokopedia.seller.gmsubscribe.view.checkout.presenter.GMCheckoutPresenter;
import com.tokopedia.seller.gmsubscribe.view.checkout.presenter.GMCheckoutPresenterImpl;
import com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel.GMAutoSubscribeViewModel;
import com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel.GMCheckoutCurrentSelectedViewModel;
import com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel.GMCheckoutViewModel;
import com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel.GMVoucherViewModel;
import com.tokopedia.seller.gmsubscribe.view.checkout.widget.AutoSubscribeViewHolder;
import com.tokopedia.seller.gmsubscribe.view.checkout.widget.AutoSubscribeViewHolderCallback;
import com.tokopedia.seller.gmsubscribe.view.checkout.widget.CodeVoucherViewHolderCallback;
import com.tokopedia.seller.gmsubscribe.view.checkout.widget.CodeVoucherViewHolder;
import com.tokopedia.seller.gmsubscribe.view.checkout.widget.CurrentSelectedProductViewHolder;
import com.tokopedia.seller.gmsubscribe.view.checkout.widget.CurrentSelectedProductViewHolderCallback;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GMCheckoutFragment
        extends BasePresenterFragment<GMCheckoutPresenterImpl>
        implements GMCheckoutView,
        CurrentSelectedProductViewHolderCallback,
        AutoSubscribeViewHolderCallback,
        CodeVoucherViewHolderCallback {

    public static final String TAG = "GMCheckoutFragment";
    private static final String SELECTED_PRODUCT = "SELECTED_PRODUCT";
    private static final Integer UNDEFINED_SELECTED_AUTO_EXTEND = -1;

    private Integer selectedProduct;
    private Integer autoExtendSelectedProduct = UNDEFINED_SELECTED_AUTO_EXTEND;
    private CurrentSelectedProductViewHolder currentSelectedProductViewHolder;
    private AutoSubscribeViewHolder autoSubscribeViewHolder;
    private CodeVoucherViewHolder codeVoucherViewHolder;
    private CompositeSubscription subscription;
    private GMCheckoutFragmentCallback callback;
    private Button buttonContinueCheckout;
    private TkpdProgressDialog progressDialog;


    public static Fragment createFragment(int selectedFragment){
        Fragment fragment  = new GMCheckoutFragment();
        Bundle args = new Bundle();
        args.putInt(SELECTED_PRODUCT, selectedFragment);
        fragment.setArguments(args);
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
    public void onSaveState(Bundle bundle) {

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
        subscription = new CompositeSubscription();
        presenter = GMCheckoutDependencyInjection.createPresenter();
    }

    @Override
    protected void initialListener(Activity activity) {
        if(activity instanceof GMCheckoutFragmentCallback){
            callback = (GMCheckoutFragmentCallback) activity;
        } else {
            throw new RuntimeException("Please implement GMCheckoutFragmentListener in the activity");
        }

    }

    @Override
    protected void setupArguments(Bundle bundle) {
        selectedProduct = bundle.getInt(SELECTED_PRODUCT);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_gmsubscribe_checkout;
    }

    @Override
    protected void initView(View view) {
        currentSelectedProductViewHolder = new CurrentSelectedProductViewHolder(this, view);
        autoSubscribeViewHolder = new AutoSubscribeViewHolder(this, view);
        codeVoucherViewHolder = new CodeVoucherViewHolder(this, view);
        buttonContinueCheckout = (Button) view.findViewById(R.id.button_checkout_gm_subscribe);
        buttonContinueCheckout.setOnClickListener(getContinueCheckoutListener());
        presenter.attachView(this);
        progressDialog = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {
        Log.d(TAG, "looking for product with id " + selectedProduct);
        presenter.getCurrentSelectedProduct(selectedProduct);
    }

    @Override
    public void renderCurrentSelectedProduct(GMCheckoutCurrentSelectedViewModel viewModel) {
        currentSelectedProductViewHolder.renderView(viewModel);
    }

    @Override
    public void changeCurrentSelected() {
        callback.changeCurrentSelected(selectedProduct);
    }

    @Override
    public void updateSelectedProduct(int selectedProduct) {
        this.selectedProduct = selectedProduct;
        presenter.getCurrentSelectedProduct(selectedProduct);
        if(!isAutoSubscribeUnselected()){
            presenter.getExtendSelectedProduct(selectedProduct, autoExtendSelectedProduct);
        }
    }

    @Override
    public void updateSelectedAutoProduct(int autoExtendSelectedProduct) {
        this.autoExtendSelectedProduct = autoExtendSelectedProduct;
        presenter.getExtendSelectedProduct(selectedProduct, autoExtendSelectedProduct);
    }

    @Override
    public void renderAutoSubscribeProduct(GMAutoSubscribeViewModel gmAutoSubscribeViewModel) {
        autoSubscribeViewHolder.renderAutoSubscribeProduct(gmAutoSubscribeViewModel);
    }

    @Override
    public void renderVoucherView(GMVoucherViewModel gmVoucherViewModel) {
        codeVoucherViewHolder.renderVoucherView(gmVoucherViewModel);
    }

    @Override
    public void goToDynamicPayment(GMCheckoutViewModel gmCheckoutDomainModel) {
        callback.goToDynamicPayment(gmCheckoutDomainModel.getPaymentUrl(), gmCheckoutDomainModel.getParameter());
    }

    @Override
    public void failedGetCurrentProduct() {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getCurrentSelectedProduct(selectedProduct);
            }
        });
    }

    @Override
    public void failedGetAutoSubscribeProduct() {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getExtendSelectedProduct(selectedProduct, autoExtendSelectedProduct);
            }
        });
    }

    @Override
    public void failedCheckout() {
        NetworkErrorHelper.showSnackbar(getActivity());
    }

    @Override
    public void selectAutoSubscribePackageFirstTime() {
        callback.selectAutoSubscribePackageFirstTime();
    }

    @Override
    public void changeAutoSubscribePackage() {
        callback.changeAutoSubscribePackage(autoExtendSelectedProduct);
    }

    @Override
    public boolean isAutoSubscribeUnselected() {
        return autoExtendSelectedProduct == UNDEFINED_SELECTED_AUTO_EXTEND;
    }

    @Override
    public void checkVoucher(String voucherCode) {
        presenter.checkVoucherCode(voucherCode, selectedProduct);
    }

    public View.OnClickListener getContinueCheckoutListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(codeVoucherViewHolder.isVoucherOpen()){
                    presenter.checkoutWithVoucherCheckGMSubscribe(selectedProduct, autoExtendSelectedProduct, codeVoucherViewHolder.getVoucherCode());
                } else {
                    presenter.checkoutGMSubscribe(selectedProduct, autoExtendSelectedProduct, codeVoucherViewHolder.getVoucherCode());
                }
            }
        };
    }

    @Override
    public void showProgressDialog(){
        progressDialog.showDialog();
    }

    @Override
    public void dismissProgressDialog(){
        progressDialog.dismiss();
    }

    @Override
    public void showGenericError() {
        NetworkErrorHelper.showSnackbar(getActivity());
    }

}


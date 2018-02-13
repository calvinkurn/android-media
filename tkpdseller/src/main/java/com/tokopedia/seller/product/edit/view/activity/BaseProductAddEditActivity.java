package com.tokopedia.seller.product.edit.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.base.view.dialog.BaseTextPickerDialogFragment;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.view.dialog.AddWholeSaleDialog;
import com.tokopedia.seller.product.edit.view.fragment.BaseProductAddEditFragment;
import com.tokopedia.seller.product.edit.view.fragment.ProductAddFragment;
import com.tokopedia.seller.product.edit.view.model.wholesale.WholesaleModel;
import com.tokopedia.seller.product.edit.view.presenter.ProductAddPresenter;
import com.tokopedia.seller.product.edit.view.service.UploadProductService;

/**
 * Created by nakama on 13/02/18.
 */

public abstract class BaseProductAddEditActivity extends BaseSimpleActivity
    implements HasComponent<ProductComponent>,
        BaseTextPickerDialogFragment.Listener,
        AddWholeSaleDialog.WholeSaleDialogListener,
        ProductAddFragment.Listener {
    public static final int MAX_IMAGES = 5;

    TkpdProgressDialog tkpdProgressDialog;

    public static final int PRODUCT_REQUEST_CODE = 8293;

    protected abstract int getCancelMessageRes();

    protected abstract boolean needDeleteCacheOnBack();

    protected abstract Fragment getNewFragment();

    protected void createProductAddFragment() {
        if (getFragment() != null) {
            return;
        }
        inflateFragment();
    }

    @Override
    public ProductComponent getComponent() {
        return ((SellerModuleRouter) getApplication()).getProductComponent();
    }

    public BaseProductAddEditFragment<? extends ProductAddPresenter> getBaseProductAddFragment() {
        Fragment fragment = getFragment();
        if (fragment != null && fragment instanceof BaseProductAddEditFragment) {
            return (BaseProductAddEditFragment<?>) fragment;
        }
        return null;
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    public void onTextPickerSubmitted(String text) {

    }

    @Override
    public void addWholesaleItem(WholesaleModel item) {
        if (getBaseProductAddFragment() != null && getBaseProductAddFragment().isVisible()) {
            getBaseProductAddFragment().addWholesaleItem(item);
        }
    }

    @Override
    public void startAddWholeSaleDialog(
            WholesaleModel fixedPrice,
            @CurrencyTypeDef int currencyType,
            WholesaleModel previousWholesalePrice, boolean officialStore) {
        AddWholeSaleDialog addWholeSaleDialog = AddWholeSaleDialog.newInstance(
                fixedPrice, currencyType, previousWholesalePrice, officialStore
        );
        addWholeSaleDialog.show(getSupportFragmentManager(), AddWholeSaleDialog.TAG);
        addWholeSaleDialog.setOnDismissListener(new AddWholeSaleDialog.OnDismissListener() {
            @Override
            public void onDismiss() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        View view = getCurrentFocus();
                        if (view != null) {
                            CommonUtils.hideSoftKeyboard(view);
                            view.clearFocus();
                        }
                    }
                });
            }
        });
    }

    public void startUploadProduct(long productId) {
        startUploadProductService(productId);
        finish();
    }

    private void startUploadProductService(long productId) {
        BaseProductAddEditFragment productAddFragment = getBaseProductAddFragment();
        boolean isAdd = true;
        if (productAddFragment != null) {
            isAdd = productAddFragment.isAddStatus();
        }
        startService(UploadProductService.getIntent(this, productId, isAdd));
    }

    public void startUploadProductWithShare(long productId) {
        startUploadProductService(productId);
        startActivity(ProductDetailRouter.createAddProductDetailInfoActivity(this));
        finish();
    }

    @Override
    public void startUploadProductAndAdd(Long productId) {
        startUploadProductService(productId);
        ProductAddActivity.start(this);
        finish();
    }

    @Override
    public void successSaveDraftToDBWhenBackpressed() {
        CommonUtils.UniversalToast(this, getString(R.string.product_draft_product_has_been_saved_as_draft));
        finish();
    }

    @Override
    public void startUploadProductAndAddWithShare(Long productId) {
        startUploadProductService(productId);
        ProductAddActivity.start(this);
        startActivity(ProductDetailRouter.createAddProductDetailInfoActivity(this));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (showDialogSaveDraftOnBack()) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                    .setMessage(getString(getCancelMessageRes()))
                    .setPositiveButton(getString(R.string.label_exit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteNotUsedTkpdCacheImage();
                            backPressedHandleTaskRoot();
                        }
                    }).setNegativeButton(getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            // no op, just dismiss
                        }
                    });
            alertDialogBuilder.setNeutralButton(getString(R.string.product_draft_save_as_draft), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    boolean doSave = saveProductToDraft();
                    UnifyTracking.eventClickAddProduct(AppEventTracking.Category.ADD_PRODUCT,
                            AppEventTracking.EventLabel.SAVE_DRAFT);
                    if (!doSave) {
                        backPressedHandleTaskRoot();
                    }
                }
            });
            AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();
        } else {
            backPressedHandleTaskRoot();
        }
    }

    private boolean saveProductToDraft() {
        // save newly added product ToDraft
        Fragment fragment = getFragment();
        if (fragment != null && fragment instanceof BaseProductAddEditFragment) {
            ((BaseProductAddEditFragment) fragment).saveDraft(false);
            return true;
        }
        return false;
    }

    private void backPressedHandleTaskRoot() {
        if (isTaskRoot()) {
            Intent homeIntent = ((TkpdCoreRouter) getApplication()).getHomeIntent(this);
            startActivity(homeIntent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private boolean showDialogSaveDraftOnBack() {
        BaseProductAddEditFragment fragment = getBaseProductAddFragment();
        return fragment != null && fragment.showDialogSaveDraftOnBack();
    }

    private void deleteNotUsedTkpdCacheImage() {
        if (needDeleteCacheOnBack()) {
            BaseProductAddEditFragment fragment = getBaseProductAddFragment();
            if (fragment != null) {
                fragment.deleteNotUsedTkpdCacheImage();
            }
        }
    }

    public void showProgressDialog() {
        if (tkpdProgressDialog == null) {
            tkpdProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        }
        tkpdProgressDialog.showDialog();
    }

    public void dismissDialog() {
        if (tkpdProgressDialog != null) {
            tkpdProgressDialog.dismiss();
        }
    }

}

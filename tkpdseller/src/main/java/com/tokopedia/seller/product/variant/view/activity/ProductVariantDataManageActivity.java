package com.tokopedia.seller.product.variant.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantValue;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.VariantData;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantDataManageFragment;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantManageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendry on 8/2/17.
 */

public class ProductVariantDataManageActivity extends BaseSimpleActivity implements HasComponent<ProductComponent>,
        ProductVariantDataManageFragment.OnProductVariantDataManageFragmentListener {

    public static final String EXTRA_VARIANT_ID = "var_id";
    public static final String EXTRA_VARIANT_NAME = "var_name";
    public static final String EXTRA_VARIANT_VALUE_LIST = "var_lst";
    public static final String EXTRA_SELECTED_VARIANT_ID_LIST = "sel_var_id_lst";

    public static final String EXTRA_ACTION_DELETE = "del";
    public static final String EXTRA_ACTION_SUBMIT = "sbmt";

    public static final int REQUEST_CODE = 905;

    private long variantId;
    private String variantName;

    public static void start(Context context, Fragment fragment,
                             long variantId,
                             String variantName,
                             ArrayList<ProductVariantValue> productVariantValueArrayList,
                             ArrayList<Long> selectedVariantValueId){
        Intent intent = createInstance(context, variantId, variantName,
                productVariantValueArrayList, selectedVariantValueId);
        fragment.startActivityForResult(intent, REQUEST_CODE);
    }

    public static Intent createInstance(Context context,
                                        long variantId,
                                        String variantName,
                                        ArrayList<ProductVariantValue> productVariantValueArrayList,
                                        ArrayList<Long> selectedVariantValueId){
        Intent intent = new Intent(context, ProductVariantDataManageActivity.class);
        intent.putExtra(EXTRA_VARIANT_ID, variantId);
        intent.putExtra(EXTRA_VARIANT_NAME, variantName);
        intent.putParcelableArrayListExtra(EXTRA_VARIANT_VALUE_LIST, productVariantValueArrayList);
        intent.putExtra(EXTRA_SELECTED_VARIANT_ID_LIST, selectedVariantValueId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO hendry for test only
        if (!getIntent().hasExtra(EXTRA_VARIANT_ID)) {
            variantName = "Norman ganggu";
            variantId = 10;
        } else {
            variantName = getIntent().getStringExtra(EXTRA_VARIANT_NAME);
            variantId = getIntent().getLongExtra(EXTRA_VARIANT_ID, 0L);
        }
        toolbar.setTitle(variantName);
    }

    @Override
    protected Fragment getNewFragment() {
        return ProductVariantDataManageFragment.newInstance();
    }

    @Override
    public ProductComponent getComponent() {
        return ((SellerModuleRouter) getApplication()).getProductComponent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_product_variant_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            showDeleteDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showDeleteDialog(){
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                .setMessage(MethodChecker.fromHtml(getString(R.string.delete_this_variant, variantName)))
                .setPositiveButton(getString(R.string.action_delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onDeleteVariant();
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        // no op
                    }
                }).create();
        dialog.show();
    }

    public void onDeleteVariant() {
        Intent intent = new Intent(EXTRA_ACTION_DELETE);
        intent.putExtra(EXTRA_VARIANT_ID, variantId);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onSubmitVariant(List<Long> selectedVariantValueIds) {
        Intent intent = new Intent(EXTRA_ACTION_SUBMIT);
        intent.putExtra(EXTRA_VARIANT_ID, variantId);
        intent.putExtra(EXTRA_VARIANT_VALUE_LIST,(ArrayList) selectedVariantValueIds);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
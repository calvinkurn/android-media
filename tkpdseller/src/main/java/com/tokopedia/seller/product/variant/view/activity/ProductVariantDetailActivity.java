package com.tokopedia.seller.product.variant.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantDetailFragment;
import com.tokopedia.seller.product.variant.view.model.ProductVariantDetailViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendry on 8/2/17.
 */

public class ProductVariantDetailActivity extends BaseSimpleActivity implements
        ProductVariantDetailFragment.OnProductVariantDataManageFragmentListener {

    public static final String EXTRA_VARIANT_OPTION_ID = "var_id";
    public static final String EXTRA_VARIANT_NAME = "var_name";
    public static final String EXTRA_VARIANT_HAS_STOCK = "var_has_stock";
    public static final String EXTRA_VARIANT_VALUE_LIST = "var_lst";
    public static final String EXTRA_SELECTED_VARIANT_ID_LIST = "sel_var_id_lst";
    public static final String SAVED_HAS_ANY_CHANGES = "any_chg";

    public static final String EXTRA_ACTION_DELETE = "del";
    public static final String EXTRA_ACTION_SUBMIT = "sbmt";

    public static final int REQUEST_CODE = 905;

    private long variantId;
    private String variantName;

    public static void start(Context context, Fragment fragment,
                             long variantId,
                             String variantName,
                             boolean hasStock,
                             ArrayList<ProductVariantDetailViewModel> productVariantValueArrayList,
                             ArrayList<Long> selectedVariantValueId){
        Intent intent = createInstance(context, variantId, variantName, hasStock,
                productVariantValueArrayList, selectedVariantValueId);
        fragment.startActivityForResult(intent, REQUEST_CODE);
    }

    public static Intent createInstance(Context context,
                                        long variantId,
                                        String variantName,
                                        boolean hasStock,
                                        ArrayList<ProductVariantDetailViewModel> productVariantValueArrayList,
                                        ArrayList<Long> selectedVariantValueId){
        Intent intent = new Intent(context, ProductVariantDetailActivity.class);
        intent.putExtra(EXTRA_VARIANT_OPTION_ID, variantId);
        intent.putExtra(EXTRA_VARIANT_NAME, variantName);
        intent.putExtra(EXTRA_VARIANT_HAS_STOCK, hasStock);
        intent.putParcelableArrayListExtra(EXTRA_VARIANT_VALUE_LIST, productVariantValueArrayList);
        intent.putExtra(EXTRA_SELECTED_VARIANT_ID_LIST, selectedVariantValueId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        variantName = intent.getStringExtra(EXTRA_VARIANT_NAME);
        variantId = intent.getLongExtra(EXTRA_VARIANT_OPTION_ID, 0L);
        toolbar.setTitle(variantName);
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }

    @Override
    protected Fragment getNewFragment() {
        return ProductVariantDetailFragment.newInstance();
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
                }).setNegativeButton(getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        // no op
                    }
                }).create();
        dialog.show();
    }

    public void onDeleteVariant() {
        Intent intent = new Intent(EXTRA_ACTION_DELETE);
        intent.putExtra(EXTRA_VARIANT_OPTION_ID, variantId);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onSubmitVariant(boolean isVariantHasStock, List<Long> selectedVariantValueIds) {
        Intent intent = new Intent(EXTRA_ACTION_SUBMIT);
        intent.putExtra(EXTRA_VARIANT_OPTION_ID, variantId);
        intent.putExtra(EXTRA_VARIANT_HAS_STOCK, isVariantHasStock);
        intent.putExtra(EXTRA_VARIANT_VALUE_LIST,(ArrayList) selectedVariantValueIds);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }
}
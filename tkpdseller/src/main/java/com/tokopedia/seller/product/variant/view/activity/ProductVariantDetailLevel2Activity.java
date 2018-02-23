package com.tokopedia.seller.product.variant.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantViewModel;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantDetailFragment;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantDetailLevel1Fragment;

import java.util.List;

/**
 * Created by hendry on 8/2/17.
 */

public class ProductVariantDetailLevel2Activity extends BaseSimpleActivity implements
        ProductVariantDetailLevel1Fragment.OnProductVariantDataManageFragmentListener {

    public static final String EXTRA_PRODUCT_VARIANT_DATA = "var_data";
    public static final String EXTRA_VARIANT_LEVEL1_ID = "var_id";
//    public static final String EXTRA_VARIANT_HAS_STOCK = "var_has_stock";
//    public static final String EXTRA_VARIANT_VALUE_LIST = "var_lst";
//    public static final String EXTRA_SELECTED_VARIANT_ID_LIST = "sel_var_id_lst";

//    public static final String EXTRA_ACTION_DELETE = "del";
    public static final String EXTRA_ACTION_SUBMIT = "sbmt";

    public static final int VARIANT_EDIT_LEVEL1_REQUEST_CODE = 905;

    private long variantLevel1Id;

    private ProductVariantViewModel productVariantViewModel;

    public static void start(Context context, Fragment fragment,
                             ProductVariantViewModel productVariantViewModel,
                             int variantLevel1Id,
                             String variantName){
        Intent intent = getIntent(context, productVariantViewModel, variantLevel1Id, variantName);
        fragment.startActivityForResult(intent, VARIANT_EDIT_LEVEL1_REQUEST_CODE);
    }

    public static Intent getIntent(Context context,
                                   ProductVariantViewModel productVariantViewModel,
                                   int variantLevel1Id,
                                   String variantName){
        Intent intent = new Intent(context, ProductVariantDetailLevel2Activity.class);
        intent.putExtra(EXTRA_PRODUCT_VARIANT_DATA, productVariantViewModel);
        intent.putExtra(EXTRA_VARIANT_LEVEL1_ID, variantLevel1Id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        productVariantViewModel = intent.getParcelableExtra(EXTRA_PRODUCT_VARIANT_DATA);

        variantLevel1Id = intent.getLongExtra(EXTRA_VARIANT_LEVEL1_ID, 0L);

        //TODO title for variant name
//        variantName = intent.getStringExtra(EXTRA_VARIANT_NAME);
//        toolbar.setTitle(getTitle() + " " + variantName);

    }

    @Override
    protected Fragment getNewFragment() {
        return ProductVariantDetailLevel1Fragment.newInstance();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_product_variant_data, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_delete) {
//            showDeleteDialog();
//            return true;
//        } else {
//            return super.onOptionsItemSelected(item);
//        }
//    }

//    private void showDeleteDialog(){
//        AlertDialog dialog = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
//                .setMessage(MethodChecker.fromHtml(getString(R.string.delete_this_variant, variantName)))
//                .setPositiveButton(getString(R.string.action_delete), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        onDeleteVariant();
//                    }
//                }).setNegativeButton(getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        // no op
//                    }
//                }).create();
//        dialog.show();
//    }

//    public void onDeleteVariant() {
//        Intent intent = new Intent(EXTRA_ACTION_DELETE);
//        intent.putExtra(EXTRA_VARIANT_OPTION_ID, variantLevel1Id);
//        setResult(Activity.RESULT_OK, intent);
//        finish();
//    }

    @Override
    public void onSubmitVariant(boolean isVariantHasStock, List<Long> selectedVariantValueIds) {
        Intent intent = new Intent(EXTRA_ACTION_SUBMIT);
        //TODO set productVariantViewModel from view
//        intent.putExtra(EXTRA_VARIANT_OPTION_ID, variantLevel1Id);
//        intent.putExtra(EXTRA_VARIANT_HAS_STOCK, isVariantHasStock);
//        intent.putExtra(EXTRA_VARIANT_VALUE_LIST,(ArrayList) selectedVariantValueIds);
        intent.putExtra(EXTRA_PRODUCT_VARIANT_DATA, productVariantViewModel);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }
}
package com.tokopedia.seller.product.variant.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantDetailLevel1ListFragment;
import com.tokopedia.seller.product.variant.view.model.ProductVariantDashboardNewViewModel;

/**
 * Created by hendry on 8/2/17.
 */

public class ProductVariantDetailLevel1ListActivity extends BaseSimpleActivity implements
        ProductVariantDetailLevel1ListFragment.OnProductVariantDataManageFragmentListener {

    public static final String EXTRA_PRODUCT_VARIANT_DATA = "var_data";
    public static final String EXTRA_PRODUCT_VARIANT_LV1_NAME = "var_lv1_nm";
    public static final String EXTRA_PRODUCT_VARIANT_LV2_NAME = "var_lv2_nm";
//    public static final String EXTRA_VARIANT_HAS_STOCK = "var_has_stock";
//    public static final String EXTRA_VARIANT_VALUE_LIST = "var_lst";
//    public static final String EXTRA_SELECTED_VARIANT_ID_LIST = "sel_var_id_lst";

//    public static final String EXTRA_ACTION_DELETE = "del";
    public static final String EXTRA_ACTION_SUBMIT = "sbmt";

    public static final int VARIANT_EDIT_LEVEL1_LIST_REQUEST_CODE = 905;

    private ProductVariantDashboardNewViewModel productVariantDashboardNewViewModel;
    private String varLv1name;
    private String varLv2name;

    public static void start(Context context, Fragment fragment,
                             ProductVariantDashboardNewViewModel productVariantDashboardNewViewModel,
                             String variantLv1Name, String variantLv2Name){
        Intent intent = getIntent(context, productVariantDashboardNewViewModel, variantLv1Name, variantLv2Name);
        fragment.startActivityForResult(intent, VARIANT_EDIT_LEVEL1_LIST_REQUEST_CODE);
    }

    public static Intent getIntent(Context context,
                                   ProductVariantDashboardNewViewModel productVariantDashboardNewViewModel,
                                   String variantLv1Name, String variantLv2Name){
        Intent intent = new Intent(context, ProductVariantDetailLevel1ListActivity.class);
        intent.putExtra(EXTRA_PRODUCT_VARIANT_DATA, productVariantDashboardNewViewModel);
        intent.putExtra(EXTRA_PRODUCT_VARIANT_LV1_NAME, variantLv1Name);
        intent.putExtra(EXTRA_PRODUCT_VARIANT_LV2_NAME, variantLv2Name);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        varLv1name = intent.getStringExtra(EXTRA_PRODUCT_VARIANT_LV1_NAME);
        varLv2name = intent.getStringExtra(EXTRA_PRODUCT_VARIANT_LV2_NAME);
        if (savedInstanceState == null) {
            productVariantDashboardNewViewModel = intent.getParcelableExtra(EXTRA_PRODUCT_VARIANT_DATA);
        } else {
            productVariantDashboardNewViewModel = savedInstanceState.getParcelable(EXTRA_PRODUCT_VARIANT_DATA);
        }

        //TODO title for variant name
        toolbar.setTitle(getTitle() + " " + productVariantDashboardNewViewModel.getProductVariantOptionChildLv1().getValue());

    }

    @Override
    public void onBackPressed() {
        // it has no submit button, so save the model back to caller
        onSubmitVariant();
    }

    @Override
    public ProductVariantDashboardNewViewModel getProductVariantDashboardNewViewModel() {
        return productVariantDashboardNewViewModel;
    }

    @Override
    public String getVariantName() {
        return varLv1name;
    }

    @Override
    public void onSubmitVariant() {
        Intent intent = new Intent(EXTRA_ACTION_SUBMIT);
        intent.putExtra(EXTRA_PRODUCT_VARIANT_DATA, productVariantDashboardNewViewModel);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

//    @Override
//    public void onSubmitVariant(boolean isVariantHasStock, List<Long> selectedVariantValueIds) {
//        Intent intent = new Intent(EXTRA_ACTION_SUBMIT);
//        //TODO set productVariantViewModel from view
////        intent.putExtra(EXTRA_VARIANT_OPTION_ID, variantLevel1Id);
////        intent.putExtra(EXTRA_VARIANT_HAS_STOCK, isVariantHasStock);
////        intent.putExtra(EXTRA_VARIANT_VALUE_LIST,(ArrayList) selectedVariantValueIds);
//        intent.putExtra(EXTRA_PRODUCT_VARIANT_DATA, productVariantDashboardNewViewModel);
//        setResult(Activity.RESULT_OK, intent);
//        finish();
//    }

    @Override
    protected Fragment getNewFragment() {
        return ProductVariantDetailLevel1ListFragment.newInstance();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO get data from leaf
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_PRODUCT_VARIANT_DATA, productVariantDashboardNewViewModel);
    }
}
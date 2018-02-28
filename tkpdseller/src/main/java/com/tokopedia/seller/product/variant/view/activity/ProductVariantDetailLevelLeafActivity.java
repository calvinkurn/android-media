package com.tokopedia.seller.product.variant.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
import com.tokopedia.seller.product.variant.view.fragment.ProductVariantDetailLeafFragment;
import com.tokopedia.seller.product.variant.view.model.ProductVariantDashboardNewViewModel;

/**
 * Created by hendry on 8/2/17.
 */

public class ProductVariantDetailLevelLeafActivity extends BaseSimpleActivity implements
        ProductVariantDetailLeafFragment.OnProductVariantDetailLeafFragmentListener {

    public static final String EXTRA_PRODUCT_VARIANT_LEAF_DATA = "var_data";
    public static final String EXTRA_PRODUCT_VARIANT_NAME = "var_name";
    public static final String EXTRA_CURRENCY_TYPE = "curr_type";
//    public static final String EXTRA_VARIANT_HAS_STOCK = "var_has_stock";
//    public static final String EXTRA_VARIANT_VALUE_LIST = "var_lst";
//    public static final String EXTRA_SELECTED_VARIANT_ID_LIST = "sel_var_id_lst";

    //    public static final String EXTRA_ACTION_DELETE = "del";
    public static final String EXTRA_ACTION_SUBMIT = "sbmt";

    public static final int VARIANT_EDIT_LEAF_REQUEST_CODE = 906;

    private ProductVariantCombinationViewModel productVariantCombinationViewModel;
    private String variantName;

    private @CurrencyTypeDef int currencyType;

    public static void start(Context context, Fragment fragment,
                             ProductVariantCombinationViewModel productVariantCombinationViewModel,
                             String variantName, @CurrencyTypeDef int currencyType){
        Intent intent = getIntent(context, productVariantCombinationViewModel, variantName, currencyType);
        fragment.startActivityForResult(intent, VARIANT_EDIT_LEAF_REQUEST_CODE);
    }

    public static void start(Activity activity,
                             ProductVariantCombinationViewModel productVariantCombinationViewModel,
                             String variantName, @CurrencyTypeDef int currencyType){
        Intent intent = getIntent(activity, productVariantCombinationViewModel, variantName, currencyType);
        activity.startActivityForResult(intent, VARIANT_EDIT_LEAF_REQUEST_CODE);
    }

    public static Intent getIntent(Context context,
                                   ProductVariantCombinationViewModel productVariantCombinationViewModel,
                                   String variantName, @CurrencyTypeDef int currencyType){
        Intent intent = new Intent(context, ProductVariantDetailLevelLeafActivity.class);
        intent.putExtra(EXTRA_PRODUCT_VARIANT_LEAF_DATA, productVariantCombinationViewModel);
        intent.putExtra(EXTRA_PRODUCT_VARIANT_NAME, variantName);
        intent.putExtra(EXTRA_CURRENCY_TYPE, currencyType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        variantName = intent.getStringExtra(EXTRA_PRODUCT_VARIANT_NAME);
        currencyType = intent.getIntExtra(EXTRA_CURRENCY_TYPE, CurrencyTypeDef.TYPE_IDR);

        if (savedInstanceState == null) {
            productVariantCombinationViewModel = intent.getParcelableExtra(EXTRA_PRODUCT_VARIANT_LEAF_DATA);
        } else {
            productVariantCombinationViewModel = savedInstanceState.getParcelable(EXTRA_PRODUCT_VARIANT_LEAF_DATA);
        }

        toolbar.setTitle(getTitle() + " " + variantName);
    }

    @Override
    public String getVariantName() {
        return variantName;
    }

    @Override
    public int getCurrencyTypeDef() {
        return currencyType;
    }

    @Override
    public ProductVariantCombinationViewModel getProductVariantCombinationViewModel() {
        return productVariantCombinationViewModel;
    }

    @Override
    public void onSubmitVariant() {
        Intent intent = new Intent(EXTRA_ACTION_SUBMIT);
        intent.putExtra(EXTRA_PRODUCT_VARIANT_LEAF_DATA, productVariantCombinationViewModel);
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
        return ProductVariantDetailLeafFragment.newInstance();
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
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_PRODUCT_VARIANT_LEAF_DATA, productVariantCombinationViewModel);
    }
}
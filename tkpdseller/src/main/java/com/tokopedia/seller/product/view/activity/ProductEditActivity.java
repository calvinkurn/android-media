package com.tokopedia.seller.product.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.fragment.ProductEditFragment;

/**
 * @author sebastianuskh on 4/21/17.
 */

public class ProductEditActivity extends ProductAddActivity {

    public static final String PRODUCT_ID = "PRODUCT_ID";

    public static Intent createInstance(Context context, String productId){
        Intent intent = new Intent(context, ProductEditActivity.class);
        intent.putExtra(PRODUCT_ID, productId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_product_add);
        String productId = getIntent().getStringExtra(PRODUCT_ID);
        if (StringUtils.isBlank(productId)){
            throw new RuntimeException("Product id is not selected");
        }
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
                .add(R.id.container, ProductEditFragment.createInstance(productId), ProductEditFragment.class.getSimpleName())
                .commit();
    }

}

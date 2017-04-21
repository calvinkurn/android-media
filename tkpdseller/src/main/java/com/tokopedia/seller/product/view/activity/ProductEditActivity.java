package com.tokopedia.seller.product.view.activity;

import android.os.Bundle;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.fragment.ProductEditFragment;

/**
 * @author sebastianuskh on 4/21/17.
 */

public class ProductEditActivity extends ProductAddActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_product_add);
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
                .add(R.id.container, ProductEditFragment.createInstance(), ProductEditFragment.class.getSimpleName())
                .commit();
    }

}

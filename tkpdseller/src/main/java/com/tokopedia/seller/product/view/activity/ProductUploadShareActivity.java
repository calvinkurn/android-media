package com.tokopedia.seller.product.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.share.fragment.ProductShareFragment;
import com.tokopedia.seller.R;

/**
 * @author sebastianuskh on 4/21/17.
 */

public class ProductUploadShareActivity extends TActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_product_add);
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .add(R.id.container, ProductShareFragment.newInstance(true), ProductShareFragment.TAG)
                .commit();
    }
}

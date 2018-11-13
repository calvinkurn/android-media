package com.tokopedia.tkpdpdp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.tkpdpdp.courier.CourierFragment;
import com.tokopedia.tkpdpdp.courier.CourierViewData;

import java.util.ArrayList;

/**
 * @author by HenryPri on 12/05/17.
 */

public class CourierActivity extends BaseSimpleActivity {

    private static final String ARGS_LIST = "ARGS_LIST";
    private static final String ARGS_PRODUCT_ID = "ARGS_PRODUCT_ID";
    private String productId;
    private ArrayList<CourierViewData> list;

    public static Intent createIntent(Context context,
                                      String productId,
                                      ArrayList<CourierViewData> list) {
        Intent intent = new Intent(context, CourierActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARGS_LIST, list);
        bundle.putString(ARGS_PRODUCT_ID, productId);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void setupFragment(Bundle savedInstance) {
        if (getIntent() != null
                && getIntent().getExtras() != null
                && getIntent().getExtras().getParcelableArrayList(ARGS_LIST) != null) {
            productId = getIntent().getExtras().getString(ARGS_PRODUCT_ID);
            list = getIntent().getExtras().getParcelableArrayList(ARGS_LIST);
            super.setupFragment(savedInstance);
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return CourierFragment.Companion.newInstance(productId, list);
    }
}

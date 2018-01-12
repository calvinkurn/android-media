package com.tokopedia.seller.product.edit.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.webkit.WebView;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.edit.view.fragment.ProductAddInfoFragment;

/**
 * Created by normansyahputa on 1/4/18.
 */

public class ProductAddInfoActivity extends BaseSimpleActivity {
    @Override
    protected Fragment getNewFragment() {
        return ProductAddInfoFragment.create("file:///android_asset/add-product-info.html");
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }
}

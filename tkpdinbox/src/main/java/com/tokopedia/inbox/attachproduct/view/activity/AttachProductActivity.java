package com.tokopedia.inbox.attachproduct.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.attachproduct.view.AttachProductContract;
import com.tokopedia.inbox.attachproduct.view.fragment.AttachProductFragment;
import com.tokopedia.inbox.attachproduct.view.resultmodel.ResultProduct;

import java.util.ArrayList;

/**
 * Created by Hendri on 13/02/18.
 */

public class AttachProductActivity extends BaseSimpleActivity implements AttachProductContract.Activity {
    public static final int TOKOPEDIA_ATTACH_PRODUCT_REQ_CODE = 113;
    public static final int TOKOPEDIA_ATTACH_PRODUCT_RESULT_CODE_OK = 324;
    public static final String TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY = "TKPD_ATTACH_PRODUCT_RESULTS";
    public static final String TOKOPEDIA_ATTACH_PRODUCT_SHOP_ID_KEY = "TKPD_ATTACH_PRODUCT_SHOP_ID";
    public static final String TOKOPEDIA_ATTACH_PRODUCT_SHOP_NAME_KEY = "TKPD_ATTACH_PRODUCT_SHOP_NAME";
    public static final String TOKOPEDIA_ATTACH_PRODUCT_IS_SELLER_KEY = "TKPD_ATTACH_PRODUCT_IS_SELLER";
    private String shopId;
    private String shopName;
    private boolean isSeller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopId = "";
        if(getIntent().getStringExtra(TOKOPEDIA_ATTACH_PRODUCT_SHOP_ID_KEY) != null)
            shopId = getIntent().getStringExtra(TOKOPEDIA_ATTACH_PRODUCT_SHOP_ID_KEY);
        if(!getIntent().getBooleanExtra(TOKOPEDIA_ATTACH_PRODUCT_IS_SELLER_KEY,false))
            isSeller = true;

        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        setTitle("Lampirkan Produk");
        super.setupLayout(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_white_toolbar_drop_shadow));
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_close_default));
        if(getIntent().getStringExtra(TOKOPEDIA_ATTACH_PRODUCT_SHOP_NAME_KEY) != null)
            shopName = getIntent().getStringExtra(TOKOPEDIA_ATTACH_PRODUCT_SHOP_NAME_KEY);
        else
            shopName = "";
        toolbar.setSubtitleTextAppearance(this,R.style.AttachProductToolbarSubTitle_SansSerif);
        toolbar.setTitleTextAppearance(this,R.style.AttachProductToolbarTitle_SansSerif);
        toolbar.setSubtitle(shopName);
    }

    public static Intent createInstance(Context context, String shopId, String shopName, boolean isSeller) {
        Intent intent = new Intent(context, AttachProductActivity.class);
        intent.putExtra(TOKOPEDIA_ATTACH_PRODUCT_SHOP_ID_KEY,shopId);
        intent.putExtra(TOKOPEDIA_ATTACH_PRODUCT_IS_SELLER_KEY,isSeller);
        intent.putExtra(TOKOPEDIA_ATTACH_PRODUCT_SHOP_NAME_KEY,shopName);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getTagFragment());
        if(fragment != null){
            return fragment;
        }else{
            fragment = AttachProductFragment.newInstance(this);
            return fragment;
        }
    }

    @Override
    public String getShopId() {
        return shopId;
    }

    @Override
    public void finishActivityWithResult(ArrayList<ResultProduct> products) {
        Intent data = new Intent();
        data.putParcelableArrayListExtra(TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY,products);
        setResult(TOKOPEDIA_ATTACH_PRODUCT_RESULT_CODE_OK,data);
        finish();
    }

    @Override
    public boolean isSeller() {
        return this.isSeller;
    }

    @Override
    public void goToAddProduct(String shopId) {
        TkpdInboxRouter router = (TkpdInboxRouter) MainApplication.getAppContext();
        router.startAddProduct(this,"");
    }
}

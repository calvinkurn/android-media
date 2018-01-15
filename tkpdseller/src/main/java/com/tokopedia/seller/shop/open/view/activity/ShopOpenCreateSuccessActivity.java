package com.tokopedia.seller.shop.open.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.view.activity.ProductAddActivity;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenCreateSuccessFragment;


public class ShopOpenCreateSuccessActivity extends BaseActivity {

    public static Intent getIntent(Context context) {
        return new Intent(context, ShopOpenCreateSuccessActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_toolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ShopOpenCreateSuccessFragment.newInstance(),
                            ShopOpenCreateSuccessFragment.TAG)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = ((TkpdCoreRouter)getApplication()).getHomeIntent(this);
        startActivity(intent);
        finish();
    }
}

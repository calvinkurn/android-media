package com.tokopedia.seller.seller.info.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.seller.info.view.fragment.SellerInfoWebViewFragment;

/**
 * Created by normansyahputa on 12/5/17.
 */

public class SellerInfoWebViewActivity extends BaseSimpleActivity {

    public static Intent getCallingIntent(Fragment activity, String url) {
        Bundle bundle = new Bundle();
        bundle.putString(SellerInfoWebViewFragment.EXTRA_URL, url);
        Intent intent = new Intent(activity.getActivity(), SellerInfoWebViewActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    private String extraUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if(savedInstanceState == null){
            extraUrl = extras.getString(SellerInfoWebViewFragment.EXTRA_URL);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return SellerInfoWebViewFragment.newInstance(extraUrl);
    }
}

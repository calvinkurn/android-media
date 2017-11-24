package com.tokopedia.seller.shop.district.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.shop.district.view.fragment.ShopDistrictPickerFragment;

/**
 * Created by nathan on 10/23/17.
 */

public class ShopDistrictPickerActivity extends BaseSimpleActivity {

    public static Intent createInstance(Activity activity){
        Intent intent = new Intent(activity, ShopDistrictPickerActivity.class);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return ShopDistrictPickerFragment.createInstance();
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;

    }
}
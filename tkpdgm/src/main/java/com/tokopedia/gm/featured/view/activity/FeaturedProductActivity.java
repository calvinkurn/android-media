package com.tokopedia.seller.goldmerchant.featured.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;
import com.tokopedia.seller.goldmerchant.featured.constant.FeaturedProductType;
import com.tokopedia.seller.goldmerchant.featured.data.cloud.api.FeaturedProductApi;
import com.tokopedia.seller.goldmerchant.featured.data.model.FeaturedProductGETModel;
import com.tokopedia.seller.goldmerchant.featured.data.model.FeaturedProductPOSTModel;
import com.tokopedia.seller.goldmerchant.featured.data.model.PostFeaturedProductModel;
import com.tokopedia.seller.goldmerchant.featured.di.component.DaggerFeaturedProductComponent;
import com.tokopedia.seller.goldmerchant.featured.view.fragment.FeaturedProductFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class FeaturedProductActivity extends BaseSimpleActivity implements HasComponent<GoldMerchantComponent>{

    private static final String TAG = "FeaturedProductActivity";

    @Override
    protected Fragment getNewFragment() {
        return new FeaturedProductFragment();
    }

    @Override
    protected String getTagFragment() {
        return TAG;
    }

    @Override
    public GoldMerchantComponent getComponent() {
        if(getApplication() != null && getApplication() instanceof SellerModuleRouter){
            return ((SellerModuleRouter) getApplication()).getGoldMerchantComponent();
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getFragment();
        if(fragment != null && fragment.isVisible() && fragment instanceof FeaturedProductFragment){
            int featuredProductType = ((FeaturedProductFragment) fragment).getFeaturedProductType();
            switch (featuredProductType){
                case FeaturedProductType.ARRANGE_DISPLAY:
                case FeaturedProductType.DELETE_DISPLAY:
                    ((FeaturedProductFragment) fragment).showOtherActionDialog();
                    break;
                default:
                    super.onBackPressed();
                    break;
            }
        }
    }
}

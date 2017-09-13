package com.tokopedia.seller.goldmerchant.featured.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
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

    @Inject
    SessionHandler sessionHandler;

    @Inject
    FeaturedProductApi featuredProductApi;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getApplication() != null && getApplication() instanceof SellerModuleRouter){
            DaggerFeaturedProductComponent
                    .builder()
                    .goldMerchantComponent(((SellerModuleRouter) getApplication()).getGoldMerchantComponent())
                    .build().inject(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        featuredProductApi.getFeaturedProduct(sessionHandler.getShopID())
                .flatMap(new Func1<Response<FeaturedProductGETModel>, Observable<Response<FeaturedProductGETModel>>>() {
                    @Override
                    public Observable<Response<FeaturedProductGETModel>> call(Response<FeaturedProductGETModel> featuredProductGETModelResponse) {

                        PostFeaturedProductModel postFeaturedProductModel = new PostFeaturedProductModel();
                        postFeaturedProductModel.setShopId(sessionHandler.getShopID());
                        ArrayList<PostFeaturedProductModel.ItemsFeatured> itemsFeatureds = new ArrayList<>();
                        List<FeaturedProductGETModel.Datum> data = featuredProductGETModelResponse.body().getData();
                        int i=1, size = data.size();
                        for (FeaturedProductGETModel.Datum datum : data) {
                            PostFeaturedProductModel.ItemsFeatured itemsFeatured
                                    = new PostFeaturedProductModel.ItemsFeatured();
                            itemsFeatured.setProductId(datum.getProductId());
                            if(i > size){
                                itemsFeatured.setOrder(1);
                            }else{
                                itemsFeatured.setOrder(++i);
                            }
                            itemsFeatured.setType(1);

                            itemsFeatureds.add(itemsFeatured);
                            i++;
                        }

                        postFeaturedProductModel.setItemsFeatured(itemsFeatureds);

                        Response<FeaturedProductPOSTModel> first = featuredProductApi.postFeaturedProduct(postFeaturedProductModel).toBlocking().first();
                        Log.d(TAG, first.toString());


                        return Observable.just(featuredProductGETModelResponse);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<FeaturedProductGETModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.toString());
                    }

                    @Override
                    public void onNext(Response<FeaturedProductGETModel> featuredProductGETModelResponse) {
                        Log.d(TAG, featuredProductGETModelResponse.toString());
                    }
                });
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
                    ((FeaturedProductFragment) fragment).setFeaturedProductType(FeaturedProductType.DEFAULT_DISPLAY);
                    ((FeaturedProductFragment) fragment).showOtherActionDialog();
                    if(getSupportActionBar()!= null) getSupportActionBar().setTitle(R.string.featured_product_activity_title);
                    break;
                default:
                    super.onBackPressed();
                    break;
            }
        }
    }
}

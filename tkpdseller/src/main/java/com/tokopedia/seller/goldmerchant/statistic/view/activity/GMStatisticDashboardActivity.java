package com.tokopedia.seller.goldmerchant.statistic.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.gmstat.utils.DaggerInjectorListener;
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;
import com.tokopedia.seller.goldmerchant.statistic.di.component.DaggerGMStatisticDashboardComponent;
import com.tokopedia.seller.goldmerchant.statistic.di.module.GMStatisticModule;
import com.tokopedia.seller.goldmerchant.statistic.view.fragment.GMStatisticDashboardFragment;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class GMStatisticDashboardActivity extends DrawerPresenterActivity
        implements SessionHandler.onLogoutListener, DaggerInjectorListener, HasComponent<GoldMerchantComponent> {
    public static final String IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT";
    public static final String SHOP_ID = "SHOP_ID";

    private boolean isGoldMerchant;
    private String shopId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupVar() {
        super.setupVar();
        fetchIntent(getIntent().getExtras());
        inject();
        //TODO preserve cache, uncomment below
//        gmStatClearCacheUseCase.execute(null, new Subscriber<Boolean>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.i("GMStat", "Failed clear GM cache");
//            }
//
//            @Override
//            public void onNext(Boolean aBoolean) {
//                Log.i("GMStat", "Success clear GM cache");
//            }
//        });
    }

    @Override
    protected void setupVar(Bundle savedInstanceState) {
        super.setupVar(savedInstanceState);
        fetchSaveInstance(savedInstanceState);
        inject();
    }

    @Override
    public void inject() {
        DaggerGMStatisticDashboardComponent
                .builder()
                .goldMerchantComponent(getComponent())
                .gMStatisticModule(new GMStatisticModule())
                .build().inject(this);
    }

    private void fetchSaveInstance(Bundle savedInstanceState) {
        if (savedInstanceState == null)
            return;

        isGoldMerchant = savedInstanceState.getBoolean(IS_GOLD_MERCHANT, false);
        shopId = savedInstanceState.getString(SHOP_ID);
    }

    private void fetchIntent(Bundle extras) {
        if (extras != null) {
            isGoldMerchant = extras.getBoolean(IS_GOLD_MERCHANT, false);
            shopId = extras.getString(SHOP_ID, "");

            //[START] This is staging version
//            isGoldMerchant = true;
//            shopId = shop_id_staging+"";
            //[END] This is staging version
        }
    }

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.SELLER_GM_STAT;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.content_gmstat;
    }

    @Override
    protected void initViews() {
        super.initViews();

        if (!isAfterRotate) {
            inflateNewFragment(new GMStatisticDashboardFragment());
        }
    }

    private void inflateNewFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_gmstat_fragment_container, fragment, GMStatisticDashboardFragment.TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_GOLD_MERCHANT, isGoldMerchant);
        outState.putString(SHOP_ID, shopId);
    }

    //[START] unused methods
    @Override
    protected void setupURIPass(Uri data) {
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
    }

    @Override
    protected void initialPresenter() {
    }

    @Override
    protected void setViewListener() {
    }

    @Override
    protected void initVar() {
    }

    @Override
    protected void setActionVar() {
    }

    @Override
    public String getScreenName() {
        return AppScreen.STATISTIC_PAGE;
    }

    @Override
    public GoldMerchantComponent getComponent() {
        return ((SellerModuleRouter) getApplication()).getGoldMerchantComponent(getActivityModule());
    }
}

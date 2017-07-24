package com.tokopedia.seller.goldmerchant.statistic.view.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.seller.gmstat.di.component.DaggerGMStatComponent;
import com.tokopedia.seller.gmstat.di.component.GMStatComponent;
import com.tokopedia.seller.gmstat.presenters.GMStat;
import com.tokopedia.seller.gmstat.utils.DaggerInjectorListener;
import com.tokopedia.seller.gmstat.utils.GMStatNetworkController;
import com.tokopedia.seller.goldmerchant.statistic.view.fragment.GMStatisticDashboardFragment;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatClearCacheUseCase;

import javax.inject.Inject;

import static com.tokopedia.seller.gmstat.views.GMStatHeaderViewHelper.MOVE_TO_SET_DATE;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class GMStatisticDashboardActivity extends DrawerPresenterActivity
        implements GMStat, SessionHandler.onLogoutListener, DaggerInjectorListener {
    public static final String IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT";
    public static final String SHOP_ID = "SHOP_ID";

    @Inject
    GMStatNetworkController gmStatNetworkController;
    @Inject
    ImageHandler imageHandler;

    @Inject
    GMStatClearCacheUseCase gmStatClearCacheUseCase;

    private boolean isGoldMerchant;
    private String shopId;
    private GMStatComponent gmstatComponent;

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
        gmstatComponent = DaggerGMStatComponent.builder()
                .appComponent(getApplicationComponent())
                .build();
        gmstatComponent.inject(this);
    }

    private void fetchSaveInstance(Bundle savedInstanceState) {
        if(savedInstanceState == null)
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

    @Override
    public GMStatNetworkController getGmStatNetworkController() {
        return gmStatNetworkController;
    }

    @Override
    public ImageHandler getImageHandler() {
        return imageHandler;
    }

    @Override
    public boolean isGoldMerchant() {
        return isGoldMerchant;
    }

    @Override
    public String getShopId() {
        return shopId;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is the same
        if (requestCode == MOVE_TO_SET_DATE) {
            if (data != null) {
                long sDate = data.getLongExtra(DatePickerConstant.EXTRA_START_DATE, -1);
                long eDate = data.getLongExtra(DatePickerConstant.EXTRA_END_DATE, -1);
                int lastSelection = data.getIntExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, 1);
                int selectionType = data.getIntExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, DatePickerConstant.SELECTION_TYPE_PERIOD_DATE);
                if (sDate != -1 && eDate != -1) {
                    Fragment fragment = getFragmentManager().findFragmentById(R.id.content_gmstat_fragment_container);
                    if (fragment != null && fragment instanceof GMStatisticDashboardFragment) {
                        ((GMStatisticDashboardFragment) fragment).fetchData(sDate, eDate, lastSelection, selectionType);
                    }
                }
            }
        }
    }

    private void inflateNewFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
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
    //[END] unused methods
}

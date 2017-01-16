package com.tokopedia.seller.gmstat.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.core.welcome.WelcomeActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.utils.DaggerInjectorListener;
import com.tokopedia.seller.gmstat.presenters.GMStat;
import com.tokopedia.seller.gmstat.utils.GMStatNetworkController;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tokopedia.seller.gmstat.views.GMStatHeaderViewHelper.MOVE_TO_SET_DATE;
import static com.tokopedia.seller.gmstat.views.SetDateActivity.PERIOD_TYPE;
import static com.tokopedia.seller.gmstat.views.SetDateActivity.SELECTION_PERIOD;
import static com.tokopedia.seller.gmstat.views.SetDateActivity.SELECTION_TYPE;
import static com.tokopedia.seller.gmstat.views.SetDateFragment.END_DATE;
import static com.tokopedia.seller.gmstat.views.SetDateFragment.START_DATE;

public abstract class GMStatActivity extends AppCompatActivity implements GMStat, SessionHandler.onLogoutListener, DaggerInjectorListener {

    protected GMStatNetworkController gmStatNetworkController;

    protected ImageHandler imageHandler;

    DrawerLayout drawerLayout;

    protected Toolbar toolbar;

    String titleActivityGMStat;

    int green600;

    int tkpdMainGreenColor;

    public static final String IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT";
    public static final String SHOP_ID = "SHOP_ID";
    boolean isAfterRotate = false;
    private boolean isGoldMerchant;
    private String shopId;

    private final long shop_id_staging = 560900;
//    private final long shop_id_staging = 67726;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isAfterRotate){
            fetchIntent(getIntent().getExtras());
        }
        inject();
        setContentView(R.layout.activity_gmstat);
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(green600);
        }
        toolbar.setTitle(titleActivityGMStat);
        toolbar.setBackgroundColor(tkpdMainGreenColor);
        setSupportActionBar(toolbar);
        initDrawer();

        isAfterRotate = savedInstanceState != null;
    }

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_nav);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        titleActivityGMStat = getString(R.string.title_activity_gmstat);

        green600 = ResourcesCompat.getColor(getResources(), R.color.green_600, null);

        tkpdMainGreenColor = ResourcesCompat.getColor(getResources(), R.color.tkpd_main_green, null);
    }

    protected abstract void initDrawer();

    private void fetchIntent(Bundle extras) {
        if(extras != null){
            isGoldMerchant = extras.getBoolean(IS_GOLD_MERCHANT, false);
            shopId = extras.getString(SHOP_ID, "");

            //[START] This is staging version
//            isGoldMerchant = true;
//            shopId = shop_id_staging+"";
            //[END] This is staging version
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is the same
        if(requestCode==MOVE_TO_SET_DATE){
            if(data != null){
                long sDate = data.getLongExtra(START_DATE, -1);
                long eDate = data.getLongExtra(END_DATE, -1);
                int lastSelection = data.getIntExtra(SELECTION_PERIOD, 1);
                int selectionType = data.getIntExtra(SELECTION_TYPE, PERIOD_TYPE);
                if(sDate != -1 && eDate != -1){
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
                    if(fragment != null && fragment instanceof GMStatActivityFragment){
                        ((GMStatActivityFragment)fragment).fetchData(sDate, eDate, lastSelection, selectionType);
                    }
                }
            }
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
    public void onLogout(Boolean success) {
        if (success) {
            finish();
            Intent intent;
            if (GlobalConfig.isSellerApp()) {
                intent = new Intent(this, WelcomeActivity.class);
            } else {
                intent = HomeRouter.getHomeActivity(this);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}

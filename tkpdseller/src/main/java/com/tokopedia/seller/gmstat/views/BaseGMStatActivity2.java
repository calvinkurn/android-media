package com.tokopedia.seller.gmstat.views;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.presenters.GMStat;
import com.tokopedia.seller.gmstat.utils.DaggerInjectorListener;
import com.tokopedia.seller.gmstat.utils.GMStatNetworkController;

import static com.tokopedia.seller.gmstat.views.GMStatHeaderViewHelper.MOVE_TO_SET_DATE;
import static com.tokopedia.seller.gmstat.views.SetDateActivity.PERIOD_TYPE;
import static com.tokopedia.seller.gmstat.views.SetDateActivity.SELECTION_PERIOD;
import static com.tokopedia.seller.gmstat.views.SetDateActivity.SELECTION_TYPE;
import static com.tokopedia.seller.gmstat.views.SetDateFragment.END_DATE;
import static com.tokopedia.seller.gmstat.views.SetDateFragment.START_DATE;

/**
 * Created by normansyahputa on 1/18/17.
 */

public abstract class BaseGMStatActivity2 extends DrawerPresenterActivity
    implements GMStat, SessionHandler.onLogoutListener, DaggerInjectorListener
{
    protected GMStatNetworkController gmStatNetworkController;

    protected ImageHandler imageHandler;

    String titleActivityGMStat;

    int green600;

    int tkpdMainGreenColor;

    public static final String IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT";
    public static final String SHOP_ID = "SHOP_ID";
    private boolean isGoldMerchant;
    private String shopId;

    boolean isAfterRotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupVar() {
        super.setupVar();
        fetchIntent(getIntent().getExtras());
        inject();
    }

    /**
     * @return true if first time, false if already in foreground.
     */
    @Override
    protected boolean isAfterRotate(Bundle savedInstanceState) {
        isAfterRotate = super.isAfterRotate(savedInstanceState);
        return !isAfterRotate;
    }

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
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.SELLER_GM_STAT;
    }

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
    protected int getLayoutId() {
        return R.layout.content_gmstat;
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
        return null;
    }

    @Override
    protected void initViews() {
        super.initViews();
        titleActivityGMStat = getString(R.string.title_activity_gmstat);

        green600 = ResourcesCompat.getColor(getResources(), R.color.green_600, null);

        tkpdMainGreenColor = ResourcesCompat.getColor(getResources(), R.color.tkpd_main_green, null);
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
        if(requestCode==MOVE_TO_SET_DATE){
            if(data != null){
                long sDate = data.getLongExtra(START_DATE, -1);
                long eDate = data.getLongExtra(END_DATE, -1);
                int lastSelection = data.getIntExtra(SELECTION_PERIOD, 1);
                int selectionType = data.getIntExtra(SELECTION_TYPE, PERIOD_TYPE);
                if(sDate != -1 && eDate != -1){
                    Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment);
                    if(fragment != null && fragment instanceof GMStatActivityFragment){
                        ((GMStatActivityFragment)fragment).fetchData(sDate, eDate, lastSelection, selectionType);
                    }
                }
            }
        }
    }

    private void inflateNewFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_gmstat_fragment_container, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isAfterRotate)
            inflateNewFragment(new GMStatActivityFragment(), GMStatActivityFragment.TAG);
    }
}

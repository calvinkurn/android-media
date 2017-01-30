package com.tokopedia.seller.gmstat.views;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.seller.R;

import static com.tokopedia.seller.gmstat.views.BaseGMStatActivity.IS_GOLD_MERCHANT;
import static com.tokopedia.seller.gmstat.views.GMStatHeaderViewHelper.MOVE_TO_SET_DATE;
import static com.tokopedia.seller.gmstat.views.SetDateConstant.CUSTOM_END_DATE;
import static com.tokopedia.seller.gmstat.views.SetDateConstant.CUSTOM_START_DATE;
import static com.tokopedia.seller.gmstat.views.SetDateConstant.PERIOD_TYPE;
import static com.tokopedia.seller.gmstat.views.SetDateConstant.SELECTION_PERIOD;
import static com.tokopedia.seller.gmstat.views.SetDateConstant.SELECTION_TYPE;

/**
 * Created by normansyahputa on 1/18/17.
 */
public class SetDateActivity extends BasePresenterActivity implements SetDateFragment.SetDate {

    private boolean isGoldMerchant;
    private boolean isAfterRotate;
    private int selectionPeriod;
    private int selectionType;
    private long sDate = -1, eDate = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.content_set_date;
    }


    @Override
    protected void initView() {
        if (!isAfterRotate)
            inflateNewFragment(new SetDateFragment(), GMStatActivityFragment.TAG);
    }

    /**
     * @return true if first time, false if already in foreground.
     */
    @Override
    protected boolean isAfterRotate(Bundle savedInstanceState) {
        isAfterRotate = super.isAfterRotate(savedInstanceState);
        return !isAfterRotate;
    }

    @Override
    protected void setupVar() {
        super.setupVar();
        fetchIntent(getIntent().getExtras());
    }

    private void fetchIntent(Bundle extras) {
        if (extras != null) {
            isGoldMerchant = extras.getBoolean(IS_GOLD_MERCHANT, false);
            selectionPeriod = extras.getInt(SELECTION_PERIOD, 1);
            selectionType = extras.getInt(SELECTION_TYPE, PERIOD_TYPE);
            sDate = extras.getLong(CUSTOM_START_DATE, -1);
            eDate = extras.getLong(CUSTOM_END_DATE, -1);
        }
    }

    @Override
    public void returnStartAndEndDate(long startDate, long endDate, int lastSelection, int selectionType) {
        Intent intent = new Intent();
        intent.putExtra(SetDateFragment.START_DATE, startDate);
        intent.putExtra(SetDateFragment.END_DATE, endDate);
        if (lastSelection < 0) {
            lastSelection = selectionPeriod;
        }
        intent.putExtra(SELECTION_PERIOD, lastSelection);
        intent.putExtra(SELECTION_TYPE, selectionType);
        setResult(MOVE_TO_SET_DATE, intent);
        finish();
    }

    @Override
    public boolean isGMStat() {
        return isGoldMerchant;
    }

    @Override
    public int selectionPeriod() {
        return selectionPeriod;
    }

    @Override
    public int selectionType() {
        return selectionType;
    }

    @Override
    public long sDate() {
        return sDate;
    }

    @Override
    public long eDate() {
        return eDate;
    }

    private void inflateNewFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.set_date_fragment_container, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
        return null;
    }
    //[END] unused methods
}

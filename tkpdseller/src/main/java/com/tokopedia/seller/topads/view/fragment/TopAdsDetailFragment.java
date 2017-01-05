package com.tokopedia.seller.topads.view.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.presenter.TopAdsDetailPresenter;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailViewListener;
import com.tokopedia.seller.topads.view.widget.TopAdsLabelSwitch;
import com.tokopedia.seller.topads.view.widget.TopAdsLabelView;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class TopAdsDetailFragment<T extends TopAdsDetailPresenter> extends BasePresenterFragment<T> implements TopAdsDetailViewListener {

    @BindView(R2.id.container_detail_topads)
    LinearLayout containerDetail;

    @BindView(R2.id.name)
    TopAdsLabelView name;

    @BindView(R2.id.status)
    TopAdsLabelSwitch status;

    @BindView(R2.id.max_bid)
    TopAdsLabelView maxBid;

    @BindView(R2.id.avg_cost)
    TopAdsLabelView avgCost;

    @BindView(R2.id.start)
    TopAdsLabelView start;

    @BindView(R2.id.end)
    TopAdsLabelView end;

    @BindView(R2.id.daily_budget)
    TopAdsLabelView dailyBudget;

    @BindView(R2.id.sent)
    TopAdsLabelView sent;

    @BindView(R2.id.impr)
    TopAdsLabelView impr;

    @BindView(R2.id.click)
    TopAdsLabelView click;

    @BindView(R2.id.ctr)
    TopAdsLabelView ctr;

    @BindView(R2.id.favorite)
    TopAdsLabelView favorite;
    private ProgressDialog progressDialog;

    public TopAdsDetailFragment() {
        // Required empty public constructor
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle bundle) {

    }

    @Override
    public void onRestoreState(Bundle bundle) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.promo_topads_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle bundle) {

    }

    @Override
    protected void initView(View view) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void dismissProgress() {
        progressDialog.dismiss();
    }

    protected void setData(Ad ad){
        name.setValue(ad.getName());
        status.setValue(ad.getStatus() == 1 ? true:false);
        maxBid.setValue(ad.getPriceBidFmt());
        avgCost.setValue(ad.getStatTotalSpent());
        start.setValue(ad.getStartDate() + " - " + ad.getStartTime());
        end.setValue(ad.getEndDate() + " - " + ad.getEndTime());
        dailyBudget.setValue(ad.getPriceDailyFmt());
        sent.setValue(ad.getPriceDailySpentFmt());
        impr.setValue(ad.getStatTotalImpression());
        click.setValue(ad.getStatTotalClick());
        ctr.setValue(ad.getStatTotalCtr());
        favorite.setValue(ad.getStatTotalConversion());
    }

}

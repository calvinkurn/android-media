package com.tokopedia.seller.topads.view.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.presenter.TopAdsDetailPresenter;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailViewListener;
import com.tokopedia.seller.topads.view.widget.TopAdsLabelSwitch;
import com.tokopedia.seller.topads.view.widget.TopAdsLabelView;

import java.util.Date;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class TopAdsDetailFragment<T extends TopAdsDetailPresenter> extends BasePresenterFragment<T> implements TopAdsDetailViewListener, CompoundButton.OnCheckedChangeListener {

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
    private Date startDate;
    private Date endDate;

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
    public void onResume() {
        super.onResume();
        if (presenter.isDateUpdated(startDate, endDate)) {
            startDate = presenter.getStartDate();
            endDate = presenter.getEndDate();
            loadData();
        }
    }

    protected void loadData() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(presenter.getRangeDateFormat(startDate, endDate));
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void dismissProgress() {
        progressDialog.dismiss();
    }

    protected void loadAdDetail(Ad ad){
        name.setContent(ad.getName());
        switch (ad.getStatus()) {
            case TopAdsConstant.STATUS_AD_ACTIVE:
            case TopAdsConstant.STATUS_AD_NOT_SENT:
                status.setChecked(true);
                break;
            default:
                status.setChecked(false);
                break;
        }
        status.setSwitchStatusText(ad.getStatusDesc());
        maxBid.setContent(ad.getPriceBidFmt());
        avgCost.setContent(ad.getStatTotalSpent());
        start.setContent(ad.getStartDate() + " - " + ad.getStartTime());
        end.setContent(ad.getEndDate() + " - " + ad.getEndTime());
        dailyBudget.setContent(ad.getPriceDailyFmt());
        sent.setContent(ad.getPriceDailySpentFmt());
        impr.setContent(ad.getStatTotalImpression());
        click.setContent(ad.getStatTotalClick());
        ctr.setContent(ad.getStatTotalCtr());
        favorite.setContent(ad.getStatTotalConversion());
        status.setListenerValue(this);
    }
}
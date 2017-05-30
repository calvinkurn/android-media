package com.tokopedia.seller.topads.keyword.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.widget.DateLabelView;
import com.tokopedia.seller.lib.widget.LabelSwitch;
import com.tokopedia.seller.lib.widget.LabelView;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.data.model.data.Ad;
import com.tokopedia.seller.topads.data.model.data.ProductAd;
import com.tokopedia.seller.topads.keyword.di.component.DaggerTopAdsKeywordDetailComponent;
import com.tokopedia.seller.topads.keyword.di.module.TopAdsKeywordDetailModule;
import com.tokopedia.seller.topads.keyword.view.listener.TopAdsKeywordDetailViewListener;
import com.tokopedia.seller.topads.keyword.view.presenter.TopadsKeywordDetailPresenter;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailViewListener;
import com.tokopedia.seller.topads.view.presenter.TopAdsDatePickerPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsDatePickerPresenterImpl;

import javax.inject.Inject;

import static com.tokopedia.core.network.NetworkErrorHelper.createSnackbarWithAction;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

public class TopAdsKeywordDetailFragment extends TopAdsDatePickerFragment implements TopAdsKeywordDetailViewListener {

    protected LabelView keywordName;
    protected DateLabelView dateLabelView;

    protected LabelView priceAndSchedule;
    protected LabelView name;
    private LabelSwitch status;
    private LabelView maxBid;
    private LabelView avgCost;
    protected LabelView start;
    protected LabelView end;
    protected LabelView dailyBudget;
    private LabelView sent;
    private LabelView impr;
    private LabelView click;
    private LabelView ctr;
    protected LabelView favorite;

    private SwipeToRefresh swipeToRefresh;
    protected ProgressDialog progressDialog;
    private SnackbarRetry snackbarRetry;

    protected Ad ad;
    protected String adId;

    @Inject
    TopadsKeywordDetailPresenter topadsKeywordDetailPresenter;

    public static Fragment createInstance(Ad ad, String adId) {
        Fragment fragment = new TopAdsKeywordDetailFragment();
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(TopAdsExtraConstant.EXTRA_AD, ad);
//        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
//        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected TopAdsDatePickerPresenter getDatePickerPresenter() {
        return new TopAdsDatePickerPresenterImpl(getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            setupArguments(savedInstanceState);
        }
    }

    protected void setupArguments(Bundle bundle) {
        ad = bundle.getParcelable(TopAdsExtraConstant.EXTRA_AD);
        adId = bundle.getString(TopAdsExtraConstant.EXTRA_AD_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_ads_keyword_detail, container);
        keywordName = (LabelView) view.findViewById(R.id.keyword);
        dateLabelView = (DateLabelView) view.findViewById(R.id.date_label_view);

        name = (LabelView) view.findViewById(R.id.name);
        status = (LabelSwitch) view.findViewById(R.id.status);
        maxBid = (LabelView) view.findViewById(R.id.max_bid);
        avgCost = (LabelView) view.findViewById(R.id.avg_cost);
        start = (LabelView) view.findViewById(R.id.start);
        end = (LabelView) view.findViewById(R.id.end);
        dailyBudget = (LabelView) view.findViewById(R.id.daily_budget);
        sent = (LabelView) view.findViewById(R.id.sent);
        impr = (LabelView) view.findViewById(R.id.impr);
        click = (LabelView) view.findViewById(R.id.click);
        ctr = (LabelView) view.findViewById(R.id.ctr);
        favorite = (LabelView) view.findViewById(R.id.favorite);
        swipeToRefresh = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
        priceAndSchedule = (LabelView) view.findViewById(R.id.title_price_and_schedule);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new RefreshHandler(getActivity(), getView(), new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                loadData();
            }
        });
        snackbarRetry = createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                refreshAd();
            }
        });
        snackbarRetry.setColorActionRetry(ContextCompat.getColor(getActivity(), R.color.green_400));
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        setDatePresent();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_top_ads_keyword_detail, menu);
        menu.findItem(R.id.menu_edit).setVisible(ad != null);
        menu.findItem(R.id.menu_delete).setVisible(ad != null);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_edit) {
            editAd();
            return true;
        } else if (item.getItemId() == R.id.menu_delete) {
            showDeleteConfirmation(getString(R.string.title_delete_keyword_topads), getString(R.string.label_confirm_delete_keyword_topads));
        }
        return super.onOptionsItemSelected(item);
    }

    protected void loadAdDetail(Ad ad) {
        name.setContent(ad.getName());
        switch (ad.getStatus()) {
            case TopAdsConstant.STATUS_AD_ACTIVE:
            case TopAdsConstant.STATUS_AD_NOT_SENT:
                setStatusSwitch(true);
                break;
            default:
                setStatusSwitch(false);
                break;
        }
        status.setSwitchStatusText(ad.getStatusDesc());
        maxBid.setContent(getString(R.string.top_ads_bid_format_text, ad.getPriceBidFmt(), ad.getLabelPerClick()));
        avgCost.setContent(ad.getStatAvgClick());
        start.setContent(ad.getStartDate() + " - " + ad.getStartTime());
        if (TextUtils.isEmpty(ad.getEndTime())) {
            end.setContent(ad.getEndDate());
        } else {
            end.setContent(getString(R.string.top_ads_range_date_text, ad.getEndDate(), ad.getEndTime()));
        }
        if(TextUtils.isEmpty(ad.getPriceDailySpentFmt())) {
            dailyBudget.setContent(ad.getPriceDailyFmt());
        }else{
            dailyBudget.setContent(getString(R.string.topads_format_daily_budget, ad.getPriceDailySpentFmt(), ad.getPriceDailyFmt()));
        }
        sent.setContent(ad.getStatTotalSpent());
        impr.setContent(ad.getStatTotalImpression());
        click.setContent(ad.getStatTotalClick());
        ctr.setContent(ad.getStatTotalCtr());
        favorite.setContent(ad.getStatTotalConversion());
    }

    protected void showDeleteConfirmation(String title, String content) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        alertDialog.setTitle(title);
        alertDialog.setMessage(content);
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAd();
            }
        });
        alertDialog.setNegativeButton(R.string.No, null);
        alertDialog.show();
    }

    private void editAd() {

    }

    private void deleteAd() {
        showLoading();
        topadsKeywordDetailPresenter.deleteAd(ad.getId());
    }

    @Override
    protected void fetchData() {
        loadData();
    }

    @Override
    protected void loadData() {
        showLoading();
        setDatePresent();
        if (ad != null) {
            onAdLoaded(ad);
        } else {
            refreshAd();
        }
    }

    private void setDatePresent() {
        dateLabelView.setDate(getDatePickerPresenter().getStartDate(), getDatePickerPresenter().getEndDate());
    }

    private void refreshAd() {
        topadsKeywordDetailPresenter.refreshAd(getDatePickerPresenter().getStartDate(),
                getDatePickerPresenter().getEndDate(), ad.getId());
    }

    private void showLoading() {
        if (!swipeToRefresh.isRefreshing()) {
            progressDialog.show();
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DaggerTopAdsKeywordDetailComponent
                .builder()
                .appComponent(getComponent(AppComponent.class))
                .topAdsKeywordDetailModule(new TopAdsKeywordDetailModule())
                .build()
                .inject(this);
    }

    @Override
    public void onAdLoaded(Ad ad) {
        this.ad = ad;
        hideLoading();
        loadAdDetail(ad);
        getActivity().invalidateOptionsMenu();
    }


    protected void setStatusSwitch(boolean checked) {
        status.setSwitchEnabled(true);
        status.setListenerValue(null);
        status.setChecked(checked);
        status.setListenerValue(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    turnOnAd();
                } else {
                    turnOffAd();
                }
            }
        });
    }

    protected void turnOnAd() {
        showLoading();
    }

    protected void turnOffAd() {
        showLoading();
    }

    protected void setResultAdDetailChanged() {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, true);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    private void setResultAdDeleted() {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_DELETED, true);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    private void hideLoading() {
        progressDialog.dismiss();
        swipeToRefresh.setRefreshing(false);
        snackbarRetry.hideRetrySnackbar();
    }

    @Override
    public void onLoadAdError() {
        hideLoading();
        snackbarRetry = createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                refreshAd();
            }
        });
        snackbarRetry.showRetrySnackbar();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onTurnOnAdSuccess() {
        ad = null;
        loadData();
        setResultAdDetailChanged();
        snackbarRetry.hideRetrySnackbar();
    }

    @Override
    public void onTurnOnAdError() {
        setStatusSwitch(!status.isChecked());
        hideLoading();
        snackbarRetry = createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                setStatusSwitch(true);
                turnOnAd();
            }
        });
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void onTurnOffAdSuccess() {
        ad = null;
        loadData();
        setResultAdDetailChanged();
        snackbarRetry.hideRetrySnackbar();
    }

    @Override
    public void onTurnOffAdError() {
        setStatusSwitch(!status.isChecked());
        hideLoading();
        snackbarRetry = createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                setStatusSwitch(false);
                turnOffAd();
            }
        });
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void onDeleteAdSuccess() {
        hideLoading();
        setResultAdDeleted();
        if (isAdded()) {
            getActivity().finish();
        }
    }

    @Override
    public void onDeleteAdError() {
        hideLoading();
        snackbarRetry = createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                deleteAd();
            }
        });
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        topadsKeywordDetailPresenter.unSubscribe();
    }
}

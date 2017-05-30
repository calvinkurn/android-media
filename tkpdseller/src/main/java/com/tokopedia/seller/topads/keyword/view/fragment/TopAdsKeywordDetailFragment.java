package com.tokopedia.seller.topads.keyword.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.tokopedia.seller.topads.keyword.constant.KeywordTypeDef;
import com.tokopedia.seller.topads.keyword.di.component.DaggerTopAdsKeywordDetailComponent;
import com.tokopedia.seller.topads.keyword.di.module.TopAdsKeywordDetailModule;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordEditDetailPositiveActivity;
import com.tokopedia.seller.topads.keyword.view.listener.TopAdsKeywordDetailViewListener;
import com.tokopedia.seller.topads.keyword.view.model.KeywordAd;
import com.tokopedia.seller.topads.keyword.view.presenter.TopadsKeywordDetailPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsDatePickerPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsDatePickerPresenterImpl;

import javax.inject.Inject;

import static com.tokopedia.core.network.NetworkErrorHelper.createSnackbarWithAction;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

public class TopAdsKeywordDetailFragment extends TopAdsDatePickerFragment implements TopAdsKeywordDetailViewListener {

    protected DateLabelView dateLabelView;
    protected LabelView keywordName;
    protected LabelView keywordType;
    private LabelSwitch status;
    protected LabelView priceMax;
    private LabelView maxBid;
    private LabelView avgCost;
    private LabelView sent;
    private LabelView impr;
    private LabelView click;
    private LabelView ctr;
    protected LabelView favorite;

    private SwipeToRefresh swipeToRefresh;
    protected ProgressDialog progressDialog;
    private SnackbarRetry snackbarRetry;

    protected KeywordAd ad;
    protected String adId;

    boolean adStatusChanged = false;

    @Inject
    TopadsKeywordDetailPresenter topadsKeywordDetailPresenter;

    public static Fragment createInstance(KeywordAd ad, String adId) {
        Fragment fragment = new TopAdsKeywordDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_AD, ad);
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected TopAdsDatePickerPresenter getDatePickerPresenter() {
        return new TopAdsDatePickerPresenterImpl(getActivity());
    }

    @Override
    protected void setupArguments(Bundle bundle) {
        ad = bundle.getParcelable(TopAdsExtraConstant.EXTRA_AD);
        adId = bundle.getString(TopAdsExtraConstant.EXTRA_AD_ID);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        keywordName = (LabelView) view.findViewById(R.id.keyword);
        dateLabelView = (DateLabelView) view.findViewById(R.id.date_label_view);
        keywordType = (LabelView) view.findViewById(R.id.name);
        status = (LabelSwitch) view.findViewById(R.id.status);
        maxBid = (LabelView) view.findViewById(R.id.max_bid);
        avgCost = (LabelView) view.findViewById(R.id.avg_cost);
        sent = (LabelView) view.findViewById(R.id.sent);
        impr = (LabelView) view.findViewById(R.id.impr);
        click = (LabelView) view.findViewById(R.id.click);
        ctr = (LabelView) view.findViewById(R.id.ctr);
        favorite = (LabelView) view.findViewById(R.id.favorite);
        swipeToRefresh = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
        priceMax = (LabelView) view.findViewById(R.id.title_price_and_schedule);
        snackbarRetry = createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                refreshAd();
            }
        });
        snackbarRetry.setColorActionRetry(ContextCompat.getColor(getActivity(), R.color.green_400));
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_keyword_detail;
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        new RefreshHandler(getActivity(), getView(), new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                loadData();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // check if the request code is the same
        if (requestCode == TopAdsConstant.REQUEST_CODE_AD_EDIT && intent != null) {
            adStatusChanged = intent.getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adStatusChanged) {
            refreshAd();
            setResultAdDetailChanged();
            adStatusChanged = false;
        }
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void loadAdDetail(KeywordAd ad) {
        keywordName.setContent(ad.getName());
        keywordType.setContent(ad.getkeywordTypeDesc());
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
        TopAdsKeywordEditDetailPositiveActivity.start(getActivity(), ad);
    }

    private void deleteAd() {
        showLoading();
        topadsKeywordDetailPresenter.deleteAd(ad.getId());
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
        if (ad != null) {
            topadsKeywordDetailPresenter.refreshAd(getDatePickerPresenter().getStartDate(),
                    getDatePickerPresenter().getEndDate(), ad.getId(), isPositive());
        }else{
            topadsKeywordDetailPresenter.refreshAd(getDatePickerPresenter().getStartDate(),
                    getDatePickerPresenter().getEndDate(), adId, isPositive());
        }
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
    public void onAdLoaded(KeywordAd ad) {
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

    public int isPositive() {
        return KeywordTypeDef.KEYWORD_TYPE_BROAD;
    }
}

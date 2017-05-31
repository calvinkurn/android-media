package com.tokopedia.seller.topads.keyword.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.CompoundButton;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.widget.DateLabelView;
import com.tokopedia.seller.lib.widget.LabelView;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.keyword.constant.KeywordTypeDef;
import com.tokopedia.seller.topads.keyword.di.component.DaggerTopAdsKeywordDetailComponent;
import com.tokopedia.seller.topads.keyword.di.module.TopAdsKeywordDetailModule;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsKeywordEditDetailPositiveActivity;
import com.tokopedia.seller.topads.keyword.view.listener.TopAdsKeywordDetailViewListener;
import com.tokopedia.seller.topads.keyword.view.model.KeywordAd;

import static com.tokopedia.core.network.NetworkErrorHelper.createSnackbarWithAction;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

public class TopAdsKeywordDetailFragment extends TopAdsBaseKeywordDetailFragment implements TopAdsKeywordDetailViewListener {

    private DateLabelView dateLabelView;
    private LabelView maxBid;
    private LabelView avgCost;
    private LabelView sent;
    private LabelView impr;
    private LabelView click;
    private LabelView ctr;
    private LabelView favorite;

    public static Fragment createInstance(KeywordAd ad, String adId) {
        Fragment fragment = new TopAdsKeywordDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_AD, ad);
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void initView(View view) {
        super.initView(view);
        dateLabelView = (DateLabelView) view.findViewById(R.id.date_label_view);
        maxBid = (LabelView) view.findViewById(R.id.max_bid);
        avgCost = (LabelView) view.findViewById(R.id.avg_cost);
        sent = (LabelView) view.findViewById(R.id.sent);
        impr = (LabelView) view.findViewById(R.id.impr);
        click = (LabelView) view.findViewById(R.id.click);
        ctr = (LabelView) view.findViewById(R.id.ctr);
        favorite = (LabelView) view.findViewById(R.id.favorite);
        dateLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_keyword_detail;
    }

    protected void loadAdDetail(KeywordAd ad) {
        super.loadAdDetail(ad);
        maxBid.setContent(getString(R.string.top_ads_bid_format_text, ad.getPriceBidFmt(), ad.getLabelPerClick()));
        avgCost.setContent(ad.getStatAvgClick());
        sent.setContent(ad.getStatTotalSpent());
        impr.setContent(ad.getStatTotalImpression());
        click.setContent(ad.getStatTotalClick());
        ctr.setContent(ad.getStatTotalCtr());
        favorite.setContent(ad.getStatTotalConversion());
    }

    @Override
    protected void editAd() {
        TopAdsKeywordEditDetailPositiveActivity.start(getActivity(), ad);
    }

    @Override
    protected void loadData() {
        super.loadData();
        setDatePresent();
    }

    private void setDatePresent() {
        dateLabelView.setDate(getDatePickerPresenter().getStartDate(), getDatePickerPresenter().getEndDate());
    }

    @Override
    protected int isPositive() {
        return KeywordTypeDef.KEYWORD_TYPE_BROAD;
    }
}

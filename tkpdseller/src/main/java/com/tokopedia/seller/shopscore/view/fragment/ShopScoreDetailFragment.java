package com.tokopedia.seller.shopscore.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shopscore.di.ShopScoreDetailDependencyInjector;
import com.tokopedia.seller.shopscore.view.model.ShopScoreDetailItemViewModel;
import com.tokopedia.seller.shopscore.view.model.ShopScoreDetailStateEnum;
import com.tokopedia.seller.shopscore.view.model.ShopScoreDetailSummaryViewModel;
import com.tokopedia.seller.shopscore.view.presenter.ShopScoreDetailPresenterImpl;
import com.tokopedia.seller.shopscore.view.recyclerview.ShopScoreDetailAdapter;

import java.util.List;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailFragment extends BaseDaggerFragment implements ShopScoreDetailView {
    public static final String TAG = "ShopScoreDetail";
    private ShopScoreDetailFragmentCallback callback;
    private ShopScoreDetailAdapter adapter;
    private ShopScoreDetailPresenterImpl presenter;
    private TextView summaryDetailTitle;
    private TextView descriptionGoldBadge;
    private TextView buttonGoToGmSubscribe;
    private ImageView imageViewGoldBadge;
    private View.OnClickListener goToGmSubscribe = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callback.goToGmSubscribe();
        }
    };
    private View.OnClickListener goToSellerCenter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callback.goToSellerCenter();
        }
    };



    public static Fragment createFragment() {
        return new ShopScoreDetailFragment();
    }

    @Override
    protected void initInjector() {
        presenter = ShopScoreDetailDependencyInjector.getPresenter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_shop_score_detail, container, false);

        setupRecyclerView(parentView);

        summaryDetailTitle = (TextView) parentView.findViewById(R.id.text_view_shop_score_summary_detail_tittle);
        descriptionGoldBadge = (TextView) parentView.findViewById(R.id.description_shop_score_detail_gold_badge_info);
        imageViewGoldBadge = (ImageView) parentView.findViewById(R.id.image_view_gold_badge);

        buttonGoToGmSubscribe = (TextView) parentView.findViewById(R.id.button_go_to_gm_subscribe);
        buttonGoToGmSubscribe.setOnClickListener(goToGmSubscribe);
        parentView.findViewById(R.id.button_go_to_seller_center).setOnClickListener(goToSellerCenter);

        presenter.attachView(this);
        presenter.getShopScoreDetail();
        return parentView;
    }

    private void setupRecyclerView(View parentView) {
        RecyclerView recyclerView = (RecyclerView) parentView.findViewById(R.id.recycler_view_shop_score_detail);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ShopScoreDetailAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShopScoreDetailFragmentCallback) {
            this.callback = (ShopScoreDetailFragmentCallback) context;
        } else {
            throw new RuntimeException("Please implement ShopScoreDetailFragmentCallback in the Activity");
        }
    }


    @Override
    protected String getScreenName() {
        return null;
    }


    @Override
    public void renderShopScoreDetail(List<ShopScoreDetailItemViewModel> viewModel) {
        adapter.updateData(viewModel);
    }

    @Override
    public void renderShopScoreSummary(ShopScoreDetailSummaryViewModel viewModel) {
        String stringConcat = buildStringSummary(viewModel);
        summaryDetailTitle.setText(Html.fromHtml(stringConcat));
    }

    @Override
    public void renderShopScoreState(ShopScoreDetailStateEnum shopScoreDetailStateEnum) {
        String description;
        int icon;
        switch (shopScoreDetailStateEnum) {
            case GOLD_MERCHANT_QUALIFIED_BADGE:
                description = getString(R.string.desc_shop_score_gold_merchant_qualified_badge);
                icon = R.drawable.ic_gm_badge_qualified;
                break;
            case GOLD_MERCHANT_NOT_QUALIFIED_BADGE:
                description = getString(R.string.desc_shop_score_gold_merchant_not_qualified_badge);
                icon = R.drawable.ic_gm_badge_not_qualified;
                break;
            case NOT_GOLD_MERCHANT_QUALIFIED_BADGE:
                description = getString(R.string.desc_shop_score_not_gold_merchant_qualified_badge);
                icon = R.drawable.ic_gm_badge_qualified;
                buttonGoToGmSubscribe.setVisibility(View.VISIBLE);
                break;
            case NOT_GOLD_MERCHANT_NOT_QUALIFIED_BADGE:
                description = getString(R.string.desc_shop_score_not_gold_merchant_not_qualified_badge);
                icon = R.drawable.ic_gm_badge_not_qualified;
                buttonGoToGmSubscribe.setVisibility(View.VISIBLE);
                break;
            default:
                description = getString(R.string.desc_shop_score_gold_merchant_qualified_badge);
                icon = R.drawable.ic_gm_badge_not_qualified;
                break;
        }
        setShopScoreGoldBadgeState(description, icon);
    }

    private void setShopScoreGoldBadgeState(String description, int icon) {
        descriptionGoldBadge.setText(description);
        imageViewGoldBadge.setImageDrawable(getResources().getDrawable(icon));

    }

    private String buildStringSummary(ShopScoreDetailSummaryViewModel viewModel) {
        return getString(R.string.subtitle_first_shop_score_detail_summary)
                + " "
                + "<font color=#"
                + Integer.toHexString(viewModel.getColor())
                + "><strong>"
                + viewModel.getText()
                + "</strong></font>"
                + " "
                + getString(R.string.subtitle_second_shop_score_detail_summary)
                + " "
                + "<strong>"
                + viewModel.getValue()
                + "</strong>"
                + ".";
    }


}

package com.tokopedia.seller.shopscore.view.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.gm.resource.GMConstant;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shopscore.di.ShopScoreDetailDependencyInjector;
import com.tokopedia.seller.shopscore.view.model.ShopScoreDetailItemViewModel;
import com.tokopedia.seller.shopscore.view.model.ShopScoreDetailStateEnum;
import com.tokopedia.seller.shopscore.view.model.ShopScoreDetailSummaryViewModel;
import com.tokopedia.seller.shopscore.view.presenter.ShopScoreDetailPresenterImpl;
import com.tokopedia.seller.shopscore.view.recyclerview.ShopScoreDetailAdapter;

import java.util.List;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailFragment extends BaseDaggerFragment implements ShopScoreDetailView {
    public static final String TAG = "ShopScoreDetail";
    public static final String IS_NEW_GOLD_BADGE = "is_new_gold_badge";
    public static final String TRUE = "true";
    private ShopScoreDetailFragmentCallback callback;
    private ShopScoreDetailAdapter adapter;
    private ShopScoreDetailPresenterImpl presenter;
    private LinearLayout containerView;
    private TextView summaryDetailTitle;
    private TextView descriptionGoldBadge;
    private TextView buttonGoToGmSubscribe;
    private ImageView imageViewGoldBadge;
    private FrameLayout mainFrame;
    private TkpdProgressDialog progressDialog;
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
    private View.OnClickListener goToCompleteInformation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callback.goToCompleteInformation();
        }
    };
    private NetworkErrorHelper.RetryClickedListener retryLoadShopScore = new NetworkErrorHelper.RetryClickedListener() {
        @Override
        public void onRetryClicked() {
            presenter.getShopScoreDetail();
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

        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.MAIN_PROGRESS);

        containerView = (LinearLayout) parentView.findViewById(R.id.container_view);

        mainFrame = (FrameLayout) parentView.findViewById(R.id.main_frame);

        summaryDetailTitle = (TextView) parentView.findViewById(R.id.text_view_shop_score_summary_detail_tittle);
        descriptionGoldBadge = (TextView) parentView.findViewById(R.id.description_shop_score_detail_gold_badge_info);
        imageViewGoldBadge = (ImageView) parentView.findViewById(R.id.image_view_gold_badge);

        buttonGoToGmSubscribe = (TextView) parentView.findViewById(R.id.button_go_to_gm_subscribe);
        buttonGoToGmSubscribe.setOnClickListener(goToGmSubscribe);
        parentView.findViewById(R.id.button_go_to_seller_center).setOnClickListener(goToSellerCenter);
        parentView.findViewById(R.id.button_go_to_complete_information).setOnClickListener(goToCompleteInformation);

        TextView textView = parentView.findViewById(R.id.description_shop_score_detail_gold_badge_info);
        textView.setText(getString(R.string.description_shop_score_gold_badge_state,
                getString(GMConstant.getGMBadgeTitleResource(getActivity()))));

        presenter.attachView(this);
        presenter.getShopScoreDetail();
        return parentView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe();

    }

    private void setupRecyclerView(View parentView) {
        RecyclerView recyclerView = (RecyclerView) parentView.findViewById(R.id.recycler_view_shop_score_detail);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ShopScoreDetailAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
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
        setNoGravity();
        String stringConcat = buildStringSummary(viewModel);
        summaryDetailTitle.setText(Html.fromHtml(stringConcat));
    }

    @Override
    public void renderShopScoreState(ShopScoreDetailStateEnum shopScoreDetailStateEnum) {
        Drawable icon;
        switch (shopScoreDetailStateEnum) {
            case GOLD_MERCHANT_QUALIFIED_BADGE:
            case GOLD_MERCHANT_NOT_QUALIFIED_BADGE:
                icon = GMConstant.getGMDrawable(getContext());
                break;
            case NOT_GOLD_MERCHANT_QUALIFIED_BADGE:
            case NOT_GOLD_MERCHANT_NOT_QUALIFIED_BADGE:
                icon = GMConstant.getGMRegularBadgeDrawable(getContext());
                buttonGoToGmSubscribe.setVisibility(View.VISIBLE);
                break;
            default:
                icon = GMConstant.getGMRegularBadgeDrawable(getContext());
                break;
        }
        setShopScoreGoldBadgeState(icon);

    }

    @Override
    public void showProgressDialog() {
        progressDialog.showDialog();
        containerView.setVisibility(View.GONE);
    }

    @Override
    public void dismissProgressDialog() {
        progressDialog.dismiss();
        containerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void emptyState() {
        containerView.setVisibility(View.GONE);
        setGravityCenter();
        NetworkErrorHelper
                .showEmptyState(
                        getActivity(),
                        mainFrame,
                        getString(R.string.error_title_shop_score_failed),
                        getString(R.string.error_subtitle_shop_score_failed),
                        getString(R.string.label_try_again),
                        R.drawable.ic_error_network,
                        retryLoadShopScore
                );
    }

    private void setGravityCenter() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER;
        mainFrame.setLayoutParams(params);
    }

    private void setNoGravity() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        mainFrame.setLayoutParams(params);
    }

    private void setShopScoreGoldBadgeState(Drawable icon) {
        imageViewGoldBadge.setImageDrawable(icon);
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

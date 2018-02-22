package com.tokopedia.home.explore.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.core.analytics.HomePageTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.home.SimpleWebViewWithFilePickerActivity;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.home.IHomeRouter;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.VerticalSpaceItemDecoration;
import com.tokopedia.home.explore.domain.model.LayoutRows;
import com.tokopedia.home.explore.listener.CategoryAdapterListener;
import com.tokopedia.home.explore.view.activity.ExploreActivity;
import com.tokopedia.home.explore.view.adapter.ExploreAdapter;
import com.tokopedia.home.explore.view.adapter.TypeFactory;
import com.tokopedia.home.explore.view.adapter.viewmodel.ExploreSectionViewModel;

/**
 * Created by errysuprayogi on 1/26/18.
 */

public class ExploreFragment extends BaseListFragment<Visitable, TypeFactory> implements CategoryAdapterListener {

    public static final String PARAM_TYPE_FRAGMENT = "PARAM_TYPE_FRAGMENT";
    public static final int TYPE_BELI = 0;
    public static final int TYPE_BAYAR = 1;
    public static final int TYPE_PESAN = 2;
    public static final int TYPE_AJUKAN = 3;
    public static final int TYPE_JUAL = 4;

    private int TYPE_FRAGMENT;

    private VerticalSpaceItemDecoration spaceItemDecoration;
    private ExploreSectionViewModel data;

    public static ExploreFragment newInstance(int position) {

        Bundle args = new Bundle();

        ExploreFragment fragment = new ExploreFragment();
        args.putInt(PARAM_TYPE_FRAGMENT, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onItemClicked(Visitable visitable) {

    }

    @Override
    public RecyclerView getRecyclerView(View view) {
        RecyclerView recyclerView = super.getRecyclerView(view);
        spaceItemDecoration = new VerticalSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.margin_card_home), true, 0);
        recyclerView.addItemDecoration(spaceItemDecoration);
        return recyclerView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TYPE_FRAGMENT = getArguments().getInt(PARAM_TYPE_FRAGMENT);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        renderData();
    }

    @Override
    public void loadData(int page) {

    }

    @Override
    protected TypeFactory getAdapterTypeFactory() {
        return new ExploreAdapter(getFragmentManager(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onMarketPlaceItemClicked(LayoutRows data) {
        TrackingUtils.sendMoEngageClickMainCategoryIcon(data.getName());
        ((IHomeRouter) getActivity().getApplication()).openIntermediaryActivity(
                getActivity(),
                String.valueOf(data.getCategoryId()),
                data.getName()
        );
    }

    @Override
    public void onDigitalItemClicked(LayoutRows data) {
        if (((TkpdCoreRouter) getActivity().getApplication())
                .isSupportedDelegateDeepLink(data.getApplinks())) {
            DigitalCategoryDetailPassData passData = new DigitalCategoryDetailPassData.Builder()
                    .appLinks(data.getApplinks())
                    .categoryId(String.valueOf(data.getCategoryId()))
                    .categoryName(data.getName())
                    .url(data.getUrl())
                    .build();
            ((IHomeRouter) getActivity().getApplication()).onDigitalItemClick(getActivity(),
                    passData, data.getApplinks());
        } else {
            onGimickItemClicked(data);
        }
        TrackingUtils.sendMoEngageClickMainCategoryIcon(data.getName());
    }

    @Override
    public void onGimickItemClicked(LayoutRows data) {
        String redirectUrl = data.getUrl();
        if (redirectUrl != null && redirectUrl.length() > 0) {
            String resultGenerateUrl = URLGenerator.generateURLSessionLogin(
                    Uri.encode(redirectUrl), MainApplication.getAppContext());
            openWebViewGimicURL(resultGenerateUrl, data.getUrl(), data.getName());
        }
    }

    @Override
    public void trackingItemGridClick(LayoutRows data) {
        switch (TYPE_FRAGMENT) {
            case TYPE_BELI:
                HomePageTracking.eventClickExplorerItem(
                        HomePageTracking.BELI_INI_ITU_CLICK,
                        String.format("%s - %s", data.getCategoryId(), data.getName())
                );
                break;
            case TYPE_BAYAR:
                HomePageTracking.eventClickExplorerItem(
                        HomePageTracking.BAYAR_INI_ITU_CLICK,
                        data.getName()
                );
                break;
            case TYPE_PESAN:
                HomePageTracking.eventClickExplorerItem(
                        HomePageTracking.PESAN_INI_ITU_CLICK,
                        data.getName()
                );
                break;
            case TYPE_AJUKAN:
                HomePageTracking.eventClickExplorerItem(
                        HomePageTracking.AJUKAN_INI_ITU_CLICK,
                        data.getName()
                );
                break;
        }
    }

    @Override
    public void showNetworkError(String string) {
        if (isAdded() && getActivity() != null) {
            ((ExploreActivity) getActivity()).showNetworkError(string);
        }
    }

    @Override
    public void openShopSetting() {
        String shopId = SessionHandler.getShopID(getContext());
        if (!shopId.equals("0")) {
            HomePageTracking.eventClickEditShop();
            onGoToShopSetting();
        } else {
            HomePageTracking.eventClickOpenShop();
            onGoToCreateShop();
        }
    }

    @Override
    public void onApplinkClicked(LayoutRows data) {
        TkpdCoreRouter router = ((TkpdCoreRouter) getActivity().getApplicationContext());
        if (router.isSupportedDelegateDeepLink(data.getApplinks())) {
            router.actionApplink(getActivity(), data.getApplinks());
        } else {
            openWebViewURL(data.getUrl(), getActivity());
        }
    }

    public void openWebViewURL(String url, Context context) {
        if (url != "" && context != null) {
            Intent intent = new Intent(context, BannerWebView.class);
            intent.putExtra("url", url);
            context.startActivity(intent);
        }
    }

    private void openWebViewGimicURL(String url, String label, String title) {
        if (!url.equals("")) {
            Intent intent = SimpleWebViewWithFilePickerActivity.getIntent(getActivity(), url);
            intent.putExtra(BannerWebView.EXTRA_TITLE, title);
            startActivity(intent);
            UnifyTracking.eventHomeGimmick(label);
        }
    }

    @Override
    public void openShop() {
        if (SessionHandler.isV2Login(getContext())) {
            String shopId = SessionHandler.getShopID(getContext());
            if (!shopId.equals("0")) {
                onGoToShop(shopId);
            } else {
                onGoToCreateShop();
            }
        } else {
            onGoToLogin();
        }
    }

    private void onGoToLogin() {
        Intent intent = ((TkpdCoreRouter) getActivity().getApplication()).getLoginIntent(getContext());
        getActivity().startActivityForResult(intent, ExploreActivity.REQUEST_LOGIN);
    }

    private void onGoToCreateShop() {
        Intent intent = SellerRouter.getActivityShopCreateEdit(getContext());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    private void onGoToShop(String shopId) {
        Intent intent = new Intent(getContext(), ShopInfoActivity.class);
        intent.putExtras(ShopInfoActivity.createBundle(shopId, ""));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    private void onGoToShopSetting() {
        Intent intent = SellerRouter.getActivityManageShop(getActivity());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    @Override
    public void onDigitalMoreClicked() {

    }

    public void setData(ExploreSectionViewModel data) {
        this.data = data;
        renderData();
    }

    private void renderData() {
        if (data != null && isAdded()) {
            renderList(data.getVisitableList());
        }
    }
}

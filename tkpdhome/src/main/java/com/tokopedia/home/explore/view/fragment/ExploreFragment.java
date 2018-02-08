package com.tokopedia.home.explore.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.home.SimpleWebViewActivity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by errysuprayogi on 1/26/18.
 */

public class ExploreFragment extends BaseListFragment<Visitable, TypeFactory> implements CategoryAdapterListener {

    private VerticalSpaceItemDecoration spaceItemDecoration;
    private ExploreSectionViewModel data;

    public static ExploreFragment newInstance() {

        Bundle args = new Bundle();

        ExploreFragment fragment = new ExploreFragment();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        renderList(data.getVisitableList());
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
        ((IHomeRouter) getActivity().getApplication()).openIntermediaryActivity(getActivity(),
                String.valueOf(data.getCategoryId()), data.getName());
        Map<String, String> values = new HashMap<>();
        values.put(getString(R.string.value_category_name), data.getName());
        UnifyTracking.eventHomeCategory(data.getName());
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
    public void showNetworkError(String string) {
        if (isAdded() && getActivity() != null) {
            ((ExploreActivity) getActivity()).showNetworkError(string);
        }
    }

    @Override
    public void openShopSetting() {
        String shopId = SessionHandler.getShopID(getContext());
        if (!shopId.equals("0")) {
            onGoToShop(shopId);
        } else {
            onGoToCreateShop();
        }
    }

    @Override
    public void onApplinkClicked(LayoutRows data) {
        TkpdCoreRouter router = ((TkpdCoreRouter) getActivity().getApplicationContext());
        if(router.isSupportedDelegateDeepLink(data.getApplinks())){
            router.actionApplink(getActivity(), data.getApplinks());
        } else{
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
            Intent intent = SimpleWebViewActivity.getCallingIntent(getActivity(), url);
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
        Intent intentHome = ((TkpdCoreRouter) getActivity().getApplication()).getHomeIntent(getContext());
        intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getActivity().startActivities(new Intent[]{intentHome, intent});
        getActivity().finish();
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

    @Override
    public void onDigitalMoreClicked() {

    }

    public void setData(ExploreSectionViewModel data) {
        this.data = data;
    }
}

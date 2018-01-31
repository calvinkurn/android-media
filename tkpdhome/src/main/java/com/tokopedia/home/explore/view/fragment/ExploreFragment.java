package com.tokopedia.home.explore.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.home.SimpleWebViewActivity;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.home.IHomeRouter;
import com.tokopedia.home.R;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.home.explore.domain.model.CategoryLayoutRowModel;
import com.tokopedia.home.explore.listener.CategoryAdapterListener;
import com.tokopedia.home.explore.view.adapter.ExploreAdapter;
import com.tokopedia.home.explore.view.adapter.TypeFactory;
import com.tokopedia.home.explore.view.adapter.viewmodel.CategoryGridListViewModel;
import com.tokopedia.home.explore.view.presentation.ExploreContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by errysuprayogi on 1/26/18.
 */

public class ExploreFragment extends BaseListFragment<Visitable, TypeFactory> implements ExploreContract.View, CategoryAdapterListener {

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<Visitable> list = new ArrayList<>();
        CategoryGridListViewModel gridListViewModel = new CategoryGridListViewModel();
        gridListViewModel.setTitle("Beli ini itu di Tokopedia");
        ArrayList<CategoryLayoutRowModel> rowModels = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            rowModels.add(createDummyRowModel());
        }
        gridListViewModel.setItemList(rowModels);
        list.add(gridListViewModel);
        renderList(list);
    }

    private CategoryLayoutRowModel createDummyRowModel() {
        CategoryLayoutRowModel rowModel = new CategoryLayoutRowModel();
        rowModel.setType("Marketplace");
        rowModel.setCategoryId(54);
        rowModel.setApplinks("tokopedia://category/54");
        rowModel.setName("Souvenir, Kado & Hadiah");
        rowModel.setImageUrl("https://ecs7.tokopedia.net/img/category/new/v1/icon_kado.png");
        return rowModel;
    }

    @Override
    public void loadData(int page) {

    }

    @Override
    protected TypeFactory getAdapterTypeFactory() {
        return new ExploreAdapter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showNetworkError(String message) {

    }

    @Override
    public void removeNetworkError() {

    }

    @Override
    public void onMarketPlaceItemClicked(CategoryLayoutRowModel data) {
        TrackingUtils.sendMoEngageClickMainCategoryIcon(data.getName());
        ((IHomeRouter) getActivity().getApplication()).openIntermediaryActivity(getActivity(),
                String.valueOf(data.getCategoryId()), data.getName());
        Map<String, String> values = new HashMap<>();
        values.put(getString(R.string.value_category_name), data.getName());
        UnifyTracking.eventHomeCategory(data.getName());
    }

    @Override
    public void onDigitalItemClicked(CategoryLayoutRowModel data) {
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
    public void onGimickItemClicked(CategoryLayoutRowModel data) {
        String redirectUrl = data.getUrl();
        if (redirectUrl != null && redirectUrl.length() > 0) {
            String resultGenerateUrl = URLGenerator.generateURLSessionLogin(
                    Uri.encode(redirectUrl), MainApplication.getAppContext());
            openWebViewGimicURL(resultGenerateUrl, data.getUrl(), data.getName());
        }
    }

    @Override
    public void onApplinkClicked(CategoryLayoutRowModel data) {
        ((TkpdCoreRouter) getActivity().getApplication()).actionApplinkFromActivity(getActivity() ,
                data.getUrl());
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
    public void openWebViewURL(String url, Context context) {
        if (url != "" && context != null) {
            Intent intent = new Intent(context, BannerWebView.class);
            intent.putExtra("url", url);
            context.startActivity(intent);
        }
    }
}

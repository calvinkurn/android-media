package com.tokopedia.home.explore.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.home.SimpleWebViewActivity;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.home.IHomeRouter;
import com.tokopedia.home.R;
import com.tokopedia.home.explore.domain.model.LayoutRows;
import com.tokopedia.home.explore.domain.model.LayoutSections;
import com.tokopedia.home.explore.listener.CategoryAdapterListener;
import com.tokopedia.home.explore.view.adapter.ExploreAdapter;
import com.tokopedia.home.explore.view.adapter.TypeFactory;
import com.tokopedia.home.explore.view.adapter.viewmodel.CategoryGridListViewModel;
import com.tokopedia.home.explore.view.adapter.viewmodel.SellViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by errysuprayogi on 1/26/18.
 */

public class ExploreFragment extends BaseListFragment<Visitable, TypeFactory> implements CategoryAdapterListener {

    private static final String EXTRA_DATA = "EXTRA_DATA";
    private static final String EXTRA_POS = "EXTRA_POS";

    public static ExploreFragment newInstance(LayoutSections sectionsModel, int pos) {

        Bundle args = new Bundle();
        args.putParcelable(EXTRA_DATA, sectionsModel);
        args.putInt(EXTRA_POS, pos);
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
        LayoutSections sectionsModel = getArguments().getParcelable(EXTRA_DATA);
        renderList(mappingModel(sectionsModel));
    }

    private List<Visitable> mappingModel(LayoutSections section) {
        List<Visitable> list = new ArrayList<>();
        if (getArguments().getInt(EXTRA_POS, 0) == 4) {
            if (SessionHandler.isUserHasShop(getActivity())) {
                list.add(mappingManageShop());
            } else {
                list.add(mappingOpenShop());
            }
        }
        CategoryGridListViewModel viewModel = new CategoryGridListViewModel();
        viewModel.setSectionId(section.getId());
        viewModel.setTitle(section.getTitle());
        viewModel.setItemList(section.getLayoutRows());
        list.add(viewModel);
        return list;
    }

    private Visitable mappingOpenShop() {
        SellViewModel model = new SellViewModel();
        model.setTitle(getString(R.string.empty_shop_wording_title));
        model.setSubtitle(getString(R.string.empty_shop_wording_subtitle));
        model.setBtn_title(getString(R.string.buka_toko));
        return model;
    }

    private Visitable mappingManageShop() {
        SellViewModel model = new SellViewModel();
        model.setTitle(getString(R.string.open_shop_wording_title));
        model.setSubtitle(getString(R.string.manage_shop_wording_subtitle));
        model.setBtn_title(getString(R.string.manage_toko));
        return model;
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

    }

    @Override
    public void onApplinkClicked(LayoutRows data) {
        ((TkpdCoreRouter) getActivity().getApplication()).actionApplinkFromActivity(getActivity(),
                data.getApplinks());
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

    }

    @Override
    public void onDigitalMoreClicked() {

    }
}

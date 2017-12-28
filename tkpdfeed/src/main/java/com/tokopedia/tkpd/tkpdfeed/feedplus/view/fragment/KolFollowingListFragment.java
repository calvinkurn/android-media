package com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.KolFollowingAdapter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.di.DaggerFeedPlusComponent;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.KolFollowingList;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.presenter.KolFollowingListPresenter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolFollowingViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by yfsx on 28/12/17.
 */

public class KolFollowingListFragment extends BaseDaggerFragment
        implements KolFollowingList.View {

    RecyclerView rvItem;
    ProgressBar progressBar;

    int userId;

    KolFollowingAdapter adapter;

    @Inject
    KolFollowingListPresenter presenter;

    public static KolFollowingListFragment createInstance(Bundle bundle) {
        KolFollowingListFragment fragment = new KolFollowingListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_kol_following_list, container, false);
        rvItem = (RecyclerView) parentView.findViewById(R.id.rv_item);
        progressBar = (ProgressBar) parentView.findViewById(R.id.progress_bar);
        rvItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        presenter.attachView(this);
        return parentView;
    }

    private void initView() {
        rvItem.setVisibility(View.GONE);
        showLoading();
        presenter.getKolFollowingList(userId);
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);

        DaggerFeedPlusComponent daggerFeedPlusComponent =
                (DaggerFeedPlusComponent) DaggerFeedPlusComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerFeedPlusComponent.inject(this);

    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessGetKolFollowingList(List<KolFollowingViewModel> itemList) {
        adapter = new KolFollowingAdapter(getActivity());
        rvItem.setAdapter(adapter);
        adapter.setItemList(itemList);
    }

    @Override
    public void onErrorGetKolFollowingList(String error) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                initView();
            }
        });
    }

    @Override
    public void onListItemClicked(KolFollowingViewModel item) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}

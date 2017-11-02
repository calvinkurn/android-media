package com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.activity.KolProfileWebViewActivity;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.KolCommentAdapter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.kol.KolTypeFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.kol.KolTypeFactoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.di.DaggerFeedPlusComponent;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.KolComment;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.presenter.KolCommentPresenter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolCommentHeaderViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolCommentProductViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolCommentViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolComments;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author by nisie on 10/27/17.
 */

public class KolCommentFragment extends BaseDaggerFragment implements KolComment.View {

    RecyclerView listComment;
    KolCommentAdapter adapter;

    @Inject
    KolCommentPresenter presenter;

    public static KolCommentFragment createInstance(Bundle bundle) {
        KolCommentFragment fragment = new KolCommentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_KOL_COMMENTS;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_kol_comment, container, false);
        listComment = (RecyclerView) parentView.findViewById(R.id.comment_list);
        prepareView();
        presenter.attachView(this);
        return parentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getCommentFirstTime();
    }

    private void prepareView() {
        KolTypeFactory typeFactory = new KolTypeFactoryImpl(this);
        adapter = new KolCommentAdapter(typeFactory);
        listComment.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager
                .VERTICAL, false));
        listComment.setAdapter(adapter);
    }

    @Override
    public void onGoToProfile(String url) {
        startActivity(KolProfileWebViewActivity.getCallingIntent(getActivity(), url));
    }

    @Override
    public void showLoading() {
        adapter.showLoading();
    }

    @Override
    public void onErrorGetCommentsFirstTime(String errorMessage) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage, new NetworkErrorHelper
                .RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getCommentFirstTime();
            }
        });
    }

    @Override
    public void onSuccessGetCommentsFirstTime(KolComments kolComments) {
        ArrayList<Visitable> list = new ArrayList<>();
        list.add(new KolCommentHeaderViewModel("https://imagerouter.tokopedia" +
                ".com/img/500-square/product-1/2017/11/1/5623332/5623332_e4959646-b9d0-4447-84a4-e4337693d304_500_550.jpeg",
                "Nisie", "Komen komen aja", "10 hari yang lalu", true));
        list.add(new KolCommentViewModel("https://imagerouter.tokopedia" +
                ".com/img/500-square/product-1/2017/11/1/5623332/5623332_e4959646-b9d0-4447-84a4-e4337693d304_500_550.jpeg",
                "Nisie2", "Komen komen aja", "10 hari yang lalu"));
        list.add(new KolCommentViewModel("https://imagerouter.tokopedia" +
                ".com/img/500-square/product-1/2017/11/1/5623332/5623332_e4959646-b9d0-4447-84a4-e4337693d304_500_550.jpeg",
                "Nisie3", "Komen komen aja", "10 hari yang lalu"));
        list.add(new KolCommentViewModel("https://imagerouter.tokopedia" +
                ".com/img/500-square/product-1/2017/11/1/5623332/5623332_e4959646-b9d0-4447-84a4-e4337693d304_500_550.jpeg",
                "Nisie4", "Komen komen aja", "10 hari yang lalu"));
        list.add(new KolCommentViewModel("https://imagerouter.tokopedia" +
                ".com/img/500-square/product-1/2017/11/1/5623332/5623332_e4959646-b9d0-4447-84a4-e4337693d304_500_550.jpeg",
                "Nisie5", "Komen komen aja", "10 hari yang lalu"));
        list.add(new KolCommentProductViewModel("https://imagerouter.tokopedia" +
                ".com/img/500-square/product-1/2017/11/1/5623332/5623332_e4959646-b9d0-4447-84a4-e4337693d304_500_550.jpeg",
                "Topi si Nisie", "Rp 250.000", false));
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void removeLoading() {
        adapter.removeLoading();
    }

    @Override
    public void loadMoreComments() {
        if (adapter.getHeader() != null) {
            adapter.getHeader().setLoading(true);
            adapter.notifyItemChanged(0);
        }
        presenter.loadMoreComments();
    }

    @Override
    public void onSuccessGetComments(KolComments kolComments) {

        if (adapter.getHeader() != null) {
            adapter.getHeader().setCanLoadMore(true);
            adapter.getHeader().setLoading(false);
            adapter.notifyItemChanged(0);
        }

        ArrayList<Visitable> list = new ArrayList<>();
        list.add(new KolCommentViewModel("https://imagerouter.tokopedia" +
                ".com/img/500-square/product-1/2017/11/1/5623332/5623332_e4959646-b9d0-4447-84a4-e4337693d304_500_550.jpeg",
                "Nisie7", "Komen komen aja", "10 hari yang lalu"));
        list.add(new KolCommentViewModel("https://imagerouter.tokopedia" +
                ".com/img/500-square/product-1/2017/11/1/5623332/5623332_e4959646-b9d0-4447-84a4-e4337693d304_500_550.jpeg",
                "Nisie8", "Komen komen aja", "10 hari yang lalu"));
        list.add(new KolCommentViewModel("https://imagerouter.tokopedia" +
                ".com/img/500-square/product-1/2017/11/1/5623332/5623332_e4959646-b9d0-4447-84a4-e4337693d304_500_550.jpeg",
                "Nisie9", "Komen komen aja", "10 hari yang lalu"));
        list.add(new KolCommentViewModel("https://imagerouter.tokopedia" +
                ".com/img/500-square/product-1/2017/11/1/5623332/5623332_e4959646-b9d0-4447-84a4-e4337693d304_500_550.jpeg",
                "Nisie10", "Komen komen aja", "10 hari yang lalu"));
        adapter.addList(list);
    }
}

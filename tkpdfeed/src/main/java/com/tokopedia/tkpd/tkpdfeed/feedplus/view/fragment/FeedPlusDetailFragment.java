package com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.base.presentation.EndlessRecyclerviewListener;
import com.tokopedia.core.database.model.PagingHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlusDetail;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.activity.FeedPlusDetailActivity;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.DetailFeedAdapter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.FeedPlusDetailTypeFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.FeedPlusDetailTypeFactoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.di.DaggerFeedPlusComponent;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.presenter.FeedPlusDetailPresenter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.util.ShareBottomDialog;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.util.ShareModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ActivityCardViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedPlusDetailFragment extends BaseDaggerFragment
        implements FeedPlusDetail.View {

    private static final String ARGS_DATA = "ARGS_DATA";
    private static final String ARGS_DETAIL_ID = "DETAIL_ID";


    RecyclerView recyclerView;
    TextView shareButton;
    TextView seeShopButon;

    @Inject
    FeedPlusDetailPresenter presenter;

    private EndlessRecyclerviewListener recyclerviewScrollListener;
    private LinearLayoutManager layoutManager;
    private DetailFeedAdapter adapter;
    private ShareBottomDialog shareBottomDialog;
    private CallbackManager callbackManager;
    private PagingHandler pagingHandler;
    private String detailId;

    public static FeedPlusDetailFragment createInstance(Bundle bundle) {
        FeedPlusDetailFragment fragment = new FeedPlusDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_FEED_DETAIL;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar(savedInstanceState);
    }

    private void initVar(Bundle savedInstanceState) {
        if (savedInstanceState != null)
            detailId = savedInstanceState.getString(ARGS_DETAIL_ID);
        else if (getArguments() != null)
            detailId = getArguments().getString(FeedPlusDetailActivity.EXTRA_DETAIL_ID);
        else
            detailId = "";

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerviewScrollListener = onRecyclerViewListener();
        FeedPlusDetailTypeFactory typeFactory = new FeedPlusDetailTypeFactoryImpl(this);
        adapter = new DetailFeedAdapter(typeFactory);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        pagingHandler = new PagingHandler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_feed_plus_detail, container, false);
        recyclerView = (RecyclerView) parentView.findViewById(R.id.detail_list);
        shareButton = (TextView) parentView.findViewById(R.id.share_button);
        seeShopButon = (TextView) parentView.findViewById(R.id.see_shop);
        prepareView();
        presenter.attachView(this);
        return parentView;

    }

    private void prepareView() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(recyclerviewScrollListener);
    }

    private EndlessRecyclerviewListener onRecyclerViewListener() {
        return new EndlessRecyclerviewListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

            }
        };
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.getFeedDetail(detailId, pagingHandler.getPage());

        shareButton.setOnClickListener(onShareClicked());
        seeShopButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoToShopDetail();
            }
        });

    }

    private View.OnClickListener onShareClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareBottomDialog == null) {
                    shareBottomDialog = new ShareBottomDialog(
                            FeedPlusDetailFragment.this,
                            callbackManager);
                }

                shareBottomDialog.setShareModel(new ShareModel("https://www.tokopedia.com/",
                        "Tokopedia",
                        "",
                        ""));
                shareBottomDialog.show();
            }
        };
    }

    @Override
    public void onWishlistClicked() {

    }

    @Override
    public void onGoToShopDetail() {

    }

    @Override
    public void onErrorGetFeedDetail(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessGetFeedDetail(
            ArrayList<Visitable> listDetail,
            boolean hasNextPage) {
        adapter.addList(listDetail);
        adapter.notifyDataSetChanged();
        pagingHandler.setHasNext(hasNextPage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARGS_DETAIL_ID, detailId);
    }


}

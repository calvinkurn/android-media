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
import com.tokopedia.core.base.di.component.DaggerAppComponent;
import com.tokopedia.core.base.di.module.ActivityModule;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.base.presentation.EndlessRecyclerviewListener;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.R2;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlusDetail;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.activity.FeedPlusDetailActivity;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.DetailFeedAdapter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.FeedPlusDetailTypeFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.FeedPlusDetailTypeFactoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.di.DaggerFeedPlusComponent;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.presenter.FeedPlusDetailPresenter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.util.ShareBottomDialog;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.util.ShareModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.FeedDetailViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ActivityCardViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedPlusDetailFragment extends BaseDaggerFragment
        implements FeedPlusDetail.View {

    private static final String ARGS_DATA = "ARGS_DATA";

    @BindView(R2.id.detail_list)
    RecyclerView recyclerView;

    @BindView(R2.id.share_button)
    TextView shareButton;

    @BindView(R2.id.see_shop)
    TextView seeShopButon;

    @Inject
    FeedPlusDetailPresenter presenter;

    private Unbinder unbinder;
    private EndlessRecyclerviewListener recyclerviewScrollListener;
    private LinearLayoutManager layoutManager;
    private DetailFeedAdapter adapter;
    private ActivityCardViewModel activityCardViewModel;
    private ShareBottomDialog shareBottomDialog;
    private CallbackManager callbackManager;

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
            activityCardViewModel = savedInstanceState.getParcelable(ARGS_DATA);
        else if (getArguments() != null)
            activityCardViewModel = getArguments().getParcelable(FeedPlusDetailActivity.EXTRA_DATA);
        else
            activityCardViewModel = new ActivityCardViewModel();

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerviewScrollListener = onRecyclerViewListener();
        FeedPlusDetailTypeFactory typeFactory = new FeedPlusDetailTypeFactoryImpl(this);
        adapter = new DetailFeedAdapter(typeFactory);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

    }

    private EndlessRecyclerviewListener onRecyclerViewListener() {
        return new EndlessRecyclerviewListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_feed_plus_detail, container, false);
        unbinder = ButterKnife.bind(this, parentView);
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FeedDetailViewModel prod1 = new FeedDetailViewModel(
                "Produk1",
                "https://4.bp.blogspot.com/-zZl5RYBFxUU/V7WrX7e2rjI/AAAAAAAAAs4/_qJ8TaLqGlgT0MegrxAzFKKbhOAk8jsHACLcB/s1600/Ayam%2BBangkok%2BBagus%2B1.jpg");

        FeedDetailViewModel prod2 = new FeedDetailViewModel(
                "Produk2",
                "https://islamkajian.files.wordpress.com/2015/03/kuda.jpg");

        FeedDetailViewModel prod3 = new FeedDetailViewModel(
                "Produk3",
                "https://4.bp.blogspot.com/-zZl5RYBFxUU/V7WrX7e2rjI/AAAAAAAAAs4/_qJ8TaLqGlgT0MegrxAzFKKbhOAk8jsHACLcB/s1600/Ayam%2BBangkok%2BBagus%2B1.jpg");

        FeedDetailViewModel prod4 = new FeedDetailViewModel(
                "Produk4",
                "https://islamkajian.files.wordpress.com/2015/03/kuda.jpg");

        ArrayList<Visitable> listProduct = new ArrayList<>();
        listProduct.add(activityCardViewModel.getHeader());
        listProduct.add(prod1);
        listProduct.add(prod2);
        listProduct.add(prod3);
        listProduct.add(prod4);

        adapter.addList(listProduct);
        adapter.notifyDataSetChanged();

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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.detachView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARGS_DATA, activityCardViewModel);
    }


}

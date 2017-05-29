package com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.base.presentation.EndlessRecyclerviewListener;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.transactionmodule.TransactionAddToCartRouter;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.ClipboardHandler;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.activity.BlogWebViewActivity;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.activity.FeedPlusDetailActivity;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.activity.TransparentVideoActivity;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.FeedPlusAdapter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.FeedPlusTypeFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.FeedPlusTypeFactoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.di.DaggerFeedPlusComponent;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.presenter.FeedPlusPresenter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.util.ShareBottomDialog;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.util.ShareModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ActivityCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ProductFeedViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.PromotedShopViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 5/15/17.
 */

public class FeedPlusFragment extends BaseDaggerFragment
        implements FeedPlus.View,
        SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    SwipeToRefresh swipeToRefresh;

    @Inject
    FeedPlusPresenter presenter;

    private EndlessRecyclerviewListener recyclerviewScrollListener;
    private LinearLayoutManager layoutManager;
    private FeedPlusAdapter adapter;
    private ShareBottomDialog shareBottomDialog;
    private CallbackManager callbackManager;
    private List<Visitable> list;
    private TopAdsInfoBottomSheet infoBottomSheet;

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_HOME_PRODUCT_FEED;
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
        initVar();
    }

    private void initVar() {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        FeedPlusTypeFactory typeFactory = new FeedPlusTypeFactoryImpl(this);
        adapter = new FeedPlusAdapter(typeFactory);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        recyclerviewScrollListener = onRecyclerViewListener();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_feed_plus, container, false);
        recyclerView = (RecyclerView) parentView.findViewById(R.id.recycler_view);
        swipeToRefresh = (SwipeToRefresh) parentView.findViewById(R.id.swipe_refresh_layout);
        prepareView();
        presenter.attachView(this);
        return parentView;

    }

    private void prepareView() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(recyclerviewScrollListener);
        swipeToRefresh.setOnRefreshListener(this);
        infoBottomSheet = TopAdsInfoBottomSheet.newInstance(getActivity());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//
//        BlogViewModel imageBlog = new BlogViewModel("https://islamkajian.files.wordpress.com/2015/03/kuda.jpg", "Test Blog");
//        BlogViewModel videoBlog = new BlogViewModel("http://www.androidbegin.com/tutorial/AndroidCommercial.3gp");
//
//        ProductFeedViewModel prod1 = new ProductFeedViewModel(
//                "Produk1",
//                "Rp 10.000",
//                "https://4.bp.blogspot.com/-zZl5RYBFxUU/V7WrX7e2rjI/AAAAAAAAAs4/_qJ8TaLqGlgT0MegrxAzFKKbhOAk8jsHACLcB/s1600/Ayam%2BBangkok%2BBagus%2B1.jpg");
//        ProductFeedViewModel prod2 = new ProductFeedViewModel(
//                "Produk2",
//                "Rp 11.0000",
//                "https://islamkajian.files.wordpress.com/2015/03/kuda.jpg");
//        ProductFeedViewModel prod3 = new ProductFeedViewModel(
//
//                "Produk3",
//                "Rp 21.0000",
//                "http://img03.deviantart.net/ebe3/i/2007/294/e/c/kerbau_by_jin_concepts.jpg");
//        ProductFeedViewModel prod4 = new ProductFeedViewModel(
//                "Produk4",
//                "Rp 13.000",
//                "http://4.bp.blogspot.com/-THtQuGtlkH8/UwbV8vtw9gI/AAAAAAAACHI/d9m4DYOo-0s/s1600/Singa+Hewan+Karnivora.jpg");
//
//        ProductFeedViewModel prod5 = new ProductFeedViewModel(
//                "Produk5",
//                "Rp 23.000",
//                "http://riesalam.com/wp-content/uploads/2015/06/Penyakit-akibat-tikus-di-rumah.jpg");
//
//        ProductFeedViewModel prod6 = new ProductFeedViewModel(
//                "Produk6",
//                "Rp 43.000",
//                "https://hellosehat.com/wp-content/uploads/2016/05/anjing-yang-aman-untuk-alergi.jpg");
//
//        ProductFeedViewModel prod7 = new ProductFeedViewModel(
//                "Produk7",
//                "Rp 43.000",
//                "https://4.bp.blogspot.com/-zZl5RYBFxUU/V7WrX7e2rjI/AAAAAAAAAs4/_qJ8TaLqGlgT0MegrxAzFKKbhOAk8jsHACLcB/s1600/Ayam%2BBangkok%2BBagus%2B1.jpg");
//
//        ProductFeedViewModel prod8 = new ProductFeedViewModel(
//                "Produk8",
//                "Rp 43.000",
//                "https://islamkajian.files.wordpress.com/2015/03/kuda.jpg");
//
//        ArrayList<ProductFeedViewModel> listProduct = new ArrayList<>();
//        listProduct.add(prod1);
//
//        ArrayList<ProductFeedViewModel> listProduct2 = new ArrayList<>();
//        listProduct2.add(prod1);
//        listProduct2.add(prod2);
//
//        ArrayList<ProductFeedViewModel> listProduct3 = new ArrayList<>();
//        listProduct3.add(prod1);
//        listProduct3.add(prod2);
//        listProduct3.add(prod3);
//
//        ArrayList<ProductFeedViewModel> listProduct4 = new ArrayList<>();
//        listProduct4.addAll(listProduct3);
//        listProduct4.add(prod4);
//
//        ArrayList<ProductFeedViewModel> listProduct5 = new ArrayList<>();
//        listProduct5.addAll(listProduct4);
//        listProduct5.add(prod5);
//
//        ArrayList<ProductFeedViewModel> listProduct6 = new ArrayList<>();
//        listProduct6.addAll(listProduct5);
//        listProduct6.add(prod6);
//
//        ArrayList<ProductFeedViewModel> listProduct8 = new ArrayList<>();
//        listProduct8.addAll(listProduct6);
//        listProduct8.add(prod7);
//        listProduct8.add(prod8);
//
//        ArrayList<PromoViewModel> listPromo = new ArrayList<>();
//        listPromo.add(new PromoViewModel("Hemat Air", "30 Juni", "AIRMURAH", prod2.getImageSource()));
//        listPromo.add(new PromoViewModel("Hemat Gas", "1 Juni - 3 Juli", "GASANGIN", prod6.getImageSource()));
//        listPromo.add(new PromoViewModel("Hemat Listrik", "6 Agustus", "LISTRIKWEK", prod1.getImageSource()));
//        listPromo.add(new PromoViewModel("Bayar BPJS", "7 Januari - 8 Maret", "BAYARBPJS", prod4.getImageSource()));
//
//
//        ArrayList<PromoViewModel> listPromo2 = new ArrayList<>();
//        listPromo2.add(new PromoViewModel("Hemat Air", "30 Juni", "AIRMURAH", prod2.getImageSource()));
//
//        ArrayList<ProductFeedViewModel> listOfficialStore = new ArrayList<>();
//        listOfficialStore.add(new ProductFeedViewModel(prod1, "https://cdn.dribbble.com/users/255/screenshots/683315/rogie_small.png", "Toko Rocky", true));
//        listOfficialStore.add(new ProductFeedViewModel(prod2, "https://cdn.dribbble.com/users/255/screenshots/683315/rogie_small.png", "Toko Rock", true));
//        listOfficialStore.add(new ProductFeedViewModel(prod3, "https://cdn.dribbble.com/users/255/screenshots/683315/rogie_small.png", "Toko Rocy", false));
//        listOfficialStore.add(new ProductFeedViewModel(prod4, "https://cdn.dribbble.com/users/255/screenshots/683315/rogie_small.png", "Toko Rcky", false));
//
//        ArrayList<ProductFeedViewModel> listPromotedProduct = new ArrayList<>();
//        listPromotedProduct.add(prod1);
//        listPromotedProduct.add(prod2);
//        listPromotedProduct.add(prod3);
//        listPromotedProduct.add(prod4);
//
//
//        List<Visitable> list = new ArrayList<>();
//        list.add(imageBlog);
//        list.add(videoBlog);
//        list.add(new ActivityCardViewModel(listProduct));
//        list.add(new ActivityCardViewModel(listProduct2));
//        list.add(new ActivityCardViewModel(listProduct3));
//        list.add(new PromoCardViewModel(listPromo));
//        list.add(new PromotedProductViewModel(listPromotedProduct));
//        list.add(new PromotedShopViewModel("Tep Shop 1", true, "Toko terbaik", listProduct3));
//        list.add(new PromotedShopViewModel("Tep Shop 2", false, "Toko terbaik", listProduct3));
//        ArrayList<ProductFeedViewModel> listInspiration = new ArrayList<>();
//        listInspiration.addAll(listOfficialStore);
//
////        list.add(new ActivityCardViewModel("Nisie 1", listProduct));
////        list.add(new ActivityCardViewModel("Nisie 2", listProduct2));
////        list.add(new ActivityCardViewModel("Nisie 3", listProduct3));
//        list.add(new PromoCardViewModel(listPromo));
//        list.add(new PromoCardViewModel(listPromo2));
//
//        list.add(new InspirationViewModel("Inspirasi dari Pembelian Anda", listInspiration));
//        list.add(new OfficialStoreViewModel("https://ecs7.tokopedia.net/img/mobile/banner-4.png", listOfficialStore));
////        list.add(new ActivityCardViewModel("Nisie 4", listProduct4));
////        list.add(new ActivityCardViewModel("Nisie 5", listProduct5));
//        list.add(new ActivityCardViewModel(listProduct6));
//        list.add(new PromotedShopViewModel("Tep Shop 1", true, "Toko terbaik", listProduct3));
//        list.add(new ActivityCardViewModel(listProduct8));
//
//         adapter.addList(list);
//        adapter.notifyDataSetChanged();

        presenter.fetchFirstPage(true);
    }


    private EndlessRecyclerviewListener onRecyclerViewListener() {
        return new EndlessRecyclerviewListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (!adapter.isLoading())
                    presenter.fetchNextPage();
            }
        };
    }

    @Override
    public void onRefresh() {
        presenter.fetchFirstPage(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void onShareButtonClicked(String shareUrl,
                                     String title,
                                     String imgUrl,
                                     String contentMessage) {

        if (shareBottomDialog == null) {
            shareBottomDialog = new ShareBottomDialog(
                    FeedPlusFragment.this,
                    callbackManager);
        }

        shareBottomDialog.setShareModel(
                new ShareModel(shareUrl,
                        title,
                        imgUrl,
                        contentMessage));

        shareBottomDialog.show();
    }

    @Override
    public void onGoToProductDetail(Integer productId) {
        Intent intent = ProductInfoActivity.createInstance(getActivity(), String.valueOf(productId));
        startActivity(intent);
    }


    @Override
    public void onGoToFeedDetail(ActivityCardViewModel activityCardViewModel) {
        Intent intent = FeedPlusDetailActivity.getIntent(
                getActivity(),
                activityCardViewModel.getFeedId());
        startActivity(intent);
    }

    @Override
    public void onGoToShopDetail(Integer shopId, String url) {
        Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
        Bundle bundle = ShopInfoActivity.createBundle(String.valueOf(shopId), url);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onCopyClicked(String s) {
        ClipboardHandler.CopyToClipboard(getActivity(), s);
        SnackbarManager.make(getActivity(), getResources().getString(R.string.copy_promo_success), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onGoToBlogWebView(String url) {
        Intent intent = BlogWebViewActivity.getIntent(getActivity(), url);
        startActivity(intent);
    }

    @Override
    public void onOpenVideo(String videoUrl, String subtitle) {
        Intent intent = TransparentVideoActivity.getIntent(getActivity(), videoUrl, subtitle);
        startActivity(intent);
    }

    @Override
    public void onGoToBuyProduct(ProductFeedViewModel productFeedViewModel) {

        ProductCartPass pass = ProductCartPass.Builder.aProductCartPass()
                .setProductId(String.valueOf(productFeedViewModel.getProductId()))
                .build();

        Intent intent = TransactionAddToCartRouter
                .createInstanceAddToCartActivity(getActivity(), pass);
        startActivity(intent);

    }

    @Override
    public void onInfoClicked() {
        infoBottomSheet.show();
    }

    @Override
    public void onFavoritedClicked(int adapterPosition) {
        adapter.getItemViewType(adapterPosition);
        PromotedShopViewModel promotedShopViewModel = (PromotedShopViewModel) list.get(adapterPosition);
        presenter.favoriteShop(promotedShopViewModel, adapterPosition);
    }

    @Override
    public void showSnackbar(String s) {
        SnackbarManager.make(getActivity(), s, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void updateFavorite(int adapterPosition) {
        PromotedShopViewModel promotedShopViewModel = (PromotedShopViewModel) list.get(adapterPosition);
        promotedShopViewModel.setFavorited(!promotedShopViewModel.isFavorited());
        adapter.notifyItemChanged(adapterPosition);
    }

    @Override
    public void onViewMorePromoClicked() {
        Intent intent = BlogWebViewActivity.getIntent(getActivity(), "https://www.tokopedia.com/promo/");
        startActivity(intent);
    }


    @Override
    public void onSuccessGetFeedFirstPage(ArrayList<Visitable> listFeed) {
        adapter.clearData();
        finishLoading();
        adapter.removeEmpty();
        if (listFeed.size() == 0) {
            adapter.showEmpty();
        } else {
            adapter.addList(listFeed);
        }
        adapter.notifyDataSetChanged();
    }

    private void finishLoading() {
        if (swipeToRefresh.isRefreshing())
            swipeToRefresh.setRefreshing(false);
        adapter.removeLoading();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorGetFeedFirstPage(String errorMessage) {
        finishLoading();
        if (adapter.getItemCount() == 0) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage,
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.fetchFirstPage(false);
                        }
                    });
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
        }
    }

    @Override
    public void onSearchShopButtonClicked() {
        Intent intent = HomeRouter.getHomeActivity(getActivity());
        intent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT, HomeRouter.INIT_STATE_FRAGMENT_FAVORITE);
        startActivity(intent);
    }

    @Override
    public void showRefresh() {
        if (!swipeToRefresh.isRefreshing()) {
            swipeToRefresh.setRefreshing(true);
        }
    }

    @Override
    public void updateCursor(String currentCursor) {
        presenter.setCursor(currentCursor);
    }

    @Override
    public void showLoading() {
        adapter.showLoading();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessGetFeed(ArrayList<Visitable> listFeed) {
        finishLoading();
        adapter.removeEmpty();
        adapter.addList(listFeed);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}

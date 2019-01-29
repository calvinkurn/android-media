package com.tokopedia.discovery.intermediary.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.analytics.CategoryPageTracking;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.entity.intermediary.CategoryHadesModel;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.NonScrollGridLayoutManager;
import com.tokopedia.core.util.NonScrollLinearLayoutManager;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.widgets.DividerItemDecoration;
import com.tokopedia.discovery.DiscoveryRouter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.intermediary.analytics.IntermediaryAnalytics;
import com.tokopedia.discovery.intermediary.di.IntermediaryDependencyInjector;
import com.tokopedia.discovery.intermediary.domain.model.BannerModel;
import com.tokopedia.discovery.intermediary.domain.model.BrandModel;
import com.tokopedia.discovery.intermediary.domain.model.ChildCategoryModel;
import com.tokopedia.discovery.intermediary.domain.model.CuratedSectionModel;
import com.tokopedia.discovery.intermediary.domain.model.HeaderModel;
import com.tokopedia.discovery.intermediary.domain.model.HotListModel;
import com.tokopedia.discovery.intermediary.domain.model.ProductModel;
import com.tokopedia.discovery.intermediary.domain.model.VideoModel;
import com.tokopedia.discovery.intermediary.view.adapter.BannerPagerAdapter;
import com.tokopedia.discovery.intermediary.view.adapter.CuratedProductAdapter;
import com.tokopedia.discovery.intermediary.view.adapter.CurationAdapter;
import com.tokopedia.discovery.intermediary.view.adapter.HotListItemAdapter;
import com.tokopedia.discovery.intermediary.view.adapter.IntermediaryBrandsAdapter;
import com.tokopedia.discovery.intermediary.view.adapter.IntermediaryCategoryAdapter;
import com.tokopedia.discovery.newdiscovery.category.presentation.CategoryActivity;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel;
import com.tokopedia.discovery.view.CategoryHeaderTransformation;
import com.tokopedia.tkpdpdp.customview.YoutubeWebViewThumbnail;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;
import com.tokopedia.topads.sdk.widget.TopAdsView;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.tokopedia.topads.sdk.domain.TopAdsParams.DEFAULT_KEY_EP;
import static com.tokopedia.topads.sdk.domain.TopAdsParams.SRC_INTERMEDIARY_VALUE;

/**
 * Created by alifa on 3/24/17.
 */

public class IntermediaryFragment extends BaseDaggerFragment implements IntermediaryContract.View,
        CuratedProductAdapter.OnItemClickListener, TopAdsItemClickListener, TopAdsListener,
        IntermediaryCategoryAdapter.CategoryListener, IntermediaryBrandsAdapter.BrandListener,
        BannerPagerAdapter.OnPromoClickListener,
        WishListActionListener {

    public static final String TAG = "INTERMEDIARY_FRAGMENT";
    private static final long SLIDE_DELAY = 8000;
    public static final String DEFAULT_ITEM_VALUE = "1";
    public static final int REQUEST_CODE_LOGIN = 561;
    private static final String PERFORMANCE_TRACE_INTERMEDIARY = "mp_category_intermediary";

    private NestedScrollView nestedScrollView;
    private ImageView imageHeader;
    private TextView titleHeader;
    private LinearLayout expandLayout;
    private LinearLayout hideLayout;
    private RecyclerView revampCategoriesRecyclerView;
    private RecyclerView curationRecyclerView;
    private CardView cardViewHotList;
    private CardView cardViewVideo;
    private LinearLayout placeHolderVideo;
    private RecyclerView hotListRecyclerView;
    private TextView viewAllCategory;
    private RelativeLayout bannerContainer;
    private RelativeLayout headerContainer;
    private TextView videoTitle;
    private TextView videoDesc;
    private CardView cardOfficial;
    private RecyclerView brandsRecyclerView;
    private TopAdsView topAdsView;
    private TopAdsBannerView topAdsBannerView;

    private TextView btnSeeAllOfficialBrand;

    private CirclePageIndicator bannerIndicator;
    private View banner;
    private ViewPager bannerViewPager;
    private BannerPagerAdapter bannerPagerAdapter;
    private Handler bannerHandler;
    private Runnable incrementPage;

    private String departmentId = "";
    private String trackerAttribution = "";
    private IntermediaryCategoryAdapter categoryAdapter;
    private IntermediaryBrandsAdapter brandsAdapter;
    private IntermediaryCategoryAdapter.CategoryListener categoryListener;
    private IntermediaryBrandsAdapter.BrandListener brandListener;
    private ArrayList<ChildCategoryModel> activeChildren = new ArrayList<>();
    private boolean isUsedUnactiveChildren = false;
    private CurationAdapter curationAdapter;
    private IntermediaryContract.Presenter presenter;
    private NonScrollGridLayoutManager gridLayoutManager;
    private UserSessionInterface userSession;
    private PerformanceMonitoring performanceMonitoring;
    private boolean isTraceStopped;

    public static IntermediaryFragment createInstance(String departmentId, String trackerAttribution) {
        IntermediaryFragment intermediaryFragment = new IntermediaryFragment();
        intermediaryFragment.departmentId = departmentId;
        intermediaryFragment.setTrackerAttribution(trackerAttribution);
        return intermediaryFragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        presenter = IntermediaryDependencyInjector.getPresenter(getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performanceMonitoring = PerformanceMonitoring.start(PERFORMANCE_TRACE_INTERMEDIARY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_intermediary, container, false);

        initView(parentView);
        initListener();

        btnSeeAllOfficialBrand = parentView.findViewById(R.id.see_all_official_brand_button);
        btnSeeAllOfficialBrand.setOnClickListener(new SeeAllOfficialOnClickListener());

        presenter.attachView(this);
        presenter.setWishlishListener(this);
        return parentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userSession = new UserSession(getActivity());
        presenter.getIntermediaryCategory(departmentId);
    }

    private void initView(View view) {
        nestedScrollView = view.findViewById(R.id.nested_intermediary);
        imageHeader = view.findViewById(R.id.image_header);
        titleHeader = view.findViewById(R.id.title_header);
        expandLayout = view.findViewById(R.id.expand_layout);
        hideLayout = view.findViewById(R.id.hide_layout);
        revampCategoriesRecyclerView = view.findViewById(R.id.recycler_view_revamp_categories);
        curationRecyclerView = view.findViewById(R.id.recycler_view_curation);
        cardViewHotList = view.findViewById(R.id.card_hoth_intermediary);
        cardViewVideo = view.findViewById(R.id.card_video_intermediary);
        placeHolderVideo = view.findViewById(R.id.youtube_video_place_holder);
        hotListRecyclerView = view.findViewById(R.id.recycler_view_hot_list);
        viewAllCategory = view.findViewById(R.id.category_view_all);
        bannerContainer = view.findViewById(R.id.banner_container);
        headerContainer = view.findViewById(R.id.header_container);
        videoTitle = view.findViewById(R.id.intermediary_video_title);
        videoDesc = view.findViewById(R.id.intermediary_video_desc);
        cardOfficial = view.findViewById(R.id.card_official_intermediary);
        brandsRecyclerView = view.findViewById(R.id.rv_official_intermediary);
        topAdsView = view.findViewById(R.id.top_ads_view);
        topAdsBannerView = view.findViewById(R.id.top_ads_banner);
    }

    private void initListener() {
        viewAllCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewAllCategory();
            }
        });
    }

    @Override
    public void onErrorAddWishList(String errorMessage, String productId) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessAddWishlist(String productId) {
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_add_wishlist));
    }

    @Override
    public void onErrorRemoveWishlist(String errorMessage, String productId) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessRemoveWishlist(String productId) {
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.msg_remove_wishlist));
    }

    YoutubeViewHolder.YouTubeThumbnailLoadInProcess youTubeThumbnailLoadInProcessListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof YoutubeViewHolder.YouTubeThumbnailLoadInProcess)
            youTubeThumbnailLoadInProcessListener = (YoutubeViewHolder.YouTubeThumbnailLoadInProcess) context;
    }

    @Override
    public void renderHeader(HeaderModel headerModel) {
        ImageHandler.loadImageFitTransformation(imageHeader.getContext(), imageHeader,
                headerModel.getHeaderImageUrl(), new CategoryHeaderTransformation(imageHeader.getContext()));
        titleHeader.setText(headerModel.getCategoryName().toUpperCase());
        titleHeader.setShadowLayer(24, 0, 0, com.tokopedia.core2.R.color.checkbox_text);
        viewAllCategory.setVisibility(View.VISIBLE);
        headerContainer.setVisibility(View.VISIBLE);
        bannerContainer.setVisibility(View.GONE);
        viewAllCategory.setText("Lihat Produk " + headerModel.getCategoryName() + " Lainnya");
        ((IntermediaryActivity) getActivity()).updateTitle(headerModel.getCategoryName());
    }

    @Override
    public void renderTopAds() {
        TopAdsParams params = new TopAdsParams();
        params.getParam().put(TopAdsParams.KEY_SRC, SRC_INTERMEDIARY_VALUE);
        params.getParam().put(TopAdsParams.KEY_EP, DEFAULT_KEY_EP);
        params.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID, departmentId);

        Config config = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(userSession.getUserId())
                .topAdsParams(params)
                .build();

        topAdsView.setAdsItemClickListener(this);
        topAdsView.setAdsListener(this);
        topAdsView.setConfig(config);
        topAdsView.loadTopAds();

        TopAdsParams adsBannerParams = new TopAdsParams();
        adsBannerParams.getParam().put(TopAdsParams.KEY_SRC, SRC_INTERMEDIARY_VALUE);
        adsBannerParams.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID, departmentId);
        adsBannerParams.getParam().put(TopAdsParams.KEY_ITEM, DEFAULT_ITEM_VALUE);
        adsBannerParams.getParam().put(TopAdsParams.KEY_USER_ID, userSession.getUserId());

        Config configAdsBanner = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(userSession.getUserId())
                .setEndpoint(Endpoint.CPM)
                .topAdsParams(adsBannerParams)
                .build();
        topAdsBannerView.setConfig(configAdsBanner);
        topAdsBannerView.setTopAdsBannerClickListener(new TopAdsBannerClickListener() {
            @Override
            public void onBannerAdsClicked(String appLink, CpmData data) {
                TkpdCoreRouter router = ((TkpdCoreRouter) getActivity().getApplicationContext());
                if (router.isSupportedDelegateDeepLink(appLink)) {
                    router.actionApplink(getActivity(), appLink);
                } else if (appLink != "") {
                    Intent intent = new Intent(getContext(), BannerWebView.class);
                    intent.putExtra("url", appLink);
                    startActivity(intent);
                }
            }
        });
        topAdsBannerView.loadTopAds();
    }

    @Override
    public void renderCategoryChildren(final List<ChildCategoryModel> childCategoryModelList) {

        if (childCategoryModelList != null && childCategoryModelList.size() > 9) {
            activeChildren.addAll(childCategoryModelList
                    .subList(0, 9));
            isUsedUnactiveChildren = true;
        } else if (childCategoryModelList != null) {
            activeChildren.addAll(childCategoryModelList);
        }

        revampCategoriesRecyclerView.setVisibility(View.VISIBLE);
        revampCategoriesRecyclerView.setHasFixedSize(true);
        revampCategoriesRecyclerView.setLayoutManager(
                new NonScrollGridLayoutManager(getActivity(), 3,
                        GridLayoutManager.VERTICAL, false));
        categoryAdapter = new IntermediaryCategoryAdapter(getCategoryWidth(), activeChildren, this);
        revampCategoriesRecyclerView.setAdapter(categoryAdapter);
        if (isUsedUnactiveChildren) {
            expandLayout.setVisibility(View.VISIBLE);
            expandLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UnifyTracking.eventExpandCategoryIntermediary(getActivity(),departmentId);
                    categoryAdapter.addDataChild(childCategoryModelList
                            .subList(9, childCategoryModelList.size()));
                    expandLayout.setVisibility(View.GONE);
                    isUsedUnactiveChildren = false;
                    hideLayout.setVisibility(View.VISIBLE);

                }
            });
            if (hideLayout != null) {
                hideLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (categoryAdapter != null) categoryAdapter.hideExpandable();
                        if (expandLayout != null) expandLayout.setVisibility(View.VISIBLE);
                        isUsedUnactiveChildren = true;
                        if (hideLayout != null) hideLayout.setVisibility(View.GONE);
                        if (revampCategoriesRecyclerView != null)
                            revampCategoriesRecyclerView.scrollToPosition(0);
                    }
                });
            }
        }
    }

    @Override
    public void renderCuratedProducts(List<CuratedSectionModel> curatedSectionModelList) {
        curationRecyclerView.setHasFixedSize(true);
        curationRecyclerView.setNestedScrollingEnabled(false);
        curationAdapter = new CurationAdapter(getActivity(), this);
        curationAdapter.setHomeMenuWidth(getCategoryWidth());
        curationRecyclerView.setLayoutManager(
                new NonScrollLinearLayoutManager(getActivity(),
                        LinearLayoutManager.VERTICAL,
                        false)
        );
        curationRecyclerView.setAdapter(curationAdapter);
        curationAdapter.setDataList(curatedSectionModelList);
        curationAdapter.notifyDataSetChanged();
    }

    @Override
    public void trackEventEnhance(Map<String, Object> maps) {
        CategoryPageTracking.eventEnhance(getActivity(), maps);
    }

    @Override
    public void renderHotList(List<HotListModel> hotListModelList) {
        cardViewHotList.setVisibility(View.VISIBLE);

        HotListItemAdapter hotListItemAdapter = new HotListItemAdapter(hotListModelList,
                getCategoryWidth(), getActivity(), departmentId);

        hotListRecyclerView.setHasFixedSize(true);
        hotListRecyclerView.setNestedScrollingEnabled(false);
        gridLayoutManager = new NonScrollGridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(onSpanSizeLookup());
        hotListRecyclerView.setLayoutManager(gridLayoutManager);
        hotListRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider300));
        hotListRecyclerView.setAdapter(hotListItemAdapter);
    }

    @Override
    public void renderBanner(List<BannerModel> bannerModelList) {
        bannerHandler = new Handler();
        incrementPage = runnableIncrement();
        bannerPagerAdapter = new BannerPagerAdapter(getActivity(), bannerModelList, departmentId, this);
        banner = getActivity().getLayoutInflater().inflate(R.layout.banner_intermediary, bannerContainer);
        bannerViewPager = (ViewPager) banner.findViewById(R.id.view_pager_intermediary);
        bannerIndicator = (CirclePageIndicator) banner.findViewById(R.id.indicator_intermediary);
        bannerViewPager.setAdapter(bannerPagerAdapter);
        bannerViewPager.addOnPageChangeListener(onBannerChange());
        bannerIndicator.setFillColor(ContextCompat.getColor(getContext(), R.color.tkpd_dark_orange));
        bannerIndicator.setPageColor(ContextCompat.getColor(getContext(), R.color.white));
        bannerIndicator.setViewPager(bannerViewPager);
        bannerPagerAdapter.notifyDataSetChanged();
        RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) bannerViewPager.getLayoutParams();
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        bannerViewPager.setLayoutParams(param);
        bannerContainer.setVisibility(View.VISIBLE);
        headerContainer.setVisibility(View.GONE);
        if (bannerModelList.size() == 1) bannerIndicator.setVisibility(View.GONE);
        startSlide();
    }

    private ViewPager.OnPageChangeListener onBannerChange() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                stopSlide();
                startSlide();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }

    private Runnable runnableIncrement() {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    int currentItem = bannerViewPager.getCurrentItem();
                    int maxItems = bannerViewPager.getAdapter().getCount();
                    if (maxItems != 0) {
                        bannerViewPager.setCurrentItem((currentItem + 1) % maxItems, true);
                    } else {
                        bannerViewPager.setCurrentItem(0, true);
                    }
                    bannerHandler.postDelayed(incrementPage, SLIDE_DELAY);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

        };
    }


    private void stopSlide() {
        if (bannerHandler != null && incrementPage != null)
            bannerHandler.removeCallbacks(incrementPage);
    }

    private void startSlide() {
        bannerHandler.removeCallbacks(incrementPage);
        bannerHandler.postDelayed(incrementPage, SLIDE_DELAY);
    }

    @Override
    public void renderVideo(VideoModel videoModel) {
        cardViewVideo.setVisibility(View.VISIBLE);
        videoTitle.setText(videoModel.getTitle());
        videoDesc.setText(videoModel.getDescription());
        if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(getContext().getApplicationContext())
                .equals(YouTubeInitializationResult.SUCCESS)) {

            placeHolderVideo.addView(new YoutubeViewHolder(getContext(), videoModel.getVideoUrl(), departmentId, youTubeThumbnailLoadInProcessListener));

        } else {
            placeHolderVideo.addView(new YoutubeWebViewThumbnail(getContext(), videoModel.getVideoUrl()));
        }
    }

    @Override
    public void renderBrands(List<BrandModel> brandModels) {
        cardOfficial.setVisibility(View.VISIBLE);
        brandsRecyclerView.setVisibility(View.VISIBLE);
        brandsRecyclerView.setHasFixedSize(true);
        brandsRecyclerView.setLayoutManager(
                new NonScrollGridLayoutManager(getActivity(), 3,
                        GridLayoutManager.VERTICAL, false));
        brandsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider300));
        brandsAdapter = new IntermediaryBrandsAdapter(getCategoryWidth(), brandModels, this);
        brandsRecyclerView.setAdapter(brandsAdapter);
    }

    @Override
    public void showLoading() {
        if (isAdded() && ((IntermediaryActivity) getActivity()).getProgressBar() != null) {
            ((IntermediaryActivity) getActivity()).getProgressBar().setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        if (isAdded() && ((IntermediaryActivity) getActivity()).getProgressBar() != null) {
            ((IntermediaryActivity) getActivity()).getProgressBar().setVisibility(View.GONE);
        }
    }

    @Override
    public void emptyState() {
        showErrorEmptyState();
    }

    @Override
    public void skipIntermediaryPage() {
        if (isAdded()) {
            stopFirebaseTrace();
            CategoryActivity.moveTo(
                    getActivity(),
                    departmentId,
                    ((IntermediaryActivity) getActivity()).getCategoryName(),
                    true,
                    trackerAttribution
            );
            getActivity().overridePendingTransition(0, 0);
            getActivity().finish();
        }
    }

    @Override
    public void skipIntermediaryPage(CategoryHadesModel categoryHadesModel) {
        if (isAdded()) {
            CategoryActivity.moveTo(
                    getActivity(),
                    CategoryHeaderModel.convertIntermediaryToCategoryHeader(categoryHadesModel),
                    true,
                    trackerAttribution
            );
            getActivity().overridePendingTransition(0, 0);
            getActivity().finish();
        }
    }

    @Override
    public void backToTop() {
        nestedScrollView.fullScroll(NestedScrollView.FOCUS_UP);
    }

    @Override
    public void updateDepartementId(String id) {
        departmentId = id;
    }

    @Override
    public void onStart() {
        super.onStart();
        ScreenTracking.eventDiscoveryScreenAuth(getActivity(), departmentId);
    }

    @Override
    public void onStop() {
        stopSlide();
        super.onStop();
    }

    private void showErrorEmptyState() {
        NetworkErrorHelper.showEmptyState(getActivity(), ((IntermediaryActivity) getActivity())
                        .getFrameLayout(),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.getIntermediaryCategory(departmentId);
                    }
                });
    }

    private int getCategoryWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width / 2;
    }

    public void viewAllCategory() {
/*        BrowseProductActivity.moveTo(
                getActivity(),
                departmentId,
                TopAdsApi.SRC_DIRECTORY,
                BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY,
                ((IntermediaryActivity) getActivity()).getCategoryName()
        );*/
        CategoryActivity.moveTo(
                getActivity(),
                departmentId,
                ((IntermediaryActivity) getActivity()).getCategoryName(),
                true,
                ""
        );
        getActivity().finish();
    }

    @Override
    public void onItemClicked(ProductModel productModel, String curatedName) {
        trackEventEnhance(createClickProductDataLayer(productModel));
        com.tokopedia.core.var.ProductItem data = new com.tokopedia.core.var.ProductItem();
        data.setId(String.valueOf(productModel.getId()));
        data.setName(productModel.getName());
        data.setPrice(productModel.getPrice());
        data.setImgUri(productModel.getImageUrl());
        data.setTrackerAttribution(productModel.getTrackerAttribution());
        data.setTrackerListName(productModel.getTrackerListName());
        Bundle bundle = new Bundle();
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(getActivity());
        bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
        UnifyTracking.eventCuratedIntermediary(getActivity(),departmentId,
                curatedName, productModel.getName());
    }

    private Map<String, Object> createClickProductDataLayer(ProductModel product) {
        return DataLayer.mapOf("event", "productClick",
                "eventCategory", "intermediary page",
                "eventAction", "click product curation",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "click", DataLayer.mapOf(
                                "actionField", DataLayer.mapOf("list", product.getTrackerListName()),
                                "products", DataLayer.listOf(
                                        product.generateClickDataLayer()
                                )
                        )
                )
        );
    }

    @Override
    public void onTopAdsLoaded(List<Item> list) {
        hideLoading();
        topAdsView.setVisibility(View.VISIBLE);
        backToTop();
    }

    @Override
    public void onTopAdsFailToLoad(int errorCode, String message) {
        hideLoading();
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        ProductItem data = new ProductItem();
        data.setId(product.getId());
        data.setName(product.getName());
        data.setPrice(product.getPriceFormat());
        data.setImgUri(product.getImage().getM_ecs());
        Bundle bundle = new Bundle();
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(getActivity());
        bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) {
        Intent intent = ((DiscoveryRouter) getActivity().getApplication()).getShopPageIntent(getActivity(), shop.getId());
        getActivity().startActivity(intent);
    }

    @Override
    public void onAddFavorite(int position, Data shopData) {
        //TODO: this listener not used in this sprint
    }

    @Override
    public void onCategoryRevampClick(ChildCategoryModel child) {
        CategoryActivity.moveToDestroyIntermediary(
                getActivity(),
                child.getCategoryId(),
                child.getCategoryName(),
                true
        );
        UnifyTracking.eventLevelCategoryIntermediary(getActivity(),departmentId, child.getCategoryId());
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public void setTrackerAttribution(String trackerAttribution) {
        this.trackerAttribution = trackerAttribution;
    }

    @Override
    public String getTrackerAttribution() {
        if (trackerAttribution == null || trackerAttribution.isEmpty()) return "none/other";
        else return trackerAttribution;
    }

    @Override
    public boolean isUserHasLogin() {
        return userSession.isLoggedIn();
    }

    @Override
    public void launchLoginActivity(Bundle extras) {
        Intent intent = ((DiscoveryRouter) MainApplication.getAppContext()).getLoginIntent
                (getActivity());
        intent.putExtras(extras);
        startActivityForResult(intent, REQUEST_CODE_LOGIN);
    }

    @Override
    public String getUserId() {
        return userSession.getUserId();
    }

    @Override
    public void stopFirebaseTrace() {
        if (performanceMonitoring != null && !isTraceStopped) {
            performanceMonitoring.stopTrace();
            isTraceStopped = true;
        }
    }

    private GridLayoutManager.SpanSizeLookup onSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                int regularColumnSize = 1;
                int fullColumnSize = 2;

                if (position == 0) {
                    return fullColumnSize;
                } else {
                    return regularColumnSize;
                }
            }
        };
    }

    @Override
    public void onBrandClick(BrandModel brandModel) {
        UnifyTracking.eventOfficialStoreIntermediary(getActivity(),departmentId, brandModel.getBrandName());
        Intent intent = ((DiscoveryRouter) getActivity().getApplication()).getShopPageIntent(getActivity(), brandModel.getId());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    @Override
    public void onPromoClick(String applink, String url) {
        if (getActivity() != null
                && getActivity().getApplicationContext() instanceof TkpdCoreRouter
                && !TextUtils.isEmpty(applink)
                && ((TkpdCoreRouter) getActivity().getApplicationContext())
                .isSupportedDelegateDeepLink(applink)) {
            ((TkpdCoreRouter) getActivity().getApplicationContext())
                    .actionApplink(getActivity(), applink);
        } else {
            switch ((DeepLinkChecker.getDeepLinkType(url))) {
                case DeepLinkChecker.BROWSE:
                    DeepLinkChecker.openBrowse(url, getActivity());
                    break;
                case DeepLinkChecker.HOT:
                    DeepLinkChecker.openHot(url, getActivity());
                    break;
                case DeepLinkChecker.CATALOG:
                    DeepLinkChecker.openCatalog(url, getActivity());
                    break;
                default:
                    Intent intent = new Intent(getActivity(), BannerWebView.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
            }
        }
    }

    public void viewAllOfficialStores() {
        if(getActivity() != null){
            RouteManager.route(getActivity(), ApplinkConst.OFFICIAL_STORES);
        }
    }

    private class SeeAllOfficialOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            IntermediaryAnalytics.eventClickSeeAllOfficialStores(getActivity());
            viewAllOfficialStores();
        }
    }
}

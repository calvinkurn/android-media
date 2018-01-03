package com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.viewholder;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.util.NonScrollGridLayoutManager;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.BannerPagerAdapter;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.RevampCategoryAdapter;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ChildCategoryModel;
import com.tokopedia.discovery.view.CategoryHeaderTransformation;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.view.TopAdsBannerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


/**
 * @author by alifa on 11/1/17.
 */

public class CategoryRevampHeaderViewHolder extends AbstractViewHolder<CategoryHeaderModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.revamp_category_header;
    public static final String DEFAULT_ITEM_VALUE = "1";

    ImageView imageHeader;
    TextView titleHeader;
    LinearLayout expandLayout;
    LinearLayout hideLayout;
    RecyclerView revampCategoriesRecyclerView;
    TextView totalProduct;
    private final TopAdsBannerView topAdsBannerView;

    private static final long SLIDE_DELAY = 8000;

    private final Context context;
    private RelativeLayout imageHeaderContainer;
    private RelativeLayout bannerContainer;
    private ViewPager bannerViewPager;
    private CirclePageIndicator bannerIndicator;
    private BannerPagerAdapter bannerPagerAdapter;
    private Handler bannerHandler;
    private Runnable incrementPage;
    private RevampCategoryAdapter categoryAdapter;

    private final RevampCategoryAdapter.CategoryListener categoryListener;
    private boolean isUsedUnactiveChildren = false;
    private ArrayList<ChildCategoryModel> activeChildren = new ArrayList<>();

    public CategoryRevampHeaderViewHolder(View itemView, RevampCategoryAdapter.CategoryListener categoryListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.imageHeader = (ImageView) itemView.findViewById(R.id.image_header);
        this.expandLayout = (LinearLayout) itemView.findViewById(R.id.expand_layout);
        this.hideLayout = (LinearLayout) itemView.findViewById(R.id.hide_layout);
        this.revampCategoriesRecyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view_revamp_categories);
        this.titleHeader = (TextView) itemView.findViewById(R.id.title_header);
        this.totalProduct = (TextView) itemView.findViewById(R.id.total_product);
        this.bannerViewPager = (ViewPager) itemView.findViewById(R.id.view_pager_intermediary);
        this.bannerIndicator = (CirclePageIndicator) itemView.findViewById(R.id.indicator_intermediary);
        this.bannerContainer = (RelativeLayout) itemView.findViewById(R.id.banner_container);
        this.imageHeaderContainer = (RelativeLayout) itemView.findViewById(R.id.image_header_container);
        this.topAdsBannerView = (TopAdsBannerView) itemView.findViewById(R.id.topAdsBannerView);
        this.categoryListener = categoryListener;
    }

    private void initTopAds(String depId) {
        TopAdsParams adsParams = new TopAdsParams();
        adsParams.getParam().put(TopAdsParams.KEY_SRC, BrowseApi.DEFAULT_VALUE_SOURCE_DIRECTORY);
        adsParams.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID, depId);
        adsParams.getParam().put(TopAdsParams.KEY_ITEM, DEFAULT_ITEM_VALUE);

        Config config = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(SessionHandler.getLoginID(context))
                .setEndpoint(Endpoint.CPM)
                .topAdsParams(adsParams)
                .build();
        this.topAdsBannerView.setConfig(config);
        this.topAdsBannerView.setTopAdsBannerClickListener(new TopAdsBannerClickListener() {
            @Override
            public void onBannerAdsClicked(String applink) {
                categoryListener.onBannerAdsClicked(applink);
            }
        });
        this.topAdsBannerView.loadTopAds();
    }

    public void bind(final CategoryHeaderModel categoryHeaderModel) {
        initTopAds(categoryHeaderModel.getDepartementId());
        activeChildren = new ArrayList<>();
        hideLayout.setVisibility(View.GONE);
        if (categoryHeaderModel.getChildCategoryModelList() != null && categoryHeaderModel.getChildCategoryModelList().size() > 9) {
            activeChildren.addAll(categoryHeaderModel.getChildCategoryModelList().subList(0, 9));
            isUsedUnactiveChildren = true;
        } else if (categoryHeaderModel.getChildCategoryModelList() != null) {
            activeChildren.addAll(categoryHeaderModel.getChildCategoryModelList());
        }
        if (activeChildren.size() > 0) {
            revampCategoriesRecyclerView.setVisibility(View.VISIBLE);
            revampCategoriesRecyclerView.setHasFixedSize(true);
            revampCategoriesRecyclerView.setLayoutManager(
                    new NonScrollGridLayoutManager(context, 3,
                            GridLayoutManager.VERTICAL, false));
            categoryAdapter = new RevampCategoryAdapter(getCategoryWidth(), activeChildren, categoryListener);
            revampCategoriesRecyclerView.setAdapter(categoryAdapter);
        } else {
            revampCategoriesRecyclerView.setVisibility(View.GONE);
        }
        if (categoryHeaderModel.getHeaderModel().getHeaderImageUrl() != null &&
                !categoryHeaderModel.getHeaderModel().getHeaderImageUrl().equals("")) {
            ImageHandler.loadImageFitTransformation(imageHeader.getContext(), imageHeader,
                    categoryHeaderModel.getHeaderModel().getHeaderImageUrl(), new CategoryHeaderTransformation(imageHeader.getContext()));
            titleHeader.setText(categoryHeaderModel.getHeaderModel().getCategoryName().toUpperCase());
            titleHeader.setShadowLayer(24, 0, 0, R.color.checkbox_text);
        } else {
            imageHeaderContainer.setVisibility(View.GONE);
        }
        if (isUsedUnactiveChildren) {
            expandLayout.setVisibility(View.VISIBLE);
            expandLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UnifyTracking.eventShowMoreCategory(categoryHeaderModel.getDepartementId());
                    categoryAdapter.addDataChild(categoryHeaderModel.getChildCategoryModelList()
                            .subList(9, categoryHeaderModel.getChildCategoryModelList().size()));
                    expandLayout.setVisibility(View.GONE);
                    isUsedUnactiveChildren = false;
                    hideLayout.setVisibility(View.VISIBLE);

                }
            });
            hideLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoryAdapter.hideExpandable();
                    expandLayout.setVisibility(View.VISIBLE);
                    isUsedUnactiveChildren = true;
                    hideLayout.setVisibility(View.GONE);
                }
            });
        }
        if (categoryHeaderModel.getTotalData() > 0) {
            totalProduct.setText(NumberFormat.getNumberInstance(Locale.US)
                    .format(categoryHeaderModel.getTotalData()).replace(',', '.') + " Produk");
            totalProduct.setVisibility(View.VISIBLE);
        }
        if (categoryHeaderModel.getBannerModelList() != null
                && categoryHeaderModel.getBannerModelList().size() > 0) {
            bannerHandler = new Handler();
            incrementPage = runnableIncrement();
            bannerPagerAdapter = new BannerPagerAdapter(context, categoryHeaderModel.getBannerModelList(), categoryHeaderModel.getDepartementId());
            bannerViewPager.setAdapter(bannerPagerAdapter);
            bannerViewPager.addOnPageChangeListener(onBannerChange());
            bannerIndicator.setFillColor(ContextCompat.getColor(context, R.color.tkpd_dark_orange));
            bannerIndicator.setPageColor(ContextCompat.getColor(context, R.color.white));
            bannerIndicator.setViewPager(bannerViewPager);
            bannerPagerAdapter.notifyDataSetChanged();
            RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) bannerViewPager.getLayoutParams();
            bannerViewPager.setLayoutParams(param);
            if (categoryHeaderModel.getBannerModelList().size() == 1)
                bannerIndicator.setVisibility(View.GONE);
            imageHeaderContainer.setVisibility(View.GONE);
            bannerContainer.setVisibility(View.VISIBLE);
            startSlide();
        }
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


    private void stopSlide() {
        if (bannerHandler != null && incrementPage != null)
            bannerHandler.removeCallbacks(incrementPage);
    }

    private void startSlide() {
        bannerHandler.removeCallbacks(incrementPage);
        bannerHandler.postDelayed(incrementPage, SLIDE_DELAY);
    }

    private int getCategoryWidth() {
        WindowManager wm = (WindowManager) MainApplication.getAppContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width / 2;
    }
}


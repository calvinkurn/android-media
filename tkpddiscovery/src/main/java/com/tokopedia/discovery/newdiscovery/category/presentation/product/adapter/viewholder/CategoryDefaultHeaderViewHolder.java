package com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.viewholder;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.util.NonScrollGridLayoutManager;
import com.tokopedia.core.widgets.DividerItemDecoration;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.DefaultCategoryAdapter;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ChildCategoryModel;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * @author by alifa on 10/31/17.
 */

public class CategoryDefaultHeaderViewHolder extends AbstractViewHolder<CategoryHeaderModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.default_category_header;
    public static final String DEFAULT_ITEM_VALUE = "1";

    RecyclerView defaultCategoriesRecyclerView;
    LinearLayout expandLayout;
    LinearLayout hideLayout;
    CardView cardViewCategory;
    TextView totalProduct;
    private final TopAdsBannerView topAdsBannerView;

    private final DefaultCategoryAdapter.CategoryListener categoryListener;
    private DefaultCategoryAdapter categoryAdapter;
    private Context context;
    private ArrayList<ChildCategoryModel> activeChildren = new ArrayList<>();
    private boolean isUsedUnactiveChildren = false;
    private boolean isInit;

    public CategoryDefaultHeaderViewHolder(View itemView, DefaultCategoryAdapter.CategoryListener categoryListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.categoryListener = categoryListener;
        this.defaultCategoriesRecyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view_default_categories);
        this.expandLayout = (LinearLayout) itemView.findViewById(R.id.expand_layout);
        this.hideLayout = (LinearLayout) itemView.findViewById(R.id.hide_layout);
        this.cardViewCategory = (CardView) itemView.findViewById(R.id.card_category);
        this.totalProduct = (TextView) itemView.findViewById(R.id.total_product);
        this.topAdsBannerView = (TopAdsBannerView) itemView.findViewById(R.id.topAdsBannerView);
    }

    private void initTopAds(String depId) {
        TopAdsParams adsParams = new TopAdsParams();
        adsParams.getParam().put(TopAdsParams.KEY_SRC, BrowseApi.DEFAULT_VALUE_SOURCE_DIRECTORY);
        adsParams.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID, depId);
        adsParams.getParam().put(TopAdsParams.KEY_ITEM, DEFAULT_ITEM_VALUE);
        adsParams.getParam().put(TopAdsParams.KEY_USER_ID, categoryListener.getUserId());

        Config config = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .topAdsParams(adsParams)
                .setUserId(categoryListener.getUserId())
                .setEndpoint(Endpoint.CPM)
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
        if (!isInit) {
            initTopAds(categoryHeaderModel.getDepartementId());
            isInit = true;
        }
        activeChildren = new ArrayList<>();
        hideLayout.setVisibility(View.GONE);
        if (categoryHeaderModel.getChildCategoryModelList() != null && categoryHeaderModel.getChildCategoryModelList().size() > 6) {
            activeChildren.addAll(categoryHeaderModel.getChildCategoryModelList().subList(0, 6));
            isUsedUnactiveChildren = true;
        } else if (categoryHeaderModel.getChildCategoryModelList() != null) {
            activeChildren.addAll(categoryHeaderModel.getChildCategoryModelList());
        }
        defaultCategoriesRecyclerView.setVisibility(View.VISIBLE);
        defaultCategoriesRecyclerView.setHasFixedSize(true);
        defaultCategoriesRecyclerView.setLayoutManager(
                new NonScrollGridLayoutManager(context, 2,
                        GridLayoutManager.VERTICAL, false));
        defaultCategoriesRecyclerView.addItemDecoration(new DividerItemDecoration(
                context, R.drawable.divider300));
        categoryAdapter = new DefaultCategoryAdapter(getCategoryWidth(),
                activeChildren, categoryListener);
        defaultCategoriesRecyclerView.setAdapter(categoryAdapter);
        if (isUsedUnactiveChildren) {
            expandLayout.setVisibility(View.VISIBLE);
            expandLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UnifyTracking.eventShowMoreCategory(v.getContext(), categoryHeaderModel.getDepartementId());
                    categoryAdapter.addDataChild(categoryHeaderModel.getChildCategoryModelList()
                            .subList(6, categoryHeaderModel.getChildCategoryModelList().size()));
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
        if (categoryHeaderModel.getChildCategoryModelList() == null || categoryHeaderModel.getChildCategoryModelList().isEmpty()) {
            cardViewCategory.setVisibility(View.GONE);
        }
        if (categoryHeaderModel.getTotalData() > 0) {
            totalProduct.setText(NumberFormat.getNumberInstance(Locale.US)
                    .format(categoryHeaderModel.getTotalData()).replace(',', '.') + " Produk");
            totalProduct.setVisibility(View.VISIBLE);
        }
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

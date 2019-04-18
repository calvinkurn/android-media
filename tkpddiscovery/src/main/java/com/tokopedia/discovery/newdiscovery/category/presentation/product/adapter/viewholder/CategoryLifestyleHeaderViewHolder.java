package com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tagmanager.DataLayer;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.QuickSingleFilterView;
import com.tokopedia.design.quickfilter.custom.CustomMultipleFilterView;
import com.tokopedia.design.quickfilter.custom.CustomViewRoundedQuickFilterItem;
import com.tokopedia.design.quickfilter.custom.CustomViewRounderCornerFilterView;
import com.tokopedia.design.quickfilter.custom.multiple.view.QuickMultipleFilterView;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.ChildCategoryLifestyleAdapter;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.RevampCategoryAdapter;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ChildCategoryModel;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;
import com.tokopedia.track.TrackApp;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by nakama on 1/4/18.
 */

public class CategoryLifestyleHeaderViewHolder extends AbstractViewHolder<CategoryHeaderModel> implements QuickSingleFilterView.ActionListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_category_header_lifestyle;

    public static final String DEFAULT_ITEM_VALUE = "1";
    public static final String SHOP = "shop";
    private final Context context;
    private final ImageView imageHeader;
    private final RelativeLayout imageHeaderContainer;
    private final View layoutChildCategory;
    private final RecyclerView listChildCategory;
    private final RevampCategoryAdapter.CategoryListener categoryListener;
    private final TextView titleHeader;
    private final TextView totalProduct;
    private CustomMultipleFilterView quickMultipleFilterView;
    private final TopAdsBannerView topAdsBannerView;
    private final SubCategoryLifestyleItemDecoration itemDecoration;
    private boolean isInit;
    HashMap<String, String> selectedFilterList = new HashMap<>();
    List<QuickFilterItem> filterItems = new ArrayList<>();

    public CategoryLifestyleHeaderViewHolder(View itemView,
                                             RevampCategoryAdapter.CategoryListener listener) {
        super(itemView);
        this.context = itemView.getContext();
        this.imageHeader = (ImageView) itemView.findViewById(R.id.image_header);
        this.titleHeader = (TextView) itemView.findViewById(R.id.title_header);
        this.totalProduct = (TextView) itemView.findViewById(R.id.total_product);
        this.quickMultipleFilterView = (CustomMultipleFilterView) itemView.findViewById(R.id.quickFilterView);
        this.imageHeaderContainer = (RelativeLayout) itemView.findViewById(R.id.image_header_container);
        this.layoutChildCategory = itemView.findViewById(R.id.view_child_category);
        this.listChildCategory = itemView.findViewById(R.id.recyclerview_child_category);
        this.topAdsBannerView = (TopAdsBannerView) itemView.findViewById(R.id.topAdsBannerView);
        this.itemDecoration = new SubCategoryLifestyleItemDecoration(itemView.getResources().getDimensionPixelSize(R.dimen.dp_8));
        this.categoryListener = listener;
        this.quickMultipleFilterView.setListener(this);
    }

    private void initTopAds(String depId, String categoryName) {
        TopAdsParams adsParams = new TopAdsParams();
        adsParams.getParam().put(TopAdsParams.KEY_SRC, BrowseApi.DEFAULT_VALUE_SOURCE_DIRECTORY);
        adsParams.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID, depId);
        adsParams.getParam().put(TopAdsParams.KEY_ITEM, DEFAULT_ITEM_VALUE);
        adsParams.getParam().put(TopAdsParams.KEY_USER_ID, categoryListener.getUserId());

        Config config = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(categoryListener.getUserId())
                .setEndpoint(Endpoint.CPM)
                .topAdsParams(adsParams)
                .build();
        this.topAdsBannerView.setConfig(config);
        this.topAdsBannerView.setTopAdsBannerClickListener(new TopAdsBannerClickListener() {
            @Override
            public void onBannerAdsClicked(int position, String applink, CpmData data) {
                categoryListener.onBannerAdsClicked(applink);
                if(applink.contains(SHOP)) {
                    TopAdsGtmTracker.eventCategoryPromoShopClick(context, categoryName, data, position);
                } else {
                    TopAdsGtmTracker.eventCategoryPromoProductClick(context, categoryName, data, position);
                }
            }
        });
        this.topAdsBannerView.setTopAdsImpressionListener(new TopAdsItemImpressionListener() {
            @Override
            public void onImpressionHeadlineAdsItem(int position, CpmData data) {
                TopAdsGtmTracker.eventCategoryPromoView(context, categoryName, data, position);
            }
        });
        this.topAdsBannerView.loadTopAds();
    }

    @Override
    public void bind(CategoryHeaderModel model) {
        if (!isInit) {
            initTopAds(model.getDepartementId(), model.getHeaderModel().getCategoryName());
            isInit = true;
        }
        renderBannerCategory(model);
        renderChildCategory(model);
        renderTotalProduct(model);
        renderQuickFilterView(model.getOptionList());
    }

    private void trackImpression(CategoryHeaderModel model) {
        if (!model.isDoneTrackImpression()) {
            List<Object> list = new ArrayList<>();
            for (int i = 0; i < model.getChildCategoryModelList().size(); i++) {
                ChildCategoryModel looper = model.getChildCategoryModelList().get(i);
                list.add(
                        DataLayer.mapOf(
                                "id", looper.getCategoryId(),
                                "name", String.format("category %s - subcategory banner", model.getHeaderModel().getCategoryName().toLowerCase()),
                                "position", String.valueOf(i+1),
                                "creative", looper.getCategoryName()
                                )
                );
            }
            model.setDoneTrackImpression(true);
            eventCategoryLifestyleImpression(list);
        }
    }

    public static void eventCategoryLifestyleImpression(List<Object> list) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf("event", "promoView",
                        "eventCategory", "category page",
                        "eventAction", "subcategory impression",
                        "eventLabel", "",
                        "ecommerce", DataLayer.mapOf(
                                "promoView", DataLayer.mapOf(
                                        "promotions", DataLayer.listOf(list.toArray(new Object[list.size()]))))
                )
        );
    }

    private void renderBannerCategory(CategoryHeaderModel model) {
        renderSingleBanner(
                model.getHeaderImage(),
                model.getHeaderModel().getCategoryName()
        );
    }

    private boolean isHasBanner(CategoryHeaderModel model) {
        return model.getBannerModelList() != null && !model.getBannerModelList().isEmpty();
    }

    private void renderChildCategory(CategoryHeaderModel model) {
        if (!isHasChild(model)) {
            layoutChildCategory.setVisibility(View.GONE);
        } else {
            trackImpression(model);
            layoutChildCategory.setVisibility(View.VISIBLE);
            layoutChildCategory.setBackgroundColor(
                    generateHexLifestyleBackgroundColor(model.getHeaderImageHexColor())
            );

            ChildCategoryLifestyleAdapter adapter = new ChildCategoryLifestyleAdapter(categoryListener, model.getHeaderModel().getCategoryName());
            adapter.setListCategory(model.getChildCategoryModelList());
            adapter.notifyDataSetChanged();
            listChildCategory.setHasFixedSize(true);
            listChildCategory.setLayoutManager(generateLayoutManager(model.getChildCategoryModelList().size()));
            listChildCategory.removeItemDecoration(itemDecoration);
            listChildCategory.addItemDecoration(itemDecoration);
            listChildCategory.setAdapter(adapter);
        }
    }

    private int generateHexLifestyleBackgroundColor(String hexColor) {
        int color = ContextCompat.getColor(context, R.color.white);
        if (!TextUtils.isEmpty(hexColor)) {
            try {
                color = Color.parseColor(hexColor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return color;
    }

    private RecyclerView.LayoutManager generateLayoutManager(int size) {
        if (size == 3) {
            return new GridLayoutManager(context, 3);
        } else {
            if (size < 8) {
                return new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            } else {
                return new GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false);
            }
        }
    }

    private boolean isRootCategory(CategoryHeaderModel model) {
        return TextUtils.equals(model.getDepartementId(), model.getRootCategoryId());
    }

    private boolean isHasChild(CategoryHeaderModel model) {
        return model.getChildCategoryModelList() != null &&
                !model.getChildCategoryModelList().isEmpty();
    }

    private boolean isOnRootCategory(CategoryHeaderModel model) {
        return model.getDepartementId().equals(model.getRootCategoryId());
    }

    protected void renderTotalProduct(CategoryHeaderModel model) {
        if (model.getTotalData() > 0) {
            totalProduct.setText(
                    String.format(
                            "%s Produk",
                            NumberFormat.getNumberInstance(Locale.US)
                                    .format(model.getTotalData())
                                    .replace(',', '.')
                    )
            );
            totalProduct.setVisibility(View.VISIBLE);
        } else {
            totalProduct.setVisibility(View.GONE);
        }
    }

    protected void renderQuickFilterView(List<Option> quickFilterItems) {

        if(quickFilterItems==null || quickFilterItems.isEmpty()){
            return;
        }
        if (filterItems.isEmpty()) {
            for (int i = 0; i < quickFilterItems.size(); i++) {
                CustomViewRoundedQuickFilterItem quickFilterItem = new CustomViewRoundedQuickFilterItem();
                quickFilterItem.setName(quickFilterItems.get(i).getName());
                quickFilterItem.setType(quickFilterItems.get(i).getKey() + "=" + quickFilterItems.get(i).getValue());
                filterItems.add(quickFilterItem);
            }
        }
        for (QuickFilterItem quickFilterItem: filterItems) {
            String[] str = quickFilterItem.getType().split("=");
            if (!quickFilterItem.isSelected() && selectedFilterList.containsKey(str[0])) {
                selectedFilterList.remove(str[0]);
            }
        }
        categoryListener.setQuickFilterList(filterItems);
        quickMultipleFilterView.renderFilter(filterItems);
    }

    protected void renderSingleBanner(String headerImage, String categoryName) {
        ImageHandler.LoadImage(imageHeader, headerImage);
        titleHeader.setText(categoryName);
        titleHeader.setShadowLayer(24, 0, 0, R.color.checkbox_text);

        imageHeaderContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void selectFilter(String typeFilter) {
        String[] str = typeFilter.split("=");
        String eventLabel;
        if (selectedFilterList.containsKey(str[0])) {
            selectedFilterList.remove(str[0]);
            eventLabel = "false";
        } else {
            selectedFilterList.put(str[0], str[1]);
            eventLabel = "true";
        }
        eventLabel = str[0] + "-" + str[1] + "-" + eventLabel;
        categoryListener.onQuickFilterSelected(selectedFilterList, eventLabel, str[0]);
    }
}

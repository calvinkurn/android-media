package com.tokopedia.home.beranda.presentation.view.viewmodel;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by henrypriyono on 1/12/18.
 */

public class InspirationViewModel implements Visitable<HomeTypeFactory> {

    private static final String EVENT_NAME = "event";
    private static final String EVENT_CATEGORY = "eventCategory";
    private static final String EVENT_ACTION = "eventAction";
    private static final String EVENT_LABEL = "eventLabel";
    private static final String EVENT_ECOMMERCE = "ecommerce";
    private static final String STATIC_VALUE_PRODUCT_VIEW = "productView";
    private static final String STATIC_VALUE_PRODUCT_CLICK = "productClick";
    private static final String STATIC_VALUE_HOMEPAGE_PRODUCT_IMPRESSION = "product recommendation impression";
    private static final String STATIC_VALUE_HOMEPAGE = "homepage";
    private static final String STATIC_VALUE_HOMEPAGE_PRODUCT_CLICK = "product recommendation click";
    private static final String STATIC_FORMAT_RECOMMENDATION = "/ - p%d - %s - %s";
    private static final String STATIC_VALUE_YOUR_RECOMMENDATION = "rekomendasi untuk anda";

    private String title;
    protected ArrayList<InspirationProductViewModel> listProduct;
    private int rowNumber;
    private String source;
    private int positionFeedCard;
    private String eventLabel;

    public InspirationViewModel(String title,
                                ArrayList<InspirationProductViewModel> listProduct,
                                String source) {
        this.title = title;
        this.listProduct = listProduct;
        this.source = source;
    }

    @Override
    public int type(HomeTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }

    public ArrayList<InspirationProductViewModel> getListProduct() {
        return listProduct;
    }

    public void setListProduct(ArrayList<InspirationProductViewModel> listProduct) {
        this.listProduct = listProduct;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Map<String, Object> getHomePageImpressionDataLayer() {
        List<Object> list = getListProductAsObjectDataLayer();
        return DataLayer.mapOf(EVENT_NAME, STATIC_VALUE_PRODUCT_VIEW,
                EVENT_CATEGORY, STATIC_VALUE_HOMEPAGE,
                EVENT_ACTION, STATIC_VALUE_HOMEPAGE_PRODUCT_IMPRESSION,
                EVENT_LABEL, "",
                EVENT_ECOMMERCE, DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(
                                list.toArray(new Object[list.size()])
                        ))
        );
    }

    public List<Object> getListProductAsObjectDataLayer() {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < getListProduct().size(); i++) {
            InspirationProductViewModel viewModel = getListProduct().get(i);
            list.add(
                    DataLayer.mapOf(
                            "name", viewModel.getName(),
                            "id", viewModel.getProductId(),
                            "price", Integer.toString(CurrencyFormatHelper.convertRupiahToInt(viewModel.getPriceInt())),
                            "brand", "none / other",
                            "category", "none / other",
                            "variant", "none / other",
                            "list", String.format(STATIC_FORMAT_RECOMMENDATION, getPositionFeedCard(), STATIC_VALUE_YOUR_RECOMMENDATION, viewModel.getRecommedationType()),
                            "position", i + 1
                    )
            );
        }
        return list;
    }

    public void setPositionFeedCard(int positionFeedCard) {
        this.positionFeedCard = positionFeedCard;
    }

    public int getPositionFeedCard() {
        return positionFeedCard;
    }

    public void setEventLabel(String eventLabel) {
        this.eventLabel = eventLabel;
    }

    public String getEventLabel() {
        return eventLabel;
    }

    public Map<String, Object> getHomePageClickDataLayer(int adapterPosition) {
        return DataLayer.mapOf(EVENT_NAME, STATIC_VALUE_PRODUCT_CLICK,
                EVENT_CATEGORY, STATIC_VALUE_HOMEPAGE,
                EVENT_ACTION, STATIC_VALUE_HOMEPAGE_PRODUCT_CLICK,
                EVENT_LABEL, String.format("%s - %s", STATIC_VALUE_YOUR_RECOMMENDATION, getListProduct().get(adapterPosition).getRecommedationType()),
                EVENT_ECOMMERCE, DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "click", DataLayer.mapOf(
                                "actionField", DataLayer.mapOf("list", String.format(STATIC_FORMAT_RECOMMENDATION, getPositionFeedCard(), STATIC_VALUE_YOUR_RECOMMENDATION, getListProduct().get(adapterPosition).getRecommedationType())),
                                "products", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "name", getListProduct().get(adapterPosition).getName(),
                                                "id", getListProduct().get(adapterPosition).getProductId(),
                                                "price", Integer.toString(CurrencyFormatHelper.convertRupiahToInt(getListProduct().get(adapterPosition).getPriceInt())),
                                                "brand", "none / other",
                                                "category", "none / other",
                                                "variant", "none / other",
                                                "list", String.format(STATIC_FORMAT_RECOMMENDATION, getPositionFeedCard(), STATIC_VALUE_YOUR_RECOMMENDATION, getListProduct().get(adapterPosition).getRecommedationType()),
                                                "position", adapterPosition + 1
                                        )
                                )
                        )
                )
        );
    }
}

package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 5/15/17.
 */

public class ActivityCardViewModel extends ProductCardViewModel {

    private final int totalProduct;
    private final int page;
    private int positionFeedCard;
    private ProductCardHeaderViewModel productCardHeaderViewModel;
    private String shareUrl;
    private String actionText;
    private String feedId;
    private String shareLinkDescription;
    private String cursor;
    private int rowNumber;
    private String eventLabel;

    public ActivityCardViewModel(ProductCardHeaderViewModel productCardHeaderViewModel,
                                 ArrayList<ProductFeedViewModel> listProduct,
                                 String shareUrl,
                                 String shareLinkDescription,
                                 String actionText,
                                 String feedId,
                                 int totalProduct,
                                 String cursor, int page) {
        this.listProduct = listProduct;
        this.productCardHeaderViewModel = productCardHeaderViewModel;
        this.shareUrl = shareUrl;
        this.shareLinkDescription = shareLinkDescription;
        this.actionText = actionText;
        this.feedId = feedId;
        this.totalProduct = totalProduct;
        this.cursor = cursor;
        this.page = page;
    }

    @Override
    public int type(FeedPlusTypeFactory typeFactory) {
        return typeFactory.type(this);
    }


    public ProductCardHeaderViewModel getHeader() {
        return productCardHeaderViewModel;
    }

    public void setHeader(ProductCardHeaderViewModel productCardHeaderViewModel) {
        this.productCardHeaderViewModel = productCardHeaderViewModel;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getActionText() {
        return actionText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public ProductCardHeaderViewModel getProductCardHeaderViewModel() {
        return productCardHeaderViewModel;
    }

    public void setProductCardHeaderViewModel(ProductCardHeaderViewModel productCardHeaderViewModel) {
        this.productCardHeaderViewModel = productCardHeaderViewModel;
    }

    public String getShareLinkDescription() {
        return shareLinkDescription;
    }

    public void setShareLinkDescription(String shareLinkDescription) {
        this.shareLinkDescription = shareLinkDescription;
    }

    public int getTotalProduct() {
        return totalProduct;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public int getPage() {
        return page;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public List<Object> getListProductAsObjectDataLayer(String eventLabel, String userId, int positionFeedCard) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < getListProduct().size(); i++) {
            ProductFeedViewModel viewModel = getListProduct().get(i);
            list.add(
                    DataLayer.mapOf(
                            "name", viewModel.getName(),
                            "id", viewModel.getProductId(),
                            "price", viewModel.getPriceInt(),
                            "brand", "",
                            "category", "",
                            "variant", "",
                            "list", String.format("/feed - product %d - %s", positionFeedCard, eventLabel),
                            "position", i,
                            "userId", userId
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
}

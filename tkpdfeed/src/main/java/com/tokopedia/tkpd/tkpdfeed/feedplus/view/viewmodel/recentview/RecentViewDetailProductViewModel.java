package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.recentview;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.recentview.RecentViewTypeFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.LabelsViewModel;

import java.util.List;

/**
 * @author by nisie on 7/4/17.
 */

public class RecentViewDetailProductViewModel implements Visitable<RecentViewTypeFactory> {

    private String name;
    private String price;
    private String imageSource;
    private boolean isFreeReturn;
    private boolean isWishlist;
    private int rating;
    private Integer productId;
    private boolean isGold;
    private final boolean isOfficial;
    private final String shopName;
    private final String shopLocation;
    private List<LabelsViewModel> labels;

    public RecentViewDetailProductViewModel(Integer productId,
                                            String name,
                                            String price,
                                            String imageSource,
                                            List<LabelsViewModel> labels,
                                            boolean isFreeReturn,
                                            boolean isWishlist,
                                            int rating,
                                            boolean isGold,
                                            boolean isOfficial,
                                            String shopName,
                                            String shopLocation) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageSource = imageSource;
        this.labels = labels;
        this.isFreeReturn = isFreeReturn;
        this.isWishlist = isWishlist;
        this.rating = rating;
        this.isGold = isGold;
        this.isOfficial = isOfficial;
        this.shopName = shopName;
        this.shopLocation = shopLocation;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImageSource() {
        return imageSource;
    }

    public boolean isFreeReturn() {
        return isFreeReturn;
    }

    public boolean isWishlist() {
        return isWishlist;
    }

    public int getRating() {
        return rating;
    }

    public Integer getProductId() {
        return productId;
    }

    public boolean isGold() {
        return isGold;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    @Override
    public int type(RecentViewTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public List<LabelsViewModel> getLabels() {
        return labels;
    }
}

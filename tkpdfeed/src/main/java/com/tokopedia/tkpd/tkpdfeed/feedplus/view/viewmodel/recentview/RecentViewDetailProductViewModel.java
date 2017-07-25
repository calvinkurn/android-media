package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.recentview;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.recentview.RecentViewTypeFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.recentview.RecentViewDetailProductViewHolder;

/**
 * @author by nisie on 7/4/17.
 */

public class RecentViewDetailProductViewModel implements Visitable<RecentViewTypeFactory> {

    private String name;
    private String price;
    private String imageSource;
    private String cashback;
    private boolean isWholesale;
    private boolean isPreorder;
    private boolean isFreeReturn;
    private boolean isWishlist;
    private int rating;
    private Integer productId;
    private boolean isGold;
    private final boolean isOfficial;
    private final String shopName;
    private final String shopLocation;

    public RecentViewDetailProductViewModel(Integer productId,
                                            String name,
                                            String price,
                                            String imageSource,
                                            String cashback,
                                            boolean isWholesale,
                                            boolean isPreorder,
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
        this.cashback = cashback;
        this.isWholesale = isWholesale;
        this.isPreorder = isPreorder;
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

    public String getCashback() {
        return cashback;
    }

    public boolean isWholesale() {
        return isWholesale;
    }

    public boolean isPreorder() {
        return isPreorder;
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
}
